package com.example.rickmortyepisodedata.data.episodes

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.example.rickmortyepisodedata.EpisodeDetailsQuery
import com.example.rickmortyepisodedata.EpisodeQuery
import com.example.rickmortyepisodedata.domain.episodedetails.models.CharacterDomainModel
import com.example.rickmortyepisodedata.domain.episodedetails.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.domain.episodes.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.domain.repositories.EpisodesRepository
import com.example.rickmortyepisodedata.domain.utils.DateFormatter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EpisodesRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : EpisodesRepository {

    override suspend fun listEpisodes(page: Int): Flow<List<EpisodeDomainModel>> {
        return apolloClient.query(EpisodeQuery(Optional.present(page))).toFlow()
            .flowOn(dispatcher)
            .map { response: ApolloResponse<EpisodeQuery.Data> ->
                val data: EpisodeQuery.Data? = response.data
                val episodes: EpisodeQuery.Episodes? = data?.episodes
                episodes?.results?.map { result: EpisodeQuery.Result? ->
                    result ?: error("Missing result data")
                    EpisodeDomainModel(
                        id = result.id ?: error("Missing ID"),
                        name = result.name,
                        episode = result.episode,
                        airDate = DateFormatter.parse(result.air_date, DateFormatter.FORMAT_2)
                    )
                } ?: emptyList()
            }
    }

    override suspend fun getEpisodeDetails(id: String): Flow<EpisodeDetailsDomainModel> {
        return apolloClient.query(EpisodeDetailsQuery(id)).toFlow()
            .flowOn(dispatcher)
            .map { response: ApolloResponse<EpisodeDetailsQuery.Data> ->
                val data: EpisodeDetailsQuery.Data? = response.data
                val episode: EpisodeDetailsQuery.Episode =
                    data?.episode ?: error("Missing episode data")
                EpisodeDetailsDomainModel(
                    EpisodeDomainModel(
                        id = episode.id ?: error("Missing ID"),
                        name = episode.name,
                        episode = episode.episode,
                        airDate = DateFormatter.parse(episode.air_date, DateFormatter.FORMAT_2)
                    ),
                    episode.characters.map { character ->
                        character ?: error("Null character data")
                        CharacterDomainModel(
                            character.name,
                            character.id ?: error("Missing ID"),
                            character.gender,
                            character.image,
                            character.species
                        )
                    }
                )
            }
    }
}