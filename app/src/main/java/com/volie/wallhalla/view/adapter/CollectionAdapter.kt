package com.volie.wallhalla.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.volie.wallhalla.data.model.collection.Collection
import com.volie.wallhalla.databinding.ItemCollectionBinding

class CollectionAdapter(
    val onItemClick: (collection: Collection) -> Unit
) :
    ListAdapter<Collection, CollectionAdapter.CollectionViewHolder>(
        CollectionDiffCallBack()
    ) {
    inner class CollectionViewHolder(private val binding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val item = currentList[position]
            with(binding) {
                Glide.with(root.context)
                    .load("https://picsum.photos/400/200")
                    .into(ivCollectionItem)
                tvCollectionTitleItem.text = item.title
                tvCollectionPhotoCountItem.text = item.photos_count.toString()
                tvCollectionVideoCountItem.text = item.videos_count.toString()
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding =
            ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}

class CollectionDiffCallBack : DiffUtil.ItemCallback<Collection>() {
    override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
        return oldItem == newItem
    }
}
