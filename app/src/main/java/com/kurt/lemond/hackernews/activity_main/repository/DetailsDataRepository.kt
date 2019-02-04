package com.kurt.lemond.hackernews.activity_main.repository

import androidx.annotation.VisibleForTesting
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import io.reactivex.Single


abstract class DetailsDataRepository<T> {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract fun loadDetails(dataId: Long): Single<T>

    fun getDetails(dataId: Long): Single<DataWrapper<T>> {
        return loadDetails(dataId)
                .map {details ->
                    wrapWithSuccessDataWrapper(details)
                }
                .onErrorReturn { throwable ->
                    wrapWithErrorDataWrapper(throwable)
                }
    }

}