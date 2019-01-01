package com.kurt.lemond.hackernews.activity_main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kurt.lemond.hackernews.R
import com.kurt.lemond.hackernews.activity_main.repository.model.StoryDetails
import com.kurt.lemond.hackernews.databinding.ItemStoryBinding


class StoriesAdapter: PagedListAdapter<StoryDetails, StoriesAdapter.StoryViewHolder>(STORY_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val itemStoryBinding = DataBindingUtil.inflate<ItemStoryBinding>(LayoutInflater.from(parent.context), R.layout.item_story, parent, false)
        return StoryViewHolder(itemStoryBinding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val storyDetail = getItem(position)
        holder.itemStoryBinding.storyDetail = storyDetail

        if (storyDetail == null) {
            holder.itemStoryBinding.shimmerContainer.startShimmerAnimation()
        } else {
            holder.itemStoryBinding.shimmerContainer.stopShimmerAnimation()
        }

    }

    class StoryViewHolder(val itemStoryBinding: ItemStoryBinding): RecyclerView.ViewHolder(itemStoryBinding.root)

    companion object {
        private val STORY_COMPARATOR = object : DiffUtil.ItemCallback<StoryDetails>() {
            override fun areItemsTheSame(oldItem: StoryDetails, newItem: StoryDetails): Boolean =
                    oldItem.itemId == newItem.itemId

            override fun areContentsTheSame(oldItem: StoryDetails, newItem: StoryDetails): Boolean =
                    oldItem == newItem
        }
    }
}