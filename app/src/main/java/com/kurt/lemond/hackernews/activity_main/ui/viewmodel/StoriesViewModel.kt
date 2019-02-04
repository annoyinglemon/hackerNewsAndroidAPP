package com.kurt.lemond.hackernews.activity_main.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.IdsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.activity_main.repository.pagination.ItemDataSourceFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class StoriesViewModel(private val idsDataRepository: IdsDataRepository,
                       private val detailsDataRepository: DetailsDataRepository<StoryDetails>): ViewModel() {

    val idListData = MutableLiveData<DataWrapper<List<Long>>>()

    private var storiesDataSourceFactory: ItemDataSourceFactory<StoryDetails>? = null

    private val disposableCollector = CompositeDisposable()
    private val pageListBuilderConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .setEnablePlaceholders(false)
            .build()

    fun fetchStoryIds() {
        val fetchIdsDisposable = idsDataRepository.getIds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { idsDataWrapper ->
                    idListData.postValue(idsDataWrapper)
                }

        disposableCollector.add(fetchIdsDisposable)
    }

    fun initializeStoryPagedListListData(storyIds: List<Long>): LiveData<PagedList<DataWrapper<StoryDetails>>> {
        storiesDataSourceFactory = ItemDataSourceFactory(storyIds, detailsDataRepository, disposableCollector)
        return LivePagedListBuilder(storiesDataSourceFactory!!, pageListBuilderConfig).build()
    }

    override fun onCleared() {
        disposableCollector.clear()
        super.onCleared()
    }
}