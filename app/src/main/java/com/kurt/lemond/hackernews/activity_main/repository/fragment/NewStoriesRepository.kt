package com.kurt.lemond.hackernews.activity_main.repository.fragment

import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.NewStoriesService
import io.reactivex.Single

class NewStoriesRepository(private val newStoriesService: NewStoriesService,
                           private val storyDetailsService: StoryDetailsService): DataRepository<StoryDetails>() {

    override fun getIds(): Single<DataWrapper<List<Long>>> {
        return newStoriesService.getNewStoryIds()
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