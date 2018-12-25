package com.kurt.lemond.hackernews.activity_main.fragment_best_stories.repository

import io.reactivex.Single
import retrofit2.http.GET

interface BestStoriesService {

    @GET("/beststories.json")
    fun getBestStoryIds(): Single<List<Long>>

}