import android.util.Log
import com.akv.akvandroidapprelease.ui.auth.AuthViewModel
import com.akv.akvandroidapprelease.ui.auth.state.AuthViewState
import com.akv.akvandroidapprelease.ui.main.favorite.state.FavoriteStateEvent
import com.akv.akvandroidapprelease.ui.main.favorite.state.FavoriteViewState
import com.akv.akvandroidapprelease.ui.main.favorite.viewmodel.FavoriteViewModel
import com.akv.akvandroidapprelease.ui.main.home.state.HomeStateEvent
import com.akv.akvandroidapprelease.ui.main.home.state.HomeViewState
import com.akv.akvandroidapprelease.ui.main.home.viewmodel.HomeViewModel
import com.akv.akvandroidapprelease.ui.main.messages.detailState.DetailsStateEvent
import com.akv.akvandroidapprelease.ui.main.messages.detailState.DetailsViewModel
import com.akv.akvandroidapprelease.ui.main.messages.detailState.DetailsViewState
import com.akv.akvandroidapprelease.ui.main.messages.state.MessagesStateEvent
import com.akv.akvandroidapprelease.ui.main.messages.state.MessagesViewState
import com.akv.akvandroidapprelease.ui.main.messages.state.RequestStateEvent
import com.akv.akvandroidapprelease.ui.main.messages.state.RequestViewState
import com.akv.akvandroidapprelease.ui.main.messages.viewmodel.MessagesViewModel
import com.akv.akvandroidapprelease.ui.main.messages.viewmodel.RequestViewModel
import com.akv.akvandroidapprelease.ui.main.profile.my_house.state.MyHouseStateStateEvent
import com.akv.akvandroidapprelease.ui.main.profile.my_house.state.MyHouseViewModel
import com.akv.akvandroidapprelease.ui.main.profile.my_house.state.MyHouseViewState
import com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel.PaymentStateEvent
import com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel.PaymentViewModel
import com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel.PaymentViewState
import com.akv.akvandroidapprelease.ui.main.profile.state.ProfileViewState
import com.akv.akvandroidapprelease.ui.main.profile.support.viewmodel.SupportViewModel
import com.akv.akvandroidapprelease.ui.main.profile.support.viewmodel.SupportViewState
import com.akv.akvandroidapprelease.ui.main.profile.viewmodel.ProfileViewModel
import com.akv.akvandroidapprelease.ui.main.search.state.SearchStateEvent
import com.akv.akvandroidapprelease.ui.main.search.state.SearchViewState
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.*
import com.akv.akvandroidapprelease.ui.main.search.zhilye.ZhilyeViewModel
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeReviewsStateEvent
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeViewState
import com.akv.akvandroidapprelease.ui.main.search.zhilye.viewmodels.ZhilyeBookViewModel
import com.akv.akvandroidapprelease.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
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

fun PaymentViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.paymentHistoryField.page = 1
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
    setStateEvent(SearchStateEvent.BlogSearchEvent)
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.searchQuery}")
}

fun PaymentViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(PaymentStateEvent.PaymentHistoryEvent)
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.paymentHistoryField.page}")
}

fun FavoriteViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(FavoriteStateEvent.FavoriteMyListEvent)
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.blogFields.blogList}")
}


fun ZhilyeReviewViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(ZhilyeReviewsStateEvent.ZhilyeReviewsEvent)
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${viewState.value!!.reviewsField.reviewList}")
}

fun MyHouseViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(MyHouseStateStateEvent.MyHouseEvent)
}

fun MessagesViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    Log.e(TAG, "loadFirstPageChat: ${viewState.value!!.myChatFields}")
    setStateEvent(MessagesStateEvent.ChatInfoEvent)
}

fun RequestViewModel.loadOrderFirstPage() {
    setOrderQueryInProgress(true)
    setOrderQueryExhausted(false)
    resetOrderPage()
    Log.e(TAG, "loadFirstPageChatOrder: ${viewState.value!!.ordersField}")
    setStateEvent(RequestStateEvent.OrdersListStateEvent)
}

fun DetailsViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    Log.e(TAG, "loadFirstPageChat: ${viewState.value!!.myChatFields}")
    setStateEvent(DetailsStateEvent.ChatDetailEvent)
}

fun HomeViewModel.loadFirstPage(){
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    Log.e(TAG, "loadFirstPageChat: ${viewState.value!!.homeReservationField}")
    setStateEvent(HomeStateEvent.HomeInfoEvent)
}

