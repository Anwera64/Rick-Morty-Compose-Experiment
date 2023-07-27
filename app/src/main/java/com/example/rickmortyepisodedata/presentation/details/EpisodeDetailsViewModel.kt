package com.example.rickmortyepisodedata.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortyepisodedata.domain.episodes.EpisodesUseCase
import com.example.rickmortyepisodedata.domain.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailsEvent
import com.example.rickmortyepisodedata.presentation.details.model.EpisodeDetailsState
import com.example.rickmortyepisodedata.presentation.mappers.EpisodesMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val useCase: EpisodesUseCase
) : ViewModel() {

    private val _episodeStateflow =
        MutableStateFlow<EpisodeDetailsState>(EpisodeDetailsState.LOADING)
    val episodeStateFlow: StateFlow<EpisodeDetailsState> = _episodeStateflow

    private var searchToggled: Boolean = false
    private var id: String? = null

    fun requestDetails(id: String?) = viewModelScope.launch {
        if (id == null) {
            _episodeStateflow.emit(EpisodeDetailsState.Failed(Throwable("Null ID")))
            return@launch
        }
        this@EpisodeDetailsViewModel.id = id
        _episodeStateflow.emit(EpisodeDetailsState.LOADING)
        useCase.getEpisodeDetails(id)
            .catch { throwable ->
                val newState = EpisodeDetailsState.Failed(throwable)
                _episodeStateflow.emit(newState)
            }
            .collect { domainModel: EpisodeDetailsDomainModel ->
                val episodeDetailData = EpisodesMapper.mapEpisodeDetailModelToData(domainModel)
                val newState = EpisodeDetailsState.Success(episodeDetailData)
                _episodeStateflow.emit(newState)
            }
    }

    fun receiveEvent(event: EpisodeDetailsEvent) = viewModelScope.launch {
        when (event) {
            EpisodeDetailsEvent.BACK -> _episodeStateflow.emit(EpisodeDetailsState.BACK)
            is EpisodeDetailsEvent.SearchRequested -> TODO()
            is EpisodeDetailsEvent.SearchToggled -> handleSearchtoggledEvent(event)
        }
    }

    private suspend fun handleSearchtoggledEvent(event: EpisodeDetailsEvent.SearchToggled) {
        searchToggled = !searchToggled
        if (searchToggled) {
            dispatchSearchToggled(event)
        } else {
            requestDetails(id)
        }
    }

    private suspend fun dispatchSearchToggled(event: EpisodeDetailsEvent.SearchToggled) {
        if (event.episodeDetailData == null) {
            _episodeStateflow.emit(EpisodeDetailsState.Failed(Throwable("Missing data")))
            return
        }
        val state = EpisodeDetailsState.Success(event.episodeDetailData.copy(searchToggled = true))
        _episodeStateflow.emit(state)
    }
}