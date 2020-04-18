package com.akv.akvandroidapprelease.di.payment


import com.akv.akvandroidapprelease.api.main.OpenApiMainService
import com.akv.akvandroidapprelease.persistence.BlogPostDao
import com.akv.akvandroidapprelease.repository.main.ProfileRepository
import com.akv.akvandroidapprelease.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class PaymentModule{

    @PaymentScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @PaymentScope
    @Provides
    fun provideProfileRepositoryMyHouse(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): ProfileRepository {
        return ProfileRepository(openApiMainService,blogPostDao,sessionManager)
    }

}