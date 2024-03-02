package com.example.newsapp.di

import com.example.newsapp.network.NewsRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    /**
     * Creating NewsRequest object here is not mandatory,
     * but creating for modularity and best practices
     */
    @Provides
    @Singleton
    fun provideRequestClient(): NewsRequest {
        return NewsRequest()
    }
}