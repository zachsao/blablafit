package com.example.fsudouest.blablafit.Model

class User {

    var id: Int = 0
    var nom: String? = null
    var email: String? = null
    var note: Double = 0.toDouble()
    var photoUrl: String? = null


    constructor(nomComplet: String, email: String, photoUrl: String) {
        this.nom = nomComplet
        this.email = email
        this.note = 0.0
        this.photoUrl = photoUrl
    }

    constructor() {}

    override fun toString(): String {
        return this.nom!!
    }
}
