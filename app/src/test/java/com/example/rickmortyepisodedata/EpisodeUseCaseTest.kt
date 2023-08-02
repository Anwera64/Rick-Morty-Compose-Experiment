package com.example.rickmortyepisodedata

import com.example.rickmortyepisodedata.episodes.EpisodesUseCase
import com.example.rickmortyepisodedata.models.CharacterDomainModel
import com.example.rickmortyepisodedata.models.EpisodeDetailsDomainModel
import com.example.rickmortyepisodedata.models.EpisodeDomainModel
import com.example.rickmortyepisodedata.repositories.EpisodesRepository
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodeUseCaseTest {

    private val repository: com.example.rickmortyepisodedata.repositories.EpisodesRepository = mockk()
    private val useCase: com.example.rickmortyepisodedata.episodes.EpisodesUseCase =
        com.example.rickmortyepisodedata.episodes.EpisodesUseCase(repository)

    @Test
    fun `GIVEN a page number WHEN episode list requested THEN episode list returned`() = runTest {
        val expectedResponse: com.example.rickmortyepisodedata.models.EpisodeDomainModel = mockk()
        val expectedList = listOf(expectedResponse)
        val page = 0
        coEvery { repository.listEpisodes(any()) } returns flow { emit(expectedList) }

        val result = useCase.listEpisodes(page).first()

        coVerify(exactly = 1) { repository.listEpisodes(page) }
        assertEquals(expectedList, result)
    }

    @Test
    fun `GIVEN an ID WHEN episode details requested THEN episode details returned`() = runTest {
        val id = "ID"
        val expectedResponse: com.example.rickmortyepisodedata.models.EpisodeDetailsDomainModel = mockk()

        every { expectedResponse.characterList } returns emptyList()
        coEvery { repository.getEpisodeDetails(any()) } returns flow { emit(expectedResponse) }

        val result = useCase.getEpisodeDetails(id).first()

        coVerify(exactly = 1) { repository.getEpisodeDetails(id) }
        assertEquals(result, expectedResponse)
    }

    @Test
    fun `GIVEN episode details requested WHEN filtered THEN only 1 result returned`() = runTest {
        val id = "ID"
        val rickMock = mockk<com.example.rickmortyepisodedata.models.CharacterDomainModel> {
            every { name } returns "Rick"
        }
        val mortyMock = mockk<com.example.rickmortyepisodedata.models.CharacterDomainModel> {
            every { name } returns "Morty"
        }
        val charList = listOf(rickMock, mortyMock)
        val expectedResponse: com.example.rickmortyepisodedata.models.EpisodeDetailsDomainModel = mockk {
            every { characterList } returns charList
        }

        coEvery { repository.getEpisodeDetails(any()) } returns flow { emit(expectedResponse) }

        val result = useCase.getEpisodeDetails(id).first()

        coVerify(exactly = 1) { repository.getEpisodeDetails(id) }
        assertEquals(expectedResponse, result)

        val finalResult = useCase.filterCharacters("rick").first()

        assertEquals(listOf(rickMock), finalResult)
    }

    @Test
    fun `GIVEN episode details requested WHEN filtered THEN no result returned`() = runTest {
        val id = "ID"
        val rickMock = mockk<com.example.rickmortyepisodedata.models.CharacterDomainModel> {
            every { name } returns "Rick"
        }
        val mortyMock = mockk<com.example.rickmortyepisodedata.models.CharacterDomainModel> {
            every { name } returns "Morty"
        }
        val charList = listOf(rickMock, mortyMock)
        val expectedResponse: com.example.rickmortyepisodedata.models.EpisodeDetailsDomainModel = mockk {
            every { characterList } returns charList
        }

        coEvery { repository.getEpisodeDetails(any()) } returns flow { emit(expectedResponse) }

        val result = useCase.getEpisodeDetails(id).first()

        coVerify(exactly = 1) { repository.getEpisodeDetails(id) }
        assertEquals(expectedResponse, result)

        val finalResult = useCase.filterCharacters("Bazinga").first()

        assertEquals(emptyList<com.example.rickmortyepisodedata.models.CharacterDomainModel>(), finalResult)
    }
}