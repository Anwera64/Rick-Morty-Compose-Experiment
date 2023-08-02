package com.example.rickmortyepisodedata.domain

import com.example.rickmortyepisodedata.domain.episodes.EpisodesUseCase
import com.example.rickmortyepisodedata.domain.episodes.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.domain.episodes.models.EpisodePageDomainModel
import com.example.rickmortyepisodedata.domain.repositories.EpisodesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodeUseCaseTest {

    private val repository: EpisodesRepository = mockk()
    private val useCase: EpisodesUseCase = EpisodesUseCase(repository)

    @Test
    fun `WHEN episode list requested THEN first page of episode list returned`() = runTest {
        val expectedResponse: EpisodeDomainModel = mockk()
        val expectedList = listOf(expectedResponse)
        val page = 1
        coEvery { repository.listEpisodes(any()) } returns flow { emit(expectedList) }

        val result = useCase.listEpisodes().first()

        coVerify(exactly = 1) { repository.listEpisodes(page) }
        assertEquals(expectedList, result)
    }

    @Test
    fun `GIVEN pages available WHEN page requested THEN next page returned`() = runTest {
        val expectedResponse: EpisodeDomainModel = mockk()
        val expectedList = listOf(expectedResponse)
        val expectedResult = EpisodePageDomainModel(expectedList, false)
        val page = 1

        coEvery { repository.listEpisodes(1) } returns flow { emit(expectedList) }
        coEvery { repository.listEpisodes(2) } returns flow { emit(expectedList) }

        val result = useCase.listEpisodes().first()

        coVerify(exactly = 1) { repository.listEpisodes(page) }
        assertEquals(expectedList, result)

        val nextPage: EpisodePageDomainModel = useCase.requestNextPage().first()

        coVerify(exactly = 1) { repository.listEpisodes(page + 1) }
        assertEquals(expectedResult, nextPage)
    }

    @Test
    fun `GIVEN pages unavailable WHEN page requested THEN empty page returned`() = runTest {
        val expectedResponse: EpisodeDomainModel = mockk()
        val expectedList = listOf(expectedResponse)
        val expectedResult = EpisodePageDomainModel(emptyList(), true)
        val page = 1

        coEvery { repository.listEpisodes(1) } returns flow { emit(expectedList) }
        coEvery { repository.listEpisodes(2) } returns flow { emit(emptyList()) }

        val result = useCase.listEpisodes().first()

        coVerify(exactly = 1) { repository.listEpisodes(page) }
        assertEquals(expectedList, result)

        val nextPage: EpisodePageDomainModel = useCase.requestNextPage().first()

        coVerify(exactly = 1) { repository.listEpisodes(page + 1) }
        assertEquals(expectedResult, nextPage)
    }
}