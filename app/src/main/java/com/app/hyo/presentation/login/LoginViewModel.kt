package com.app.hyo.presentation.login // Changed from .register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Assuming AppEntryUseCases might still be relevant after login, or you might have specific LoginUseCases
import com.app.hyo.domain.usecases.app_entry.AppEntryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor( // Changed from RegisterViewModel
    private val appEntryUseCases: AppEntryUseCases // Or replace with LoginUseCases if applicable
) : ViewModel() {

    private val _loginEvents = Channel<LoginEventState>() // Changed from _registrationEvents and RegistrationEventState
    val loginEvents = _loginEvents.receiveAsFlow() // Changed from registrationEvents

    fun onEvent(event: LoginEvent) { // Changed from RegisterEvent
        when (event) {
            is LoginEvent.LoginUser -> { // Changed from RegisterEvent.RegisterUser
                loginUser(event.email, event.password) // Changed from registerUser
            }
        }
    }

    private fun loginUser(email: String, password: String) { // Changed from registerUser
        viewModelScope.launch {
            try {
                // TODO: Implement actual user login logic here.
                // For demonstration, simulating a successful login
                println("Attempting to login user with email: $email and password: $password")

                // Simulate a network call or a repository operation
                kotlinx.coroutines.delay(1000) // Simulate async operation

                // Simulate success
                _loginEvents.send(LoginEventState.Success) // Changed from _registrationEvents and RegistrationEventState

                // If successful login should also save app entry state
                // (e.g., to mark user as logged in).
                // This depends on your app's specific flow.
                // appEntryUseCases.saveAppEntry() // Or a similar method for login state

            } catch (e: Exception) {
                // Simulate failure
                _loginEvents.send(LoginEventState.Error(e.message ?: "Unknown login error")) // Changed
            }
        }
    }
}

// Sealed class for login events state
sealed class LoginEventState { // Changed from RegistrationEventState
    object Success : LoginEventState()
    data class Error(val message: String) : LoginEventState()
    object Loading : LoginEventState() // Optional: for showing loading indicator
}