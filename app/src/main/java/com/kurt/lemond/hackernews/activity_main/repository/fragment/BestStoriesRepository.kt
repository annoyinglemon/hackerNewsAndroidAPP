package com.kurt.lemond.hackernews.activity_main.repository.fragment

import com.kurt.lemond.hackernews.activity_main.repository.StoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.BestStoriesService
import io.reactivex.Single

class BestStoriesRepository(private val bestStoriesService: BestStoriesService,
                            private val storyDetailsService: StoryDetailsService): StoriesRepository {

    override fun getStoryIds(): Single<List<Long>> {
        return bestStoriesService.getBestStoryIds()
    }

    override fun getStoryDetails(storyId: Long): Single<StoryDetails> {
        return storyDetailsService.getStoryDetails(storyId.toString())
    }
}