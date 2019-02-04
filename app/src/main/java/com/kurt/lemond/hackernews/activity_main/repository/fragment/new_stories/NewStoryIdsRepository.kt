package com.kurt.lemond.hackernews.activity_main.repository.fragment.new_stories

import com.kurt.lemond.hackernews.activity_main.repository.IdsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.NewStoriesService
import io.reactivex.Single


class NewStoryIdsRepository(private val newStoriesService: NewStoriesService): IdsDataRepository() {

    override fun loadIds(): Single<List<Long>> {
        return newStoriesService.getNewStoryIds()
    }

}