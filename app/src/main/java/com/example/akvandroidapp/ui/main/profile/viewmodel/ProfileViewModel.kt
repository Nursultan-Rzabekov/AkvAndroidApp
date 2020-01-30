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

            is ProfileStateEvent.GetProfileInfoEvent ->{
                return sessionManager.cachedToken.value?.let { authToken ->
                    profileRepository.createGetProfileInfo(
                        authToken
                    )
                }?: AbsentLiveData.create()
            }

            is ProfileStateEvent.EditProfileInfoEvent ->{
                Log.d("qwe","PostCreateHouse 555555 ${stateEvent.birth_day}")

                val email = RequestBody.create(MediaType.parse("text/plain"), stateEvent.email!!)
                val phone = RequestBody.create(MediaType.parse("text/plain"), stateEvent.phone!!)
                val birth_day = RequestBody.create(MediaType.parse("text/plain"), stateEvent.birth_day!!)
                val gender = RequestBody.create(MediaType.parse("text/plain"), stateEvent.gender.toString())
                return sessionManager.cachedToken.value?.let { authToken ->
                    profileRepository.updateProfileInfo(
                        authToken,
                        email = email,
                        phone = phone,
                        gender = gender,
                        birth_day = birth_day,
                        userPic = stateEvent.image
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











