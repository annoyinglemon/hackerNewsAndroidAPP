package com.kurt.lemond.hackernews.activity_main.repository.fragment

import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.BestStoriesService
import io.reactivex.Single


class BestStoriesRepository(private val bestStoriesService: BestStoriesService,
                            private val storyDetailsService: StoryDetailsService): DataRepository<StoryDetails>() {

    override fun loadIds(): Single<List<Long>> {
        return bestStoriesService.getBestStoryIds()
    }

    override fun loadDetails(storyId: Long): Single<StoryDetails> {
        return storyDetailsService.getStoryDetails(storyId.toString())
    }

}