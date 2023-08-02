package com.example.rickmortyepisodedata.repositories

import com.example.rickmortyepisodedata.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.models.EpisodeDomainModel
import kotlinx.coroutines.flow.Flow

interface EpisodesRepository {

    suspend fun listEpisodes(page: Int): Flow<List<EpisodeDomainModel>>

    suspend fun getEpisodeDetails(id: String): Flow<EpisodeDetailsDomainModel>
}