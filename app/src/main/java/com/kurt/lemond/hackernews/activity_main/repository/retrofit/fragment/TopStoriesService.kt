package com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment

import io.reactivex.Single
import retrofit2.http.GET

interface TopStoriesService {

    @GET("/v0/topstories.json")
    fun getTopStoryIds(): Single<List<Long>>

}