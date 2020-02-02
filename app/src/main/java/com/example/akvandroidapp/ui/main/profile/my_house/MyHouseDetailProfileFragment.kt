package com.example.akvandroidapp.ui.main.profile.my_house


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.entity.ZhilyeReservation
import com.example.akvandroidapp.session.HouseUpdateData
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.profile.my_house.adapters.GalleryPhoto
import com.example.akvandroidapp.ui.main.profile.my_house.adapters.HouseEarning
import com.example.akvandroidapp.ui.main.profile.my_house.state.MyHouseStateStateEvent
import com.example.akvandroidapp.ui.main.profile.my_house.state.MyHouseViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setZhilyeHouseId
import com.example.akvandroidapp.util.Converters
import com.example.akvandroidapp.util.DateUtils
import com.savvi.rangedatepicker.CalendarPickerView
import handleIncomingZhilyeData
import kotlinx.android.synthetic.main.fragment_my_adds_detailed.*
import kotlinx.android.synthetic.main.fragment_my_adds_detailed_layout.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MyHouseDetailProfileFragment : BaseMyHouseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_detailed_layout, container, false)
    }

    @Inject
    lateinit var sessionManager: SessionManager

    private var argument:BlogPost? = null

    private var houseId:Int? = null
    private var earnings: ArrayList<HouseEarning>? = null
    private var houseUpdateData: HouseUpdateData? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsProfileFragment: ${viewModel}")

        argument = arguments?.getParcelable("item")
        houseId = argument?.id

        houseId?.let {
            viewModel.setZhilyeHouseId(it).let {
                viewModel.setStateEvent(MyHouseStateStateEvent.MyHouseZhilyeEvent())
            }
        }

        setToolbar()
        subscribeObservers()
        initCalendar(getBlockedDates())

        earning.setOnClickListener {
            navNextFragment()
        }

        fragment_my_adds_detailed_change_btn.setOnClickListener {
            navNextDetailEditFragment()
        }
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer{ dataState ->
            if(dataState != null) {
                handleUpdate(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, androidx.lifecycle.Observer{ viewState ->
            if(viewState != null){
                Log.d("MyHouseDetailProfile","list house +${viewState.zhilyeFields.houseId}")
                Log.d("MyHouseDetailProfile","list zhilyeDetail +${viewState.zhilyeFields.zhilyeDetail}")
                Log.d("MyHouseDetailProfile","list Recommendations +${viewState.zhilyeFields.blogListRecommendations}")
                Log.d("MyHouseDetailProfile","list Accomadations +${viewState.zhilyeFields.zhilyeDetailAccomadations}")
                Log.d("MyHouseDetailProfile","list Photos +${viewState.zhilyeFields.zhilyeDetailPhotos}")
                Log.d("MyHouseDetailProfile","list DetailRules +${viewState.zhilyeFields.zhilyeDetailRules}")
                Log.d("MyHouseDetailProfile","list User +${viewState.zhilyeFields.zhilyeUser}")
                Log.d("MyHouseDetailProfile","list DetailNearBuildings +${viewState.zhilyeFields.zhilyeDetailNearBuildings}")
                Log.d("MyHouseDetailProfile","list Reservations +${viewState.zhilyeFields.zhilyeReservationsList}")

                setEarnings(viewState.zhilyeFields.zhilyeReservationsList)

                fragment_my_adds_detailed_title_tv.text = viewState.zhilyeFields.zhilyeDetail.name.toString()

                if (viewState.zhilyeFields.zhilyeDetailPhotos.isNotEmpty())
                    Glide.with(this)
                        .load(viewState.zhilyeFields.zhilyeDetailPhotos[0].image)
                        .error(R.drawable.test_image_back)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(fragment_my_adds_detailed_iv)

                val galleryPhotos = mutableListOf<GalleryPhoto>()
                viewState.zhilyeFields.zhilyeDetailPhotos.forEach {
                    galleryPhotos.add(
                        GalleryPhoto(null, Uri.parse(it.image))
                    )
                }

                val rules = mutableListOf<String>()
                viewState.zhilyeFields.zhilyeDetailRules.forEach {
                    rules.add(
                        it.name.toString()
                    )
                }

                val facilities = mutableListOf<String>()
                viewState.zhilyeFields.zhilyeDetailAccomadations.forEach {
                    facilities.add(
                        it.name.toString()
                    )
                }

                val nears = mutableListOf<String>()
                viewState.zhilyeFields.zhilyeDetailNearBuildings.forEach {
                    nears.add(
                        it.name.toString()
                    )
                }

                val dates = mutableListOf<Date>()
                dates.add(Date())

                val blockedDates = mutableListOf<Date>()
                viewState.zhilyeFields.zhilyeReservationsList.forEach {
                    blockedDates.addAll(
                        DateUtils.getDatesBetween(
                            DateUtils.convertStringToDate(it.check_in.toString()),
                            DateUtils.convertStringToDate(it.check_out.toString())
                        )
                    )
                }

                initCalendar(blockedDates)

                houseUpdateData = HouseUpdateData(
                    id = viewState.zhilyeFields.houseId,
                    title = viewState.zhilyeFields.zhilyeDetail.name,
                    description = viewState.zhilyeFields.zhilyeDetail.description,
                    address = viewState.zhilyeFields.zhilyeDetail.address,
                    price = viewState.zhilyeFields.zhilyeDetail.price,
                    photosList = galleryPhotos,
                    facilitiesList = facilities,
                    nearByList = nears,
                    houseRulesList = rules,
                    availableDates = dates
                )
            }
        })
    }

    private fun handleUpdate(dataState: DataState<MyHouseViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingZhilyeData(it)
                }
            }
        }
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{

            }
        }
    }

    private fun navNextFragment(){
        val bundle = Bundle().apply {
            putParcelableArrayList("earnings", earnings?: arrayListOf<HouseEarning>())
        }
        findNavController().navigate(R.id.action_profileMyHouseDetailProfileFragment_to_myHouseMoneyProfileFragment, bundle)
    }

    private fun navNextDetailEditFragment(){
        if (houseUpdateData != null)
            sessionManager.setHouseUpdateData(
                houseUpdateData!!
            )
        else
            activity?.finish()
        findNavController().navigate(R.id.action_profileMyHouseDetailProfileFragment_to_myHouseDetailEditProfileFragment)
    }

    private fun initCalendar(dates: List<Date>){

        val lastyear = Calendar.getInstance()
        lastyear.add(Calendar.YEAR, 0)
        lastyear.add(Calendar.MONTH, 0)
        lastyear.add(Calendar.DAY_OF_MONTH, 0)
        lastyear.set(Calendar.DAY_OF_MONTH, 1)

        val nextyear = Calendar.getInstance()
        nextyear.set(Calendar.YEAR, nextyear.get(Calendar.YEAR)+1)
        nextyear.set(Calendar.MONTH, Calendar.DECEMBER)
        nextyear.set(Calendar.DAY_OF_MONTH, 31)

        fragment_my_adds_detailed_calendar.isEnabled = false

        fragment_my_adds_detailed_calendar
            .init(lastyear.time, nextyear.time)
            .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
            .withHighlightedDates(
                DateUtils.getDatesFromToday(dates)
            )

    }

    private fun getBlockedDates(): List<Date>{
        return listOf(
        )
    }

    private fun setEarnings(reservations: List<ZhilyeReservation>){
        val earning = ArrayList<HouseEarning>()
        var totalIncome = 0
        for (reservation in reservations) {
            earning.add(
                HouseEarning(
                    id = reservation.user_id,
                    nickname = reservation.first_name.toString(),
                    earning = reservation.income,
                    date = DateUtils.convertStringToDate(reservation.check_in.toString())
                )
            )
            totalIncome += reservation.income
        }

        fragment_my_adds_detailed_earning_tv.text =
            ("+${Converters.pretifyPrice(totalIncome)}kzt")

        earnings = earning
    }

    private fun setToolbar(){
        fragment_my_adds_detailed_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_my_adds_detailed_toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }
    }

}