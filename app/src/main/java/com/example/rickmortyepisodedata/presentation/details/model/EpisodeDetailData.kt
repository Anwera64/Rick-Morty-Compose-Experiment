package com.example.rickmortyepisodedata.presentation.details.model

import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData

class EpisodeDetailData(
    val details: EpisodeData,
    val characters: List<CharacterData>
)