package com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment

import io.reactivex.Single
import retrofit2.http.GET

interface NewStoriesService {

    @GET("/v0/newstories.json")
    fun getNewStoryIds(): Single<List<Long>>

}