package com.example.akvandroidapp.ui.main.profile.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.akvandroidapp.repository.main.ProfileRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.search.state.SearchStateEvent
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.Constants
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject


data class BlockedDates(private val check_in: String, private val check_out:String)

class ProfileViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<ProfileStateEvent, ProfileViewState>(){

    override fun handleStateEvent(
        stateEvent: ProfileStateEvent
    ): LiveData<DataState<ProfileViewState>> {

        when(stateEvent){
            is ProfileStateEvent.CreateNewBlogEvent -> {
                Log.d("qwe","PostCreateHouse 555555 ${sessionManager.cachedToken.value}")
                return sessionManager.cachedToken.value?.let { authToken ->

                    val name = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdTitle)
                    val description = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdDescription)
                    val rooms = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdRoomsCount.toString())
                    val address = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdAddressList[2])
                    val longitude = RequestBody.create(MediaType.parse("text/plain"), 55.5.toString())
                    val latitude = RequestBody.create(MediaType.parse("text/plain"), 55.5.toString())
                    val cityId = RequestBody.create(MediaType.parse("text/plain"),(Constants.mapCity.getValue(stateEvent._addAdAddressList[2]).toString()))
                    val price = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdPrice.toString())
                    val beds = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdBedsCount.toString())
                    val guests = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAdGuestsCount.toString())
                    val rules = RequestBody.create(MediaType.parse("text/plain"), "Не курить")
                    val nearBuildings = RequestBody.create(MediaType.parse("text/plain"), "Больница")
                    val blockedDates = RequestBody.create(MediaType.parse("text/plain"), "[{\"check_in\": \"2019-12-20\", \"check_out\": \"2019-12-31\"}, {\"check_in\": \"2019-12-10\", \"check_out\": \"2012-12-19\"}]")
                    val houseTypeId = RequestBody.create(MediaType.parse("text/plain"),(Constants.mapTypeHouse.getValue(stateEvent._addAdType).toString()))
                    val accommodations = RequestBody.create(MediaType.parse("text/plain"), "Утюг")
                    val discount7days = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAd7DaysDiscount.toString())
                    val discount30days = RequestBody.create(MediaType.parse("text/plain"), stateEvent._addAd30DaysDiscount.toString())


                    Log.d(TAG,"PostCreateHouse cityId + ${Constants.mapCity.getValue(stateEvent._addAdAddressList[2])}")
                    Log.d(TAG,"PostCreateHouse houseId + ${Constants.mapTypeHouse.getValue(stateEvent._addAdType)}")
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
                        discount30days
                    )
                }?: AbsentLiveData.create()
            }


            is ProfileStateEvent.GetProfileInfoEvent ->{
                Log.d("qwe","PostCreateHouse 555555 ${sessionManager.cachedToken.value}")
                return sessionManager.cachedToken.value?.let { authToken ->

                    profileRepository.createGetProfileInfo(
                        authToken
                    )
                }?: AbsentLiveData.create()
            }

            is ProfileStateEvent.EditProfileInfoEvent ->{
                Log.d("qwe","PostCreateHouse 555555 ${sessionManager.cachedToken.value}")

                val name = RequestBody.create(MediaType.parse("text/plain"), stateEvent.first_name!!)

                return sessionManager.cachedToken.value?.let { authToken ->
                    profileRepository.updateProfileInfo(
                        authToken,
                        name,
                        stateEvent.image
                    )
                }?: AbsentLiveData.create()
            }

            is ProfileStateEvent.MyHouseEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    Log.d("Go", "Go go go go  + ${getPage()}")

                    profileRepository.myHouseList(
                        authToken = authToken,
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is ProfileStateEvent.None -> {
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


    override fun initNewViewState(): ProfileViewState {
        return ProfileViewState()
    }

    fun cancelActiveJobs(){
        handlePendingData()
        profileRepository.cancelActiveJobs()

    }

    fun clearNewBlogFields(){
        val update = getCurrentViewStateOrNew()
        update.blogFields = ProfileViewState.NewBlogFields()
        setViewState(update)
    }

    fun handlePendingData(){
        setStateEvent(ProfileStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}











