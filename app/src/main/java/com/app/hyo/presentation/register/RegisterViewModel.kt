package com.app.hyo.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hyo.domain.model.User
import com.app.hyo.domain.usecases.app_entry.AppEntryUseCases
import com.app.hyo.util.PasswordHasher
import com.app.hyo.domain.manger.UserRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registrationEvents = Channel<RegistrationEventState>()
    val registrationEvents = _registrationEvents.receiveAsFlow()

    fun onEvent(event: RegisterEvent, name: String, telepon: String) { // Modified to take name and telepon
        when (event) {
            is RegisterEvent.RegisterUser -> {
                // Basic validation (you should add more robust validation)
                if (event.email.isBlank() || event.password.isBlank() || name.isBlank() || telepon.isBlank()) {
                    viewModelScope.launch {
                        _registrationEvents.send(RegistrationEventState.Error("All fields are required."))
                    }
                    return
                }
                // Consider adding password confirmation logic here if RegisterScreen has it
                registerUser(name, telepon, event.email, event.password)
            }
        }
    }


    private fun registerUser(name: String, telepon: String, email: String, password: String) {
        viewModelScope.launch {
            _registrationEvents.send(RegistrationEventState.Loading) // Send loading state
            try {
                val hashedPassword = PasswordHasher.hashPassword(password)
                val newUser = User(name = name, telepon = telepon, email = email, hashedPassword = hashedPassword)

                when (val result = userRepository.registerUser(newUser)) {
                    is com.app.hyo.domain.manger.Result.Success -> {
                        _registrationEvents.send(RegistrationEventState.Success)
                        // Optionally, save app entry or navigate, etc.
                        // appEntryUseCases.saveAppEntry() // If registration means onboarding is complete
                    }
                    is com.app.hyo.domain.manger.Result.Error -> {
                        _registrationEvents.send(RegistrationEventState.Error(result.exception.message ?: "Registration failed"))
                    }
                }
            } catch (e: Exception) {
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