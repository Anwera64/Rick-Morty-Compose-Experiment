package com.example.rickmortyepisodedata.data.clients

import com.apollographql.apollo3.ApolloClient
import com.example.rickmortyepisodedata.data.BuildConfig

object ApolloClientProvider {

    fun getInstance(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.SERVER_URL)
            .build()
    }
}