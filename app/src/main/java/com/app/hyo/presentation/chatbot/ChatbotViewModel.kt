package com.app.hyo.presentation.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hyo.R // For placeholder image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val id: Long,
    val text: String,
    val isFromUser: Boolean,
    val imageResId: Int? = null, // For images in messages
    val timestamp: Long = System.currentTimeMillis()
)

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    // Add your use cases here if needed, e.g., for a real chatbot API
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private var nextMessageId = 0L

    // To control visibility of suggestion chips
    private val _shouldShowSuggestions = MutableStateFlow(false)
    val shouldShowSuggestions: StateFlow<Boolean> = _shouldShowSuggestions.asStateFlow()


    init {
        // Load initial sample messages
        loadSampleMessages()
    }

    private fun loadSampleMessages() {
        _messages.value = listOf(
            ChatMessage(nextMessageId++, "Apakah ini sehat untuk bayi 6 bulan?", true, R.drawable.ic_network_error),
            ChatMessage(nextMessageId++, "Tentunya tidak ya, sistem pencernaan bayi masih sangat sensitif dan berkembang", false)
        )
        _shouldShowSuggestions.value = true // Show suggestions after bot's first message
    }

    fun onEvent(event: ChatbotEvent) {
        when (event) {
            is ChatbotEvent.SendMessage -> {
                val userMessage = ChatMessage(nextMessageId++, event.text, true)
                _messages.update { it + userMessage }
                _shouldShowSuggestions.value = false // Hide suggestions when user sends a message

                // Simulate bot reply
                viewModelScope.launch {
                    delay(1000) // Simulate network delay
                    val botReplyText = when {
                        event.text.equals("Let's do it", ignoreCase = true) -> "Okay! What would you like to discuss first?"
                        event.text.equals("Great!", ignoreCase = true) -> "I'm glad you think so! How can I help further?"
                        event.text.toLowerCase().contains("sehat") -> "Makanan sehat untuk bayi meliputi ASI eksklusif hingga 6 bulan, diikuti MPASI bergizi seimbang seperti bubur saring, buah-buahan lunak, dan sayuran kukus. Hindari gula, garam, dan madu untuk bayi di bawah 1 tahun."
                        else -> "Maaf, saya belum mengerti. Bisa coba tanyakan hal lain seputar nutrisi anak?"
                    }
                    val botMessage = ChatMessage(nextMessageId++, botReplyText, false)
                    _messages.update { it + botMessage }
                    _shouldShowSuggestions.value = true // Show suggestions again after bot replies
                }
            }
        }
    }
}
