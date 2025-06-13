package com.app.hyo.presentation.camerax

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PredictViewModel: ViewModel(){
    private val _uiState = MutableStateFlow("Tunggu sebentar, sedang memproses...")
    val uiState: StateFlow<String> get() = _uiState

    fun updateState(newState: String) {
        _uiState.value = newState
    }
}