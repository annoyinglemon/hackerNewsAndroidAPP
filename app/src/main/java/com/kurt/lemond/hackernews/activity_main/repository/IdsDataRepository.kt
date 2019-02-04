package com.kurt.lemond.hackernews.activity_main.repository

import androidx.annotation.VisibleForTesting
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import io.reactivex.Single


abstract class IdsDataRepository {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    protected abstract fun loadIds(): Single<List<Long>>

    fun getIds(): Single<DataWrapper<List<Long>>> {
        return loadIds()
                .map {idList ->
                    wrapWithSuccessDataWrapper(idList)
                }
                .onErrorReturn { throwable ->
                    wrapWithErrorDataWrapper(throwable)
                }
    }

}