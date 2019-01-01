package com.kurt.lemond.hackernews.activity_main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kurt.lemond.hackernews.R
import com.kurt.lemond.hackernews.activity_main.ui.viewmodel.StoriesViewModel
import com.kurt.lemond.hackernews.databinding.FragmentStoriesBinding
import io.reactivex.disposables.CompositeDisposable

abstract class StoriesFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fragmentStoriesBinding: FragmentStoriesBinding
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var viewModel: StoriesViewModel

    private val disposableCollector = CompositeDisposable()

    override fun onDetach() {
        disposableCollector.clear()
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stories, container, false)

        viewModel = initializeStoriesViewModel()

        storiesAdapter = StoriesAdapter()

        fragmentStoriesBinding.recyclerViewStories.layoutManager = LinearLayoutManager(activity)
        fragmentStoriesBinding.recyclerViewStories.adapter = storiesAdapter
        fragmentStoriesBinding.recyclerViewStories.addItemDecoration(DividerItemDecoration(activity, VERTICAL))

        viewModel.bestStoriesList.observe(this, Observer { pagedList ->
            storiesAdapter.submitList(pagedList)
        })

        viewModel.swipeRefreshLoadingVisibility.observe(this, Observer { visibility ->
            fragmentStoriesBinding.swipeRefreshStories.isRefreshing = (visibility == View.VISIBLE)
        })

        viewModel.onFetchError.observe(this, Observer {throwable ->
            // todo change this error placeholder
            Toast.makeText(activity, throwable.message, Toast.LENGTH_LONG).show()
        })

        return fragmentStoriesBinding.root
    }

    override fun onRefresh() {
        viewModel.onSwipeToRefresh()
    }

    abstract fun initializeStoriesViewModel(): StoriesViewModel

}