import android.util.Log
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteStateEvent
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteViewState
import com.example.akvandroidapp.ui.main.favorite.viewmodel.FavoriteViewModel
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
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeBookViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeViewState
import okhttp3.MultipartBody


fun SearchViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

fun FavoriteViewModel.resetPage(){
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

fun DetailsViewModel.setImageMultipart(photos:  MultipartBody.Part?) {
    val update = getCurrentViewStateOrNew()
    update.sendMessageFields.images = photos
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

fun FavoriteViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(FavoriteStateEvent.FavoriteMyListEvent())
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.blogList}")
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

fun FavoriteViewModel.handleIncomingBlogListData(viewState: FavoriteViewState){
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
    setBlogListDataImages(viewState.myChatFields.blogListImages)
}

fun ZhilyeBookViewModel.handleIncomingRequest(viewState: ZhilyeBookViewState){
    Log.d(TAG, "ZhilyeBookViewModel, DataState: ${viewState}")
    setResponse(viewState.reservationRequestField.response.response)
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
    setCount(viewState.homeReservationField.count)
    setBlogListData(viewState.homeReservationField.reservationList)
}

fun ZhilyeViewModel.handleIncomingZhilyeData(viewState: ZhilyeViewState){
    setZhilyeData(viewState.zhilyeFields)
}

fun ProfileViewModel.handleIncomingProfileInfo(viewState: ProfileViewState){
    setProfileInfo(viewState.profileInfoFields)
}

fun ProfileViewModel.handleIncomingProfileInfoUpdate(viewState: ProfileViewState){
    setProfileInfoUpdate(viewState.profileInfoUpdateFields)
}

