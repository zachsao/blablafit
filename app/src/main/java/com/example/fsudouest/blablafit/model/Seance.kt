package com.example.fsudouest.blablafit.model

import java.io.Serializable
import java.util.Date

data class Seance(
        var titre: String = "",
        var lieu: String = "",
        var description: String = "",
        var date: Date = Date(),
        var nb_participants: String = "",
        var participants: List<String> = listOf(),
        var duree: String = "",
        var id: String = "",
        var auteurPhotoUrl: String = "",
        var auteur: String = ""
) : Serializable