package com.kurt.lemond.hackernews.activity_main.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.kurt.lemond.hackernews.R
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesViewModel
import com.kurt.lemond.hackernews.databinding.FragmentStoriesBinding
import io.reactivex.disposables.CompositeDisposable

abstract class StoriesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fragmentStoriesBinding: FragmentStoriesBinding
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var viewModel: StoriesViewModel
    private val disposableCollector = CompositeDisposable()

    private var errorSnackbar: Snackbar? = null

    override fun onDetach() {
        disposableCollector.clear()
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stories, container, false)
        fragmentStoriesBinding.swipeRefreshStories.setOnRefreshListener(this)

        viewModel = initializeStoriesViewModel()

        storiesAdapter = StoriesAdapter()

        fragmentStoriesBinding.recyclerViewStories.layoutManager = LinearLayoutManager(activity)
        fragmentStoriesBinding.recyclerViewStories.adapter = storiesAdapter
        fragmentStoriesBinding.recyclerViewStories.addItemDecoration(DividerItemDecoration(activity, VERTICAL))

        viewModel.bestStoriesList.observe(this, Observer { pagedList ->
            storiesAdapter.submitList(pagedList)
            Log.d("StoriesFragment", "pagedList.size: ${pagedList.size}")
        })

        viewModel.swipeRefreshLoadingVisibility.observe(this, Observer { visibility ->
            fragmentStoriesBinding.swipeRefreshStories.isRefreshing = (visibility == View.VISIBLE)
        })

        viewModel.fetchIdListState.observe(this, Observer { state ->
            Log.d("StoriesFragment", "fetchIdListState: $state")

            when (state) {
                DataWrapper.State.NO_NETWORK -> {

                    if (errorSnackbar == null) {
                        errorSnackbar = Snackbar.make(fragmentStoriesBinding.root, "No Network", Snackbar.LENGTH_INDEFINITE)
                    }
                    errorSnackbar?.setText("No Network")
                    errorSnackbar?.show()
                }

                DataWrapper.State.ERROR -> {
                    if (errorSnackbar == null) {
                        errorSnackbar = Snackbar.make(fragmentStoriesBinding.root, "Error occurred", Snackbar.LENGTH_INDEFINITE)
                    }
                    errorSnackbar?.setText("Error occurred")
                    errorSnackbar?.show()
                }
                else -> errorSnackbar?.dismiss()
            }
        })

        return fragmentStoriesBinding.root
    }

    override fun onRefresh() {
        viewModel.onSwipeToRefresh()
    }

    abstract fun initializeStoriesViewModel(): StoriesViewModel

}