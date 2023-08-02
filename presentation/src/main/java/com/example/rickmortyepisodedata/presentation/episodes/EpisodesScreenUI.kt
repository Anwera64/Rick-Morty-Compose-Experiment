package com.example.rickmortyepisodedata.presentation.episodes

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
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
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodesData
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
            uiState = uiState,
            onEpisodeClicked = { id -> navHostController.navigate("$EPISODES_KEY/$id") },
            requestNextPage = viewModel::requestNextPage
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EpisodesScreenContent(
    uiState: State<EpisodesState>,
    onEpisodeClicked: (id: String?) -> Unit,
    requestNextPage: (data: EpisodesData) -> Unit,
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
            is EpisodesState.Success -> EpisodesList(
                data = state.data,
                onEpisodeClicked = onEpisodeClicked,
                requestNextPage = requestNextPage,
                modifier = modifier
            )

            is EpisodesState.Failed -> ErrorView(modifier)
            EpisodesState.LOADING -> LoadingView(modifier = modifier.fillMaxSize())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun EpisodesList(
    data: EpisodesData,
    onEpisodeClicked: (id: String?) -> Unit,
    requestNextPage: (data: EpisodesData) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState: LazyListState = rememberLazyListState()
    val endReached: Boolean by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index == layoutInfo.totalItemsCount - 1
        }
    }
    if (!data.reachedEnd && endReached && !data.isLoadingNextPage) {
        requestNextPage(data)
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(data.episodes, key = EpisodeData::id) { episode ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
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
        item(key = "loader") {
            AnimatedVisibility(
                visible = data.isLoadingNextPage,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    LoadingView(modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 8.dp))
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
            newValue = EpisodesState.Success(
                mockData()
            )
        )
        EpisodesScreenContent(uiState, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodePreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState = rememberUpdatedState(
            newValue = EpisodesState.Success(
                mockData()
            )
        )
        EpisodesScreenContent(uiState, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodePreviewLoadingPage() {
    RickMortyEpisodeDataTheme {
        val uiState = rememberUpdatedState(
            newValue = EpisodesState.Success(
                mockData(true)
            )
        )
        EpisodesScreenContent(uiState, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodePreviewLoadingPageDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState = rememberUpdatedState(
            newValue = EpisodesState.Success(
                mockData(true)
            )
        )
        EpisodesScreenContent(uiState, {}, {})
    }
}

private fun mockData(isLoadingNextPage: Boolean = false): EpisodesData {
    return EpisodesData(
        episodes = listOf(
            EpisodeData("1", "Episode 1", "S01E01", "01/06/2023"),
            EpisodeData("2", "Episode 2", "S01E02", "02/06/2023")
        ),
        isLoadingNextPage = isLoadingNextPage
    )
}