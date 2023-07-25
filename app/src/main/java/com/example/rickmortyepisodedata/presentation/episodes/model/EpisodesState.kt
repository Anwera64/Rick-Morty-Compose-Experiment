package com.example.rickmortyepisodedata.presentation.episodes.model

import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailData
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailsState

sealed class EpisodesState {

    object LOADING : EpisodesState()

    class EpisodesLoaded(val episodes: List<EpisodeData>) : EpisodesState()

    class Failed(val throwable: Throwable): EpisodesState()
}