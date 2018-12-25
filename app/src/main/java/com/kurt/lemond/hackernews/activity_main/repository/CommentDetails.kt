package com.kurt.lemond.hackernews.activity_main.repository

import com.google.gson.annotations.SerializedName

class CommentDetails(@SerializedName("by") val commenter: String,
                     @SerializedName("id") val commentId: Long,
                     @SerializedName("kids") val subCommentIds: List<Long>,
                     @SerializedName("parent") val parent: String,
                     @SerializedName("text") val comment: String,
                     @SerializedName("time") val timeInUnix: Long,
                     @SerializedName("type") val type: String)