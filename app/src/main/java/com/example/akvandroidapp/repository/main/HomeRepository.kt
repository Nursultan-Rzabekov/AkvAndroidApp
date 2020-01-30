package com.example.akvandroidapp.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.HomeListResponse
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.entity.HomeReservation
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.home.state.HomeViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
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
                    //val image: String = reservation.house.photos?.get(0)?.image ?: "////////////////"
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
                            reservation.house_id,
                            "qwerty",
                            "https://akv-technopark.herokuapp.com",
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
                                    isQueryExhausted = false,
                                    isQueryInProgress = true,
                                    count = count
                                )
                            )
                        )
                    )
                }
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
}












