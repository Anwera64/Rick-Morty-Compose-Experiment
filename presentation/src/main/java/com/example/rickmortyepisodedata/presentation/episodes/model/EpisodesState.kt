package com.example.rickmortyepisodedata.presentation.episodes.model

sealed class EpisodesState {

    object LOADING : EpisodesState()

    class EpisodesLoaded(val episodes: List<EpisodeData>) : EpisodesState()

    class Failed(val throwable: Throwable): EpisodesState()
}