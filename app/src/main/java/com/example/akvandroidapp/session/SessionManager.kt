package com.example.akvandroidapp.session

import android.app.Application
import android.content.Context
import com.yandex.mapkit.geometry.Point
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.akvandroidapp.entity.AccountProperties
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.AccountPropertiesDao
import com.example.akvandroidapp.persistence.AuthTokenDao
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.ui.main.search.filter.FilterCity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList


@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val blogPostDao: BlogPostDao,
    val application: Application
) {
    private val TAG: String = "AppDebug"
    private val DEFAULT_TYPE = 0

    //Room
    private val hostMode = MutableLiveData<Boolean>()

    //Map
    private val _locationList = MutableLiveData<MutableList<Point>>()

    //Token
    private val _cachedToken = MutableLiveData<AuthToken>()

    //AccountProperties
    private val _accountProperties = MutableLiveData<AccountProperties>()

    //Favorite
    private val _favoritePostList = MutableLiveData<MutableList<BlogPost>>()

    //Filter
    private val _chekedFilterCity = MutableLiveData<FilterCity>()

    private val _typeOfApartment = MutableLiveData<Int>()

    //Accommodations
    private val _facilitiesList = MutableLiveData<MutableList<Int>>()

    //Add ad
    private val _addAdInfo = MutableLiveData<AddAdInfo>()

    //Settings
    private val _settingsInfo = MutableLiveData<SettingsInfo>()

    //Profile
    private val _profileInfo = MutableLiveData<ProfileInfo>()

    //House Update Data
    private val _houseUpdateData = MutableLiveData<HouseUpdateData>()

    //Filter Update Data
    private val _filterUpdateData = MutableLiveData<FilterUpdateData>()

    init {
        _chekedFilterCity.value = FilterCity("нет", false, true, -1)
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

//    val accountProperties: LiveData<AccountProperties>
//        get() = if(_accountProperties.value == null){
//            CoroutineScope(IO).launch {
//                _cachedToken.value?.id?.let {
//                    withContext(Main) {
//                        _accountProperties.value = accountPropertiesDao.searchByPkUser(it)
//                    }
//                }
//            }
//            _accountProperties
//        }else{
//            _accountProperties
//        }


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

    val filterUpdateData: LiveData<FilterUpdateData>
        get() = _filterUpdateData

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


    fun setFilterUpdateData(filterUpdateData: FilterUpdateData) {
        GlobalScope.launch(Main) {
            _filterUpdateData.value = filterUpdateData
        }
        Log.e("HOUSE_UPDATE_DATA", "${_houseUpdateData.value}")
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

    fun setHouseUpdateFacilityItem(facility: List<String>, checked: Boolean){
        GlobalScope.launch(Main){
            if (checked)
                _houseUpdateData.value?.facilitiesList = facility.toMutableList()
        }
        Log.e("HOUSE_UPDATE_FACILITI", "${_houseUpdateData.value?.facilitiesList}")
    }

    fun setHouseUpdateNearItem(near: List<String>, checked: Boolean){
        GlobalScope.launch(Main){
            if (checked)
                _houseUpdateData.value?.nearByList = near.toMutableList()
        }
        Log.e("HOUSE_UPDATE_NEAR", "${_houseUpdateData.value?.nearByList}")
    }

    fun setHouseUpdateRulesItem(rule: List<String>, checked: Boolean){
        GlobalScope.launch(Main){
            if (checked)
                _houseUpdateData.value?.houseRulesList = rule.toMutableList()
        }
        Log.e("HOUSE_UPDATE_RULES", "${_houseUpdateData.value?.houseRulesList}")
    }

    fun setHouseUpdateDates(dates: List<Date>){
        GlobalScope.launch(Main){
            _houseUpdateData.value?.availableDates = dates.toMutableList()
        }
        Log.e("HOUSE_UPDATE_DATES", "${_houseUpdateData.value?.houseRulesList}")
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

    fun clearHouseUpdateDates(){
        GlobalScope.launch(Main){
            _houseUpdateData.value?.availableDates?.clear()
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

    fun setProfileInfo(nickname: String, birthdate: String, gender: Int, phonenumber: String, email: String, imageBackend:String? = null){
        GlobalScope.launch(Main) {
            _profileInfo.value?.image = null
            _profileInfo.value?.nickname = nickname
            _profileInfo.value?.birthdate = birthdate
            _profileInfo.value?.gender = gender
            _profileInfo.value?.phonenumber = phonenumber
            _profileInfo.value?.email = email
            _profileInfo.value?.imageBackend = imageBackend
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

    fun setAddAdFacilityListItem(facilities: List<String>, checked: Boolean) {
        GlobalScope.launch(Main){
            if (checked)
                _addAdInfo.value?._addAdFacilityList = facilities.toMutableList()
        }
        Log.e("SESSION_ADD_AD_FACILITI", "${_addAdInfo.value?._addAdFacilityList}")
    }

    fun setAddAdAvailableDates(dates: List<Date>){
        GlobalScope.launch(Main) {
            _addAdInfo.value?._addAdAvailableList = dates.toMutableList()
        }
        Log.e("SESSION_ADD_AD_DATES", "${_addAdInfo.value?._addAdAvailableList}")
    }

    fun clearAddAdAvailableDates(){
        GlobalScope.launch(Main) {
            _addAdInfo.value?._addAdAvailableList?.clear()
        }
        Log.e("SESSION_ADD_AD_DATES", "${_addAdInfo.value?._addAdAvailableList}")
    }

    fun clearAddAdFacilityList(){
        GlobalScope.launch(Main) {
            _addAdInfo.value?._addAdFacilityList?.clear()
        }
        Log.e("SESSION_CLEAR_FACILITY", "${_addAdInfo.value?._addAdFacilityList}")
    }

    fun setAddAdNearByListItem(near: List<String>, checked: Boolean) {
        GlobalScope.launch(Main){
            if (checked)
                _addAdInfo.value?._addAdNearByList = near.toMutableList()
        }
        Log.e("SESSION_ADD_AD_NEAR", "${_addAdInfo.value?._addAdNearByList}")
    }

    fun clearAddAdNearList() {
        GlobalScope.launch(Main) {
            _addAdInfo.value?._addAdNearByList?.clear()
        }
        Log.e("SESSION_CLEAR_NEAR", "${_addAdInfo.value?._addAdNearByList}")
    }

    fun setAddAdRulesListItem(rule: List<String>, checked: Boolean) {
        GlobalScope.launch(Main){
            if (checked)
                _addAdInfo.value?._addAdRulesList = rule.toMutableList()
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
        Log.e("SESSION_ADD_PRICE", "${_addAdInfo.value?._addAd7DaysDiscount}")
    }

    fun clearAddAdPriceAndDiscounts(){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdPrice = 0
            _addAdInfo.value?._addAd7DaysDiscount = 0
            _addAdInfo.value?._addAd30DaysDiscount = 0
        }
        Log.e("SESSION_CLEAR_PRICE", "${_addAdInfo.value?._addAd7DaysDiscount}")
    }

    fun setAddAdImage(images: List<Uri>){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdImage = images.toMutableList()
        }
    }

    fun clearAddAdImages(){
        GlobalScope.launch(Main) {
            _addAdInfo.value?._addAdImage = mutableListOf()
        }
    }

    fun setAddAdtitleAndDescription(title: String, desc: String){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdDescription = desc
            _addAdInfo.value?._addAdTitle = title
        }
    }

    fun setAddAdAddressList(country: Int, region: Int, city: Int, address: String){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdAddress = address
            _addAdInfo.value?._addAdAddressCityId = city
            _addAdInfo.value?._addAdAddressRegionId = region
            _addAdInfo.value?._addAdAddressCountry = country
        }
        Log.e("SESSION_ADD_AD_ADDRESS",
            "${_addAdInfo.value?._addAdAddress}")
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

    fun clearAddAdCounts(){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdGuestsCount = 0
            _addAdInfo.value?._addAdRoomsCount = 0
            _addAdInfo.value?._addAdBedsCount = 0
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

    fun clearAddAdAllInfo(){
        GlobalScope.launch(Main){
            _addAdInfo.value = AddAdInfo()
        }
        Log.e("SESSION_ADD_AD_ALL", "${_addAdInfo.value}")
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
                blogPostDao.insert(blogPost)
                Log.d(TAG, "favorite ${_favoritePostList.value}")
            }
            else{
                _favoritePostList.value?.remove(blogPost)
                blogPostDao.delete(blogPost)
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
                _cachedToken.value!!.id?.let {
                    authTokenDao.nullifyToken(it)
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
            _chekedFilterCity.value = FilterCity("нет", false, true, -1)
        }
    }

    fun clearFilterFacilities(){
        GlobalScope.launch(Main) {
            _facilitiesList.value = mutableListOf()
        }
    }
}
