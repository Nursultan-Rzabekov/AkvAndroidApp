package com.example.akvandroidapp.ui.main.search.zhilye

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
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_BEDS_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_BEDS_RIGHT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_PRICE_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_PRICE_RIGHT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_ROOMS_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_ROOMS_RIGHT
import javax.inject.Inject

class ZhilyeBookViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchRepository: SearchRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<ZhilyeBookStateEvent, ZhilyeBookViewState>(){

    override fun handleStateEvent(stateEvent: ZhilyeBookStateEvent): LiveData<DataState<ZhilyeBookViewState>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initNewViewState(): ZhilyeBookViewState {
        return ZhilyeBookViewState()
    }

    fun cancelActiveJobs(){
        searchRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(ZhilyeBookStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}











