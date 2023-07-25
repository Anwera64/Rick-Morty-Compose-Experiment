package com.example.rickmortyepisodedata.domain.episodes

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodesUseCase @Inject constructor(private val episodesRepository: EpisodesRepository) {

    suspend fun listEpisodes(page: Int) : Flow<List<EpisodeDomainModel>> {
        return episodesRepository.listEpisodes(page)
    }
}