package com.example.rickmortyepisodedata.presentation.mappers

import com.example.rickmortyepisodedata.domain.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.domain.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailData
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData

object EpisodesMapper {

    fun mapEpisodeModelToData(episodeDomainModel: EpisodeDomainModel): EpisodeData {
        return EpisodeData(
            id = episodeDomainModel.id,
            name = episodeDomainModel.name,
            episodeNumber = episodeDomainModel.episode,
            airDate = episodeDomainModel.airDate.toString() // TODO format it nicely
        )
    }

    fun mapEpisodeDetailModelToData(domainModel: EpisodeDetailsDomainModel): EpisodeDetailData {
        return EpisodeDetailData(
            details = mapEpisodeModelToData(domainModel.episodeDomainModel),
            characters = domainModel.characterList.map(CharacterMapper::mapCharacterModelToData)
        )
    }
}