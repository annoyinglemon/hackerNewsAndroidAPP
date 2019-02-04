package com.kurt.lemond.hackernews.activity_main.repository.fragment.best_stories

import com.kurt.lemond.hackernews.activity_main.repository.IdsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.BestStoriesService
import io.reactivex.Single


class BestStoryIdsRepository(private val bestStoriesService: BestStoriesService): IdsDataRepository() {

    override fun loadIds(): Single<List<Long>> {
        return bestStoriesService.getBestStoryIds()
    }

}