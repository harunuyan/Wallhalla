package com.volie.wallhalla.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.volie.wallhalla.R
import com.volie.wallhalla.data.model.Media
import com.volie.wallhalla.databinding.ItemFeedBinding

class CollectionFeedPhotoAdapter(
    val onItemClick: (media: Media) -> Unit,
    val onFavClick: (media: Media, position: Int) -> Unit
) : ListAdapter<Media, CollectionFeedPhotoAdapter.PhotoViewHolder>(
    CollectionFeedPhotoDiffCallBack()
) {
    inner class PhotoViewHolder(
        private val binding: ItemFeedBinding,
        onFavClick: (media: Media, position: Int) -> Unit,
        onItemClick: (media: Media) -> Unit
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
                    .load(item.src?.large2x)
                    .into(ivFeedItem)

                if (item.isLiked) {
                    ivFeedItemFav.setImageResource(R.drawable.ic_favorited)
                } else {
                    ivFeedItemFav.setImageResource(R.drawable.ic_fav)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionFeedPhotoAdapter.PhotoViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding, onFavClick, onItemClick)
    }

    override fun onBindViewHolder(
        holder: CollectionFeedPhotoAdapter.PhotoViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}

class CollectionFeedPhotoDiffCallBack : DiffUtil.ItemCallback<Media>() {
    override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem == newItem
    }

}
