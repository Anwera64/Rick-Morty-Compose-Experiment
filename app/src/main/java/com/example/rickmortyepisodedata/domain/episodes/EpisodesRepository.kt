package com.example.rickmortyepisodedata.domain.episodes

import kotlinx.coroutines.flow.Flow

interface EpisodesRepository {

    suspend fun listEpisodes(page: Int): Flow<List<EpisodeDomainModel>>
}