private fun SearchViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.blogFields.page // get current page
    update.blogFields.page = page + 1
    setViewState(update)
}

private fun PaymentViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.paymentHistoryField.page
    update.paymentHistoryField.page = page + 1
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
        setStateEvent(SearchStateEvent.BlogSearchEvent)
    }
}

fun PaymentViewModel.nextPage(){
    if(!viewState.value!!.paymentHistoryField.isQueryInProgress
        && !viewState.value!!.paymentHistoryField.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setPaymentHistoryData(listOf())
        setQueryInProgress(true)
        setStateEvent(PaymentStateEvent.PaymentHistoryEvent)
    }
}

fun FavoriteViewModel.nextPage(){
    if (!viewState.value!!.blogFields.isQueryInProgress
        && !viewState.value!!.blogFields.isQueryExhausted){
        Log.d(TAG, "FavoriteViewModel: Attempting to load next page...")
        incrementPageNumber()
        setBlogListData(listOf())
        setQueryInProgress(true)
        setStateEvent(FavoriteStateEvent.FavoriteMyListEvent)
    }
}

fun DetailsViewModel.nextPage(){
    if (!viewState.value!!.myChatFields.isQueryInProgress
        && !viewState.value!!.myChatFields.isQueryExhausted){
        Log.d(TAG, "DetailsViewModel: Attempting to load next page...")
        setBlogListData(listOf())
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(DetailsStateEvent.ChatDetailEvent)
    }
}

fun ZhilyeReviewViewModel.nextPage(){
    if(!viewState.value!!.reviewsField.isQueryInProgress
        && !viewState.value!!.reviewsField.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        setBlogListData(listOf())
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(ZhilyeReviewsStateEvent.ZhilyeReviewsEvent)
    }
}

fun MessagesViewModel.nextPage(){
    if(!viewState.value!!.myChatFields.isQueryInProgress
        && !viewState.value!!.myChatFields.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        setBlogListData(listOf())
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(MessagesStateEvent.ChatInfoEvent)
    }
}

fun RequestViewModel.nextOrderPage(){
    if(!viewState.value!!.ordersField.isQueryInProgress
        && !viewState.value!!.ordersField.isQueryExhausted){
        Log.d(TAG, "MessagesViewModel: Attempting to load next page...")
        incrementOrderPageNumber()
        setOrderListData(listOf())
        setOrderQueryInProgress(true)
        setStateEvent(RequestStateEvent.OrdersListStateEvent)
    }
}

fun MyHouseViewModel.nextPage(){
    if(!viewState.value!!.myHouseFields.isQueryInProgress
        && !viewState.value!!.myHouseFields.isQueryExhausted){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setBlogListData(listOf())
        setQueryInProgress(true)
        setStateEvent(MyHouseStateStateEvent.MyHouseEvent)
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
        setStateEvent(HomeStateEvent.HomeInfoEvent)
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

fun AuthViewModel.handleIncomingBlogListData(viewState: AuthViewState){
    Log.e(TAG, "state not witsh qqq  qqq + ${viewState.authViewStateResponse}")
//    setQueryInProgress(viewState.authViewStateResponse?.isQueryInProgress!!)
//    setQueryExhausted(viewState.authViewStateResponse?.isQueryExhausted!!)
    setBlogListData(viewState.authViewStateResponse?.state)
}

fun PaymentViewModel.handleIncomingBlogListData(viewState: PaymentViewState){
    Log.d(TAG, "PaymentViewModel, DataState: ${viewState}")
    Log.d(TAG, "PaymentViewModel, DataState: isQueryInProgress?: " +
            "${viewState.paymentHistoryField.isQueryInProgress}")
    Log.d(TAG, "PaymentViewModel, DataState: isQueryExhausted?: " +
            "${viewState.paymentHistoryField.isQueryExhausted}")
    setQueryInProgress(viewState.paymentHistoryField.isQueryInProgress)
    setQueryExhausted(viewState.paymentHistoryField.isQueryExhausted)
    setPaymentHistoryData(viewState.paymentHistoryField.payments)
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

fun SupportViewModel.handleFeedback(viewState: SupportViewState){
    Log.d(TAG, "SupportViewModel, DataState: ${viewState}")
    setFeedbackId(viewState.id)
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
    setIsReviewCreated(viewState.isReviewCreatedField)
    setIsReviewUpdated(viewState.isReviewUpdatedField)
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
    setProfileInfoValidation(viewState.isPhoneNumberValid)
    setProfileInfoCodeSend(viewState.isCodeSend)
}

