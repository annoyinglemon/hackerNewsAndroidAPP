package com.kurt.lemond.hackernews.activity_main.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.pagination.ItemDataSource
import com.kurt.lemond.hackernews.activity_main.repository.pagination.ItemDataSourceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class StoriesViewModel(dataRepository: DataRepository<StoryDetails>): ViewModel() {

    private val disposableCollector = CompositeDisposable()

    val fetchIdListState = MutableLiveData<DataWrapper.State>()

    val swipeRefreshLoadingVisibility = MutableLiveData<Int>()

    private val bestStoriesDataSourceFactory = ItemDataSourceFactory(dataRepository, disposableCollector)
    private val bestStoriesDataSource =  bestStoriesDataSourceFactory.create()

    private val config = PagedList.Config.Builder()
            .setPageSize(5)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(true)
            .build()

    val bestStoriesList = LivePagedListBuilder(bestStoriesDataSourceFactory, config).build()

    init {
        if (bestStoriesDataSource is ItemDataSource) {
            val disposable = bestStoriesDataSource.itemIdsStateObservable
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("StoriesViewModel", "fetchIdListState: $it")
                        fetchIdListState.value = it
                    }
            disposableCollector.add(disposable)
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