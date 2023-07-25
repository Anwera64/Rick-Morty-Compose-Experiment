package com.example.rickmortyepisodedata.di

import com.apollographql.apollo3.ApolloClient
import com.example.rickmortyepisodedata.data.episodes.EpisodesRepositoryImpl
import com.example.rickmortyepisodedata.domain.episodes.EpisodesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DataModule {

    @Provides
    fun provideEpisodesRepository(apolloClient: ApolloClient): EpisodesRepository {
        return EpisodesRepositoryImpl(apolloClient)
    }
}