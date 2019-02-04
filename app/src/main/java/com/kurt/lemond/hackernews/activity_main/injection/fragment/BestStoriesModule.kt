package com.kurt.lemond.hackernews.activity_main.injection.fragment

import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.BestStoriesService
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesFragmentViewModelFactory
import com.kurt.lemond.hackernews.activity_main.injection.fragment.qualifier.BestStoriesPage
import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.BestStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.fragment.BestStoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class BestStoriesModule {

    @Provides
    @BestStoriesFragmentScope
    fun provideBestStoriesService(retrofit: Retrofit): BestStoriesService {
        return retrofit.create(BestStoriesService::class.java)
    }

    @Provides
    @BestStoriesFragmentScope
    @BestStoriesPage
    fun provideStoryDetailsService(retrofit: Retrofit): StoryDetailsService {
        return retrofit.create(StoryDetailsService::class.java)
    }

    @Provides
    @BestStoriesFragmentScope
    @BestStoriesPage
    fun provideStoriesRepository(bestStoriesService: BestStoriesService, @BestStoriesPage storyDetailsService: StoryDetailsService): DataRepository<StoryDetails> {
        return BestStoriesRepository(bestStoriesService, storyDetailsService)
    }

    @Provides
    @BestStoriesFragmentScope
    @BestStoriesPage
    fun provideViewModelFactory(@BestStoriesPage storyRepository: DataRepository<StoryDetails>): StoriesFragmentViewModelFactory {
        return StoriesFragmentViewModelFactory(storyRepository)
    }

}