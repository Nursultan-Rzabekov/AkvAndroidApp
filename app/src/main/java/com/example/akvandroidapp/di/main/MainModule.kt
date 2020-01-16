package com.example.akvandroidapp.di.main


import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.persistence.AppDatabase
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.main.*
import com.example.akvandroidapp.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @MainScope
    @Provides
    fun provideHomeRepository(
        openApiMainService: OpenApiMainService,
        sessionManager: SessionManager
    ): HomeRepository {
        return HomeRepository(openApiMainService, sessionManager)
    }

    @MainScope
    @Provides
    fun provideSearchRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): SearchRepository {
        return SearchRepository(openApiMainService, blogPostDao, sessionManager)
    }

    @MainScope
    @Provides
    fun provideFavoriteRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): FavoriteRepository {
        return FavoriteRepository(openApiMainService, blogPostDao, sessionManager)
    }

    @MainScope
    @Provides
    fun provideMessagesRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ):MessagesRepository{
        return MessagesRepository(openApiMainService,blogPostDao,sessionManager)
    }

    @MainScope
    @Provides
    fun provideProfileRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ):ProfileRepository{
        return ProfileRepository(openApiMainService,blogPostDao,sessionManager)
    }
}

















