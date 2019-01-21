package com.kurt.lemond.hackernews.activity_main.repository.pagination

import androidx.paging.PositionalDataSource
import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


/**
 * Class that fetches all item ids such as story ids and comment ids.
 * Data-type should be Story datatype or Comment datatype on child classes
 */
class ItemDataSource<T>(private val dataRepository: DataRepository<T>,
                        private val disposableCollector: CompositeDisposable) : PositionalDataSource<DataWrapper<T>>() {

    private val allItemIds = ArrayList<Long>()

    var itemIdsStateObservable = PublishSubject.create<DataWrapper.State>()
    var fetchIdsErrorMessageObservable = PublishSubject.create<String>()

    init {
        itemIdsStateObservable.onNext(DataWrapper.State.SUCCESS)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<DataWrapper<T>>) {
        val itemList = ArrayList<DataWrapper<T>>()
        val detailsSingle = ArrayList<Single<DataWrapper<T>>>()

        for (i in params.startPosition until (params.startPosition + params.loadSize)) {
            detailsSingle.add(dataRepository.getDetails(allItemIds[i]))
        }
        val disposable = Single.merge(detailsSingle)
                .subscribe { t ->
                    itemList.add(t)
                }
        disposableCollector.add(disposable)

        callback.onResult(itemList)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<DataWrapper<T>>) {
        val itemList = ArrayList<DataWrapper<T>>()

        val disposable = dataRepository.getIds()
                .map {dataWrappedIdList ->
                    // notify state and error message
                    itemIdsStateObservable.onNext(dataWrappedIdList.getState())
                    if (!dataWrappedIdList.errorMessage.isNullOrEmpty()) {
                        fetchIdsErrorMessageObservable.onNext(dataWrappedIdList.errorMessage!!)
                    } else {
                        fetchIdsErrorMessageObservable.onNext("")
                    }

                    // when state == ERROR, allItemIds's size is 0
                    allItemIds.clear()
                    if (dataWrappedIdList.getState() == DataWrapper.State.SUCCESS) {
                        allItemIds.addAll(dataWrappedIdList.data!!)
                    }
                    dataWrappedIdList.getState()
                }
                .flatMapPublisher {dataState ->
                    val detailsSingle = ArrayList<Single<DataWrapper<T>>>()

                    if (dataState == DataWrapper.State.SUCCESS) {
                        for (i in params.requestedStartPosition until params.requestedLoadSize) {
                        // when id list's data state is SUCCESS, load each item's details,
                            detailsSingle.add(dataRepository.getDetails(allItemIds[i]))
                        }
                    }

                    Single.concat(detailsSingle)

                }.subscribe {dataDetailsWrapper ->
                    itemList.add(dataDetailsWrapper)
                }
        disposableCollector.add(disposable)

        // 2nd parameter is the position where the itemList starts in the list (allItemIds which has a size of 200),
        // 0 means we want our list to start on the beginning of our list that has 200 items
        callback.onResult(itemList, 0, allItemIds.size)
    }
}