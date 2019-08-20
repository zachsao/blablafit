package com.example.fsudouest.blablafit.model

data class Conversation(
        val id: String = "",
        val user: User = User(),
        val chatLog: List<Chat> = emptyList()
)

data class Chat(
        val senderId: String = "",
        val message: String = "",
        val receiverId: String = "",
        val timestamp: Long = -1
)