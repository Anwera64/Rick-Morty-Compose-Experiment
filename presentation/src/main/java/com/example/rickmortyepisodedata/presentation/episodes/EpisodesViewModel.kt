package com.example.rickmortyepisodedata.presentation.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortyepisodedata.domain.episodes.EpisodesUseCase
import com.example.rickmortyepisodedata.domain.episodes.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.domain.episodes.models.EpisodePageDomainModel
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
            .catch { throwable -> emitFailure(throwable) }
            .collect { domainModels -> handleInitialRequest(domainModels) }
    }

    private suspend fun handleInitialRequest(domainModels: List<EpisodeDomainModel>) {
        val episodes = domainModels.map(EpisodesMapper::mapEpisodeModelToData)
        val data = EpisodesData(episodes)
        val newState = EpisodesState.Success(data)
        _episodeStateflow.emit(newState)
    }

    fun requestNextPage(data: EpisodesData) = viewModelScope.launch {
        val loadingNextPageState = EpisodesState.Success(
            data = data.copy(isLoadingNextPage = true)
        )
        _episodeStateflow.emit(loadingNextPageState)
        useCase.requestNextPage()
            .catch { throwable -> emitFailure(throwable) }
            .collect { pageDomainModel -> handleNewPage(pageDomainModel, data) }
    }

    private suspend fun handleNewPage(pageDomainModel: EpisodePageDomainModel, data: EpisodesData) {
        val newEpisodes = pageDomainModel.episodes.map(EpisodesMapper::mapEpisodeModelToData)
        val newData = EpisodesData(
            episodes = data.episodes + newEpisodes,
            reachedEnd = pageDomainModel.reachedPageEnd
        )
        val newState = EpisodesState.Success(newData)
        _episodeStateflow.emit(newState)
    }

    private suspend fun emitFailure(throwable: Throwable) {
        val newState = EpisodesState.Failed(throwable)
        _episodeStateflow.emit(newState)
    }
}