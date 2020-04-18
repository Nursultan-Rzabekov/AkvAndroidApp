package com.akv.akvandroidapprelease.di.auth

import android.content.SharedPreferences
import com.akv.akvandroidapprelease.api.auth.OpenApiAuthService
import com.akv.akvandroidapprelease.persistence.AccountPropertiesDao
import com.akv.akvandroidapprelease.persistence.AuthTokenDao
import com.akv.akvandroidapprelease.repository.auth.AuthRepository
import com.akv.akvandroidapprelease.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule{

    @AuthScope
    @Provides
    fun provideOpenApiAuthService(retrofitBuilder: Retrofit.Builder): OpenApiAuthService {
        return retrofitBuilder
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService,
        preferences: SharedPreferences,
        editor: SharedPreferences.Editor
        ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager,
            preferences,
            editor
        )
    }

}