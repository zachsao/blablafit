package com.example.fsudouest.blablafit.features.messages

import com.example.fsudouest.blablafit.features.messages.ui.UserViewItem

sealed class MessagesState {
    abstract val data: MessagesData

    data class Idle(override val data: MessagesData): MessagesState()
    data class Loading(override val data: MessagesData): MessagesState()
    data class ConversationsLoaded(override val data: MessagesData): MessagesState()
    data class ConversationsEmpty(override  val data: MessagesData): MessagesState()
}

data class MessagesData(
        val conversations: List<UserViewItem> = emptyList()
)