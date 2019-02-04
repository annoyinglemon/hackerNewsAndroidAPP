package com.kurt.lemond.hackernews.activity_main.injection

import com.kurt.lemond.hackernews.activity_main.injection.scope.MainActivityScope
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.fragment.StoryDetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class StoryDetailsModule {

    @Provides
    @MainActivityScope
    fun provideStoryDetailsService(retrofit: Retrofit): StoryDetailsService {
        return retrofit.create(StoryDetailsService::class.java)
    }

    @Provides
    @MainActivityScope
    fun provideStoryDetailsRepository(storyDetailsService: StoryDetailsService): DetailsDataRepository<StoryDetails> {
        return StoryDetailsDataRepository(storyDetailsService)
    }

}