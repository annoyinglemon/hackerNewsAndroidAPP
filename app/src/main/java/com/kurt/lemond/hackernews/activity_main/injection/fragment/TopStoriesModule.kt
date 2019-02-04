package com.kurt.lemond.hackernews.activity_main.injection.fragment

import com.kurt.lemond.hackernews.activity_main.injection.fragment.qualifier.TopStoriesPage
import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.TopStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.IdsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.fragment.top_stories.TopStoryIdsRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
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
    fun provideStoriesRepository(topStoriesService: TopStoriesService): IdsDataRepository {
        return TopStoryIdsRepository(topStoriesService)
    }

    @Provides
    @TopStoriesFragmentScope
    @TopStoriesPage
    fun provideViewModelFactory(@TopStoriesPage idsDataRepository: IdsDataRepository, detailsDataRepository: DetailsDataRepository<StoryDetails>): StoriesFragmentViewModelFactory {
        return StoriesFragmentViewModelFactory(idsDataRepository, detailsDataRepository)
    }

}