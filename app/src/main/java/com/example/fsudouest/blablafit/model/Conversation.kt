package com.example.fsudouest.blablafit.model

data class Conversation(
        val userIds: MutableList<String> = mutableListOf()
)

data class Chat(
        val senderId: String = "",
        val message: String = "",
        val recipientId: String = "",
        val senderName: String = "",
        val timestamp: Long = -1
)