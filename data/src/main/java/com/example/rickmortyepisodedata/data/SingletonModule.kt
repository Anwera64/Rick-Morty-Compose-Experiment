package com.example.rickmortyepisodedata.data

import com.apollographql.apollo3.ApolloClient
import com.example.rickmortyepisodedata.data.clients.ApolloClientProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Provides
    fun provideApolloClient(): ApolloClient {
        return ApolloClientProvider.getInstance()
    }
}