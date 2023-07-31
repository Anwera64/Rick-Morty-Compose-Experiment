package com.example.rickmortyepisodedata.presentation.details.model

sealed class EpisodeDetailsState {

    object LOADING : EpisodeDetailsState()

    class Success(val episodeDetailData: EpisodeDetailData) : EpisodeDetailsState()

    class Failed(val throwable: Throwable) : EpisodeDetailsState()
}