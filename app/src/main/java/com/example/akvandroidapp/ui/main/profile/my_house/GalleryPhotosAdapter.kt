package com.example.akvandroidapp.ui.main.profile.my_house

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.gallery_photo_recycler_view_item.view.*

class GalleryPhotosAdapter(
    private val requestManager: RequestManager,
    private val closeInteraction: PhotoCloseInteraction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var photos: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(
            requestManager,
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_photo_recycler_view_item, parent, false),
            closeInteraction = closeInteraction
        )
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PhotoViewHolder -> {
                holder.bind(photos[position])
            }
        }
    }

    fun submitList(items: MutableList<String>){
        this.photos = items
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        photos.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getPhotos(): MutableList<String> {
        return ArrayList(photos)
    }

    class PhotoViewHolder constructor(
        val requestManager: RequestManager,
        photoView: View,
        private val closeInteraction: PhotoCloseInteraction?
    ) : RecyclerView.ViewHolder(photoView) {
        private val closeIv = photoView.gallery_photo_recycler_view_item_close_iv
        private val imageIv = photoView.gallery_photo_recycler_view_item_iv

        fun bind(image: String?){
            if (image != null)
                requestManager
                    .load(image)
                    .error(R.drawable.test_image_back)
                    .transition(withCrossFade())
                    .into(imageIv)
            else
                requestManager
                    .load(R.drawable.default_image)
                    .transition(withCrossFade())
                    .into(imageIv)

            closeIv.setOnClickListener {
                closeInteraction?.onItemClosed(adapterPosition, image)
            }
        }

    }


    interface PhotoCloseInteraction{
        fun onItemClosed(position: Int, item: String?)
    }

}