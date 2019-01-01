package com.kurt.lemond.hackernews.activity_main.injection.fragment

import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.BestStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.NewStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.injection.fragment.scope.TopStoriesFragmentScope
import com.kurt.lemond.hackernews.activity_main.ui.fragment.BestStoriesFragment
import com.kurt.lemond.hackernews.activity_main.ui.fragment.NewStoriesFragment
import com.kurt.lemond.hackernews.activity_main.ui.fragment.TopStoriesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsBinderModule {

    @BestStoriesFragmentScope
    @ContributesAndroidInjector(modules = [BestStoriesModule::class])
    internal abstract fun bindBestStoriesFragment(): BestStoriesFragment

    @NewStoriesFragmentScope
    @ContributesAndroidInjector(modules = [NewStoriesModule::class])
    internal abstract fun bindNewStoriesFragment(): NewStoriesFragment

    @TopStoriesFragmentScope
    @ContributesAndroidInjector(modules = [TopStoriesModule::class])
    internal abstract fun bindTopStoriesFragment(): TopStoriesFragment
}