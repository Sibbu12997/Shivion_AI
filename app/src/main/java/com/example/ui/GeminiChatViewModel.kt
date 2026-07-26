package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entities.MessageEntity
import com.example.data.remote.GeminiApiClient
import com.example.data.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class GeminiUiState {
    object Idle : GeminiUiState()
    object Sending : GeminiUiState()
    data class Streaming(val partialResponse: String) : GeminiUiState()
    data class Success(val fullResponse: String) : GeminiUiState()
    data class Error(val errorMessage: String) : GeminiUiState()
}

class GeminiChatViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = ChatRepository(
        chatDao = db.chatDao(),
        messageDao = db.messageDao(),
        aiAssistantDao = db.aiAssistantDao(),
        userProfileDao = db.userProfileDao()
    )

    private val _uiState = MutableStateFlow<GeminiUiState>(GeminiUiState.Idle)
    val uiState: StateFlow<GeminiUiState> = _uiState.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    private val _currentStreamingText = MutableStateFlow("")
    val currentStreamingText: StateFlow<String> = _currentStreamingText.asStateFlow()

    /**
     * Handles sending user message and reactively receiving Gemini AI response.
     * Updates local DB reactively so the UI updates in real-time.
     */
    fun sendGeminiMessage(
        chatId: String,
        prompt: String,
        systemInstruction: String? = null,
        messageType: String = "TEXT",
        fileName: String? = null,
        fileSize: String? = null
    ) {
        if (prompt.isBlank() && messageType == "TEXT") return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = GeminiUiState.Sending
            _isAiTyping.value = true
            _currentStreamingText.value = ""

            // 1. Instantly save user message into local Room DB (Reactive Flow updates UI immediately)
            repository.sendMessage(
                chatId = chatId,
                content = prompt,
                messageType = messageType,
                fileName = fileName,
                fileSize = fileSize
            )

            // 2. Fetch Gemini response
            try {
                val responseText = GeminiApiClient.askGemini(
                    prompt = prompt,
                    systemInstruction = systemInstruction ?: "You are a helpful AI assistant in WorkAI Chat."
                )

                _currentStreamingText.value = responseText
                _uiState.value = GeminiUiState.Success(responseText)
            } catch (e: Exception) {
                val errorMsg = e.localizedMessage ?: "Failed to receive response from Gemini AI."
                _uiState.value = GeminiUiState.Error(errorMsg)
            } finally {
                _isAiTyping.value = false
            }
        }
    }

    fun resetState() {
        _uiState.value = GeminiUiState.Idle
        _currentStreamingText.value = ""
        _isAiTyping.value = false
    }
}
