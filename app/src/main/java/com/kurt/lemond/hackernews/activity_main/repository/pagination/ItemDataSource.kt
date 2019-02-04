package com.kurt.lemond.hackernews.activity_main.repository.pagination

import android.util.Log
import androidx.paging.PositionalDataSource
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


/**
 * Class that fetches all item ids such as story ids and comment ids.
 * Data-type should be Story datatype or Comment datatype on child classes
 */
class ItemDataSource<T>(private val allItemIds: List<Long>,
                        private val detailsDataRepository: DetailsDataRepository<T>,
                        private val disposableCollector: CompositeDisposable) : PositionalDataSource<DataWrapper<T>>() {


    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<DataWrapper<T>>) {
        val itemList = ArrayList<DataWrapper<T>>()

        Log.d("itemDataSource", "start.loadRange: ${params.startPosition}")
        Log.d("itemDataSource", "size.loadRange: ${params.loadSize}")

        if (params.startPosition < allItemIds.size) {
            val detailsSingle = ArrayList<Single<DataWrapper<T>>>()

            for (i in params.startPosition until (params.startPosition + params.loadSize)) {
                detailsSingle.add(detailsDataRepository.getDetails(allItemIds[i]))
            }

            val disposable = Single.concat(detailsSingle)
                    .subscribe { dataDetailsWrapper ->
                        itemList.add(dataDetailsWrapper)
                    }

            disposableCollector.add(disposable)
        }

        callback.onResult(itemList)
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<DataWrapper<T>>) {
        val itemList = ArrayList<DataWrapper<T>>()
        val detailsSingle = ArrayList<Single<DataWrapper<T>>>()

        Log.d("itemDataSource", "start.loadInitial: ${params.requestedStartPosition}")
        Log.d("itemDataSource", "size.loadInitial: ${params.requestedLoadSize}")

        for (i in params.requestedStartPosition until params.requestedLoadSize) {
            detailsSingle.add(detailsDataRepository.getDetails(allItemIds[i]))
        }

        val disposable = Single.concat(detailsSingle)
                .subscribe { dataDetailsWrapper ->
                    itemList.add(dataDetailsWrapper)
                }

        disposableCollector.add(disposable)

        // 2nd parameter is the position where the itemList starts in the list (allItemIds which has a size of 200),
        // 0 means we want our list to start on the beginning of our list that has 200 items
        callback.onResult(itemList, 0, allItemIds.size)
    }
}