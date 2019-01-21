package com.kurt.lemond.hackernews.activity_main.repository.pagination

import androidx.paging.DataSource
import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import io.reactivex.disposables.CompositeDisposable

class ItemDataSourceFactory<T>(private val dataRepository: DataRepository<T>,
                            private val disposableCollector: CompositeDisposable): DataSource.Factory<Int, DataWrapper<T>>() {

    override fun create(): DataSource<Int, DataWrapper<T>> {
        return ItemDataSource(dataRepository, disposableCollector)
    }
}