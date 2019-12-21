package com.example.akvandroidapp.ui.main.search.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.akvandroidapp.repository.main.SearchRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.main.search.state.SearchStateEvent
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_BEDS_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_BEDS_RIGHT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_PRICE_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_PRICE_RIGHT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_ROOMS_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_ROOMS_RIGHT
import javax.inject.Inject

class SearchViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchRepository: SearchRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<SearchStateEvent, SearchViewState>(){

//    init {
//        setBlogFilter(
//            sharedPreferences.getString(
//                Search_FILTER,
//                ""
//            ).toString()
//        )
//        setBlogOrder(
//            sharedPreferences.getString(
//                Search_ORDER,
//                ""
//            ).toString()
//        )
//    }

    override fun handleStateEvent(stateEvent: SearchStateEvent): LiveData<DataState<SearchViewState>> {
        when(stateEvent){

            is SearchStateEvent.BlogSearchEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    Log.d("RangeBar", "result  + ${getFilterPriceLeft()}")
                    Log.d("RangeBar", "Touch  + ${getPage()}")
                    searchRepository.searchBlogPosts(
                        authToken = authToken,
                        city__name = getSearchQuery(),
                        price__gte = getFilterPriceLeft(),
                        price__lte = getFilterPriceRight(),
                        room__gte = getFilterRoomsLeft(),
                        room__lte = getFilterRoomsRight(),
                        beds_gte = getFilterBedsLeft(),
                        beds_lte = getFilterBedsLeft(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.CheckAuthorOfBlogPost -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.searchBlogPosts(
                        authToken = authToken,
                        city__name = getSearchQuery(),
                        price__gte = getFilterPriceLeft(),
                        price__lte = getFilterPriceRight(),
                        room__gte = getFilterRoomsLeft(),
                        room__lte = getFilterRoomsRight(),
                        beds_gte = getFilterBedsLeft(),
                        beds_lte = getFilterBedsLeft(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.DeleteBlogPostEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.searchBlogPosts(
                        authToken = authToken,
                        city__name = getSearchQuery(),
                        price__gte = getFilterPriceLeft(),
                        price__lte = getFilterPriceRight(),
                        room__gte = getFilterRoomsLeft(),
                        room__lte = getFilterRoomsRight(),
                        beds_gte = getFilterBedsLeft(),
                        beds_lte = getFilterBedsLeft(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.UpdateBlogPostEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.searchBlogPosts(
                        authToken = authToken,
                        city__name = getSearchQuery(),
                        price__gte = getFilterPriceLeft(),
                        price__lte = getFilterPriceRight(),
                        room__gte = getFilterRoomsLeft(),
                        room__lte = getFilterRoomsRight(),
                        beds_gte = getFilterBedsLeft(),
                        beds_lte = getFilterBedsLeft(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.None ->{
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

    override fun initNewViewState(): SearchViewState {
        return SearchViewState()
    }


    fun saveFilterOptions(filter_price_left: Int,
                          filter_price_right: Int,
                          filter_rooms_left: Int,
                          filter_rooms_right: Int,
                          filter_beds_left: Int,
                          filter_beds_right: Int){
        editor.putInt(Search_FILTER_PRICE_LEFT, filter_price_left)
        editor.apply()

        editor.putInt(Search_FILTER_PRICE_RIGHT, filter_price_right)
        editor.apply()

        editor.putInt(Search_FILTER_ROOMS_LEFT, filter_rooms_left)
        editor.apply()

        editor.putInt(Search_FILTER_ROOMS_RIGHT, filter_rooms_right)
        editor.apply()

        editor.putInt(Search_FILTER_BEDS_LEFT, filter_beds_left)
        editor.apply()

        editor.putInt(Search_FILTER_BEDS_RIGHT, filter_beds_right)
        editor.apply()
    }

    fun cancelActiveJobs(){
        searchRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(SearchStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}











