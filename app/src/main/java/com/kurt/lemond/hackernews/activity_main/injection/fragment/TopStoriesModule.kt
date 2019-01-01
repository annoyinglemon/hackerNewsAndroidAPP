package com.kurt.lemond.hackernews.activity_main.injection.fragment

import com.kurt.lemond.hackernews.activity_main.injection.fragment.qualifier.TopStoriesPage
import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.TopStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.repository.StoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.fragment.TopStoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.TopStoriesService
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesFragmentViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class TopStoriesModule {

    @Provides
    @TopStoriesFragmentScope
    fun provideTopStoriesService(retrofit: Retrofit): TopStoriesService {
        return retrofit.create(TopStoriesService::class.java)
    }

    @Provides
    @TopStoriesFragmentScope
    @TopStoriesPage
    fun provideStoryDetailsService(retrofit: Retrofit): StoryDetailsService {
        return retrofit.create(StoryDetailsService::class.java)
    }

    @Provides
    @TopStoriesFragmentScope
    @TopStoriesPage
    fun provideStoriesRepository(topStoriesService: TopStoriesService, @TopStoriesPage storyDetailsService: StoryDetailsService): StoriesRepository {
        return TopStoriesRepository(topStoriesService, storyDetailsService)
    }

    @Provides
    @TopStoriesFragmentScope
    @TopStoriesPage
    fun provideViewModelFactory(@TopStoriesPage storiesRepository: StoriesRepository): StoriesFragmentViewModelFactory {
        return StoriesFragmentViewModelFactory(storiesRepository)
    }

}