package com.kurt.lemond.hackernews.activity_main.repository.pagination

import androidx.paging.DataSource
import com.kurt.lemond.hackernews.activity_main.repository.StoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.BestStoriesService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

class StoriesDataSourceFactory(private val storiesRepository: StoriesRepository,
                               private val errorConsumer: Consumer<Throwable>,
                               private val disposableCollector: CompositeDisposable): DataSource.Factory<Int, StoryDetails>() {

    override fun create(): DataSource<Int, StoryDetails> {
        return StoriesDataSource(storiesRepository, errorConsumer, disposableCollector)
    }
}