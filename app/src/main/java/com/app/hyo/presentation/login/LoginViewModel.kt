package com.app.hyo.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hyo.domain.manger.LocalUserManger
import com.app.hyo.domain.manger.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val localUserManger: LocalUserManger // Injected LocalUserManger
) : ViewModel() {

    private val _loginEvents = Channel<LoginEventState>()
    val loginEvents = _loginEvents.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoginUser -> {
                if (event.email.isBlank() || event.password.isBlank()) {
                    viewModelScope.launch {
                        _loginEvents.send(LoginEventState.Error("Email and password cannot be empty."))
                    }
                    return
                }
                loginUser(event.email, event.password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginEvents.send(LoginEventState.Loading)
            try {
                when (val result = userRepository.loginUser(email, password)) {
                    is com.app.hyo.domain.manger.Result.Success -> {
                        localUserManger.saveUserEmail(result.data.email) // Save user email to session
                        _loginEvents.send(LoginEventState.Success(result.data.name))
                    }
                    is com.app.hyo.domain.manger.Result.Error -> {
                        _loginEvents.send(LoginEventState.Error(result.exception.message ?: "Login failed"))
                    }
                }
            } catch (e: Exception) {
                _loginEvents.send(LoginEventState.Error(e.message ?: "Unknown login error"))
            }
        }
    }
}

sealed class LoginEventState {
    data class Success(val userName: String) : LoginEventState()
    data class Error(val message: String) : LoginEventState()
    object Loading : LoginEventState()
}