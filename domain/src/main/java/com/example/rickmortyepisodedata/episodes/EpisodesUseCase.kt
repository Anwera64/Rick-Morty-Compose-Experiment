package com.example.rickmortyepisodedata.episodes

import com.example.rickmortyepisodedata.models.CharacterDomainModel
import com.example.rickmortyepisodedata.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.repositories.EpisodesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class EpisodesUseCase @Inject constructor(private val episodeRepository: EpisodesRepository) {

    private var originalCharacterList: List<CharacterDomainModel> = emptyList()

    suspend fun listEpisodes(page: Int): Flow<List<EpisodeDomainModel>> {
        return episodeRepository.listEpisodes(page)
    }

    suspend fun getEpisodeDetails(id: String): Flow<EpisodeDetailsDomainModel> {
        return episodeRepository
            .getEpisodeDetails(id)
            .onEach { detailsDomainModel ->
                originalCharacterList = detailsDomainModel.characterList
            }
    }

    suspend fun filterCharacters(query: String): Flow<List<CharacterDomainModel>> {
        return flow {
            val newList = originalCharacterList.filter { characterData ->
                val normalizedQuery = query.lowercase()
                val normalizedName = characterData.name?.lowercase()
                normalizedName?.contains(normalizedQuery) ?: false
            }
            emit(newList)
        }
    }
}