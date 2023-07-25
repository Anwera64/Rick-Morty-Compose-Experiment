package com.example.rickmortyepisodedata.presentation.episodes.mapper

import com.example.rickmortyepisodedata.domain.episodes.EpisodeDomainModel
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData

object EpisodesMapper {

    fun mapEpisodeModelToData(episodeDomainModel: EpisodeDomainModel): EpisodeData {
        return EpisodeData(
            id = episodeDomainModel.id,
            name = episodeDomainModel.name,
            episode = episodeDomainModel.episode,
            airDate = episodeDomainModel.airDate.toString() // TODO format it nicely
        )
    }
}