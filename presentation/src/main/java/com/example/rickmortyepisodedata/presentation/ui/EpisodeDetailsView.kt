package com.example.rickmortyepisodedata.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.rickmortyepisodedata.presentation.R
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData

@Composable
fun EpisodeDetailsView(episode: EpisodeData, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = episode.name ?: stringResource(R.string.missing_episode_name),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = episode.episodeNumber ?: stringResource(R.string.missing_episode_number),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = episode.airDate ?: stringResource(R.string.missing_airing_date),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}