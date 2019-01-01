package com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment

import io.reactivex.Single
import retrofit2.http.GET

interface BestStoriesService {

    @GET("/v0/beststories.json")
    fun getBestStoryIds(): Single<List<Long>>

}