package com.example.fsudouest.blablafit.model

data class Conversation(
        val user: User,
        val chatLog: List<Chat>
){
    constructor(): this(User(), emptyList())
}

data class Chat(
        val senderId: String,
        val message: String
){
    constructor(): this("","")
}