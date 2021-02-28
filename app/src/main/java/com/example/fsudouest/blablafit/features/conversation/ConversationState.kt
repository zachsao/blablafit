package com.example.fsudouest.blablafit.features.conversation

import com.xwray.groupie.kotlinandroidextensions.Item

sealed class ConversationState {
    abstract val data: ConversationData

    data class Idle(override val data: ConversationData) : ConversationState()
    data class ConversationLoaded(override val data: ConversationData) : ConversationState()
    data class ConversationCreated(override val data: ConversationData) : ConversationState()
    data class ChatsUpdated(override val data: ConversationData) : ConversationState()
}

data class ConversationData(
        val chats: List<Item> = emptyList(),
        val conversationId: String? = null
)