package com.kurt.lemond.hackernews.activity_main.repository.pagination

import androidx.paging.DataSource
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import io.reactivex.disposables.CompositeDisposable

class ItemDataSourceFactory<T>(private val allItemIds: List<Long>,
                               private val detailsDataRepository: DetailsDataRepository<T>,
                               private val disposableCollector: CompositeDisposable): DataSource.Factory<Int, DataWrapper<T>>() {

    lateinit var itemDataSource: ItemDataSource<T>

    override fun create(): DataSource<Int, DataWrapper<T>> {
        itemDataSource = ItemDataSource(allItemIds, detailsDataRepository, disposableCollector)
        return itemDataSource
    }

}