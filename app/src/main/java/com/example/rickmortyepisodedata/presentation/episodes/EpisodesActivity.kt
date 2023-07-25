package com.example.rickmortyepisodedata.presentation.episodes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.example.rickmortyepisodedata.presentation.details.EpisodeDetailsActivity
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailsState
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodesState
import com.example.rickmortyepisodedata.presentation.episodes.ui.EpisodesScreen
import com.example.rickmortyepisodedata.presentation.theme.RickMortyEpisodeDataTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: EpisodesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun requestNextPage() {
        viewModel.requestEpisodes(1) // TODO make page selection dynamic from the use case
    }

    private fun onEpisodeClicked(id: String?) {
        val intent = Intent(this, EpisodeDetailsActivity::class.java)
            .putExtra(EpisodeDetailsActivity.ID_KEY, id)
        startActivity(intent)
    }

    private fun setupUI() {
        setContent {
            val uiState: State<EpisodesState> = viewModel.episodeStateFlow.collectAsState()
            RickMortyEpisodeDataTheme() {
                EpisodesScreen(uiState, ::onEpisodeClicked)
            }
        }
    }
}