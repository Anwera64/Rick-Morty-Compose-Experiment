package com.example.rickmortyepisodedata.di

import com.example.rickmortyepisodedata.domain.episodes.EpisodesRepository
import com.example.rickmortyepisodedata.domain.episodes.EpisodesUseCase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    fun providesEpisodesUseCase(episodesRepository: EpisodesRepository): EpisodesUseCase {
        return EpisodesUseCase(episodesRepository)
    }
}