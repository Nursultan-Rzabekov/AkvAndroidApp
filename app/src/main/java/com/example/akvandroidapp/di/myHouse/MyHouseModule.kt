package com.example.akvandroidapp.di.myHouse


import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.main.ProfileRepository
import com.example.akvandroidapp.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MyHouseModule{

    @MyHouseScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @MyHouseScope
    @Provides
    fun provideProfileRepositoryMyHouse(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): ProfileRepository {
        return ProfileRepository(openApiMainService,blogPostDao,sessionManager)
    }

}