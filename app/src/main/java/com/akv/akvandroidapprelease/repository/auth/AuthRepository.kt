package com.akv.akvandroidapprelease.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.akv.akvandroidapprelease.api.auth.OpenApiAuthService
import com.akv.akvandroidapprelease.api.auth.network_responses.CodeResponse
import com.akv.akvandroidapprelease.api.auth.network_responses.LoginResponse
import com.akv.akvandroidapprelease.api.auth.network_responses.RegistrationResponse
import com.akv.akvandroidapprelease.api.main.GenericResponse
import com.akv.akvandroidapprelease.entity.AccountProperties
import com.akv.akvandroidapprelease.entity.AuthToken
import com.akv.akvandroidapprelease.persistence.AccountPropertiesDao
import com.akv.akvandroidapprelease.persistence.AuthTokenDao
import com.akv.akvandroidapprelease.repository.JobManager
import com.akv.akvandroidapprelease.repository.NetworkBoundResource
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Response
import com.akv.akvandroidapprelease.ui.ResponseType
import com.akv.akvandroidapprelease.ui.auth.state.*
import com.akv.akvandroidapprelease.util.AbsentLiveData
import com.akv.akvandroidapprelease.util.ApiSuccessResponse
import com.akv.akvandroidapprelease.util.ErrorHandling.Companion.ERROR_SAVE_ACCOUNT_PROPERTIES
import com.akv.akvandroidapprelease.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.akv.akvandroidapprelease.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.akv.akvandroidapprelease.util.GenericApiResponse
import com.akv.akvandroidapprelease.util.PreferenceKeys
import com.akv.akvandroidapprelease.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor
): JobManager("AuthRepository")
{
    private val TAG: String = "AppDebug"

    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>>{

        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if(loginFieldErrors != LoginFields.LoginError.none()){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<LoginResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){

            // Ignore
            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                if(response.body.errorMessage?.first() == (GENERIC_AUTH_ERROR)){
                    return onErrorReturn(response.body.errorMessage?.first(), true, false)
                }

                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        id = response.body.user.id,
                        email = response.body.user.email,
                        gender = response.body.user.gender,
                        name = response.body.user.name,
                        phone = response.body.user.phone,
                        birth_day = response.body.user.birth_day
                    )
                )

                val result = authTokenDao.insert(
                    AuthToken(
                        response.body.user.id,
                        response.body.token
                    )
                )

                if(result < 0){
                    return onCompleteJob(DataState.error(
                        Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog()))
                    )
                }

                Log.d(TAG,"phone or email ${email}")

                saveAuthenticatedUserToPrefs(email = email)

                sessionManager.login(AuthToken(response.body.user.id, response.body.token))

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.user.id, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.
                    login(email, password)
            }

            override fun setJob(job: Job) {
                addJob("attemptLogin", job)
            }

        }.asLiveData()
    }

    fun attemptRegistration(
        email: String,
        gender: Int,
        phone: String,
        password: String,
        first_name: String,
        last_name: String,
        birth_day: String
    ): LiveData<DataState<AuthViewState>>{

        val registrationFieldErrors = RegistrationFields(email, first_name, birth_day).isValidForRegistration()
        if(registrationFieldErrors != RegistrationFields.RegistrationError.none()){
            return returnErrorResponse(registrationFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<RegistrationResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){

            // Ignore
            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }


            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {

                response.body.response?.let {
                    if(!it){
                        return onErrorReturn(response.body.errorMessage, true, false)
                    }
                }

                val result1 = accountPropertiesDao.insertAndReplace(
                    AccountProperties(
                        response.body.id,
                        response.body.email,
                        response.body.gender,
                        response.body.first_name,
                        response.body.phone,
                        response.body.birth_day
                    )
                )

                // will return -1 if failure
                if(result1 < 0){
                    onCompleteJob(DataState.error(
                        Response(ERROR_SAVE_ACCOUNT_PROPERTIES, ResponseType.Dialog()))
                    )
                    return
                }


                Log.e(TAG, "state not witsh  123123 + ${response.body.id} + ${response.body.email}")

                sessionManager.setAccountProperties(AccountProperties(
                    response.body.id,
                    response.body.email,
                    response.body.gender,
                    response.body.first_name,
                    response.body.phone,
                    response.body.birth_day
                ))

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authViewStateResponse = AuthViewStateResponse(
                                AccountProperties(
                                    response.body.id,
                                    response.body.email,
                                    response.body.gender,
                                    response.body.first_name,
                                    response.body.phone,
                                    response.body.birth_day
                                ),
                                isQueryInProgress = false,
                                isQueryExhausted = true
                            )
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(
                    email = email,
                    gender = gender,
                    phone = phone,
                    password = password,
                    re_password = password,
                    first_name = first_name,
                    last_name = last_name,
                    birth_day = birth_day)
            }

            override fun setJob(job: Job) {
                addJob("attemptRegistration", job)
            }
        }.asLiveData()
    }


    fun sendCode(phone: String): LiveData<DataState<AuthViewState>>{
        return object: NetworkBoundResource<CodeResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){

            // Ignore
            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }


            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<CodeResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response.body.response}")

                if(!response.body.response){
                    return onErrorReturn(response.body.message, true, false)
                }

            }

            override fun createCall(): LiveData<GenericApiResponse<CodeResponse>> {
                return openApiAuthService.sendCode(phone)
            }

            override fun setJob(job: Job) {
                addJob("attemptSendCode", job)
            }
        }.asLiveData()
    }


    fun forgerEmailCode(email: String): LiveData<DataState<AuthViewState>>{
        return object: NetworkBoundResource<GenericResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){

            // Ignore
            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }


            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response.body}")
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiAuthService.forgetEmail(email)
            }

            override fun setJob(job: Job) {
                addJob("attemptSendCode", job)
            }
        }.asLiveData()
    }

    fun verifyCode(phone: String, code:String): LiveData<DataState<AuthViewState>>{

        val verifyCodeFieldErrors = VerifyCodeFields(phone,code).isValidForSendCode()
        if(verifyCodeFieldErrors != VerifyCodeFields.VerifyCodeError.none()){
            return returnErrorResponse(verifyCodeFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<GenericResponse, Any, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){

            // Ignore
            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response.body.response}")

                if(!response.body.response){
                    return onErrorReturn(response.body.response.toString(), true, false)
                }

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            responseVerify = response.body.response
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiAuthService.verifyCode(phone,code)
            }

            override fun setJob(job: Job) {
                addJob("attemptSendCode", job)
            }
        }.asLiveData()
    }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>>{
        val previousAuthUserEmail: String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)

        if(previousAuthUserEmail.isNullOrBlank()) {
            Log.d(TAG, "checkPreviousAuthUser: No previously authenticated user found.")
            return returnNoTokenFound()
        }
        else{
            return object: NetworkBoundResource<Void, Any, AuthViewState>(
                sessionManager.isConnectedToTheInternet(),
                false,
                false,
                false
            ){
                // Ignore
                override fun loadFromCache(): LiveData<AuthViewState> {
                    return AbsentLiveData.create()
                }

                // Ignore
                override suspend fun updateLocalDb(cacheObject: Any?) {

                }

                override suspend fun createCacheRequestAndReturn() {
                    Log.d(TAG, "createCacheRequestAndReturn: searching for token... email : ${previousAuthUserEmail}")

                    accountPropertiesDao.searchByPhone(previousAuthUserEmail).let { accountProperties ->
                        Log.d(TAG, "createCacheRequestAndReturn: searching for token... account properties: ${accountProperties}")
                        accountProperties?.let {
                            if(accountProperties.id!! > -1){
                                authTokenDao.searchByPk(accountProperties.id!!).let { authToken ->
                                    if(authToken != null){
                                        if(authToken.token != null){
                                            onCompleteJob(
                                                DataState.data(
                                                    AuthViewState(authToken = authToken)
                                                )
                                            )
                                            return
                                        }
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "createCacheRequestAndReturn: AuthToken not found...")
                        onCompleteJob(
                            DataState.data(
                                null,
                                Response(
                                    RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                                    ResponseType.None()
                                )
                            )
                        )
                    }
                }

                // not used in this case
                override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {
                }

                // not used in this case
                override fun createCall(): LiveData<GenericApiResponse<Void>> {
                    return AbsentLiveData.create()
                }

                override fun setJob(job: Job) {
                    addJob("checkPreviousAuthUser", job)
                }


            }.asLiveData()
        }
    }

    private fun saveAuthenticatedUserToPrefs(email: String? = null){
        sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
        sharedPrefsEditor.apply()
    }

    private fun returnNoTokenFound(): LiveData<DataState<AuthViewState>>{
        return object: LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.data(null, Response(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None()))
            }
        }
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType): LiveData<DataState<AuthViewState>>{
        Log.d(TAG, "returnErrorResponse: ${errorMessage}")

        return object: LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }
}







