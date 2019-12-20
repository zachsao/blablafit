package com.example.fsudouest.blablafit.model

import java.io.Serializable
import java.util.Date

data class Seance(
        var titre: String = "",
        var lieu: String = "",
        var description: String = "",
        var date: Date = Date(),
        var maxParticipants: Int = 0,
        var duree: String = "",
        var idAuteur: String = "",
        var id: String = "",
        var participants: List<String> = emptyList(),
        var nomAuteur: String = "",
        var photoAuteur: String? = null
) : Serializable