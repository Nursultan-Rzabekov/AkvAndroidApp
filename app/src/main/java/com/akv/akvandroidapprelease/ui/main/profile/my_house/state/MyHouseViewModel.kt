package com.akv.akvandroidapprelease.ui.main.profile.my_house.state


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.ProfileRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getHouseId
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getPage
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getState
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getZhilyeHouseId
import com.akv.akvandroidapprelease.util.AbsentLiveData
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject


class MyHouseViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository
): BaseViewModel<MyHouseStateStateEvent, MyHouseViewState>()
{
    override fun handleStateEvent(stateEvent: MyHouseStateStateEvent): LiveData<DataState<MyHouseViewState>> {
        when(stateEvent){

            is MyHouseStateStateEvent.MyHouseEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    profileRepository.myHouseList(
                        authToken = authToken,
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is MyHouseStateStateEvent.MyHouseStateEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    Log.d("Go", "Go go go go  + ${getState()}")
                    Log.d("Go", "Go go go go  + ${getHouseId()}")
                    if(getState() == 0){
                        profileRepository.myHouseStateActivate(
                            authToken = authToken,
                            houseId = getHouseId()
                        )
                    }
                    else{
                        profileRepository.myHouseStateDeactivate(
                            authToken = authToken,
                            houseId = getHouseId()
                        )
                    }
                }?: AbsentLiveData.create()
            }

            is MyHouseStateStateEvent.MyHouseZhilyeEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    profileRepository.getZhilyeWithHouseId(
                        authToken,
                        houseId = getZhilyeHouseId()
                    )
                }?: AbsentLiveData.create()
            }

            is MyHouseStateStateEvent.MyHouseUpdateEvent -> {
//                var facilities: MutableList<RequestBody>? = null
//                if (stateEvent.facilitiesList != null){
//                    facilities = mutableListOf()
//                    stateEvent.facilitiesList?.forEach {
//                        Log.e("ASASDASD", "$it")
//                        facilities.add(
//                            RequestBody.create(MediaType.parse("text/plain"), it)
//                        )
//                    }
//                }
//
//                var rules: MutableList<RequestBody>? = null
//                if (stateEvent.rulesList != null){
//                    rules = mutableListOf()
//                    stateEvent.rulesList?.forEach {
//                        rules.add(
//                            RequestBody.create(MediaType.parse("text/plain"), it)
//                        )
//                    }
//                }
//
//                var nears: MutableList<RequestBody>? = null
//                if (stateEvent.nearsList != null){
//                    nears = mutableListOf()
//                    stateEvent.nearsList?.forEach {
//                        nears.add(
//                            RequestBody.create(MediaType.parse("text/plain"), it)
//                        )
//                    }
//                }

                var photoList: MutableList<RequestBody>? = null
                if (stateEvent.photoList != null){
                    photoList = mutableListOf()
                    stateEvent.photoList?.forEach {
                        photoList.add(
                            RequestBody.create(MediaType.parse("text/plain"), it)
                        )
                    }
                }

                Log.e("ASASDASD rrr", "${stateEvent.rulesList}")

                return sessionManager.cachedToken.value?.let { authToken ->
                    profileRepository.updateZhilyeInfo(
                        authToken = authToken,
                        houseId = stateEvent.house_id,
                        title =
                        if (!stateEvent.title.isNullOrBlank())
                            RequestBody.create(MediaType.parse("text/plain"), stateEvent.title.toString())
                        else null,
                        description =
                        if (!stateEvent.description.isNullOrBlank())
                            RequestBody.create(MediaType.parse("text/plain"), stateEvent.description.toString())
                        else null,
                        price =
                        if (stateEvent.price != null)
                            RequestBody.create(MediaType.parse("text/plain"), stateEvent.price.toString())
                        else null,
                        address =
                        if (stateEvent.address != null)
                            RequestBody.create(MediaType.parse("text/plain"), stateEvent.address.toString())
                        else null,
                        facilitiesList = stateEvent.facilitiesList,
                        nearsList = stateEvent.nearsList,
                        rulesList = stateEvent.rulesList,
                        photoList = photoList,
                        datesList = stateEvent.datesList
                    )
                }?: AbsentLiveData.create()
            }

            is MyHouseStateStateEvent.None ->{
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

    override fun initNewViewState(): MyHouseViewState {
        return MyHouseViewState()
    }

    fun cancelActiveJobs(){
        handlePendingData()
        profileRepository.cancelActiveJobs()
    }

    fun handlePendingData(){
        setStateEvent(MyHouseStateStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
































