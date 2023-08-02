package com.example.rickmortyepisodedata.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.example.rickmortyepisodedata.presentation.R
import com.example.rickmortyepisodedata.presentation.ui.EpisodeDetailsView
import com.example.rickmortyepisodedata.presentation.ui.ErrorView
import com.example.rickmortyepisodedata.presentation.ui.LoadingView
import com.example.rickmortyepisodedata.presentation.details.model.CharacterData
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailData
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailsEvent
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailsState
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData
import com.example.rickmortyepisodedata.presentation.theme.RickMortyEpisodeDataTheme
import com.example.rickmortyepisodedata.presentation.utils.LocalNavController

const val ID_ARGUMENT_KEY = "id"

@Composable
fun EpisodeDetailsScreen(
    id: String,
    viewModel: EpisodeDetailsViewModel = hiltViewModel()
) {
    val uiState = viewModel.episodeStateFlow.collectAsState()
    viewModel.requestDetails(id)
    val navHostController = LocalNavController.current
    RickMortyEpisodeDataTheme {
        EpisodeDetailsContent(
            uiState = uiState,
            dispatchEvent = viewModel::receiveEvent,
            backAction = { navHostController.popBackStack() })
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EpisodeDetailsContent(
    uiState: State<EpisodeDetailsState>,
    dispatchEvent: (EpisodeDetailsEvent) -> Unit,
    backAction: () -> Unit,
) {
    Scaffold(
        topBar = {
            Toolbar(
                backAction = backAction,
                searchAction = {
                    val state = uiState.value as? EpisodeDetailsState.Success
                    val data: EpisodeDetailData? = state?.episodeDetailData
                    val event = EpisodeDetailsEvent.SearchToggled(data)
                    dispatchEvent(event)
                }
            )
        },
        modifier = Modifier
            .fillMaxSize(),
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when (val state = uiState.value) {
            is EpisodeDetailsState.Success -> EpisodeDetails(
                details = state.episodeDetailData,
                dispatchEvent = dispatchEvent,
                modifier = modifier
            )

            is EpisodeDetailsState.Failed -> ErrorView(modifier)
            EpisodeDetailsState.LOADING -> LoadingView(modifier = modifier.fillMaxSize())
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
private fun SearchBar(
    searchEnabled: Boolean,
    searchQuery: String,
    searchAction: (String) -> Unit
) {
    AnimatedVisibility(
        visible = searchEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query -> searchAction(query) },
            placeholder = { Text(stringResource(R.string.search_by_name)) },
            leadingIcon = { SearchButton() },
            trailingIcon = {
                AnimatedVisibility(
                    visible = searchQuery.isNotBlank(),
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    IconButton(onClick = { searchAction("") }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun SearchButton(searchAction: () -> Unit = {}) {
    IconButton(onClick = { searchAction() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Toolbar(backAction: () -> Unit, searchAction: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.episode_details)) },
        navigationIcon = {
            IconButton(onClick = { backAction() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = { SearchButton(searchAction) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpisodeDetails(
    details: EpisodeDetailData,
    modifier: Modifier = Modifier,
    dispatchEvent: (EpisodeDetailsEvent) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 180.dp),
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
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
                SearchBar(details.searchToggled, details.query ?: "") { query ->
                    val event = EpisodeDetailsEvent.SearchRequested(details, query)
                    dispatchEvent(event)
                }
            }
        }
        items(details.characters, key = CharacterData::id) { character ->
            CharacterCard(
                character = character,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
private fun CharacterCard(character: CharacterData, modifier: Modifier = Modifier) {
    OutlinedCard(modifier) {
        SubcomposeAsyncImage(
            model = character.image,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(180.dp)
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

private fun mockData(searchEnabled: Boolean = false, query: String? = null) = EpisodeDetailData(
    EpisodeData("1", "Episode 1", "S01E01", "01/06/2023"),
    listOf(
        CharacterData("Morty", "1", "Male", null, "Human"),
        CharacterData("Rick", "2", "Male", null, "Human")
    ),
    searchToggled = searchEnabled,
    query = query
)

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsPreview() {
    RickMortyEpisodeDataTheme {
        val uiState = rememberUpdatedState(
            newValue = EpisodeDetailsState.Success(mockData())
        )
        EpisodeDetailsContent(uiState, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsPreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState =
            rememberUpdatedState(newValue = EpisodeDetailsState.Success(mockData()))
        EpisodeDetailsContent(uiState, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsSearchPreview() {
    RickMortyEpisodeDataTheme {
        val uiState = rememberUpdatedState(
            newValue = EpisodeDetailsState.Success(mockData(searchEnabled = true))
        )
        EpisodeDetailsContent(uiState, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsSearchPreviewDark() {
    RickMortyEpisodeDataTheme(darkTheme = true) {
        val uiState =
            rememberUpdatedState(newValue = EpisodeDetailsState.Success(mockData(searchEnabled = true)))
        EpisodeDetailsContent(uiState, {}, {})
    }
}