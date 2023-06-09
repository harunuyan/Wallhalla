package com.volie.wallhalla.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.volie.wallhalla.R
import com.volie.wallhalla.data.model.Media
import com.volie.wallhalla.databinding.ItemFeedBinding

class FeedAdapter(
    val onItemClick: (photo: Media) -> Unit,
    val onFavClick: (photo: Media, position: Int) -> Unit
) : ListAdapter<Media, FeedAdapter.FeedViewHolder>(
    PhotoDiffCallBack()
) {

    inner class FeedViewHolder(
        private val binding: ItemFeedBinding,
        onFavClick: (photo: Media, position: Int) -> Unit,
        onItemClick: (photo: Media) -> Unit
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
                if (item.isLiked) {
                    ivFeedItemFav.setImageResource(R.drawable.ic_favorited)
                } else {
                    ivFeedItemFav.setImageResource(R.drawable.ic_fav)
                }
                if (item.type == "Video") {
                    Glide.with(root.context)
                        .load(item.image)
                        .into(ivFeedItem)

                    ivPlayVideo.visibility = ViewGroup.VISIBLE
                    tvUserName.visibility = ViewGroup.VISIBLE
                    if (item.user?.name.isNullOrEmpty()) {
                        tvUserName.visibility = View.GONE
                    } else {
                        tvUserName.visibility = View.VISIBLE
                        tvUserName.text = item.user?.name
                    }

                } else {
                    Glide.with(root.context)
                        .load(item.src?.large2x)
                        .into(ivFeedItem)

                    ivPlayVideo.visibility = ViewGroup.GONE
                    tvUserName.visibility = ViewGroup.GONE
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

    class PhotoDiffCallBack : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }

    }
}
