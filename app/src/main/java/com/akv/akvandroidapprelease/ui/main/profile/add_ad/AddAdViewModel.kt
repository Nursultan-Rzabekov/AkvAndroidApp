package com.akv.akvandroidapprelease.ui.main.profile.add_ad


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.ProfileRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.util.AbsentLiveData
import com.akv.akvandroidapprelease.util.Constants
import com.akv.akvandroidapprelease.util.DateUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import kotlin.collections.ArrayList

data class DatesTemp(private val check_in : String,private val check_out : String)
class AddAdViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository
): BaseViewModel<AddAdStateEvent, AddAdViewState>()
{
    override fun handleStateEvent(stateEvent: AddAdStateEvent): LiveData<DataState<AddAdViewState>> {
        when(stateEvent){

            is AddAdStateEvent.CreateNewBlogEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    val name = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdTitle)
                    val description = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdDescription)
                    val rooms = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdRoomsCount.toString())
                    val address = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdAddress)
                    val longitude = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdAddressLongitude.toString())
                    val latitude = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdAddressLatitude.toString())
                    val price = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdPrice.toString())
                    val beds = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdBedsCount.toString())
                    val guests = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdGuestsCount.toString())
                    val blockedDates = RequestBody.create(MediaType.parse("text/plain"), "[{\"check_in\": \"2019-12-20\", \"check_out\": \"2019-12-31\"}, {\"check_in\": \"2019-12-10\", \"check_out\": \"2012-12-19\"}]")
                    val houseTypeId = RequestBody.create(MediaType.parse("text/plain"),(Constants.mapTypeHouse.getValue(stateEvent._addAdType).toString()))
                    val discount7days = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAd7DaysDiscount.toString())
                    val discount30days = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAd30DaysDiscount.toString())
                    val cityId = RequestBody.create(MediaType.parse("text/plain"),(stateEvent._addAdAddressCityId.toString()))
                    val regionId = RequestBody.create(MediaType.parse("text/plain"),(stateEvent._addAdAddressRegionId.toString()))
                    val countryId = RequestBody.create(MediaType.parse("text/plain"),(stateEvent._addAdAddressCountry.toString()))

                    val multipartBodyList: ArrayList<MultipartBody.Part> = arrayListOf()

                    stateEvent.rulesList.forEach {
                        multipartBodyList.add(MultipartBody.Part.createFormData("rules",it))
                    }
                    stateEvent.nearbyList.forEach {
                        multipartBodyList.add(MultipartBody.Part.createFormData("near_buildings",it))
                    }
                    stateEvent.facilitiesList.forEach {
                        multipartBodyList.add(MultipartBody.Part.createFormData("accommodations",it))
                    }

                    val listTemp : ArrayList<DatesTemp> = arrayListOf()
                    var blockedDatesCheckIn : String = DateUtils.convertDateToString(stateEvent._availableList.first())
                    var blockedDatesTemporalYear : Int = DateUtils.convertDateToString(stateEvent._availableList.first()).split("-")[0].toInt()
                    var blockedDatesTemporalMonth : Int = DateUtils.convertDateToString(stateEvent._availableList.first()).split("-")[1].toInt()
                    var blockedDatesTemporalDay : Int = (DateUtils.convertDateToString(stateEvent._availableList.first()).split("-")[2]).toInt()-1
                    var blockedDatesCheckOut: String = DateUtils.convertDateToString(stateEvent._availableList.first())

                    stateEvent._availableList.forEach {
                        val temp = DateUtils.convertDateToString(it).split("-")
                        if(temp[0].toInt() == blockedDatesTemporalYear &&
                            temp[1].toInt() == blockedDatesTemporalMonth &&
                            (temp[2]).toInt() == (blockedDatesTemporalDay + 1)){
                            blockedDatesCheckOut = DateUtils.convertDateToString(it)
                        }
                        else{
                            listTemp.add(DatesTemp(blockedDatesCheckIn,blockedDatesCheckOut))
                            blockedDatesCheckIn = DateUtils.convertDateToString(it)
                            blockedDatesCheckOut = DateUtils.convertDateToString(it)
                        }
                        blockedDatesTemporalDay = temp[2].toInt()
                        blockedDatesTemporalMonth = temp[1].toInt()
                        blockedDatesTemporalYear = temp[0].toInt()
                    }

                    Log.e("dates temporary", "See ${listTemp}")

                    profileRepository.createNewBlogPost(
                        authToken,
                        name,
                        description,
                        rooms,
                        address,
                        longitude,
                        latitude,
                        cityId,
                        price,
                        beds,
                        guests,
                        multipartBodyList,
                        stateEvent.image,
                        listTemp,
                        houseTypeId,
                        discount7days,
                        discount30days,
                        regionId,
                        countryId
                    )
                }?: AbsentLiveData.create()
            }

            is AddAdStateEvent.None ->{
                return liveData {
                    emit(
                        DataState(
                            null,
                            Loading(false),
                            null
                        )
                    )
                }
            }
        }
    }


    override fun initNewViewState(): AddAdViewState {
        return AddAdViewState()
    }

    fun cancelActiveJobs(){
        handlePendingData()
        profileRepository.cancelActiveJobs()
    }

    fun clearNewBlogFields(){
        val update = getCurrentViewStateOrNew()
        update.blogFields = AddAdViewState.NewBlogFields()
        setViewState(update)
    }

    fun handlePendingData(){
        setStateEvent(AddAdStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
































