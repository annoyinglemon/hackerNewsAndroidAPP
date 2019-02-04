package com.kurt.lemond.hackernews.activity_main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.IdsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails

class StoriesFragmentViewModelFactory(private val idsDataRepository: IdsDataRepository,
                                      private val detailsDataRepository: DetailsDataRepository<StoryDetails>): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            StoriesViewModel(idsDataRepository, detailsDataRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}