package com.kurt.lemond.hackernews.activity_main.repository

import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import io.reactivex.Single


interface StoriesRepository {

    fun getStoryIds(): Single<List<Long>>

    fun getStoryDetails(storyId: Long): Single<StoryDetails>

}