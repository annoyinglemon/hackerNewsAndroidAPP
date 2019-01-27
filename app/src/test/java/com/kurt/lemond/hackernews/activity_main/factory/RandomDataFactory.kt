package com.kurt.lemond.hackernews.activity_main.factory

import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.exception.NoInternetException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


fun createRandomString(): String {
    return UUID.randomUUID().toString()
}

fun createRandomLong(): Long {
    return Random.nextLong()
}

fun createRandomBoolean(): Boolean {
    return Random.nextBoolean()
}

fun createRandomLongList(listSize: Int): List<Long> {
    val arrayList = ArrayList<Long>()
    for (i in 0 until listSize) {
        arrayList.add(createRandomLong())
    }
    return arrayList
}

fun createRandomUser(id: Long): User {
    return User(id, createRandomString(), createRandomString())
}

fun <T> createErrorDataWrapper(): DataWrapper<T> {
    val dataWrapper = DataWrapper<T>()
    dataWrapper.setState(DataWrapper.State.ERROR)
    dataWrapper.errorMessage = createRandomString()
    dataWrapper.data = null
    return dataWrapper
}

fun <T> createNoNetworkDataWrapper(): DataWrapper<T> {
    val dataWrapper = DataWrapper<T>()
    dataWrapper.setState(DataWrapper.State.NO_NETWORK)
    dataWrapper.errorMessage = NoInternetException().message
    dataWrapper.data = null
    return dataWrapper
}

fun <T> createSuccessDataWrapper(t: T): DataWrapper<T> {
    val dataWrapper = DataWrapper<T>()
    dataWrapper.setState(DataWrapper.State.SUCCESS)
    dataWrapper.errorMessage = null
    dataWrapper.data = t
    return dataWrapper
}

