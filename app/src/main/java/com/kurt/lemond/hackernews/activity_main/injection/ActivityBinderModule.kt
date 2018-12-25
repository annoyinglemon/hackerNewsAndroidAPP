package com.kurt.lemond.hackernews.activity_main.injection

import com.kurt.lemond.hackernews.activity_main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBinderModule {

    @ContributesAndroidInjector(modules = [RetrofitModule::class])
    @MainActivityScope
    abstract fun bindMainActivity(): MainActivity

}