package com.kurt.lemond.hackernews.activity_main.repository

import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.exception.NoInternetException

fun <S> wrapWithSuccessDataWrapper(s: S): DataWrapper<S> {
    val dataWrapper = DataWrapper<S>()
    dataWrapper.data = s
    dataWrapper.setState(DataWrapper.State.SUCCESS)
    return dataWrapper
}

fun <S> wrapWithErrorDataWrapper(throwable: Throwable): DataWrapper<S> {
    val errorDataWrapper = DataWrapper<S>()
    errorDataWrapper.data = null
    if (throwable is NoInternetException) {
        errorDataWrapper.setState(DataWrapper.State.NO_NETWORK)
    } else {
        errorDataWrapper.setState(DataWrapper.State.ERROR)
    }
    errorDataWrapper.errorMessage = throwable.message
    return errorDataWrapper
}