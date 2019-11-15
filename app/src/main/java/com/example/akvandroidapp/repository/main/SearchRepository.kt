package com.example.akvandroidapp.repository.main


import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.session.SessionManager
import javax.inject.Inject

class SearchRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("SearchRepository")
{

    private val TAG: String = "AppDebug"
}
















