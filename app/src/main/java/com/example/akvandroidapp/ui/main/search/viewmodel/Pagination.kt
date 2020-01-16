import android.util.Log
import com.example.akvandroidapp.ui.main.home.state.HomeStateEvent
import com.example.akvandroidapp.ui.main.home.state.HomeViewState
import com.example.akvandroidapp.ui.main.home.viewmodel.HomeViewModel
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsStateEvent
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewModel
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewState
import com.example.akvandroidapp.ui.main.messages.state.MessagesStateEvent
import com.example.akvandroidapp.ui.main.messages.state.MessagesViewState
import com.example.akvandroidapp.ui.main.messages.viewmodel.MessagesViewModel
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.profile.viewmodel.ProfileViewModel
import com.example.akvandroidapp.ui.main.search.state.SearchStateEvent
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.SearchViewModel
import com.example.akvandroidapp.ui.main.search.viewmodel.setBlogListData
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryInProgress


fun SearchViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}


fun DetailsViewModel.setMessageBody(messagesBody: String){
    val update = getCurrentViewStateOrNew()
    update.sendMessageFields.messageBody = messagesBody
    setViewState(update)
}

fun DetailsViewModel.setEmailName(email: String){
    val update = getCurrentViewStateOrNew()
    update.sendMessageFields.email = email
    setViewState(update)
}


fun ProfileViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.myHouseFields.page = 1
    setViewState(update)
}

fun MessagesViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.page = 1
    setViewState(update)
}

fun DetailsViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.page = 1
    setViewState(update)
}

fun HomeViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.homeReservationField.page = 1
    setViewState(update)
}

fun SearchViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(SearchStateEvent.BlogSearchEvent())
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.searchQuery}")
}

fun ProfileViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(ProfileStateEvent.MyHouseEvent())
}

fun MessagesViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    Log.e(TAG, "loadFirstPageChat: ${viewState.value!!.myChatFields}")
    setStateEvent(MessagesStateEvent.ChatInfoEvent())
}

fun DetailsViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    Log.e(TAG, "loadFirstPageChat: ${viewState.value!!.myChatFields}")
    setStateEvent(DetailsStateEvent.ChatDetailEvent())
}

fun HomeViewModel.loadFirstPage(){
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    Log.e(TAG, "loadFirstPageChat: ${viewState.value!!.homeReservationField}")
    setStateEvent(HomeStateEvent.HomeInfoEvent())
}

private fun SearchViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.blogFields.page // get current page
    update.blogFields.page = page + 1
    setViewState(update)
}

private fun MessagesViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.myChatFields.page // get current page
    update.myChatFields.page = page + 1
    setViewState(update)
}

private fun ProfileViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.myHouseFields.page // get current page
    update.myHouseFields.page = page + 1
    setViewState(update)
}

private fun HomeViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.homeReservationField.page // get current page
    update.homeReservationField.page = page + 1
    setViewState(update)
}

fun SearchViewModel.nextPage(){
    if(!viewState.value!!.blogFields.isQueryInProgress
        && !viewState.value!!.blogFields.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(SearchStateEvent.BlogSearchEvent())
    }
}

fun MessagesViewModel.nextPage(){
    if(!viewState.value!!.myChatFields.isQueryInProgress
        && !viewState.value!!.myChatFields.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(MessagesStateEvent.ChatInfoEvent())
    }
}

fun ProfileViewModel.nextPage(){
    if(!viewState.value!!.myHouseFields.isQueryInProgress
        && !viewState.value!!.myHouseFields.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(ProfileStateEvent.MyHouseEvent())
    }
}

fun HomeViewModel.nextPage(){
    if(!viewState.value!!.homeReservationField.isQueryInProgress
        && !viewState.value!!.homeReservationField.isQueryExhausted){
        Log.d(TAG, "HomeViewModel: Attempting to load next page...")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(HomeStateEvent.HomeInfoEvent())
    }
}

fun SearchViewModel.handleIncomingBlogListData(viewState: SearchViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
            "${viewState.blogFields.isQueryInProgress}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.blogFields.isQueryExhausted}")
    setQueryInProgress(viewState.blogFields.isQueryInProgress)
    setQueryExhausted(viewState.blogFields.isQueryExhausted)
    setBlogListData(viewState.blogFields.blogList)
}


fun MessagesViewModel.handleIncomingBlogListData(viewState: MessagesViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
            "${viewState.myChatFields.isQueryInProgress}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.myChatFields.isQueryExhausted}")
    setQueryInProgress(viewState.myChatFields.isQueryInProgress)
    setQueryExhausted(viewState.myChatFields.isQueryExhausted)
    setBlogListData(viewState.myChatFields.blogList)
}

fun DetailsViewModel.handleIncomingBlogListData(viewState: DetailsViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
            "${viewState.myChatFields.isQueryInProgress}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.myChatFields.isQueryExhausted}")
    setQueryInProgress(viewState.myChatFields.isQueryInProgress)
    setQueryExhausted(viewState.myChatFields.isQueryExhausted)
    setBlogListData(viewState.myChatFields.blogList)
}


fun ProfileViewModel.handleIncomingBlogListData(viewState: ProfileViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
            "${viewState.myHouseFields.isQueryInProgress}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.myHouseFields.isQueryExhausted}")
    setQueryInProgress(viewState.myHouseFields.isQueryInProgress)
    setQueryExhausted(viewState.myHouseFields.isQueryExhausted)
    setBlogListData(viewState.myHouseFields.blogList)
}

fun HomeViewModel.handleIncomingReservationListData(viewState: HomeViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
            "${viewState.homeReservationField.isQueryInProgress}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.homeReservationField.isQueryExhausted}")
    setQueryInProgress(viewState.homeReservationField.isQueryInProgress)
    setQueryExhausted(viewState.homeReservationField.isQueryExhausted)
    setBlogListData(viewState.homeReservationField.reservationList)
}

