package com.kurt.lemond.hackernews.activity_main.repository.fragment

import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.BestStoriesService
import io.reactivex.Single


class BestStoriesRepository(private val bestStoriesService: BestStoriesService,
                            private val storyDetailsService: StoryDetailsService): DataRepository<StoryDetails>() {

    override fun getIds(): Single<DataWrapper<List<Long>>> {
        return bestStoriesService.getBestStoryIds()
                .map {idList ->
                    wrapWithSuccessDataWrapper(idList)
                }
                .onErrorReturn { throwable ->
                    wrapWithErrorDataWrapper(throwable)
                }
    }

    override fun getDetails(storyId: Long): Single<DataWrapper<StoryDetails>> {
        return storyDetailsService.getStoryDetails(storyId.toString())
                .map {storyDetails ->
                    wrapWithSuccessDataWrapper(storyDetails)
                }
                .onErrorReturn { throwable ->
                    wrapWithErrorDataWrapper(throwable)
                }
    }
}