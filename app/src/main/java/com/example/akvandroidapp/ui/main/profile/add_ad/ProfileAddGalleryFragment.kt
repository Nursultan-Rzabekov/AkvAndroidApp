package com.example.akvandroidapp.ui.main.profile.add_ad


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.*
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.my_house.GalleryPhoto
import com.example.akvandroidapp.ui.main.profile.my_house.GalleryPhotosAdapter
import com.example.akvandroidapp.util.Constants.Companion.GALLERY_REQUEST_CODE
import com.example.akvandroidapp.util.ErrorHandling.Companion.ERROR_SOMETHING_WRONG_WITH_IMAGE
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_ad.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_gallery.*
import javax.inject.Inject

class ProfileAddGalleryFragment : BaseAddHouseFragment(), GalleryPhotosAdapter.PhotoCloseInteraction {

    private val myDataTransfer = arrayOf<Bundle?>(null)
    private var onlineId:String? = null
    private lateinit var photosAdapter: GalleryPhotosAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        activity_add_ad_toolbar.setNavigationOnClickListener {
//            findNavController().navigateUp()
//        }
        return inflater.inflate(R.layout.fragment_add_ad_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        initRecyclerView()
        setObservers()

        fragment_add_ad_gallery_next_btn.setOnClickListener {
            sessionManager.setAddAdImage(getPhotos())
            navNextFragment()
        }
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileAddGalleryFragment_to_profileAddDescriptionFragment)
    }

    private fun setObservers() {
        sessionManager.addAdInfo.observe(viewLifecycleOwner, Observer {
            val photos = mutableListOf<GalleryPhoto>()
            for (image in it._addAdImage)
                photos.add(
                    GalleryPhoto(null, image)
                )
            photosAdapter.submitList(photos)
        })
    }

    private fun initRecyclerView(){
        fragment_add_ad_gallery_recycler_view.apply {
            layoutManager = GridLayoutManager(
                this@ProfileAddGalleryFragment.context, 3)
            photosAdapter = GalleryPhotosAdapter(
                requestManager = requestManager,
                closeInteraction = this@ProfileAddGalleryFragment
            )
            adapter = photosAdapter
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "CROP: RESULT OK")
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        activity?.let{
                            launchImageCrop(uri)
                        }
                    }?: showErrorDialog(ERROR_SOMETHING_WRONG_WITH_IMAGE)
                    val myData = myDataTransfer[requestCode]
                    onlineId = myData?.getString("imageView")
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE")
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE: uri: ${resultUri}")

                    addGalleryPhoto(resultUri)

                    //setBlogProperties(resultUri, onlineId)
                }

                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    Log.d(TAG, "CROP: ERROR")
                    showErrorDialog(ERROR_SOMETHING_WRONG_WITH_IMAGE)
                }
            }
        }
    }

    private fun launchImageCrop(uri: Uri){
        context?.let{
            CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    fun showErrorDialog(errorMessage: String){
        stateChangeListener.onDataStateChange(
            DataState(
                Event(StateError(Response(errorMessage, ResponseType.Dialog()))),
                Loading(isLoading = false),
                Data(Event.dataEvent(null), null)
            )
        )
    }

    private fun setToolbar(){
        fragment_add_ad_gallery_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_gallery_toolbar.setNavigationOnClickListener{
            sessionManager.clearAddAdImages()
            findNavController().navigateUp()
        }

        fragment_add_ad_gallery_cancel.setOnClickListener {
            activity?.finish()
            sessionManager.clearAddAdAllInfo()
        }
    }

    override fun onItemClosed(position: Int, item: GalleryPhoto?) {
        photosAdapter.removeItem(position)
    }

    override fun onAddPressed(position: Int, item: GalleryPhoto?) {
        if(stateChangeListener.isStoragePermissionGranted()){
            pickFromGallery()
        }
    }

    private fun addGalleryPhoto(uri: Uri){
        photosAdapter.addGalleryPhoto(GalleryPhoto(null, uri))
    }

    private fun getPhotos(): List<Uri> {
        val photos = mutableListOf<Uri>()
        for (photo in photosAdapter.getPhotos())
            photos.add(
                photo.uri?: Uri.parse("/////")
            )
        return photos.toList()
    }
}

















