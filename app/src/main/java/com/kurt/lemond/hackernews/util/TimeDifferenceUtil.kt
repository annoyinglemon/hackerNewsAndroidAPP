package com.kurt.lemond.hackernews.util

import java.util.*

fun getTimeDifference(newsDate: Long): String {
    val now = Calendar.getInstance()
    var periodSeconds = now.timeInMillis / 1000 - newsDate
    if (periodSeconds / 60 / 60 / 24 >= 1) {
        periodSeconds = periodSeconds / 60 / 60 / 24
        return if (periodSeconds == 1L)
            periodSeconds.toString() + " day ago"
        else
            periodSeconds.toString() + " days ago"
    } else if (periodSeconds / 60 / 60 >= 1) {
        periodSeconds = periodSeconds / 60 / 60
        return if (periodSeconds == 1L)
            periodSeconds.toString() + " hour ago"
        else
            periodSeconds.toString() + " hours ago"
    } else if (periodSeconds / 60 >= 1) {
        periodSeconds /= 60
        return if (periodSeconds == 1L)
            periodSeconds.toString() + " minute ago"
        else
            periodSeconds.toString() + " minutes ago"
    } else {
        return if (periodSeconds == 1L)
            periodSeconds.toString() + " second ago"
        else
            periodSeconds.toString() + " seconds ago"
    }
}