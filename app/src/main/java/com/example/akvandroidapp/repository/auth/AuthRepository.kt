package com.example.akvandroidapp.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.auth.OpenApiAuthService
import com.example.akvandroidapp.api.auth.network_responses.CodeResponse
import com.example.akvandroidapp.api.auth.network_responses.LoginResponse
import com.example.akvandroidapp.api.auth.network_responses.RegistrationResponse
import com.example.akvandroidapp.api.auth.network_responses.UserResponse
import com.example.akvandroidapp.entity.AccountProperties
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.persistence.AccountPropertiesDao
import com.example.akvandroidapp.persistence.AuthTokenDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Response
import com.example.akvandroidapp.ui.ResponseType
import com.example.akvandroidapp.ui.auth.state.*
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
import com.example.akvandroidapp.util.ErrorHandling.Companion.ERROR_SAVE_ACCOUNT_PROPERTIES
import com.example.akvandroidapp.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.example.akvandroidapp.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.example.akvandroidapp.util.GenericApiResponse
import com.example.akvandroidapp.util.PreferenceKeys
import com.example.akvandroidapp.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
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
                        response.body.user.id,
                        response.body.user.email,
                        response.body.user.gender,
                        response.body.user.name,
                        response.body.user.phone,
                        response.body.user.birth_day
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
                        Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                    )
                    )
                }

                Log.d(TAG,"phone or email ${email}")
                saveAuthenticatedUserToPrefs(email)

                sessionManager.login(
                    AuthToken(response.body.user.id,
                        response.body.token)
                )



                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.user.id, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, password)
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

                if(response.body.errorMessage.equals(GENERIC_AUTH_ERROR)){
                    return onErrorReturn(response.body.errorMessage, true, false)
                }

                val result1 = accountPropertiesDao.insertAndReplace(
                    AccountProperties(
                        response.body.user.id,
                        response.body.user.email,
                        response.body.user.gender,
                        response.body.user.name,
                        response.body.user.phone,
                        response.body.user.birth_day
                    )
                )

                // will return -1 if failure
                if(result1 < 0){
                    onCompleteJob(DataState.error(
                        Response(ERROR_SAVE_ACCOUNT_PROPERTIES, ResponseType.Dialog()))
                    )
                    return
                }

                // will return -1 if failure
                val result2 = authTokenDao.insert(
                    AuthToken(
                        response.body.user.id,
                        response.body.token
                    )
                )
                if(result2 < 0){
                    onCompleteJob(DataState.error(
                        Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                    ))
                    return
                }

                //saveAuthenticatedUserToPrefs(email)

                onCompleteJob(
                    DataState.data(
                        data = AuthViewState(
                            authToken = AuthToken(response.body.user.id, response.body.token)
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, gender, phone, password, first_name, last_name, birth_day)
            }

            override fun setJob(job: Job) {
                addJob("attemptRegistration", job)
            }
        }.asLiveData()
    }


    fun sendCode(phone: String): LiveData<DataState<AuthViewState>>{

//        val sendCodeFieldErrors = SendCodeFields(phone).isValidForSendCode()
//        if(!sendCodeFieldErrors.equals(SendCodeFields.SendCodeError.none())){
//            return returnErrorResponse(sendCodeFieldErrors, ResponseType.Dialog())
//        }

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


                if(response.body.response.equals(GENERIC_AUTH_ERROR)){
                    return onErrorReturn(response.body.message, true, false)
                }

//                onCompleteJob(
//                    DataState.data(
//                        data = AuthViewState(
//                            authToken = AuthToken(response.body.response)
//                        )
//                    )
//                )
            }

            override fun createCall(): LiveData<GenericApiResponse<CodeResponse>> {
                return openApiAuthService.sendCode(phone)
            }

            override fun setJob(job: Job) {
                addJob("attemptSendCode", job)
            }
        }.asLiveData()
    }

    fun verifyCode(phone: String, code:String): LiveData<DataState<AuthViewState>>{

        val verifyCodeFieldErrors = VerifyCodeFields(phone,code).isValidForSendCode()
        if(!verifyCodeFieldErrors.equals(VerifyCodeFields.VerifyCodeError.none())){
            return returnErrorResponse(verifyCodeFieldErrors, ResponseType.Dialog())
        }

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


                if(response.body.response.equals(GENERIC_AUTH_ERROR)){
                    return onErrorReturn(response.body.message, true, false)
                }

            }

            override fun createCall(): LiveData<GenericApiResponse<CodeResponse>> {
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

                    Log.d(TAG,"phone")
                    if(previousAuthUserEmail.startsWith("8")){
                        accountPropertiesDao.searchByPhone(previousAuthUserEmail).let { accountProperties ->
                            Log.d(TAG, "createCacheRequestAndReturn: searching for token... account properties: ${accountProperties}")

                            accountProperties?.let {
                                if(accountProperties.id > -1){
                                    authTokenDao.searchByPk(accountProperties.id).let { authToken ->
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
                    else{
                        Log.d(TAG,"phone ${previousAuthUserEmail}")
                        accountPropertiesDao.searchByEmail(previousAuthUserEmail).let { accountProperties ->
                            Log.d(TAG, "createCacheRequestAndReturn: searching for token... account properties: ${accountProperties}")

                            accountProperties?.let {
                                if(accountProperties.id > -1){
                                    authTokenDao.searchByPk(accountProperties.id).let { authToken ->
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

    private fun saveAuthenticatedUserToPrefs(email: String){
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







