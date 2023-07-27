package com.example.rickmortyepisodedata.domain.episodes

import com.example.rickmortyepisodedata.domain.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.domain.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.domain.repositories.EpisodesRepository
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EpisodesUseCase @Inject constructor(private val episodeRepository: EpisodesRepository) {

    suspend fun listEpisodes(page: Int): Flow<List<EpisodeDomainModel>> {
        return episodeRepository.listEpisodes(page)
    }

    suspend fun getEpisodeDetails(id: String): Flow<EpisodeDetailsDomainModel> {
        return episodeRepository.getEpisodeDetails(id)
    }

    suspend fun filterCharacters(
        episodeDetailData: EpisodeDetailData,
        query: String
    ): Flow<EpisodeDetailData> {
        return flow {
            val newList = episodeDetailData.characters.filter { characterData ->
                val normalizedQuery = query.lowercase()
                val normalizedName = characterData.name?.lowercase()
                normalizedName?.contains(normalizedQuery) ?: false
            }
            val updatedData = episodeDetailData.copy(characters = newList)
            emit(updatedData)
        }
    }
}