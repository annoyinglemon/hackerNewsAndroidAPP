package com.kurt.lemond.hackernews.activity_main.repository.pagination

import com.kurt.lemond.hackernews.activity_main.repository.StoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

class StoriesDataSource(private val storiesRepository: StoriesRepository,
                        errorConsumer: Consumer<Throwable>,
                        disposableCollector: CompositeDisposable): ItemDataSource<StoryDetails>(errorConsumer, disposableCollector) {

    override fun loadAllIds(): Single<List<Long>> {
        return storiesRepository.getStoryIds()
    }

    override fun loadItemDetails(id: Long): Single<StoryDetails> {
        return storiesRepository.getStoryDetails(id)
    }

}