package com.example.rickmortyepisodedata.presentation.mappers

import com.example.rickmortyepisodedata.domain.models.CharacterDomainModel
import com.example.rickmortyepisodedata.presentation.details.model.CharacterData

object CharacterMapper {

    fun mapCharacterModelToData(character: CharacterDomainModel): CharacterData {
        return CharacterData(
            character.name,
            character.id,
            character.gender,
            character.image,
            character.species
        )
    }
}