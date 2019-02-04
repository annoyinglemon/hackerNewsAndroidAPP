package com.kurt.lemond.hackernews.activity_main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails

class StoriesFragmentViewModelFactory(private val dataRepository: DataRepository<StoryDetails>): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            StoriesViewModel(dataRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}