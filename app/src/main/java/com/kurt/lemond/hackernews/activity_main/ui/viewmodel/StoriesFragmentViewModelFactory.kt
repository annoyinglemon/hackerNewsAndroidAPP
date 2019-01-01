package com.kurt.lemond.hackernews.activity_main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kurt.lemond.hackernews.activity_main.repository.StoriesRepository

class StoriesFragmentViewModelFactory(private val storiesRepository: StoriesRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            StoriesViewModel(storiesRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}