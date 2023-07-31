package com.volie.wallhalla.view.fragment.collection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.volie.wallhalla.data.model.Collection
import com.volie.wallhalla.databinding.ItemCollectionBinding
import kotlin.random.Random

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
                    .load(getRandomImageUrl())
                    .into(ivCollectionItem)
                tvCollectionTitleItem.text = item.title
                tvCollectionPhotoCountItem.text = "${item.photosCount} photos"
                tvCollectionVideoCountItem.text = "${item.videosCount} videos"
                if (item.description != null) {
                    tvCollectionDescriptionItem.text = item.description
                    tvCollectionDescriptionItem.visibility = View.VISIBLE
                } else {
                    tvCollectionDescriptionItem.visibility = View.GONE
                }
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

    private fun getRandomImageUrl(): String {
        val width = 380 + Random.nextInt(20)
        val height = 190 + Random.nextInt(10)
        return "https://picsum.photos/$width/$height"
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
