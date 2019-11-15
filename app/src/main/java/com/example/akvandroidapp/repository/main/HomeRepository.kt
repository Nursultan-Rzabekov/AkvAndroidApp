package com.example.akvandroidapp.repository.main


import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.session.SessionManager
import javax.inject.Inject

class HomeRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val sessionManager: SessionManager
): JobManager("HomeRepository")
{

}












