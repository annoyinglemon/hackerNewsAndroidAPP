package com.kurt.lemond.hackernews.activity_main.injection.fragment

import com.kurt.lemond.hackernews.activity_main.repository.retrofit.fragment.BestStoriesService
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesFragmentViewModelFactory
import com.kurt.lemond.hackernews.activity_main.injection.fragment.qualifier.BestStoriesPage
import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.BestStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.IdsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.fragment.best_stories.BestStoryIdsRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
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
    fun provideStoryIdsRepository(bestStoriesService: BestStoriesService): IdsDataRepository {
        return BestStoryIdsRepository(bestStoriesService)
    }

    @Provides
    @BestStoriesFragmentScope
    @BestStoriesPage
    fun provideViewModelFactory(@BestStoriesPage idsDataRepository: IdsDataRepository, storyRepositoryDetails: DetailsDataRepository<StoryDetails>): StoriesFragmentViewModelFactory {
        return StoriesFragmentViewModelFactory(idsDataRepository, storyRepositoryDetails)
    }

}