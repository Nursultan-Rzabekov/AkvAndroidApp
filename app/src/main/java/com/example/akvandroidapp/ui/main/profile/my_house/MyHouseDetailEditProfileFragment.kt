package com.example.akvandroidapp.ui.main.profile.my_house


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.HouseUpdateData
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.*
import com.example.akvandroidapp.ui.main.profile.my_house.adapters.GalleryPhoto
import com.example.akvandroidapp.ui.main.profile.my_house.adapters.GalleryPhotosAdapter
import com.example.akvandroidapp.ui.main.profile.my_house.state.MyHouseStateStateEvent
import com.example.akvandroidapp.ui.main.profile.my_house.state.MyHouseViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setUpdateResponse
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.Converters
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.MoneyTextWatcher
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_my_adds_change.*
import kotlinx.android.synthetic.main.header_my_adds_change.*
import java.util.*
import javax.inject.Inject


class MyHouseDetailEditProfileFragment : BaseMyHouseFragment(), GalleryPhotosAdapter.PhotoCloseInteraction {

    private lateinit var photosAdapter: GalleryPhotosAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    private var house: HouseUpdateData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_change_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)

        setToolbar()
        initRecyclerView()

        Log.d(TAG, "MyHouseDetailEditProfileFragment: ${viewModel}")

        setObservers()

        header_my_adds_change_cancel.setOnClickListener {
            cancelUpdate()
            findNavController().navigateUp()
        }

        fragment_my_adds_change_price_et.addTextChangedListener(MoneyTextWatcher(fragment_my_adds_change_price_et))

        header_my_adds_change_save.setOnClickListener {
            saveUpdate()
            //findNavController().navigateUp()
        }

        fragment_my_adds_change_rules.setOnClickListener {
            findNavController().navigate(R.id.action_myHouseDetailEditProfileFragment_to_myHouseRulesEditProfileFragment)
        }


        fragment_my_adds_change_facilities.setOnClickListener {
            findNavController().navigate(R.id.action_myHouseDetailEditProfileFragment_to_myHouseFacilitiesEditProfileFragment)
        }


        fragment_my_adds_change_near.setOnClickListener {
            findNavController().navigate(R.id.action_myHouseDetailEditProfileFragment_to_myHouseNearEditProfileFragment)
        }


        fragment_my_adds_change_available_dates.setOnClickListener {
            findNavController().navigate(R.id.action_myHouseDetailEditProfileFragment_to_myHouseAvailableDatesEditProfileFragment)
        }

    }

    private fun initRecyclerView(){
        fragment_my_adds_change_photos_recycler_view.apply {
            layoutManager = LinearLayoutManager(
                this@MyHouseDetailEditProfileFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false)
            photosAdapter =
                GalleryPhotosAdapter(
                    requestManager,
                    this@MyHouseDetailEditProfileFragment
                )
            adapter = photosAdapter
        }
    }

    private fun setObservers() {
        sessionManager.houseUpdateData.observe(viewLifecycleOwner, Observer {
            fragment_my_adds_change_title_et.setText(it.title.toString())
            fragment_my_adds_change_desc_et.setText(it.description.toString())
            fragment_my_adds_change_price_et.setText(it.price.toString())
            fragment_my_adds_change_address_et.setText(it.address.toString())
            fragment_my_adds_change_photos_tv.text = ("${it.photosList.size}/15")



            house = HouseUpdateData(
                id = it.id,
                title = it.title,
                description = it.description,
                address = it.address,
                facilitiesList = it.facilitiesList,
                nearByList = it.nearByList,
                houseRulesList = it.houseRulesList,
                photosList = it.photosList,
                blockedDates = it.blockedDates
            )

            Toast.makeText(requireContext(), "${it.id}", Toast.LENGTH_SHORT).show()

            val photos = it.photosList.toMutableList()
            photosAdapter.submitList(photos)
        })
    }

    override fun onItemClosed(position: Int, item: GalleryPhoto?) {
        photosAdapter.removeItem(position)
        fragment_my_adds_change_photos_tv.text = ("${photosAdapter.itemCount}/15")
    }

    override fun onAddPressed(position: Int, item: GalleryPhoto?) {
        pickFromGallery()
    }

    private fun addGalleryPhoto(uri: Uri){
        photosAdapter.addGalleryPhoto(
            GalleryPhoto(
                null,
                uri
            )
        )
        fragment_my_adds_change_photos_tv.text = ("${photosAdapter.itemCount}/15")
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when (requestCode) {
                Constants.GALLERY_REQUEST_CODE -> {
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

                    addGalleryPhoto(resultUri)
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

    fun showErrorDialog(errorMessage: String){
        stateChangeListener.onDataStateChange(
            DataState(
                Event(StateError(Response(errorMessage, ResponseType.Dialog()))),
                Loading(isLoading = false),
                Data(Event.dataEvent(null), null)
            )
        )
    }

    private fun saveUpdate(){
        if (checkInputs())
            updateHouseRequest()
        else
            showErrorDialog("Some inputs are not filled")
    }

    private fun checkInputs(): Boolean{
        if (fragment_my_adds_change_title_et.text.toString().trim().isBlank()
            || fragment_my_adds_change_desc_et.text.toString().trim().isBlank()
            || Converters.formatPriceToInt(fragment_my_adds_change_price_et.text.toString()) == 0
            || fragment_my_adds_change_address_et.text.toString().trim().isBlank())
            return false
        return true
    }

    private fun updateHouseRequest(){
        if (house != null)
            viewModel.setStateEvent(
                MyHouseStateStateEvent.MyHouseUpdateEvent(
                    house_id = house?.id!!,
                    title =
                    if(fragment_my_adds_change_title_et.text.toString().trim() != house?.title)
                        fragment_my_adds_change_title_et.text.toString().trim()
                    else null,
                    description =
                    if (fragment_my_adds_change_desc_et.text.toString().trim() != house?.description)
                        fragment_my_adds_change_desc_et.text.toString().trim()
                    else null,
                    price =
                    if (Converters.formatPriceToInt(fragment_my_adds_change_price_et.text.toString()) == house?.price)
                        Converters.formatPriceToInt(fragment_my_adds_change_price_et.text.toString())
                    else null,
                    address =
                    if (fragment_my_adds_change_address_et.text.toString().trim() == house?.address)
                        fragment_my_adds_change_address_et.text.toString().trim()
                    else null,
                    photoList = null,
                    rulesList = house?.houseRulesList,
                    facilitiesList = house?.facilitiesList,
                    nearsList = house?.nearByList,
                    datesList = null
                )
            )
        observeRequest()
    }

    private fun observeRequest(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null) {
                stateChangeListener.onDataStateChange(dataState)
                handleUpdate(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null){
                if (viewState.myHouseUpdateFields.response == true)
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleUpdate(dataState: DataState<MyHouseViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.setUpdateResponse(it.myHouseUpdateFields)
                }
            }
        }
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{

            }
        }
    }

    private fun cancelUpdate(){
        sessionManager.clearHouseUpdateData()
    }

    private fun setToolbar(){
    }

    override fun onDestroy() {
        sessionManager.clearHouseUpdateData()
        super.onDestroy()
    }
}