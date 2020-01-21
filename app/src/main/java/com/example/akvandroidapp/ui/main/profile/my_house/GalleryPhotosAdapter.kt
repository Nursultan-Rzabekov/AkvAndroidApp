package com.example.akvandroidapp.ui.main.profile.my_house

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.gallery_add_photo_item.view.*
import kotlinx.android.synthetic.main.gallery_photo_recycler_view_item.view.*

class GalleryPhotosAdapter(
    private val requestManager: RequestManager,
    private val closeInteraction: PhotoCloseInteraction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var photos: MutableList<GalleryPhoto> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            0 -> {
                return PhotoViewHolder(
                    requestManager,
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.gallery_photo_recycler_view_item,
                        parent,
                        false
                    ),
                    closeInteraction = closeInteraction
                )
            }
            else -> {
                return AddPhotoViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.gallery_add_photo_item,
                        parent,
                        false
                    ),
                    addInteraction = closeInteraction
                )
            }
        }
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PhotoViewHolder -> {
                holder.bind(photos[position])
            }
            is AddPhotoViewHolder -> {
                holder.bind(photos[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (photos[position].url == null && photos[position].uri == null)
            return 1
        return 0
    }

    fun submitList(items: MutableList<GalleryPhoto>){
        this.photos = items
        photos.add(GalleryPhoto(null, null))
        notifyDataSetChanged()
        Log.e("GALLERYADAPTER", "${photos}")
    }

    fun addGalleryPhoto(item: GalleryPhoto){
        photos.add(photos.size-1, item)
        notifyItemInserted(photos.size-2)
        notifyItemRangeChanged(photos.size-2, photos.size)
        notifyDataSetChanged()
        Log.e("GALLERYADAPTER", "${photos}")
    }

    fun removeItem(position: Int) {
        photos.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getPhotos(): MutableList<GalleryPhoto> {
        return ArrayList(photos)
    }

    class PhotoViewHolder constructor(
        val requestManager: RequestManager,
        photoView: View,
        private val closeInteraction: PhotoCloseInteraction?
    ) : RecyclerView.ViewHolder(photoView) {
        private val closeIv = photoView.gallery_photo_recycler_view_item_close_iv
        private val imageIv = photoView.gallery_photo_recycler_view_item_iv

        fun bind(image: GalleryPhoto?){
            if (image != null) {
                requestManager
                    .load(if (image.url != null) image.url else image.uri)
                    .error(R.drawable.test_image_back)
                    .transition(withCrossFade())
                    .into(imageIv)
            }
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

    class AddPhotoViewHolder(
        photoView: View,
        private val addInteraction: PhotoCloseInteraction?
    ) : RecyclerView.ViewHolder(photoView){
        private val iv = photoView.gallery_add_photo_item_iv

        fun bind(image: GalleryPhoto?){
            iv.setOnClickListener {
                addInteraction?.onAddPressed(adapterPosition, image)
            }
        }
    }


    interface PhotoCloseInteraction{
        fun onItemClosed(position: Int, item: GalleryPhoto?)
        fun onAddPressed(position: Int, item: GalleryPhoto?)
    }

}

data class GalleryPhoto(
    var url: String?,
    var uri: Uri?
)