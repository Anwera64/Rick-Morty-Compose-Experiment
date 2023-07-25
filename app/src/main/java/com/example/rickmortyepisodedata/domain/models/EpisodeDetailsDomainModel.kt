package com.example.rickmortyepisodedata.domain.models

class EpisodeDetailsDomainModel(
    val episodeDomainModel: EpisodeDomainModel,
    val characterList: List<CharacterDomainModel>,
)