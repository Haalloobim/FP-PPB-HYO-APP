package com.app.hyo.presentation.register

sealed class RegisterEvent {
    data class RegisterUser(val email: String, val password: String) : RegisterEvent()
    // You can add other events here, e.g., for login navigation, validation errors
}