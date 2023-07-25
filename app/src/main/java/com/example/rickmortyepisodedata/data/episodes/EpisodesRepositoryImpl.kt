package com.example.rickmortyepisodedata.data.episodes

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.example.rickmortyepisodedata.EpisodeQuery
import com.example.rickmortyepisodedata.domain.episodes.EpisodeDomainModel
import com.example.rickmortyepisodedata.domain.episodes.EpisodesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.Date
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
                    EpisodeDomainModel(
                        result?.id,
                        result?.name,
                        result?.episode,
                        Date() // TODO parse string to date
                    )
                } ?: emptyList()
            }
    }
}