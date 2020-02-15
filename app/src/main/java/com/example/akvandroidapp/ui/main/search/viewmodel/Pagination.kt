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
import com.example.akvandroidapp.ui.main.messages.state.RequestStateEvent
import com.example.akvandroidapp.ui.main.messages.state.RequestViewState
import com.example.akvandroidapp.ui.main.messages.viewmodel.MessagesViewModel
import com.example.akvandroidapp.ui.main.messages.viewmodel.RequestViewModel
import com.example.akvandroidapp.ui.main.profile.my_house.state.MyHouseStateStateEvent
import com.example.akvandroidapp.ui.main.profile.my_house.state.MyHouseViewModel
import com.example.akvandroidapp.ui.main.profile.my_house.state.MyHouseViewState
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.profile.viewmodel.ProfileViewModel
import com.example.akvandroidapp.ui.main.search.state.SearchStateEvent
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeBookViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeViewState
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
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


fun ZhilyeReviewViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.reviewsField.page = 1
    setViewState(update)
}

fun DetailsViewModel.setMessageBody(messagesBody: String){
    val update = getCurrentViewStateOrNew()
    update.sendMessageFields.messageBody = messagesBody
    setViewState(update)
}

fun DetailsViewModel.setUserId(userId: Int){
    val update = getCurrentViewStateOrNew()
    update.sendMessageFields.userId = userId
    setViewState(update)
}

fun DetailsViewModel.setImageMultipart(photos:  MultipartBody.Part?) {
    val update = getCurrentViewStateOrNew()
    update.sendMessageFields.images = photos
    setViewState(update)
}

fun MyHouseViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.myHouseFields.page = 1
    setViewState(update)
}

fun MessagesViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.page = 1
    setViewState(update)
}

fun RequestViewModel.resetOrderPage(){
    val update = getCurrentViewStateOrNew()
    update.ordersField.page = 1
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


fun ZhilyeReviewViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(ZhilyeReviewsStateEvent.ZhilyeReviewsEvent())
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.reviewsField.reviewList}")
}

fun MyHouseViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(MyHouseStateStateEvent.MyHouseEvent())
}

fun MessagesViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    Log.e(TAG, "loadFirstPageChat: ${viewState.value!!.myChatFields}")
    setStateEvent(MessagesStateEvent.ChatInfoEvent())
}

fun RequestViewModel.loadOrderFirstPage() {
    setOrderQueryInProgress(true)
    setOrderQueryExhausted(false)
    resetOrderPage()
    Log.e(TAG, "loadFirstPageChatOrder: ${viewState.value!!.ordersField}")
    setStateEvent(RequestStateEvent.OrdersListStateEvent())
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

private fun FavoriteViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.blogFields.page // get current page
    update.blogFields.page = page + 1
    setViewState(update)
}

private fun DetailsViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.myChatFields.page // get current page
    update.myChatFields.page = page + 1
    setViewState(update)
}

private fun ZhilyeReviewViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.reviewsField.page // get current page
    update.reviewsField.page = page + 1
    setViewState(update)
}

private fun MessagesViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.myChatFields.page // get current page
    update.myChatFields.page = page + 1
    setViewState(update)
}

private fun RequestViewModel.incrementOrderPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.ordersField.page // get current page
    update.ordersField.page = page + 1
    setViewState(update)
}

private fun MyHouseViewModel.incrementPageNumber(){
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
        setBlogListData(listOf())
        setQueryInProgress(true)
        setStateEvent(SearchStateEvent.BlogSearchEvent())
    }
}

fun FavoriteViewModel.nextPage(){
    if (!viewState.value!!.blogFields.isQueryInProgress
        && !viewState.value!!.blogFields.isQueryExhausted){
        Log.d(TAG, "FavoriteViewModel: Attempting to load next page...")
        incrementPageNumber()
        setBlogListData(listOf())
        setQueryInProgress(true)
        setStateEvent(FavoriteStateEvent.FavoriteMyListEvent())
    }
}

fun DetailsViewModel.nextPage(){
    if (!viewState.value!!.myChatFields.isQueryInProgress
        && !viewState.value!!.myChatFields.isQueryExhausted){
        Log.d(TAG, "DetailsViewModel: Attempting to load next page...")
        incrementPageNumber()
        setBlogListData(listOf())
        setQueryInProgress(true)
        setStateEvent(DetailsStateEvent.ChatDetailEvent())
    }
}

fun ZhilyeReviewViewModel.nextPage(){
    if(!viewState.value!!.reviewsField.isQueryInProgress
        && !viewState.value!!.reviewsField.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setBlogListData(listOf())
        setQueryInProgress(true)
        setStateEvent(ZhilyeReviewsStateEvent.ZhilyeReviewsEvent())
    }
}

