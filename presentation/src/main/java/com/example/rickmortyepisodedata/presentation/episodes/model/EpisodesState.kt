package com.example.rickmortyepisodedata.presentation.episodes.model

sealed class EpisodesState {

    object LOADING : EpisodesState()

    class Success(val data: EpisodesData) : EpisodesState()

    class Failed(val throwable: Throwable): EpisodesState()
}