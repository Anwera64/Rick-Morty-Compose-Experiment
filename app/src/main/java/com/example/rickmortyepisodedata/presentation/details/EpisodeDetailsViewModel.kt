package com.example.rickmortyepisodedata.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmortyepisodedata.domain.episodes.EpisodesUseCase
import com.example.rickmortyepisodedata.domain.models.EpisodeDetailsDomainModel
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

    fun requestDetails(id: String?) = viewModelScope.launch {
        if (id == null) {
            _episodeStateflow.emit(EpisodeDetailsState.Failed(Throwable("Null ID")))
            return@launch
        }

        useCase.getEpisodeDetails(id)
            .catch { throwable ->
                val newState = EpisodeDetailsState.Failed(throwable)
                _episodeStateflow.emit(newState)
            }
            .collect { domainModel: EpisodeDetailsDomainModel ->
                val episodeDetailData = EpisodesMapper.mapEpisodeDetailModelToData(domainModel)
                val newState = EpisodeDetailsState.EpisodeDetailsLoaded(episodeDetailData)
                _episodeStateflow.emit(newState)
            }
    }
}