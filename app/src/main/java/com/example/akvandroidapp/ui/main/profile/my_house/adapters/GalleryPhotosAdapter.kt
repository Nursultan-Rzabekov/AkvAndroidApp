package com.example.akvandroidapp.ui.main.profile.my_house.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.gallery_add_photo_item.view.*
import kotlinx.android.synthetic.main.gallery_photo_recycler_view_item.view.*

class GalleryPhotosAdapter(
    private val requestManager: RequestManager,
    private val closeInteraction: PhotoCloseInteraction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MAX_PHOTOS: Int = 15
    private var counter: Int = 0

    val DIFF_CALLBACK = object: DiffUtil.ItemCallback<GalleryPhoto>(){
        override fun areItemsTheSame(oldItem: GalleryPhoto, newItem: GalleryPhoto): Boolean {
            if (oldItem.url != null && newItem.url != null)
                if (oldItem.url.equals(newItem.url))
                    return true
            else if(oldItem.uri != null && newItem.uri != null)
                    if (oldItem.uri == newItem.uri)
                        return true
            return false
        }

        override fun areContentsTheSame(oldItem: GalleryPhoto, newItem: GalleryPhoto): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
        AsyncListDiffer(
            BlogRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

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

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: GalleryPhotosAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PhotoViewHolder -> {
                holder.bind(differ.currentList[position])
            }
            is AddPhotoViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList[position].url == null && differ.currentList[position].uri == null)
            return 1
        return 0
    }

    fun submitList(items: MutableList<GalleryPhoto>){
        val newList = items.toMutableList()
        counter = newList.size
        newList.add(
            GalleryPhoto(
                null,
                null
            )
        )
        differ.submitList(newList)
        Log.e("GALLERYADAPTER", "${differ.currentList}")
    }

    fun addGalleryPhoto(item: GalleryPhoto){
        if (differ.currentList.size < MAX_PHOTOS + 1) {
            val newList = differ.currentList.toMutableList()
            newList.add(differ.currentList.size - 1, item)
            counter += 1

            if (newList.size == MAX_PHOTOS + 1)
                newList.removeAt(newList.lastIndex)

            differ.submitList(newList)
            Log.e("GALLERYADAPTER", "${differ.currentList}")
        }
    }

    fun removeItem(position: Int) {
        val newList = differ.currentList.toMutableList()

        if (counter == MAX_PHOTOS)
            newList.add(
                GalleryPhoto(
                    null,
                    null
                )
            )

        newList.removeAt(position)

        counter -= 1
        differ.submitList(newList)
    }

    fun getPhotos(): MutableList<GalleryPhoto> {
        val allPhotos = mutableListOf<GalleryPhoto>()
        for (photo in differ.currentList)
            if (photo.uri != null || photo.uri != null)
                allPhotos.add(photo)
        return allPhotos
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