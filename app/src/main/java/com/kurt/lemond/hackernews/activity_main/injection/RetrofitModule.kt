package com.kurt.lemond.hackernews.activity_main.injection


import android.net.ConnectivityManager
import android.util.Log
import com.kurt.lemond.hackernews.BuildConfig
import com.kurt.lemond.hackernews.activity_main.injection.scope.MainActivityScope
import com.kurt.lemond.hackernews.exception.NoInternetException
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private val LOG_TAG = RetrofitModule::class.java.simpleName

@Module
class RetrofitModule {

    @Provides
    @MainActivityScope
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor {message ->
            Log.d(LOG_TAG, message)
        }
        interceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    @Provides
    @MainActivityScope
    fun provideConnectivityInterceptor(connectivityManager: ConnectivityManager): Interceptor {
        return Interceptor {chain ->
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo == null || !netInfo.isConnected) {
                throw NoInternetException()
            }
            val builder = chain.request().newBuilder()
            chain.proceed(builder.build())
        }
    }

    @Provides
    @MainActivityScope
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, connectivityInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @MainActivityScope
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

}