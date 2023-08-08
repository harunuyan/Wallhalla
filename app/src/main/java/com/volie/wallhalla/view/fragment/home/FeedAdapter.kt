package com.volie.wallhalla.view.fragment.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.volie.wallhalla.R
import com.volie.wallhalla.data.model.Media
import com.volie.wallhalla.databinding.ItemFeedBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FeedAdapter(
    val onItemClick: (photo: Media) -> Unit,
    val onFavClick: (photo: Media, position: Int) -> Unit
) : ListAdapter<Media, FeedAdapter.FeedViewHolder>(
    PhotoDiffCallBack()
) {

    private var job: Job? = null

    inner class FeedViewHolder(
        private val binding: ItemFeedBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivFeedItemFav.setOnClickListener {
                onDoubleTab(adapterPosition, binding.ivDoubleClickHeart)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(position: Int) {
            val item = currentList[position]
            val gestureDetector = GestureDetector(
                binding.root.context,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                        onItemClick(item)
                        return true
                    }

                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        onDoubleTab(position, binding.ivDoubleClickHeart)
                        return true
                    }
                })
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
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.ivFeedItem.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.ivFeedItem.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                return false
                            }
                        })
                        .into(ivFeedItem)

                    ivPlayVideo.visibility = ViewGroup.GONE
                    tvUserName.visibility = ViewGroup.GONE
                }
                root.setOnTouchListener { _, event ->
                    gestureDetector.onTouchEvent(event)
                    true
                }
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

    private fun onDoubleTab(position: Int, imageView: LottieAnimationView) {
        val item = currentList[position]
        imageView.visibility = View.VISIBLE
        if (!item.isLiked) {
            imageView.setAnimation("animation_heart.json")
            item.isLiked = true
        } else {
            imageView.setAnimation("animation_heart_break.json")
            item.isLiked = false
        }
        imageView.playAnimation()

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(800)
            imageView.visibility = View.GONE
            imageView.cancelAnimation()
            onFavClick(item, position)
        }
    }
}

class PhotoDiffCallBack : DiffUtil.ItemCallback<Media>() {
    override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
        return oldItem == newItem
    }
}