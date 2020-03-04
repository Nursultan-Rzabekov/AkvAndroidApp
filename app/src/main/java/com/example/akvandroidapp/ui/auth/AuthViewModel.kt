package com.example.akvandroidapp.ui.auth


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.repository.auth.AuthRepository
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.auth.state.AuthStateEvent
import com.example.akvandroidapp.ui.auth.state.AuthStateEvent.*
import com.example.akvandroidapp.ui.auth.state.AuthViewState
import com.example.akvandroidapp.ui.auth.state.LoginFields
import com.example.akvandroidapp.ui.auth.state.RegistrationFields
import javax.inject.Inject


class AuthViewModel
@Inject
constructor(val authRepository: AuthRepository): BaseViewModel<AuthStateEvent, AuthViewState>()
{
    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when(stateEvent){

            is LoginAttemptEvent -> {
                return authRepository.attemptLogin(
                    stateEvent.email,
                    stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {
                return authRepository.attemptRegistration(
                    stateEvent.email,
                    stateEvent.gender,
                    stateEvent.phone,
                    stateEvent.password,
                    stateEvent.first_name,
                    stateEvent.last_name,
                    stateEvent.birth_day
                )
            }

            is SendCodeEvent -> {
               return authRepository.sendCode(
                   stateEvent.phone)

            }

            is VerifyCodeEvent -> {
                return authRepository.verifyCode(
                    stateEvent.phone,
                    stateEvent.code
                )
            }

            is CheckPreviousAuthEvent -> {
                return authRepository.checkPreviousAuthUser()
            }


            is None ->{
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

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields){
        val update = getCurrentViewStateOrNew()
        if(update.registrationFields == registrationFields){
            return
        }
        update.registrationFields = registrationFields
        setViewState(update)
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if(update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        setViewState(update)
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken){
            return
        }
        update.authToken = authToken
        setViewState(update)
    }

    fun cancelActiveJobs(){
        handlePendingData()
        authRepository.cancelActiveJobs()
    }

    fun handlePendingData(){
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
