fun MessagesViewModel.nextPage(){
    if(!viewState.value!!.myChatFields.isQueryInProgress
        && !viewState.value!!.myChatFields.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setBlogListData(listOf())
        setQueryInProgress(true)
        setStateEvent(MessagesStateEvent.ChatInfoEvent())
    }
}

fun RequestViewModel.nextOrderPage(){
    if(!viewState.value!!.ordersField.isQueryInProgress
        && !viewState.value!!.ordersField.isQueryExhausted){
        Log.d(TAG, "MessagesViewModel: Attempting to load next page...")
        incrementOrderPageNumber()
        setOrderListData(listOf())
        setOrderQueryInProgress(true)
        setStateEvent(RequestStateEvent.OrdersListStateEvent())
    }
}

fun MyHouseViewModel.nextPage(){
    if(!viewState.value!!.myHouseFields.isQueryInProgress
        && !viewState.value!!.myHouseFields.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setBlogListData(listOf())
        setQueryInProgress(true)
        setStateEvent(MyHouseStateStateEvent.MyHouseEvent())
    }
}

fun MyHouseViewModel.handleIncomingZhilyeData(viewState: MyHouseViewState){
    setZhilyeData(viewState.zhilyeFields)
    setResponseState(viewState.myHouseStateFields.response)
}

fun HomeViewModel.nextPage(){
    if(!viewState.value!!.homeReservationField.isQueryInProgress
        && !viewState.value!!.homeReservationField.isQueryExhausted){
        Log.d(TAG, "HomeViewModel: Attempting to load next page...")
        incrementPageNumber()
        setBlogListData(listOf())
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
    setDeleteState(viewState.deleteblogFields.isDeleted)
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

fun RequestViewModel.handleIncomingOrdersListData(viewState: RequestViewState){
    Log.d(TAG, "MessagesViewModel, DataState: ${viewState}")
    Log.d(TAG, "MessagesViewModel, DataState: isQueryInProgress?: " +
            "${viewState.ordersField.isQueryInProgress}")
    Log.d(TAG, "MessagesViewModel, DataState: isQueryExhausted?: " +
            "${viewState.ordersField.isQueryExhausted}")
    setOrderQueryInProgress(viewState.ordersField.isQueryInProgress)
    setOrderQueryExhausted(viewState.ordersField.isQueryExhausted)
    setAcceptState(viewState.acceptReservationField.isAccepted, viewState.acceptReservationField.message.toString())
    setRejectState(viewState.rejectReservationField.isRejected, viewState.rejectReservationField.message.toString())
    setOrderListData(viewState.ordersField.orders)
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

fun ZhilyeBookViewModel.handleIncomingRequest(viewState: ZhilyeBookViewState){
    Log.d(TAG, "ZhilyeBookViewModel, DataState: ${viewState}")
    setResponse(viewState.reservationRequestField.response.check_in)
}

fun MyHouseViewModel.handleIncomingBlogListData(viewState: MyHouseViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
            "${viewState.myHouseFields.isQueryInProgress}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.myHouseFields.isQueryExhausted}")
    setQueryInProgress(viewState.myHouseFields.isQueryInProgress)
    setQueryExhausted(viewState.myHouseFields.isQueryExhausted)
    setBlogListData(viewState.myHouseFields.blogList)
    setResponseState(viewState.myHouseStateFields.response)
}


fun ZhilyeReviewViewModel.handleIncomingBlogListData(viewState: ZhilyeReviewsViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
            "${viewState.reviewsField.isQueryInProgress}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.reviewsField.isQueryExhausted}")
    setQueryInProgress(viewState.reviewsField.isQueryInProgress)
    setQueryExhausted(viewState.reviewsField.isQueryExhausted)
    setBlogListData(viewState.reviewsField.reviewList)
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
    setCancelState(viewState.cancelReservationField.isCancelled)
    setPayState(viewState.payReservationField.isPayed)
    setBlogListData(viewState.homeReservationField.reservationList)
}

fun ZhilyeViewModel.handleIncomingZhilyeData(viewState: ZhilyeViewState){
    setZhilyeData(viewState.zhilyeFields)
    setCreateFavourite(viewState.createblogFields.isCreated)
    setDeleteFavourite(viewState.deleteblogFields.isDeleted)
}

fun ProfileViewModel.handleIncomingProfileInfo(viewState: ProfileViewState){
    setProfileInfo(viewState.profileInfoFields)
}

fun ProfileViewModel.handleIncomingProfileInfoUpdate(viewState: ProfileViewState){
    setProfileInfoUpdate(viewState.profileInfoUpdateFields)
}

