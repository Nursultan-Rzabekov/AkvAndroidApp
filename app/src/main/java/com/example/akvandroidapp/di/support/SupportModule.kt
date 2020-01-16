package com.example.akvandroidapp.di.support


import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.persistence.AppDatabase
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.main.*
import com.example.akvandroidapp.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SupportModule {

    @SupportScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }


    @SupportScope
    @Provides
    fun provideProfileRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ):ProfileRepository{
        return ProfileRepository(openApiMainService,blogPostDao,sessionManager)
    }
}

















