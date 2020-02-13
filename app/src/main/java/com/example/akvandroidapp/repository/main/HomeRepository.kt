package com.example.akvandroidapp.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.bodies.CreateCancelReservationBody
import com.example.akvandroidapp.api.main.bodies.CreateReservationBody
import com.example.akvandroidapp.api.main.responses.HomeListResponse
import com.example.akvandroidapp.api.main.responses.PayRequestResponse
import com.example.akvandroidapp.api.main.responses.VerifyUpdateResponse
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.entity.HomeReservation
import com.example.akvandroidapp.entity.PayReservationIdResponse
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.home.state.HomeViewState
import com.example.akvandroidapp.ui.main.messages.state.RequestViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.GenericApiResponse
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
                            reservation.created_at,
                            reservation.accepted_house,
                            reservation.user.id,
                            reservation.house.id,
                            reservation.house.name,
                            "http://akv-technopark.herokuapp.com${reservation.house.photos?.first()?.image}",
                            reservation.owner.id
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












