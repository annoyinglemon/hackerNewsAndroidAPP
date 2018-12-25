package com.kurt.lemond.hackernews.injection_app

import android.app.Application
import com.kurt.lemond.hackernews.HackerNewsApplication
import com.kurt.lemond.hackernews.activity_main.injection.ActivityBinderModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@ApplicationScope
@Component(modules = [AndroidInjectionModule::class, AppContextModule::class, ActivityBinderModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindApplication(application: Application): Builder

        fun build(): ApplicationComponent
    }

    fun inject(app: HackerNewsApplication)

}