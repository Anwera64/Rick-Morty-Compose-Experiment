package com.example.rickmortyepisodedata.presentation.episodes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rickmortyepisodedata.R
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodesState
import com.example.rickmortyepisodedata.presentation.theme.RickMortyEpisodeDataTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodesScreen(uiState: State<EpisodesState>, onEpisodeClicked: (id: String?) -> Unit) {
    RickMortyEpisodeDataTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Episodes") },
                )
            },
            modifier = Modifier
                .fillMaxSize(),
        ) { paddingValues ->
            val modifier = Modifier.padding(paddingValues)
            when (val state = uiState.value) {
                is EpisodesState.EpisodesLoaded -> EpisodesList(
                    loadedState = state,
                    onEpisodeClicked = onEpisodeClicked,
                    modifier = modifier
                )

                is EpisodesState.Failed -> ErrorView(modifier)
                EpisodesState.LOADING -> LoadingView(modifier = modifier)
            }
        }
    }
}

@Composable
private fun ErrorView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.exception_message),
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpisodesList(
    loadedState: EpisodesState.EpisodesLoaded,
    onEpisodeClicked: (id: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()
    LazyColumn(modifier = modifier.fillMaxSize(), state = scrollState) {
        items(loadedState.episodes) { episode ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = {
                    onEpisodeClicked(episode.id)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    EpisodeDetails(episode)
                }
            }
        }
    }
}

@Composable
private fun EpisodeDetails(episode: EpisodeData) {
    Column {
        Text(
            text = episode.name ?: stringResource(R.string.missing_episode_name),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = episode.episodeNumber ?: stringResource(R.string.missing_episode_number),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = episode.airDate ?: stringResource(R.string.missing_airing_date),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun LoadingView(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    RickMortyEpisodeDataTheme {
        val uiState = rememberUpdatedState(newValue = EpisodesState.LOADING)
        EpisodesScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodePreview() {
    RickMortyEpisodeDataTheme {
        val uiState = rememberUpdatedState(
            newValue = EpisodesState.EpisodesLoaded(
                listOf(
                    EpisodeData("1", "Episode 1", "S01E01", "01/06/2023"),
                    EpisodeData("2", "Episode 2", "S01E02", "02/06/2023")
                )
            )
        )
        EpisodesScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState = rememberUpdatedState(newValue = EpisodesState.LOADING)
        EpisodesScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodePreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState = rememberUpdatedState(
            newValue = EpisodesState.EpisodesLoaded(
                listOf(
                    EpisodeData("1", "Episode 1", "S01E01", "01/06/2023"),
                    EpisodeData("2", "Episode 2", "S01E02", "02/06/2023")
                )
            )
        )
        EpisodesScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    RickMortyEpisodeDataTheme() {
        val uiState = rememberUpdatedState(newValue = EpisodesState.Failed(Throwable()))
        EpisodesScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState = rememberUpdatedState(newValue = EpisodesState.Failed(Throwable()))
        EpisodesScreen(uiState, {})
    }
}