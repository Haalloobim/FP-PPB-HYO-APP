package com.app.hyo.presentation.chatbot

sealed class ChatbotEvent {
    data class SendMessage(val text: String) : ChatbotEvent()
    // Add other events like LoadHistory, ImageSelected, etc.
}
