package com.example.rickmortyepisodedata.presentation.mappers

import com.example.rickmortyepisodedata.domain.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.domain.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailData
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData
import com.example.rickmortyepisodedata.domain.utils.DateFormatter

object EpisodesMapper {

    fun mapEpisodeModelToData(episodeDomainModel: EpisodeDomainModel): EpisodeData {
        return EpisodeData(
            id = episodeDomainModel.id,
            name = episodeDomainModel.name,
            episodeNumber = episodeDomainModel.episode,
            airDate = DateFormatter.format(episodeDomainModel.airDate, DateFormatter.FORMAT_1)
        )
    }

    fun mapEpisodeDetailModelToData(domainModel: EpisodeDetailsDomainModel): EpisodeDetailData {
        return EpisodeDetailData(
            details = mapEpisodeModelToData(domainModel.episodeDomainModel),
            characters = domainModel.characterList.map(CharacterMapper::mapCharacterModelToData)
        )
    }
}