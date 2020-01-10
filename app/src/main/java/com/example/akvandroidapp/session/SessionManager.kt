package com.example.akvandroidapp.session

import android.app.Application
import android.content.Context
import com.yandex.mapkit.geometry.Point
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.AuthTokenDao
import com.example.akvandroidapp.ui.main.search.filter.FilterCity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {

    private val TAG: String = "AppDebug"
    private val DEFAULT_TYPE = 0

    //Room
    private val hostMode = MutableLiveData<Boolean>()


    private val _locationList = MutableLiveData<MutableList<Point>>()
    private val _cachedToken = MutableLiveData<AuthToken>()
    private val _favoritePostList = MutableLiveData<MutableList<BlogPost>>()
    private val _chekedFilterCity = MutableLiveData<FilterCity>()
    private val _typeOfApartment = MutableLiveData<Int>()
    private val _facilitiesList = MutableLiveData<MutableList<Int>>()

    //Add ad
    private val _addAdInfo = MutableLiveData<AddAdInfo>()

    //Settings
    private val _settingsInfo = MutableLiveData<SettingsInfo>()

    //Profile
    private val _profileInfo = MutableLiveData<ProfileInfo>()

    //House Update Data
    private val _houseUpdateData = MutableLiveData<HouseUpdateData>()

    init {
        _favoritePostList.value = mutableListOf()
        _chekedFilterCity.value = FilterCity("нет", false, true)
        _typeOfApartment.value = DEFAULT_TYPE
        _facilitiesList.value = mutableListOf()
        _addAdInfo.value = AddAdInfo()
        _settingsInfo.value = SettingsInfo()
        _locationList.value = mutableListOf()
        _profileInfo.value = ProfileInfo()
        hostMode.value = false
        _houseUpdateData.value = HouseUpdateData()
    }

    val locationItem: LiveData<MutableList<Point>>
        get() = _locationList

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    val favoritePostListItem: LiveData<MutableList<BlogPost>>
        get() = _favoritePostList

    val checkedFilterCity: LiveData<FilterCity>
        get() = _chekedFilterCity

    val typeOfApartment: LiveData<Int>
        get() = _typeOfApartment

    val facilitiesList: LiveData<MutableList<Int>>
        get() = _facilitiesList

    val addAdInfo: LiveData<AddAdInfo>
        get() = _addAdInfo

    val settingsInfo: LiveData<SettingsInfo>
        get() = _settingsInfo

    val profileInfo: LiveData<ProfileInfo>
        get() = _profileInfo

    val houseUpdateData: LiveData<HouseUpdateData>
        get() = _houseUpdateData

    // Room
    val isHostMode: LiveData<Boolean>
        get() = hostMode

    fun login(newValue: AuthToken){
        setValue(newValue)
    }

    fun locationItemCount(point: ArrayList<Point>){
        setLocationValue(point)
    }

    fun favorite(blogPost: BlogPost, checked: Boolean){
        setFavoriteValue(blogPost, checked)
    }

    fun filterCity(filterCity: FilterCity){
        setFilterCity(filterCity)
    }

    fun filterTypeOfApartment(type: Int){
        setFilterTypeOfApartment(type)
    }

    fun filterFacilitiesList(facility: Int, checked: Boolean){
        setFacilityValue(facility, checked)
    }

    fun setLocationValue(point: ArrayList<Point>) {
        Log.d(TAG, "location ee ${_locationList.value}")

        GlobalScope.launch(Main) {
            _locationList.value = point
            Log.d(TAG, "location ${_locationList.value}")
        }
    }

    // House Update Data

    fun setHouseUpdateData(houseUpdateData: HouseUpdateData) {
        GlobalScope.launch(Main) {
            _houseUpdateData.value = houseUpdateData
        }
        Log.e("HOUSE_UPDATE_DATA", "${_houseUpdateData.value}")
    }

    fun clearHouseUpdateData(){
        GlobalScope.launch(Main) {
            _houseUpdateData.value = HouseUpdateData()
        }
        Log.e("HOUSE_CLEAR_DATA", "${_houseUpdateData.value}")
    }

    fun setHouseUpdateFacilityItem(facility: String, checked: Boolean){
        GlobalScope.launch(Main){
            if (checked)
                _houseUpdateData.value?.facilitiesList?.add(facility)
            else
                _houseUpdateData.value?.facilitiesList?.remove(facility)
        }
        Log.e("HOUSE_UPDATE_FACILITI", "${_houseUpdateData.value?.facilitiesList}")
    }

    fun setHouseUpdateNearItem(near: String, checked: Boolean){
        GlobalScope.launch(Main){
            if (checked)
                _houseUpdateData.value?.nearByList?.add(near)
            else
                _houseUpdateData.value?.nearByList?.remove(near)
        }
        Log.e("HOUSE_UPDATE_NEAR", "${_houseUpdateData.value?.nearByList}")
    }

    fun setHouseUpdateRulesItem(rule: String, checked: Boolean){
        GlobalScope.launch(Main){
            if (checked)
                _houseUpdateData.value?.houseRulesList?.add(rule)
            else
                _houseUpdateData.value?.houseRulesList?.remove(rule)
        }
        Log.e("HOUSE_UPDATE_RULES", "${_houseUpdateData.value?.houseRulesList}")
    }

    fun clearHouseUpdateFacilities(){
        GlobalScope.launch(Main) {
            _houseUpdateData.value?.facilitiesList?.clear()
        }
    }

    fun clearHouseUpdateNears(){
        GlobalScope.launch(Main) {
            _houseUpdateData.value?.nearByList?.clear()
        }
    }

    fun clearHouseUpdateRules(){
        GlobalScope.launch(Main) {
            _houseUpdateData.value?.houseRulesList?.clear()
        }
    }

    // Main Account

    fun changeHostMode(host: Boolean) {
        GlobalScope.launch(Main) {
            hostMode.value = host
        }
        Log.e("HOST_MDOE", "${hostMode.value}")
    }

    //Profile

    fun setProfileInfo(nickname: String, birthdate: String, gender: Int, phonenumber: String, email: String){
        GlobalScope.launch(Main) {
            _profileInfo.value?.image = null
            _profileInfo.value?.nickname = nickname
            _profileInfo.value?.birthdate = birthdate
            _profileInfo.value?.gender = gender
            _profileInfo.value?.phonenumber = phonenumber
            _profileInfo.value?.email = email
        }
        Log.e("PROFILE_INFO", "${_profileInfo.value}")
    }

    //Settings

    fun setSettingsPushNotifications(checked: Boolean) {
        GlobalScope.launch(Main) {
            _settingsInfo.value?._pushNotificationsOn = checked
        }
        Log.e("SETTINGS_PUSH", "${_settingsInfo.value?._pushNotificationsOn}")
    }

    fun setSettingsEmailNotifications(checked: Boolean) {
        GlobalScope.launch(Main) {
            _settingsInfo.value?._emailNotificationsOn = checked
        }
        Log.e("SETTINGS_EMAIL", "${_settingsInfo.value?._emailNotificationsOn}")
    }

    fun setSettingsGeolocation(type: Int) {
        GlobalScope.launch(Main) {
            _settingsInfo.value?._geolocationState = type
        }
        Log.e("SETTINGS_GEOLOCATION", "${_settingsInfo.value?._geolocationState}")
    }

    fun setSettingsRegion(region: FilterCity) {
        GlobalScope.launch(Main) {
            _settingsInfo.value?._region = region
            _settingsInfo.value?._region?.isSelected = true
        }
        Log.e("SETTINGS_REGION", "${_settingsInfo.value?._region}")
    }

    // Add Ad

    fun setAddAdFacilityListItem(facility: String, checked: Boolean) {
        GlobalScope.launch(Main){
            if (checked)
                _addAdInfo.value?._addAdFacilityList?.add(facility)
            else
                _addAdInfo.value?._addAdFacilityList?.remove(facility)
        }
        Log.e("SESSION_ADD_AD_FACILITI", "${_addAdInfo.value?._addAdFacilityList}")
    }

    fun clearAddAdFacilityList(){
        GlobalScope.launch(Main) {
            _addAdInfo.value?._addAdFacilityList?.clear()
        }
        Log.e("SESSION_CLEAR_FACILITY", "${_addAdInfo.value?._addAdFacilityList}")
    }

    fun setAddAdNearByListItem(near: String, checked: Boolean) {
        GlobalScope.launch(Main){
            if (checked)
                _addAdInfo.value?._addAdNearByList?.add(near)
            else
                _addAdInfo.value?._addAdNearByList?.remove(near)
        }
        Log.e("SESSION_ADD_AD_NEAR", "${_addAdInfo.value?._addAdNearByList}")
    }

    fun clearAddAdNearList() {
        GlobalScope.launch(Main) {
            _addAdInfo.value?._addAdNearByList?.clear()
        }
        Log.e("SESSION_CLEAR_NEAR", "${_addAdInfo.value?._addAdNearByList}")
    }

    fun setAddAdRulesListItem(rule: String, checked: Boolean) {
        GlobalScope.launch(Main){
            if (checked)
                _addAdInfo.value?._addAdRulesList?.add(rule)
            else
                _addAdInfo.value?._addAdRulesList?.remove(rule)
        }
        Log.e("SESSION_ADD_AD_RULES", "${_addAdInfo.value?._addAdRulesList}")
    }

    fun clearAddAdRulesList(){
        GlobalScope.launch(Main) {
            _addAdInfo.value?._addAdRulesList?.clear()
        }
        Log.e("SESSION_RULES", "${_addAdInfo.value?._addAdRulesList}")
    }

    fun setAddAdPriceAndDiscounts(price: Int, days7: Int, days30: Int){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdPrice = price
            _addAdInfo.value?._addAd7DaysDiscount = days7
            _addAdInfo.value?._addAd30DaysDiscount = days30
        }
        Log.e("SESSION_ADD_PRICE", "${_addAdInfo.value?._addAdPrice}")
    }

    fun setAddAdImage(imageUri1: Uri?, imageUri2: Uri?, imageUri3: Uri?, imageUri4: Uri?, imageUri5: Uri?, imageUri6: Uri?){
        GlobalScope.launch(Main){
            imageUri1?.let {
                _addAdInfo.value?._addAdImage?.add(imageUri1)
            }
            imageUri2?.let {
                _addAdInfo.value?._addAdImage?.add(imageUri2)
            }
            imageUri3?.let {
                _addAdInfo.value?._addAdImage?.add(imageUri3)
            }
            imageUri4?.let {
                _addAdInfo.value?._addAdImage?.add(imageUri4)
            }
            imageUri5?.let {
                _addAdInfo.value?._addAdImage?.add(imageUri5)
            }
            imageUri6?.let {
                _addAdInfo.value?._addAdImage?.add(imageUri6)
            }
        }
    }

    fun setAddAdtitleAndDescription(title: String, desc: String){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdDescription = desc
            _addAdInfo.value?._addAdTitle = title
        }
    }

    fun setAddAdAddressList(country: String, region: String, city: String, address: String, postIndex: String){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdAddressList?.set(0, country)
            _addAdInfo.value?._addAdAddressList?.set(1, region)
            _addAdInfo.value?._addAdAddressList?.set(2, city)
            _addAdInfo.value?._addAdAddressList?.set(3, address)
            _addAdInfo.value?._addAdAddressList?.set(4, postIndex)
        }
        Log.e("SESSION_ADD_AD_ADDRESS",
            "${_addAdInfo.value?._addAdAddressList}")
    }

    fun setAddAdCounts(guests: Int, rooms: Int, beds: Int){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdGuestsCount = guests
            _addAdInfo.value?._addAdRoomsCount = rooms
            _addAdInfo.value?._addAdBedsCount = beds
        }
        Log.e("SESSION_ADD_AD_COUNTS",
            "${_addAdInfo.value?._addAdGuestsCount}, ${_addAdInfo.value?._addAdRoomsCount}, ${_addAdInfo.value?._addAdBedsCount}")
    }



    fun setAddAdType(type: String){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdType = type
        }
        Log.e("SESSION_ADD_AD_TYPE", "${_addAdInfo.value?._addAdType}")
    }

    fun setFilterTypeOfApartment(type: Int){
        GlobalScope.launch(Main) {
            _typeOfApartment.value = type
        }
        Log.e("SESSION_TYPE", "${_typeOfApartment.value}")
    }

    fun setFacilityValue(facility: Int, checked: Boolean){
        GlobalScope.launch(Main) {
            if(checked){
                _facilitiesList.value?.add(facility)
            }
            else{
                _facilitiesList.value?.remove(facility)
            }

        }
    }

    fun setFavoriteValue(blogPost: BlogPost, checked: Boolean) {
        Log.d(TAG, "favorite ee ${_favoritePostList.value}")

        GlobalScope.launch(Main) {
            if(checked){
                _favoritePostList.value?.add(blogPost)
                Log.d(TAG, "favorite ${_favoritePostList.value}")
            }
            else{
                _favoritePostList.value?.remove(blogPost)
            }

            Log.d(TAG, "favorite ${_favoritePostList.value}")
        }
    }

    fun setFilterCity(filterCity: FilterCity){
        Log.d(TAG, "filter city ee ${_chekedFilterCity.value}")

        GlobalScope.launch(Main) {

            _chekedFilterCity.value = filterCity

            Log.d(TAG, "favorite ${_chekedFilterCity.value}")
        }
    }

    fun logout(){
        Log.d(TAG, "logout: ")


        CoroutineScope(IO).launch{
            var errorMessage: String? = null
            try{
                _cachedToken.value!!.token?.let {
//                    authTokenDao.nullifyToken(it)
                } ?: throw CancellationException("Token Error. Logging out user.")
            }catch (e: CancellationException) {
                Log.e(TAG, "logout: ${e.message}")
                errorMessage = e.message
            }
            catch (e: Exception) {
                Log.e(TAG, "logout: ${e.message}")
                errorMessage = errorMessage + "\n" + e.message
            }
            finally {
                errorMessage?.let{
                    Log.e(TAG, "logout: ${errorMessage}" )
                }
                Log.d(TAG, "logout: finally")
                setValue(null)

            }
        }
    }

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }

    fun isConnectedToTheInternet(): Boolean{
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try{
            return cm.activeNetworkInfo.isConnected
        }catch (e: Exception){
            Log.e(TAG, "isConnectedToTheInternet: ${e.message}")
        }
        return false
    }

    fun clearTypeOfApartment(){
        GlobalScope.launch(Main) {
            _typeOfApartment.value = DEFAULT_TYPE
        }
    }

    fun clearFilterCity(){
        GlobalScope.launch(Main) {
            _chekedFilterCity.value = FilterCity("нет", false, true)
        }
    }

    fun clearFilterFacilities(){
        GlobalScope.launch(Main) {
            _facilitiesList.value = mutableListOf()
        }
    }
}
