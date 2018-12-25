package com.kurt.lemond.hackernews.injection_app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides

@Module
class AppContextModule {

    @Provides
    @ApplicationScope
    fun provideAppContext(application: Application): Context  {
        return application.applicationContext
    }

    @Provides
    @ApplicationScope
    fun provideConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

}