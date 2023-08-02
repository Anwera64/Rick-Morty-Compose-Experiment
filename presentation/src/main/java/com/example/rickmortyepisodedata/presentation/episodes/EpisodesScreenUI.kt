package com.example.rickmortyepisodedata.presentation.episodes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickmortyepisodedata.presentation.R
import com.example.rickmortyepisodedata.presentation.ui.EpisodeDetailsView
import com.example.rickmortyepisodedata.presentation.ui.ErrorView
import com.example.rickmortyepisodedata.presentation.ui.LoadingView
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodesState
import com.example.rickmortyepisodedata.presentation.theme.RickMortyEpisodeDataTheme
import com.example.rickmortyepisodedata.presentation.utils.LocalNavController

const val EPISODES_KEY = "episodes"

@Composable
fun EpisodesScreen(viewModel: EpisodesViewModel = hiltViewModel()) {
    val uiState = viewModel.episodeStateFlow.collectAsState()
    RickMortyEpisodeDataTheme {
        val navHostController = LocalNavController.current
        EpisodesScreenContent(
            uiState,
            onEpisodeClicked = { id -> navHostController.navigate("$EPISODES_KEY/$id") },
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EpisodesScreenContent(
    uiState: State<EpisodesState>,
    onEpisodeClicked: (id: String?) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.episodes)) },
            )
        },
        modifier = Modifier.fillMaxSize(),
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
                    EpisodeDetailsView(episode)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodePreview() {
    RickMortyEpisodeDataTheme {
        val uiState = rememberUpdatedState(
            newValue = EpisodesState.EpisodesLoaded(
                mockData()
            )
        )
        EpisodesScreenContent(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodePreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState = rememberUpdatedState(
            newValue = EpisodesState.EpisodesLoaded(
                mockData()
            )
        )
        EpisodesScreenContent(uiState, {})
    }
}

private fun mockData() = listOf(
    EpisodeData("1", "Episode 1", "S01E01", "01/06/2023"),
    EpisodeData("2", "Episode 2", "S01E02", "02/06/2023")
)