package com.example.rickmortyepisodedata.domain.repositories

import com.example.rickmortyepisodedata.domain.episodedetails.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.domain.episodes.models.EpisodeDomainModel
import kotlinx.coroutines.flow.Flow

interface EpisodesRepository {

    suspend fun listEpisodes(page: Int): Flow<List<EpisodeDomainModel>>

    suspend fun getEpisodeDetails(id: String): Flow<EpisodeDetailsDomainModel>
}