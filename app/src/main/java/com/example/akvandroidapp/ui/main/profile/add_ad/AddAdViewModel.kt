package com.example.akvandroidapp.ui.main.profile.add_ad


import android.util.Log
import androidx.lifecycle.*
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.repository.auth.AuthRepository
import com.example.akvandroidapp.repository.main.ProfileRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.auth.state.*
import com.example.akvandroidapp.ui.auth.state.AuthStateEvent.*
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.profile.viewmodel.BlockedDates
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.Constants
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject


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
                Log.d("qwe","PostCreateHouse 555555 ${sessionManager.cachedToken.value}")
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
                    val rules = RequestBody.create(MediaType.parse("text/plain"), stateEvent.rulesList[0])
                    val nearBuildings = RequestBody.create(MediaType.parse("text/plain"), stateEvent.nearbyList[0])
                    val blockedDates = RequestBody.create(MediaType.parse("text/plain"), "[{\"check_in\": \"2019-12-20\", \"check_out\": \"2019-12-31\"}, {\"check_in\": \"2019-12-10\", \"check_out\": \"2012-12-19\"}]")

//                    val rulesList: MutableList<RequestBody>?
//                    rulesList = mutableListOf()
//                    stateEvent.rulesList.forEach {
//                        Log.e("ASASDASD", "ASASDASD $it")
//                        rulesList.add(
//                            RequestBody.create(MediaType.parse("text/plain"), it)
//                        )
//                    }

                    val houseTypeId = RequestBody.create(MediaType.parse("text/plain"),(Constants.mapTypeHouse.getValue(stateEvent._addAdType).toString()))
                    val accommodations = RequestBody.create(MediaType.parse("text/plain"), stateEvent.facilitiesList[0])
                    val discount7days = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAd7DaysDiscount.toString())
                    val discount30days = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAd30DaysDiscount.toString())

                    val cityId = RequestBody.create(MediaType.parse("text/plain"),(stateEvent._addAdAddressCityId.toString()))
                    val regionId = RequestBody.create(MediaType.parse("text/plain"),(stateEvent._addAdAddressRegionId.toString()))
                    val countryId = RequestBody.create(MediaType.parse("text/plain"),(stateEvent._addAdAddressCountry.toString()))

                    Log.d(TAG,"PostCreateHouse dis30 + ${stateEvent._addAd30DaysDiscount}")
                    Log.d(TAG,"PostCreateHouse dis7 + ${stateEvent._addAd7DaysDiscount}")
                    Log.d(TAG,"PostCreateHouse facilitiesList + ${stateEvent.facilitiesList}")
                    Log.d(TAG,"PostCreateHouse nearbyList + ${stateEvent.nearbyList}")
                    Log.d(TAG,"PostCreateHouse rulesList + ${stateEvent.rulesList}")
                    Log.d(TAG,"PostCreateHouse cityId + ${stateEvent._addAdAddressCityId}")
                    Log.d(TAG,"PostCreateHouse regionId + ${stateEvent._addAdAddressRegionId.toString()}")

                    val list:List<BlockedDates> = listOf(BlockedDates("2019-12-20","2019-12-31"))

                    Log.d(TAG,"PostCreateHouse listt + ${list}")

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
                        rules,
                        nearBuildings,
                        blockedDates,
                        stateEvent.image,
                        houseTypeId,
                        accommodations,
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
        setStateEvent(AddAdStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
































