package com.app.hyo.presentation.login // Changed from .register

sealed class LoginEvent { // Changed from RegisterEvent
    data class LoginUser(val email: String, val password: String) : LoginEvent() // Changed from RegisterUser and RegisterEvent
    // You can add other events here, e.g., for forgot password navigation, validation errors
}