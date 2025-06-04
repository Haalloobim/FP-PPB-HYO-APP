package com.app.hyo.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hyo.domain.usecases.app_entry.AppEntryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    private val _registrationEvents = Channel<RegistrationEventState>()
    val registrationEvents = _registrationEvents.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.RegisterUser -> {
                registerUser(event.email, event.password)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                // TODO: Implement actual user registration logic here.
                // For demonstration, simulating a successful registration
                println("Attempting to register user with email: $email and password: $password")

                // Simulate a network call or a repository operation
                // For now, just a delay to mimic async operation
                kotlinx.coroutines.delay(1000)

                // Simulate success
                _registrationEvents.send(RegistrationEventState.Success)

                // If successful registration should also save app entry state
                // (e.g., to bypass onboarding on subsequent app launches).
                // This depends on your app's specific flow.
                // appEntryUseCases.saveAppEntry()

            } catch (e: Exception) {
                // Simulate failure
                _registrationEvents.send(RegistrationEventState.Error(e.message ?: "Unknown registration error"))
            }
        }
    }
}

sealed class RegistrationEventState {
    object Success : RegistrationEventState()
    data class Error(val message: String) : RegistrationEventState()
    object Loading : RegistrationEventState() // Optional: for showing loading indicator
}