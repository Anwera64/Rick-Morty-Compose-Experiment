package com.example.rickmortyepisodedata.presentation.episodes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortyepisodedata.domain.episodes.EpisodesUseCase
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodeData
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodesData
import com.example.rickmortyepisodedata.presentation.episodes.model.EpisodesState
import com.example.rickmortyepisodedata.presentation.mappers.EpisodesMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(
    private val useCase: EpisodesUseCase
) : ViewModel() {

    private val _episodeStateflow = MutableStateFlow<EpisodesState>(EpisodesState.LOADING)
    val episodeStateFlow: StateFlow<EpisodesState> = _episodeStateflow

    init {
        requestEpisodes()
    }

    private fun requestEpisodes() = viewModelScope.launch {
        _episodeStateflow.emit(EpisodesState.LOADING)
        useCase.listEpisodes()
            .catch { throwable ->
                val newState = EpisodesState.Failed(throwable)
                _episodeStateflow.emit(newState)
            }
            .collect { domainModels ->
                val episodes = domainModels.map(EpisodesMapper::mapEpisodeModelToData)
                val data = EpisodesData(episodes)
                val newState = EpisodesState.Success(data)
                _episodeStateflow.emit(newState)
            }
    }

    fun requestNextPage(data: EpisodesData) = viewModelScope.launch {
        _episodeStateflow.emit(EpisodesState.Success(data.copy(isLoadingNextPage = true)))
        useCase.requestNextPage()
            .catch { throwable ->
                val newState = EpisodesState.Failed(throwable)
                _episodeStateflow.emit(newState)
            }
            .collect { domainModels ->
                val newEpisodes = domainModels.map(EpisodesMapper::mapEpisodeModelToData)
                val newData = EpisodesData(
                    episodes = data.episodes + newEpisodes,
                    reachedEnd = newEpisodes.isEmpty()
                )
                val newState = EpisodesState.Success(newData)
                _episodeStateflow.emit(newState)
            }
    }
}