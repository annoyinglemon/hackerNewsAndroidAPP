package com.kurt.lemond.hackernews.activity_main.repository.pagination

import androidx.paging.PositionalDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer


/**
 * Class that fetches all item ids such as story ids and comment ids.
 * 'Any' datatype should be Story datatype or Comment datatype on child classes
 */
abstract class ItemDataSource<T>(private val errorConsumer: Consumer<Throwable>,
                                 private val disposableCollector: CompositeDisposable) : PositionalDataSource<T>() {

    private val allItemIds = ArrayList<Long>()
    private var lastPositionLoaded = 0


    abstract fun loadAllIds(): Single<List<Long>>

    abstract fun loadItemDetails(id: Long): Single<T>

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        val start = lastPositionLoaded
        val end = start + (params.loadSize - 1)
        lastPositionLoaded = end + 1

        val itemList = ArrayList<T>()
        val detailsSingle = ArrayList<Single<T>>()

        if (lastPositionLoaded == 0 || lastPositionLoaded < allItemIds.size) {

            for (i in start..end) {
                detailsSingle.add(loadItemDetails(allItemIds[i]))
            }

            val disposable = Single.merge(detailsSingle)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer { t ->
                        itemList.add(t)
                    }, errorConsumer)
            disposableCollector.add(disposable)
        }

        callback.onResult(itemList)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        val itemList = ArrayList<T>()
        val disposable = loadAllIds()
                .flatMapPublisher { idList ->
                    allItemIds.addAll(idList)

                    val detailsSingle = ArrayList<Single<T>>()

                    for (i in params.requestedStartPosition until params.requestedLoadSize) {
                        detailsSingle.add(loadItemDetails(allItemIds[i]))
                    }

                    Single.merge(detailsSingle)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { t ->
                    itemList.add(t)
                }, errorConsumer)

        disposableCollector.add(disposable)
        callback.onResult(itemList, itemList.size, allItemIds.size)
    }
}