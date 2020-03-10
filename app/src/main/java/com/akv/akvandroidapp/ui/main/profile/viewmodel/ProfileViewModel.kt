package com.akv.akvandroidapp.ui.main.profile.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapp.repository.main.ProfileRepository
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.BaseViewModel
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.Loading
import com.akv.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.akv.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.akv.akvandroidapp.util.AbsentLiveData
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
                val email = RequestBody.create(MediaType.parse("text/plain"), stateEvent.email.toString())
                val phone = RequestBody.create(MediaType.parse("text/plain"), stateEvent.phone.toString())
                val birthDay = RequestBody.create(MediaType.parse("text/plain"), stateEvent.birth_day.toString())
                val gender = RequestBody.create(MediaType.parse("text/plain"), stateEvent.gender.toString())
                val iBan = RequestBody.create(MediaType.parse("text/plain"), stateEvent.iban.toString())
                return sessionManager.cachedToken.value?.let { authToken ->
                    profileRepository.updateProfileInfo(
                        authToken,
                        email = email,
                        phone = phone,
                        gender = gender,
                        birth_day = birthDay,
                        userPic = stateEvent.image,
                        iBan = iBan
                    )
                }?: AbsentLiveData.create()
            }

            is ProfileStateEvent.SendCodeEvent -> {
                return profileRepository.sendCode(
                    stateEvent.phone)

            }

            is ProfileStateEvent.VerifyCodeEvent -> {
                return profileRepository.verifyCode(
                    stateEvent.phone,
                    stateEvent.code
                )
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
        setStateEvent(ProfileStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}











