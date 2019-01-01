package com.kurt.lemond.hackernews.activity_main.ui.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kurt.lemond.hackernews.activity_main.repository.StoriesRepository
import com.kurt.lemond.hackernews.activity_main.repository.pagination.StoriesDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer


class StoriesViewModel(storiesRepository: StoriesRepository): ViewModel() {

    private val disposableCollector = CompositeDisposable()

    val onFetchError = MutableLiveData<Throwable>()
    val swipeRefreshLoadingVisibility = MutableLiveData<Int>()

    private val errorConsumer = Consumer<Throwable> {
        onFetchError.value = it
    }

    private val bestStoriesDataSourceFactory = StoriesDataSourceFactory(storiesRepository, errorConsumer, disposableCollector)
    private val bestStoriesDataSource =  bestStoriesDataSourceFactory.create()

    private val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(true)
            .build()

    val bestStoriesList = LivePagedListBuilder(bestStoriesDataSourceFactory, config).build()

    init {
        // this does not work!!
        storiesRepository.getStoryIds()
                .doOnSubscribe {
                    swipeRefreshLoadingVisibility.value = View.VISIBLE
                }
                .doOnSuccess {
                    swipeRefreshLoadingVisibility.value = View.GONE
                }
    }

    fun onSwipeToRefresh() {
        bestStoriesDataSource.invalidate()
    }

    override fun onCleared() {
        disposableCollector.clear()
        super.onCleared()
    }
}