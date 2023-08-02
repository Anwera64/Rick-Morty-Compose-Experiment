package com.example.rickmortyepisodedata.domain.episodes.models

import java.util.Date

class EpisodeDomainModel(
    val id: String,
    val name: String?,
    val episode: String?,
    val airDate: Date?
)