package com.kurt.lemond.hackernews.activity_main.fragment_top_stories.repository

import io.reactivex.Single
import retrofit2.http.GET

interface TopStoriesService {

    @GET("/topstories.json")
    fun getTopStoryIds(): Single<List<Long>>

}