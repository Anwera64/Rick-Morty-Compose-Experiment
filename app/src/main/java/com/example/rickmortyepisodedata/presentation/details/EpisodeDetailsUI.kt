package com.example.rickmortyepisodedata.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickmortyepisodedata.R
import com.example.rickmortyepisodedata.presentation.base.ui.EpisodeDetailsView
import com.example.rickmortyepisodedata.presentation.base.ui.ErrorView
import com.example.rickmortyepisodedata.presentation.base.ui.LoadingView
import com.example.rickmortyepisodedata.presentation.details.model.CharacterData
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailData
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailsState
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData
import com.example.rickmortyepisodedata.presentation.theme.RickMortyEpisodeDataTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailsScreen(uiState: State<EpisodeDetailsState>, backAction: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.episode_details)) },
                navigationIcon = {
                    IconButton(onClick = { backAction() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (val state = uiState.value) {
            is EpisodeDetailsState.EpisodeDetailsLoaded -> EpisodeDetails(state, modifier)
            is EpisodeDetailsState.Failed -> ErrorView(modifier)
            EpisodeDetailsState.LOADING -> LoadingView(modifier = modifier)
        }
    }
}

@Composable
fun EpisodeDetails(
    state: EpisodeDetailsState.EpisodeDetailsLoaded,
    modifier: Modifier = Modifier
) {
    val details = state.episodeDetailData
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(180.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(Modifier.fillMaxWidth()) {
                EpisodeDetailsView(
                    episode = details.details,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Characters:",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        items(details.characters) { character ->
            CharacterCard(character)
        }
    }
}

@Composable
private fun CharacterCard(character: CharacterData) {
    OutlinedCard {
        AsyncImage(
            model = character.image,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .clip(
                    MaterialTheme.shapes.medium.copy(
                        bottomEnd = CornerSize(0),
                        bottomStart = CornerSize(0)
                    )
                )
        )
        Text(
            text = character.name ?: stringResource(R.string.missing_character_name),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = "Gender: ${character.gender}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Text(
            text = "Species: ${character.species}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        )
    }
}

private fun mockData() = EpisodeDetailData(
    EpisodeData("1", "Episode 1", "S01E01", "01/06/2023"),
    listOf(
        CharacterData("Morty", "1", "Male", null, "Human"),
        CharacterData("Rick", "2", "Male", null, "Human")
    )
)

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsPreview() {
    RickMortyEpisodeDataTheme {
        val uiState = rememberUpdatedState(
            newValue = EpisodeDetailsState.EpisodeDetailsLoaded(mockData())
        )
        EpisodeDetailsScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsPreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState =
            rememberUpdatedState(newValue = EpisodeDetailsState.EpisodeDetailsLoaded(mockData()))
        EpisodeDetailsScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    RickMortyEpisodeDataTheme {
        val uiState = rememberUpdatedState(newValue = EpisodeDetailsState.LOADING)
        EpisodeDetailsScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState = rememberUpdatedState(newValue = EpisodeDetailsState.LOADING)
        EpisodeDetailsScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
    RickMortyEpisodeDataTheme() {
        val uiState = rememberUpdatedState(newValue = EpisodeDetailsState.Failed(Throwable()))
        EpisodeDetailsScreen(uiState, {})
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState = rememberUpdatedState(newValue = EpisodeDetailsState.Failed(Throwable()))
        EpisodeDetailsScreen(uiState, {})
    }
}