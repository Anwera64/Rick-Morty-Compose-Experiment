package com.example.rickmortyepisodedata.presentation.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.asLiveData
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailsState
import com.example.rickmortyepisodedata.presentation.theme.RickMortyEpisodeDataTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class EpisodeDetailsActivity : ComponentActivity() {

    private val viewModel: EpisodeDetailsViewModel by viewModels()

    companion object {
        const val ID_KEY = "ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getStringExtra(ID_KEY)
        viewModel.requestDetails(id)

        viewModel.episodeStateFlow
            .filter { state -> state is EpisodeDetailsState.BACK }
            .asLiveData()
            .observe(this) { finish() }

        setContent {
            val uiState = viewModel.episodeStateFlow.collectAsState()
            RickMortyEpisodeDataTheme {
                EpisodeDetailsScreen(uiState, viewModel::receiveEvent)
            }
        }
    }
}