package com.example.akvandroidapp.ui.main.profile.account


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.*
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.PasswordChecker
import com.example.akvandroidapp.util.SuccessHandling
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_gallery.*
import kotlinx.android.synthetic.main.fragment_profile_account_edit.*
import kotlinx.android.synthetic.main.header_profile_account.*
import kotlinx.android.synthetic.main.header_profile_account_edit.*
import kotlinx.android.synthetic.main.sign_up_pass.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.inject.Inject


class AccountUserEditProfileFragment : BaseProfileFragment() {

    @Inject
    lateinit var sessionManager: SessionManager
    var image1: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_account_edit_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)

        Log.d(TAG, "SearchFragment: ${viewModel}")

        attachUserAccounts()

        header_profile_account_edit_cancel_tv.setOnClickListener {
            findNavController().navigateUp()
        }

        header_profile_account_edit_save_tv.setOnClickListener {
            saveUserAccounts()
            findNavController().navigateUp()
        }

        header_profile_account_edit_civ.setOnClickListener {
            if(stateChangeListener.isStoragePermissionGranted()){
                pickFromGallery()
            }
        }

        fragment_profile_account_edit_birth_tv.setOnClickListener {
            showDatePicker(fragment_profile_account_edit_birth_tv.text.toString())
        }
    }

    private fun getGender(): Int{
        when(fragment_profile_account_edit_gender_radio_group.checkedRadioButtonId){
            fragment_profile_account_edit_woman_btn.id -> return 0
            fragment_profile_account_edit_man_btn.id -> return 1
        }
        return 0
    }

    private fun saveUserAccounts(){
        sessionManager.setProfileInfo(
            header_profile_account_edit_tv.text.toString(),
            fragment_profile_account_edit_birth_tv.text.toString(),
            getGender(),
            fragment_profile_account_edit_phonenumber_tv.text.toString(),
            fragment_profile_account_edit_email_tv.text.toString())
        editInfo()
        subscribeObservers()
    }

    private fun attachUserAccounts(){
        sessionManager.profileInfo.observe(viewLifecycleOwner, Observer { dataState ->
            fragment_profile_account_edit_birth_tv.text = dataState.birthdate.toString()
            when(dataState.gender) {
                0 -> {
                    fragment_profile_account_edit_gender_radio_group.check(fragment_profile_account_edit_woman_btn.id)
                }
                1 -> {
                    fragment_profile_account_edit_gender_radio_group.check(fragment_profile_account_edit_man_btn.id)
                }
                else -> {
                    fragment_profile_account_edit_gender_radio_group.check(fragment_profile_account_edit_man_btn.id)
                }
            }
            fragment_profile_account_edit_phonenumber_tv.setText(dataState.phonenumber.toString())
            fragment_profile_account_edit_email_tv.setText(dataState.email.toString())
            header_profile_account_edit_tv.text = dataState.nickname.toString()

            Glide.with(this).load(
                if (dataState.image != null) dataState.image else R.drawable.default_image)
                .into(header_profile_account_edit_civ)
        })
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE_SAVE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "CROP: RESULT OK")
            when (requestCode) {
                Constants.GALLERY_REQUEST_CODE_SAVE -> {
                    data?.data?.let { uri ->
                        activity?.let{
                            launchImageCrop(uri)
                        }
                    }?: showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE")
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE: uri: ${resultUri}")
                    setBlogProperties(resultUri)
                }

                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    Log.d(TAG, "CROP: ERROR")
                    showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
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

    private fun setBlogProperties(image: Uri?){
        if(image != null){
            image1 = image
            Glide.with(this).load(image).into(header_profile_account_edit_civ)
        }
        else{
            Glide.with(this).load(R.drawable.default_image).into(header_profile_account_edit_civ)
        }
    }

    private fun showErrorDialog(errorMessage: String){
        stateChangeListener.onDataStateChange(
            DataState(
                Event(StateError(Response(errorMessage, ResponseType.Dialog()))),
                Loading(isLoading = false),
                Data(Event.dataEvent(null), null)
            )
        )
    }

    private fun showDatePicker(date: String){
        Locale.setDefault(Locale("ru"))

        val dd = date.split('-')[2]
        val mm = date.split('-')[1]
        val yy = date.split('-')[0]

//        val c = Calendar.getInstance()
//        val sdf = SimpleDateFormat("yy-mm-dd", Locale.getDefault())
//        c.time = sdf.parse(date)!!

        val dpd = DatePickerDialog(requireContext(), R.style.MySpinnerDatePickerStyle,
            DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                fragment_profile_account_edit_birth_tv.text = ("$i-${i2+1}-$i3")
            }, yy.toInt(), mm.toInt()-1, dd.toInt())

        dpd.show()
        dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).text = getString(R.string.cancel_)
    }


    private fun editInfo(){
        image1?.path?.let {
            val imageFile = File(it)
            Log.d(TAG, "CreateBlogFragment, imageFile: file: ${imageFile}")

            val requestBody =
                RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    imageFile
                )

            Log.d(TAG, "PostCreateHouse request777: ${requestBody}")

            val multipartBody = MultipartBody.Part.createFormData(
                "photos",
                imageFile.name,
                requestBody
            )

            viewModel.setStateEvent(
                ProfileStateEvent.EditProfileInfoEvent(header_profile_account_edit_tv.text.toString(), multipartBody)
            )
        }
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.response?.let { event ->
                    event.peekContent().let { response ->
                        response.message?.let { message ->
                            if (message == SuccessHandling.SUCCESS_BLOG_CREATED) {
                                viewModel.clearNewBlogFields()
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.profileInfoFields.let{ newBlogFields ->

            }
        })
    }
}