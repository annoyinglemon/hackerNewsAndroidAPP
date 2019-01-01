package com.kurt.lemond.hackernews.activity_main.injection

import com.kurt.lemond.hackernews.activity_main.ui.MainActivity
import com.kurt.lemond.hackernews.activity_main.injection.fragment.MainFragmentsBinderModule
import com.kurt.lemond.hackernews.activity_main.injection.scope.MainActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBinderModule {

    @ContributesAndroidInjector(modules = [RetrofitModule::class, MainFragmentsBinderModule:: class])
    @MainActivityScope
    abstract fun bindMainActivity(): MainActivity

}