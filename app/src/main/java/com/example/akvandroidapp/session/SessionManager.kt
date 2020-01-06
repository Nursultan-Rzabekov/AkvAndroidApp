package com.example.akvandroidapp.session

import android.app.Application
import android.content.Context
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

    private val _cachedToken = MutableLiveData<AuthToken>()
    private val favoritePostList = MutableLiveData<MutableList<BlogPost>>()
    private val _chekedFilterCity = MutableLiveData<FilterCity>()
    private val _typeOfApartment = MutableLiveData<Int>()
    private val _facilitiesList = MutableLiveData<MutableList<Int>>()

    //Add ad
    private val _addAdInfo = MutableLiveData<AddAdInfo>()

    init {
        favoritePostList.value = mutableListOf()
        _chekedFilterCity.value = FilterCity("нет", false, true)
        _typeOfApartment.value = DEFAULT_TYPE
        _facilitiesList.value = mutableListOf()
        _addAdInfo.value = AddAdInfo()
    }

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    val favoritePostListItem: LiveData<MutableList<BlogPost>>
        get() = favoritePostList

    val checkedFilterCity: LiveData<FilterCity>
        get() = _chekedFilterCity

    val typeOfApartment: LiveData<Int>
        get() = _typeOfApartment

    val facilitiesList: LiveData<MutableList<Int>>
        get() = _facilitiesList

    val addAdInfo: LiveData<AddAdInfo>
        get() = _addAdInfo

    fun login(newValue: AuthToken){
        setValue(newValue)
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

    fun setAddAdFacilityListItem(facility: String, checked: Boolean) {
        GlobalScope.launch(Main){
            if (checked)
                _addAdInfo.value?._addAdFacilityList?.add(facility)
            else
                _addAdInfo.value?._addAdFacilityList?.remove(facility)
        }
        Log.e("SESSION_ADD_AD_FACILITI", "${_addAdInfo.value?._addAdFacilityList}")
    }

    fun setAddAdPriceAndDiscounts(price: Int, days7: Int, days30: Int){
        GlobalScope.launch(Main){
            _addAdInfo.value?._addAdPrice = price
            _addAdInfo.value?._addAd7DaysDiscount = days7
            _addAdInfo.value?._addAd30DaysDiscount = days30
        }
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
        Log.d(TAG, "favorite ee ${favoritePostList.value}")

        GlobalScope.launch(Main) {
            if(checked){
                favoritePostList.value?.add(blogPost)
                Log.d(TAG, "favorite ${favoritePostList.value}")
            }
            else{
                favoritePostList.value?.remove(blogPost)
            }

            Log.d(TAG, "favorite ${favoritePostList.value}")
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
