package com.volie.wallhalla.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.volie.wallhalla.data.model.Photo
import com.volie.wallhalla.databinding.ItemFeedBinding

class FeedAdapter : ListAdapter<Photo, FeedAdapter.FeedViewHolder>(
    PhotoDiffCallBack()
) {
    inner class FeedViewHolder(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = currentList[position]
            with(binding) {
                Glide.with(root.context)
                    .load(item.src?.medium)
                    .into(ivFeedItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}

class PhotoDiffCallBack : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }

}
