package com.kurt.lemond.hackernews.activity_main.injection.fragment

import com.kurt.lemond.hackernews.activity_main.injection.fragment.qualifier.NewStoriesPage
import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.NewStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.IdsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.fragment.new_stories.NewStoryIdsRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
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
    fun provideStoryIdsRepository(newsStoriesService: NewStoriesService): IdsDataRepository {
        return NewStoryIdsRepository(newsStoriesService)
    }

    @Provides
    @NewStoriesFragmentScope
    @NewStoriesPage
    fun provideViewModelFactory(@NewStoriesPage idsDataRepository: IdsDataRepository, detailsDataRepository: DetailsDataRepository<StoryDetails>): StoriesFragmentViewModelFactory {
        return StoriesFragmentViewModelFactory(idsDataRepository, detailsDataRepository)
    }

}