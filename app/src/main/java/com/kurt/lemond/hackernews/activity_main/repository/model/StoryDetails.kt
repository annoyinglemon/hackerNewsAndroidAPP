package com.kurt.lemond.hackernews.activity_main.repository.model

import com.google.gson.annotations.SerializedName

class StoryDetails(@SerializedName("by") val author: String,
                   @SerializedName("descendants") val commentCount: Int,
                   @SerializedName("id") val itemId: Long,
                   @SerializedName("kids") val commentIds: List<Long>,
                   @SerializedName("score") val score: Int,
                   @SerializedName("time") val timeInUnix: Long,
                   @SerializedName("title") val title: String,
                   @SerializedName("type") val type: String,
                   @SerializedName("url") val url: String)