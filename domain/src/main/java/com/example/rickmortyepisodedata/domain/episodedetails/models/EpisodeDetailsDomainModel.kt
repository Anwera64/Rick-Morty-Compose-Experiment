package com.example.rickmortyepisodedata.domain.episodedetails.models

import com.example.rickmortyepisodedata.domain.episodes.models.EpisodeDomainModel

class EpisodeDetailsDomainModel(
    val episodeDomainModel: EpisodeDomainModel,
    val characterList: List<CharacterDomainModel>,
)