package com.kurt.lemond.hackernews.activity_main.repository.fragment.top_stories

import com.kurt.lemond.hackernews.activity_main.repository.IdsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.TopStoriesService
import io.reactivex.Single


class TopStoryIdsRepository(private val topStoriesService: TopStoriesService): IdsDataRepository() {

    override fun loadIds(): Single<List<Long>> {
        return topStoriesService.getTopStoryIds()
    }

}