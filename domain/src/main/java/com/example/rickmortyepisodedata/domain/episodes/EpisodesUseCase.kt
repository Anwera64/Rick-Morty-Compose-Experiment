package com.example.rickmortyepisodedata.domain.episodes

import com.example.rickmortyepisodedata.domain.episodes.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.domain.repositories.EpisodesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodesUseCase @Inject constructor(private val episodeRepository: EpisodesRepository) {

    private var requestedPage = 1

    suspend fun listEpisodes(): Flow<List<EpisodeDomainModel>> {
        return episodeRepository.listEpisodes(requestedPage)
    }

    suspend fun requestNextPage(): Flow<List<EpisodeDomainModel>> {
        requestedPage++
        return episodeRepository.listEpisodes(requestedPage)
    }
}