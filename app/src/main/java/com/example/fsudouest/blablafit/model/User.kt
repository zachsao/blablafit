package com.example.fsudouest.blablafit.model

class User(var nomComplet: String="", var email: String="",  var photoUrl: String?=null, var note: Double=0.0) {

    override fun toString(): String {
        return this.nomComplet
    }
}
