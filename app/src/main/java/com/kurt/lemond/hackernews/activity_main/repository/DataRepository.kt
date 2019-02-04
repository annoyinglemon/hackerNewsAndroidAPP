package com.kurt.lemond.hackernews.activity_main.repository

import androidx.annotation.VisibleForTesting
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.exception.NoInternetException
import io.reactivex.Single


abstract class DataRepository<T> {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract fun loadIds(): Single<List<Long>>

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract fun loadDetails(storyId: Long): Single<T>

    fun getIds(): Single<DataWrapper<List<Long>>> {
        return loadIds()
                .map {idList ->
                    wrapWithSuccessDataWrapper(idList)
                }
                .onErrorReturn { throwable ->
                    wrapWithErrorDataWrapper(throwable)
                }
    }

    fun getDetails(storyId: Long): Single<DataWrapper<T>> {
        return loadDetails(storyId)
                .map {storyDetails ->
                    wrapWithSuccessDataWrapper(storyDetails)
                }
                .onErrorReturn { throwable ->
                    wrapWithErrorDataWrapper(throwable)
                }
    }

    private fun <S> wrapWithSuccessDataWrapper(s: S): DataWrapper<S> {
        val dataWrapper = DataWrapper<S>()
        dataWrapper.data = s
        dataWrapper.setState(DataWrapper.State.SUCCESS)
        return dataWrapper
    }

    private fun <S> wrapWithErrorDataWrapper(throwable: Throwable): DataWrapper<S> {
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

}