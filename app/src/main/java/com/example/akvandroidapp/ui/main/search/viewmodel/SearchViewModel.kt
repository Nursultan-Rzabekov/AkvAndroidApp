package com.example.akvandroidapp.ui.main.search.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.akvandroidapp.persistence.BlogQueryUtils
import com.example.akvandroidapp.repository.main.SearchRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.main.search.state.SearchStateEvent
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_ORDER
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class SearchViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchRepository: SearchRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<SearchStateEvent, SearchViewState>(){



    init {
        setBlogFilter(
            sharedPreferences.getString(
                Search_FILTER,
                BlogQueryUtils.BLOG_FILTER_DATE_UPDATED
            ).toString()
        )
        setBlogOrder(
            sharedPreferences.getString(
                Search_ORDER,
                BlogQueryUtils.BLOG_ORDER_ASC
            ).toString()
        )
    }


    override fun handleStateEvent(stateEvent: SearchStateEvent): LiveData<DataState<SearchViewState>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initNewViewState(): SearchViewState {
        return SearchViewState()
    }

    fun saveFilterOptions(filter: String, order: String){
        editor.putString(Search_FILTER, filter)
        editor.apply()

        editor.putString(Search_ORDER, order)
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











