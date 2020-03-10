package com.akv.akvandroidapp.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.akv.akvandroidapp.api.main.OpenApiMainService
import com.akv.akvandroidapp.api.main.bodies.CreateCancelReservationBody
import com.akv.akvandroidapp.api.main.responses.HomeListResponse
import com.akv.akvandroidapp.api.main.responses.PayRequestResponse
import com.akv.akvandroidapp.api.main.responses.VerifyUpdateResponse
import com.akv.akvandroidapp.entity.AuthToken
import com.akv.akvandroidapp.entity.BlogPost
import com.akv.akvandroidapp.entity.HomeReservation
import com.akv.akvandroidapp.entity.PayReservationIdResponse
import com.akv.akvandroidapp.repository.JobManager
import com.akv.akvandroidapp.repository.NetworkBoundResource
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.main.home.state.HomeViewState
import com.akv.akvandroidapp.util.AbsentLiveData
import com.akv.akvandroidapp.util.ApiSuccessResponse
import com.akv.akvandroidapp.util.Constants
import com.akv.akvandroidapp.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val sessionManager: SessionManager
): JobManager("HomeRepository")
{
    private val TAG: String = "AppDebug"

    fun getReservations(
        authToken: AuthToken,
        page: Int
    ): LiveData<DataState<HomeViewState>>{
        return object:
            NetworkBoundResource<HomeListResponse, List<BlogPost>, HomeViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ) {
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<HomeListResponse>) {
                Log.e("home reservations", "result count ${response.body.count}")
                Log.e("home reservations", "result response ${response.body.results}")

                val reservationsList: ArrayList<HomeReservation> = ArrayList()
                val count = response.body.count
                for(reservation in response.body.results){
                    reservationsList.add(
                        HomeReservation(
                            reservation.id,
                            reservation.check_in,
                            reservation.check_out,
                            reservation.guests,
                            reservation.status,
                            reservation.status_name,
                            reservation.created_at,
                            reservation.user.id,
                            reservation.house.id,
                            reservation.house.name,
                            "${Constants.BASE_URL_IMAGE}${reservation.house.photos?.first()?.image}",
                            reservation.owner.id,
                            reservation.message
                        )
                    )
                }
                Log.e("home repo reservations", "$reservationsList")
                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = HomeViewState(
                                HomeViewState.HomeReservationField(
                                    reservationList = reservationsList,
                                    isQueryExhausted = booleanQuery(count),
                                    isQueryInProgress = false,
                                    count = count
                                )
                            )
                        )
                    )
                }
            }

            private fun booleanQuery(blogPostListSize: Int):Boolean{
                if(page * Constants.PAGINATION_PAGE_SIZE >= blogPostListSize){
                    return true
                }
                return false
            }

            override fun createCall(): LiveData<GenericApiResponse<HomeListResponse>> {
                Log.d("home get reservations", "homeList page $page")
                return openApiMainService.getReservations(
                    "Token ${authToken.token!!}",
                    page = page
                )
            }

            override fun loadFromCache(): LiveData<HomeViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {

            }

            override fun setJob(job: Job) {
                addJob("homeReservations", job)
            }

        }.asLiveData()
    }

    fun cancelReservation(
        authToken: AuthToken,
        reservation_id: Int,
        messageOne:String
    ): LiveData<DataState<HomeViewState>>{
        return object:
            NetworkBoundResource<VerifyUpdateResponse, List<BlogPost>, HomeViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ){
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<VerifyUpdateResponse>) {
                Log.d("Cancel Reservation", response.body.message)

                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = HomeViewState(
                                cancelReservationField = HomeViewState.CancelReservationField(
                                    isCancelled = response.body.response,
                                    message = response.body.message
                                )
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<VerifyUpdateResponse>> {
                Log.d("Cancel Reservation", "reservation id: $reservation_id")
                return openApiMainService.cancelReservation(
                    authorization = "Token ${authToken.token!!}",
                    reservation_id = reservation_id,
                    body = CreateCancelReservationBody(messageOne)
                )
            }

            override fun loadFromCache(): LiveData<HomeViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
            }

            override fun setJob(job: Job) {
                addJob("cancelReservation", job)
            }

        }.asLiveData()
    }


    fun payReservationId(
        authToken: AuthToken,
        reservation_id: Int
    ): LiveData<DataState<HomeViewState>>{
        return object:
            NetworkBoundResource<PayRequestResponse, List<BlogPost>, HomeViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ){
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<PayRequestResponse>) {
                Log.d("pay Reservation", response.body.id.toString())

                val payRequestResponse = PayReservationIdResponse(
                    id = response.body.id,
                    location = response.body.location,
                    payment_page_url = response.body.payment_page_url
                )
                sessionManager.setPayBox(payRequestResponse)

//                withContext(Dispatchers.Main) {
//                    onCompleteJob(
//                        DataState.data(
//                            data = HomeViewState(
//                                payReservationField = HomeViewState.PayReservationField(
//                                    isPayed = true,
//                                    reservationResponse = payRequestResponse
//                                )
//                            )
//                        )
//                    )
//                }
            }

            override fun createCall(): LiveData<GenericApiResponse<PayRequestResponse>> {
                Log.d("Pay Reservation ID", "Pay Reservation ID: $reservation_id")
                return openApiMainService.createPayment(
//                    authorization = "Token ${authToken.token!!}",
                    reservation_id = reservation_id
                )
            }

            override fun loadFromCache(): LiveData<HomeViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
            }

            override fun setJob(job: Job) {
                addJob("payReservation", job)
            }

        }.asLiveData()
    }
}












