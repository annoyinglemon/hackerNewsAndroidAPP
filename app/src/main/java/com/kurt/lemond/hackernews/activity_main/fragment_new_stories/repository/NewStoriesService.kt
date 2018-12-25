package com.kurt.lemond.hackernews.activity_main.fragment_new_stories.repository

import io.reactivex.Single
import retrofit2.http.GET

interface NewStoriesService {

    @GET("/newstories.json")
    fun getNewStoryIds(): Single<List<Long>>

}