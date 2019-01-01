package com.kurt.lemond.hackernews.activity_main.ui.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.kurt.lemond.hackernews.activity_main.injection.fragment.qualifier.BestStoriesPage
import com.kurt.lemond.hackernews.activity_main.ui.StoriesFragment
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesFragmentViewModelFactory
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class BestStoriesFragment: StoriesFragment() {

    @Inject
    @field:BestStoriesPage
    lateinit var storiesFragmentViewModelFactory: StoriesFragmentViewModelFactory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun initializeStoriesViewModel(): StoriesViewModel {
        return ViewModelProviders.of(this, storiesFragmentViewModelFactory).get(StoriesViewModel::class.java)
    }

    companion object {
        fun newInstance(): BestStoriesFragment {
            return BestStoriesFragment()
        }
    }

}