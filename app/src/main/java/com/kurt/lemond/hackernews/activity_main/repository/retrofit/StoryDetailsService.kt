package com.kurt.lemond.hackernews.activity_main.repository.retrofit

import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface StoryDetailsService {

    @GET("/v0/item/{itemId}.json?")
    fun getStoryDetails(@Path("itemId") itemId: String): Single<StoryDetails>

}