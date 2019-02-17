package com.example.fsudouest.blablafit.Model

import java.io.Serializable
import java.sql.Time
import java.util.Calendar
import java.util.Date

data class Seance(var titre: String = "", var lieu: String="", var description: String? ="", var date: Date= Date(), var nb_participants: String="", var createur: String="", var duree: String="", var id: String="") : Serializable
