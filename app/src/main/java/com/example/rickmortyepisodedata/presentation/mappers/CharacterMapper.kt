package com.example.rickmortyepisodedata.presentation.mappers

import com.example.rickmortyepisodedata.models.CharacterDomainModel
import com.example.rickmortyepisodedata.presentation.details.model.CharacterData
import com.example.rickmortyepisodedata.utils.DateFormatter

object CharacterMapper {

    fun mapCharacterModelToData(character: com.example.rickmortyepisodedata.models.CharacterDomainModel): CharacterData {
        return CharacterData(
            character.name,
            character.id,
            character.gender,
            character.image,
            character.species
        )
    }
}