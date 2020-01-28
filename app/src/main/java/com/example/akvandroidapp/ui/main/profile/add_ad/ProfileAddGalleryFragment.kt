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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.*
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.Constants.Companion.GALLERY_REQUEST_CODE
import com.example.akvandroidapp.util.ErrorHandling.Companion.ERROR_SOMETHING_WRONG_WITH_IMAGE
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_ad.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_gallery.*
import javax.inject.Inject

class ProfileAddGalleryFragment : BaseAddHouseFragment(){

    private val myDataTransfer = arrayOf<Bundle?>(null)
    private var onlineId:String? = null

    @Inject
    lateinit var sessionManager: SessionManager

    var image1: Uri? = null
    var image2: Uri? = null
    var image3: Uri? = null
    var image4: Uri? = null
    var image5: Uri? = null
    var image6: Uri? = null

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

        fragment_add_ad_gallery_next_btn.setOnClickListener {
            sessionManager.setAddAdImage(image1,image2,image3,image4,image5,image6)
            navNextFragment()
        }

        fragment_add_ad_gallery_iv1.setOnClickListener {
            if(stateChangeListener.isStoragePermissionGranted()){
                pickFromGallery("fragment_add_ad_gallery_iv1")
            }
        }


        fragment_add_ad_gallery_iv2.setOnClickListener {
            if(stateChangeListener.isStoragePermissionGranted()){
                pickFromGallery("fragment_add_ad_gallery_iv2")
            }
        }

        fragment_add_ad_gallery_iv3.setOnClickListener {
            if(stateChangeListener.isStoragePermissionGranted()){
                pickFromGallery("fragment_add_ad_gallery_iv3")
            }
        }

        fragment_add_ad_gallery_iv4.setOnClickListener {
            if(stateChangeListener.isStoragePermissionGranted()){
                pickFromGallery("fragment_add_ad_gallery_iv4")
            }
        }

        fragment_add_ad_gallery_iv5.setOnClickListener {
            if(stateChangeListener.isStoragePermissionGranted()){
                pickFromGallery("fragment_add_ad_gallery_iv5")
            }
        }
        fragment_add_ad_gallery_iv6.setOnClickListener {
            if(stateChangeListener.isStoragePermissionGranted()){
                pickFromGallery("fragment_add_ad_gallery_iv6")
            }
        }
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileAddGalleryFragment_to_profileAddDescriptionFragment)
    }

    private fun pickFromGallery(imageView:String) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        val myData = Bundle()
        myData.putString("imageView", imageView)
        myDataTransfer[GALLERY_REQUEST_CODE] = myData

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

                    setBlogProperties(resultUri, onlineId)
                }

                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    Log.d(TAG, "CROP: ERROR")
                    showErrorDialog(ERROR_SOMETHING_WRONG_WITH_IMAGE)
                }
            }
        }
    }

    private fun setBlogProperties(image: Uri?, imageView: String?){
        if(image != null){
            imageView?.let {
                if(it == "fragment_add_ad_gallery_iv1"){
                    image1 = image
                    Glide.with(this).load(image).into(fragment_add_ad_gallery_iv1)
                }
                if(it == "fragment_add_ad_gallery_iv2"){
                    image2 = image
                    Glide.with(this).load(image).into(fragment_add_ad_gallery_iv2)
                }
                if(it == "fragment_add_ad_gallery_iv3"){
                    image3 = image
                    Glide.with(this).load(image).into(fragment_add_ad_gallery_iv3)
                }
                if(it == "fragment_add_ad_gallery_iv4"){
                    image4 = image
                    Glide.with(this).load(image).into(fragment_add_ad_gallery_iv4)
                }
                if(it == "fragment_add_ad_gallery_iv5"){
                    image5 = image
                    Glide.with(this).load(image).into(fragment_add_ad_gallery_iv5)
                }
                if(it == "fragment_add_ad_gallery_iv6"){
                    image6 = image
                    Glide.with(this).load(image).into(fragment_add_ad_gallery_iv6)
                }
            }
        }
        else{
            Glide.with(this).load(R.drawable.default_image).into(fragment_add_ad_gallery_iv1)
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
            findNavController().navigateUp()
        }

        fragment_add_ad_gallery_cancel.setOnClickListener {
            activity?.finish()
        }
    }
}

















