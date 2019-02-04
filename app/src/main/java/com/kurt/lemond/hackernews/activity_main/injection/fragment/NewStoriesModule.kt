package com.kurt.lemond.hackernews.activity_main.injection.fragment

import com.kurt.lemond.hackernews.activity_main.injection.fragment.qualifier.NewStoriesPage
import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.NewStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.fragment.NewStoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.StoryDetailsService
import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.NewStoriesService
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesFragmentViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class NewStoriesModule {

    @Provides
    @NewStoriesFragmentScope
    fun provideNewStoriesService(retrofit: Retrofit): NewStoriesService {
        return retrofit.create(NewStoriesService::class.java)
    }

    @Provides
    @NewStoriesFragmentScope
    @NewStoriesPage
    fun provideStoryDetailsService(retrofit: Retrofit): StoryDetailsService {
        return retrofit.create(StoryDetailsService::class.java)
    }

    @Provides
    @NewStoriesFragmentScope
    @NewStoriesPage
    fun provideStoriesRepository(newsStoriesService: NewStoriesService, @NewStoriesPage storyDetailsService: StoryDetailsService): DataRepository<StoryDetails> {
        return NewStoriesRepository(newsStoriesService, storyDetailsService)
    }

    @Provides
    @NewStoriesFragmentScope
    @NewStoriesPage
    fun provideViewModelFactory(@NewStoriesPage storyRepository: DataRepository<StoryDetails>): StoriesFragmentViewModelFactory {
        return StoriesFragmentViewModelFactory(storyRepository)
    }

}