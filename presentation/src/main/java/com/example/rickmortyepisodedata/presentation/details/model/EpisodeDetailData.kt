package com.example.rickmortyepisodedata.presentation.details.model

import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData

data class EpisodeDetailData(
    val details: EpisodeData,
    val characters: List<CharacterData>,
    val searchToggled: Boolean = false,
    val query: String? = null
)