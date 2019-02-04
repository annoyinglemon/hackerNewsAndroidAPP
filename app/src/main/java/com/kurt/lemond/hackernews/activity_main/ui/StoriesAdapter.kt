package com.kurt.lemond.hackernews.activity_main.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kurt.lemond.hackernews.R
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.databinding.ItemStoryBinding


class StoriesAdapter: PagedListAdapter<DataWrapper<StoryDetails>, StoriesAdapter.StoryViewHolder>(STORY_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val itemStoryBinding = DataBindingUtil.inflate<ItemStoryBinding>(LayoutInflater.from(parent.context), R.layout.item_story, parent, false)
        return StoryViewHolder(itemStoryBinding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val dataWrapper = getItem(position)

        val storyItemBinder = StoryItemBinder(dataWrapper)
        holder.itemStoryBinding.storyItemBinder = storyItemBinder

        if (dataWrapper == null) {
            holder.itemStoryBinding.shimmerContainer.startShimmerAnimation()
        } else {
            holder.itemStoryBinding.shimmerContainer.stopShimmerAnimation()
        }
    }

    class StoryViewHolder(val itemStoryBinding: ItemStoryBinding): RecyclerView.ViewHolder(itemStoryBinding.root)

    companion object {
        private val STORY_COMPARATOR = object : DiffUtil.ItemCallback<DataWrapper<StoryDetails>>() {
            override fun areItemsTheSame(oldItem: DataWrapper<StoryDetails>, newItem: DataWrapper<StoryDetails>): Boolean =
                    areDataWrappersTheSame(oldItem, newItem)

            override fun areContentsTheSame(oldItem: DataWrapper<StoryDetails>, newItem: DataWrapper<StoryDetails>): Boolean =
                    areDataWrappersTheSame(oldItem, newItem)
        }

        private fun areDataWrappersTheSame(oldItem: DataWrapper<StoryDetails>, newItem: DataWrapper<StoryDetails>): Boolean {
            return oldItem.errorMessage == newItem.errorMessage &&
                    oldItem.getState() == newItem.getState() &&
                    oldItem.data == newItem.data
        }
    }

    class StoryItemBinder(private val dataWrapper: DataWrapper<StoryDetails>?) {

        fun getContentVisibility(): Int {
            return if (dataWrapper != null && dataWrapper.getState() == DataWrapper.State.SUCCESS) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        fun getLoadingVisibility(): Int {
            return if (dataWrapper == null) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        fun getErrorVisibility(): Int {
            return if (dataWrapper != null && (dataWrapper.getState() == DataWrapper.State.ERROR
                            || dataWrapper.getState() == DataWrapper.State.NO_NETWORK ) ) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        fun getAuthor(): String {
            return if (dataWrapper?.data != null) {
                dataWrapper.data!!.author
            } else {
                ""
            }
        }

        fun getTitle(): String {
            return if (dataWrapper?.data != null) {
                dataWrapper.data!!.title
            } else {
                ""
            }
        }

        fun getScore(): Int {
            return if (dataWrapper?.data != null) {
                dataWrapper.data!!.score
            } else {
                0
            }
        }

        fun getTimeInUnix(): Long {
            return if (dataWrapper?.data != null) {
                dataWrapper.data!!.timeInUnix
            } else {
                0
            }
        }

        fun getErrorMessage(): String? {
            return if (dataWrapper?.errorMessage != null) {
                when {
                    dataWrapper.getState() == DataWrapper.State.NO_NETWORK -> "No network"
                    dataWrapper.getState() == DataWrapper.State.SUCCESS -> ""
                    else -> dataWrapper.errorMessage
                }
            } else {
                ""
            }
        }

    }
}