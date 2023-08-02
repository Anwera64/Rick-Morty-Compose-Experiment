package com.example.rickmortyepisodedata.data

import com.apollographql.apollo3.ApolloClient
import com.example.rickmortyepisodedata.data.episodes.EpisodesRepositoryImpl
import com.example.rickmortyepisodedata.domain.repositories.EpisodesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DataModule {

    @Provides
    fun provideEpisodeRepository(apolloClient: ApolloClient): EpisodesRepository {
        return EpisodesRepositoryImpl(apolloClient)
    }
}