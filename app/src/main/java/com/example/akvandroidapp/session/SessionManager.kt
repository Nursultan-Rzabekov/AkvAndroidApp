package com.example.akvandroidapp.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
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

    private val _cachedToken = MutableLiveData<AuthToken>()

    private val favoritePostList = MutableLiveData<MutableList<BlogPost>>()

    private val _chekedFilterCity = MutableLiveData<FilterCity>()

    init {
        favoritePostList.value = mutableListOf()
        _chekedFilterCity.value = FilterCity("Almaty, Kazakhstan", false, true)
    }

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    val favoritePostListItem: LiveData<MutableList<BlogPost>>
        get() = favoritePostList

    val checkedFilterCity: LiveData<FilterCity>
        get() = _chekedFilterCity

    fun login(newValue: AuthToken){
        setValue(newValue)
    }

    fun favorite(blogPost: BlogPost, checked: Boolean){
        setFavoriteValue(blogPost, checked)
    }

    fun filterCity(filterCity: FilterCity){
        setFilterCity(filterCity)
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
}
