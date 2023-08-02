package com.example.rickmortyepisodedata.presentation.episodes.model

data class EpisodesData(
    val episodes: List<EpisodeData>,
    val isLoadingNextPage: Boolean = false,
    val reachedEnd: Boolean = false,
)