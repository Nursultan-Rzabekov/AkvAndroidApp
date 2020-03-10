package com.akv.akvandroidapp.di.support


import com.akv.akvandroidapp.api.main.OpenApiMainService
import com.akv.akvandroidapp.persistence.BlogPostDao
import com.akv.akvandroidapp.repository.main.ProfileRepository
import com.akv.akvandroidapp.session.SessionManager
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

















