package com.example.rickmortyepisodedata.domain.episodes

import com.example.rickmortyepisodedata.domain.episodes.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.domain.episodes.models.EpisodePageDomainModel
import com.example.rickmortyepisodedata.domain.repositories.EpisodesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EpisodesUseCase @Inject constructor(private val episodeRepository: EpisodesRepository) {

    private var requestedPage = 1

    suspend fun listEpisodes(): Flow<List<EpisodeDomainModel>> {
        return episodeRepository.listEpisodes(requestedPage)
    }

    suspend fun requestNextPage(): Flow<EpisodePageDomainModel> {
        requestedPage++
        return episodeRepository.listEpisodes(requestedPage).map { newEpisodes ->
            EpisodePageDomainModel(
                episodes = newEpisodes,
                reachedPageEnd = newEpisodes.isEmpty()
            )
        }
    }
}