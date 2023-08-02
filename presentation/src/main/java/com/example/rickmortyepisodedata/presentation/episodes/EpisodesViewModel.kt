package com.example.rickmortyepisodedata.presentation.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortyepisodedata.domain.episodes.EpisodesUseCase
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

    private fun requestEpisodes(page: Int = 0) = viewModelScope.launch {
        _episodeStateflow.emit(EpisodesState.LOADING)
        useCase.listEpisodes(page)
            .catch { throwable ->
                val newState = EpisodesState.Failed(throwable)
                _episodeStateflow.emit(newState)
            }
            .collect { domainModels ->
                val episodeData = domainModels.map(EpisodesMapper::mapEpisodeModelToData)
                val newState = EpisodesState.EpisodesLoaded(episodeData)
                _episodeStateflow.emit(newState)
            }
    }
}