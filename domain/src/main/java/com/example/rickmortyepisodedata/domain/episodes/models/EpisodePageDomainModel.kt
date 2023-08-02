package com.example.rickmortyepisodedata.domain.episodes.models

data class EpisodePageDomainModel(
    val episodes: List<EpisodeDomainModel>,
    val reachedPageEnd: Boolean
)
