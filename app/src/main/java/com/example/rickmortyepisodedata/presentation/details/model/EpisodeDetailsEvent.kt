package com.example.rickmortyepisodedata.presentation.details.model

sealed class EpisodeDetailsEvent {

    class SearchToggled(val episodeDetailData: EpisodeDetailData?) : EpisodeDetailsEvent()

    class SearchRequested(
        val episodeDetailData: EpisodeDetailData,
        val query: String
    ) : EpisodeDetailsEvent()
}
