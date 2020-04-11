package com.example.fsudouest.blablafit.model

import java.io.Serializable
import java.util.Date

data class Seance(
        var titre: String = "",
        var location: Location = Location(),
        var description: String = "",
        var date: Date = Date(),
        var maxParticipants: Int = 0,
        var duree: String = "",
        var idAuteur: String = "",
        var id: String = "",
        var participants: Map<String, RequestStatus> = emptyMap(),
        var nomAuteur: String = "",
        var photoAuteur: String? = null
) : Serializable

enum class RequestStatus { PENDING, GRANTED, DENIED }

data class Location(
        val name: String? = null,
        val address: String? = null,
        val city: String? = null,
        val state: String? = null,
        val country: String? = null,
        val zipCode: String? = null
)