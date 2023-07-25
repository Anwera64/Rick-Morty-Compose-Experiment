package com.example.rickmortyepisodedata.domain.episodes

import com.example.rickmortyepisodedata.domain.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.domain.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.domain.repositories.EpisodesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodesUseCase @Inject constructor(private val episodeRepository: EpisodesRepository) {

    suspend fun listEpisodes(page: Int) : Flow<List<EpisodeDomainModel>> {
        return episodeRepository.listEpisodes(page)
    }

    suspend fun getEpisodeDetails(id: String): Flow<EpisodeDetailsDomainModel> {
        return episodeRepository.getEpisodeDetails(id)
    }
}