package com.kurt.lemond.hackernews.activity_main.repository.fragment

import com.kurt.lemond.hackernews.activity_main.repository.StoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.NewStoriesService
import io.reactivex.Single

class NewStoriesRepository(private val newStoriesService: NewStoriesService,
                           private val storyDetailsService: StoryDetailsService): StoriesRepository {

    override fun getStoryIds(): Single<List<Long>> {
        return newStoriesService.getNewStoryIds()
    }

    override fun getStoryDetails(storyId: Long): Single<StoryDetails> {
        return storyDetailsService.getStoryDetails(storyId.toString())
    }

}