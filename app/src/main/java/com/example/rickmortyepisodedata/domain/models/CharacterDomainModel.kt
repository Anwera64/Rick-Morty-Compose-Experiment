package com.example.rickmortyepisodedata.domain.models

import java.util.Date

class CharacterDomainModel(
    val name: String?,
    val created: Date?,
    val id: String,
    val gender: String?,
    val image: String?,
    val species: String?,
)