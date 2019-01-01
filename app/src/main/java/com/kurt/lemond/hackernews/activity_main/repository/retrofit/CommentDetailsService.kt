package com.kurt.lemond.hackernews.activity_main.repository.retrofit

import com.kurt.lemond.hackernews.activity_main.repository.model.CommentDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentDetailsService {

    @GET("/v0/item/{itemId}.json?")
    fun getCommentDetails(@Path("itemId") itemId: String): Single<CommentDetails>

}