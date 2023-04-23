package com.volie.wallhalla.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.volie.wallhalla.R
import com.volie.wallhalla.data.model.Photo
import com.volie.wallhalla.databinding.ItemFeedBinding

class FeedAdapter(
    val onItemClick: (photo: Photo) -> Unit,
    val onFavClick: (photo: Photo, position: Int) -> Unit
) : ListAdapter<Photo, FeedAdapter.FeedViewHolder>(
    PhotoDiffCallBack()
) {

    inner class FeedViewHolder(
        private val binding: ItemFeedBinding,
        onFavClick: (photo: Photo, position: Int) -> Unit,
        onItemClick: (photo: Photo) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivFeedItemFav.setOnClickListener {
                onFavClick(currentList[adapterPosition], adapterPosition)
            }

            binding.root.setOnClickListener {
                onItemClick(currentList[adapterPosition])
            }
        }

        fun bind(position: Int) {
            val item = currentList[position]
            with(binding) {
                Glide.with(root.context)
                    .load(item.src?.medium)
                    .into(ivFeedItem)

                if (item.isLiked) {
                    ivFeedItemFav.setImageResource(R.drawable.ic_favorited)
                } else {
                    ivFeedItemFav.setImageResource(R.drawable.ic_fav)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding, onFavClick, onItemClick)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class PhotoDiffCallBack : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }
}
