package com.kurt.lemond.hackernews.activity_main.ui

import android.os.Bundle
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


abstract class StoriesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fragmentStoriesBinding: FragmentStoriesBinding
    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var viewModel: StoriesViewModel

    private var errorSnackBar: Snackbar? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stories, container, false)

        fragmentStoriesBinding.swipeRefreshStories.setOnRefreshListener(this)
        fragmentStoriesBinding.swipeRefreshStories.isRefreshing = true

        viewModel = initializeStoriesViewModel()
        storiesAdapter = StoriesAdapter()

        fragmentStoriesBinding.recyclerViewStories.layoutManager = LinearLayoutManager(activity)
        fragmentStoriesBinding.recyclerViewStories.adapter = storiesAdapter
        fragmentStoriesBinding.recyclerViewStories.addItemDecoration(DividerItemDecoration(activity, VERTICAL))

        viewModel.idListData.observe(this, Observer { idListDataWrapper ->
            if (idListDataWrapper.getState() == DataWrapper.State.SUCCESS) {
                idListDataWrapper.data?.let {
                    refreshStoriesList(idListDataWrapper.data!!)
                }
                errorSnackBar?.dismiss()
            } else {
                idListDataWrapper.errorMessage?.let {
                    errorSnackBar = Snackbar.make(activity!!.findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG)
                    errorSnackBar?.show()
                }
                fragmentStoriesBinding.swipeRefreshStories.isRefreshing = false
            }
        })

        viewModel.fetchStoryIds()

        return fragmentStoriesBinding.root
    }

    override fun onRefresh() {
        viewModel.fetchStoryIds()
    }

    private fun refreshStoriesList(storyIds: List<Long>) {
        val pageListLiveData = viewModel.initializeStoryPagedListListData(storyIds)
        pageListLiveData.observe(this, Observer {pagedList ->
            storiesAdapter.submitList(pagedList)
            fragmentStoriesBinding.swipeRefreshStories.isRefreshing = false
        })
    }

    abstract fun initializeStoriesViewModel(): StoriesViewModel

}