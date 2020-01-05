package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.AddAdInfo
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.util.SuccessHandling.Companion.SUCCESS_BLOG_CREATED
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_post.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList
import javax.inject.Inject


class ProfileAddPostFragment : BaseProfileFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        fragment_add_ad_post_next_btn.setOnClickListener {
            subscribeObservers()
        }
    }

    private fun subscribeObservers(){
        sessionManager.addAdInfo.observe(this, Observer{ dataState ->
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
                        MediaType.parse("image/*"),
                        imageFile
                    )

                val multipartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestBody
                )

                multipartBodyList.add(multipartBody)
            }
        }

        Log.d(TAG, "PostCreateHouse 22222: ${multipartBodyList}")

        viewModel.setStateEvent(
            ProfileStateEvent.CreateNewBlogEvent(
                addAdInfo._addAdType,
                addAdInfo._addAdGuestsCount,
                addAdInfo._addAdRoomsCount,
                addAdInfo._addAdBedsCount,
                addAdInfo._addAdAddressList,
                addAdInfo._addAdDescription,
                addAdInfo._addAdTitle,
                addAdInfo._addAdPrice,
                addAdInfo._addAd7DaysDiscount,
                addAdInfo._addAd30DaysDiscount,
                multipartBodyList
            )
        )
    }
}

















