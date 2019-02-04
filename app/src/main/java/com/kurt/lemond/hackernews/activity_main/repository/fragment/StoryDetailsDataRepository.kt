package com.kurt.lemond.hackernews.activity_main.repository.fragment

import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import io.reactivex.Single


class StoryDetailsDataRepository(private val storyDetailsService: StoryDetailsService): DetailsDataRepository<StoryDetails>() {

    override fun loadDetails(dataId: Long): Single<StoryDetails> {
        return storyDetailsService.getStoryDetails(dataId.toString())
    }

}