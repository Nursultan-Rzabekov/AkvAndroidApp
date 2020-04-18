package com.akv.akvandroidapprelease.ui.main.search.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.SearchRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.ui.main.search.state.SearchStateEvent
import com.akv.akvandroidapprelease.ui.main.search.state.SearchViewState
import com.akv.akvandroidapprelease.util.AbsentLiveData
import javax.inject.Inject

class SearchViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchRepository: SearchRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<SearchStateEvent, SearchViewState>(){


    override fun handleStateEvent(stateEvent: SearchStateEvent): LiveData<DataState<SearchViewState>> {
        when(stateEvent){

            is SearchStateEvent.BlogSearchEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    Log.d("RangeBar", "result  + ${getFilterPriceLeft()}")
                    Log.d("RangeBar", "Touch  + ${getPage()}")
                    searchRepository.searchBlogPosts(
                        authToken = authToken,
                        startDate = getStartDateFilter(),
                        endDate = getEndDateFilter(),
                        adultsCount = getFilterAdultsCount(),
                        childrenCount = getFilterChildrenCount(),
                        query_name = getSearchQuery(),
                        city__name = getCityQuery(),
                        accomadations = getAccomadations(),
                        type_house = getTypeHouse(),
                        ordering = getOrdering(),
                        verified = getVerified(),
                        price__gte = getFilterPriceLeft(),
                        price__lte = getFilterPriceRight(),
                        room__gte = getFilterRoomsLeft(),
                        room__lte = getFilterRoomsRight(),
                        beds_gte = getFilterBedsLeft(),
                        beds_lte = getFilterBedsRight(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.CheckAuthorOfBlogPost -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.searchBlogPosts(
                        authToken = authToken,
                        startDate = getStartDateFilter(),
                        endDate = getEndDateFilter(),
                        adultsCount = getFilterAdultsCount(),
                        childrenCount = getFilterChildrenCount(),
                        query_name = getSearchQuery(),
                        city__name = getCityQuery(),
                        accomadations = getAccomadations(),
                        type_house = getTypeHouse(),
                        ordering = getOrdering(),
                        verified = getVerified(),
                        price__gte = getFilterPriceLeft(),
                        price__lte = getFilterPriceRight(),
                        room__gte = getFilterRoomsLeft(),
                        room__lte = getFilterRoomsRight(),
                        beds_gte = getFilterBedsLeft(),
                        beds_lte = getFilterBedsRight(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.DeleteBlogPostEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.searchBlogPosts(
                        authToken = authToken,
                        startDate = getStartDateFilter(),
                        endDate = getEndDateFilter(),
                        adultsCount = getFilterAdultsCount(),
                        childrenCount = getFilterChildrenCount(),
                        query_name = getSearchQuery(),
                        city__name = getCityQuery(),
                        accomadations = getAccomadations(),
                        type_house = getTypeHouse(),
                        ordering = getOrdering(),
                        verified = getVerified(),
                        price__gte = getFilterPriceLeft(),
                        price__lte = getFilterPriceRight(),
                        room__gte = getFilterRoomsLeft(),
                        room__lte = getFilterRoomsRight(),
                        beds_gte = getFilterBedsLeft(),
                        beds_lte = getFilterBedsRight(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.UpdateBlogPostEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.searchBlogPosts(
                        authToken = authToken,
                        startDate = getStartDateFilter(),
                        endDate = getEndDateFilter(),
                        adultsCount = getFilterAdultsCount(),
                        childrenCount = getFilterChildrenCount(),
                        query_name = getSearchQuery(),
                        city__name = getCityQuery(),
                        accomadations = getAccomadations(),
                        type_house = getTypeHouse(),
                        ordering = getOrdering(),
                        verified = getVerified(),
                        price__gte = getFilterPriceLeft(),
                        price__lte = getFilterPriceRight(),
                        room__gte = getFilterRoomsLeft(),
                        room__lte = getFilterRoomsRight(),
                        beds_gte = getFilterBedsLeft(),
                        beds_lte = getFilterBedsRight(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.DeleteFavoriteItemEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.deleteMyFavoritePostsSearch(
                        authToken = authToken,
                        houseId = stateEvent.houseId
                    )
                }?: AbsentLiveData.create()
            }

            is SearchStateEvent.СreateFavoriteItemEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.createMyFavoritePostsSearch(
                        authToken = authToken,
                        houseId = stateEvent.houseId
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











