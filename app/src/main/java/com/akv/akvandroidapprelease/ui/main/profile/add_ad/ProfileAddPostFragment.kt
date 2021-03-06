package com.akv.akvandroidapprelease.ui.main.profile.add_ad


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.session.AddAdInfo
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.main.profile.add_ad.dialogs.IbanRequestDialog
import com.akv.akvandroidapprelease.util.SuccessHandling.Companion.SUCCESS_BLOG_CREATED
import kotlinx.android.synthetic.main.fragment_add_ad_post.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject

class ProfileAddPostFragment : BaseAddHouseFragment(), IbanRequestDialog.IbanRequestInteraction{

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_ad_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()

        fragment_add_ad_post_next_btn.setOnClickListener {
            subscribeObservers()
        }
    }

    private fun showDialog(){
        val ibanDialog = IbanRequestDialog(requireContext(), this)
        ibanDialog.show()
    }

    private fun subscribeObservers(){
        sessionManager.addAdInfo.observe(viewLifecycleOwner, Observer{ dataState ->
            Log.d(TAG, "PostCreateHouse : ${dataState}")
            publishNewBlog(dataState)

        })

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.response?.let { event ->
                    event.peekContent().let { response ->
                        response.message?.let { message ->
                            if (message == SUCCESS_BLOG_CREATED) {
                                viewModel.clearNewBlogFields()
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogFields.let{ newBlogFields ->

            }
        })

    }

    private fun publishNewBlog(addAdInfo: AddAdInfo){
        val multipartBodyList: ArrayList<MultipartBody.Part> = arrayListOf()

        for(i in 0 until addAdInfo._addAdImage.size){
            addAdInfo._addAdImage[i].path?.let{filePath ->
                val imageFile = File(filePath)
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

                Log.d(TAG, "PostCreateHouse multipartBody777: ${multipartBody}")

                multipartBodyList.add(multipartBody)
            }
        }

        Log.d(TAG, "PostCreateHouse 22222: ${multipartBodyList}")

        viewModel.setStateEvent(
            AddAdStateEvent.CreateNewBlogEvent(
                addAdInfo._addAdType,
                addAdInfo._addAdGuestsCount,
                addAdInfo._addAdRoomsCount,
                addAdInfo._addAdBedsCount,
                addAdInfo._addAdAddress,
                addAdInfo._addAdAddressRegionId,
                addAdInfo._addAdAddressCityId,
                addAdInfo._addAdAddressCountry,
                addAdInfo._addAdDescription,
                addAdInfo._addAdTitle,
                addAdInfo._addAdPrice,
                addAdInfo._addAd7DaysDiscount,
                addAdInfo._addAd30DaysDiscount,
                addAdInfo._addAdFacilityList,
                addAdInfo._addAdNearByList,
                addAdInfo._addAdRulesList,
                addAdInfo._addAdAvailableList,
                addAdInfo._addAdAddressLatitude,
                addAdInfo._addAdAddressLongitude,
                multipartBodyList
            )
        )
    }

    private fun setToolbar(){
        fragment_add_ad_post_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_post_toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }

        fragment_add_ad_post_cancel.setOnClickListener {
            activity?.finish()
            sessionManager.clearAddAdAllInfo()
        }
    }

    override fun onCloseBtnListener() {}

    override fun onCancelBtnListener() {}

    override fun onPostBtnListener(iban: String) {

    }
}

















