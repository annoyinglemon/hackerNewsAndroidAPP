package com.kurt.lemond.hackernews.activity_main.repository

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemDetailsService {

    @GET("/item/{itemId}.json?")
    fun getStoryDetails(@Path("itemId") itemId: String): Single<ItemDetails>

}