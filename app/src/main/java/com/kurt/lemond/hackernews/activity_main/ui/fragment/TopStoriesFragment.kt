package com.kurt.lemond.hackernews.activity_main.ui.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.kurt.lemond.hackernews.activity_main.injection.fragment.qualifier.TopStoriesPage
import com.kurt.lemond.hackernews.activity_main.ui.StoriesFragment
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesFragmentViewModelFactory
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TopStoriesFragment: StoriesFragment() {

    @Inject
    @field:TopStoriesPage
    lateinit var storiesFragmentViewModelFactory: StoriesFragmentViewModelFactory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun initializeStoriesViewModel(): StoriesViewModel {
        return ViewModelProviders.of(this, storiesFragmentViewModelFactory).get(StoriesViewModel::class.java)
    }

    companion object {
        fun newInstance(): TopStoriesFragment {
            return TopStoriesFragment()
        }
    }

}