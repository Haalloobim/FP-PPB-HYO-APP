package com.app.hyo.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hyo.domain.manger.LocalUserManger
import com.app.hyo.domain.manger.UserRepository
import com.app.hyo.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val localUserManger: LocalUserManger,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            localUserManger.readUserEmail().onEach { email ->
                if (email != null) {
                    userRepository.getUserByEmail(email).onEach { userProfile ->
                        _user.value = userProfile
                    }.launchIn(viewModelScope)
                } else {
                    // Handle case where user email is not found, perhaps navigate to login
                    _navigationEvent.send(NavigationEvent.NavigateToLogin)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            localUserManger.clearUserSession()
            _navigationEvent.send(NavigationEvent.NavigateToLogin)
        }
    }

    sealed class NavigationEvent {
        object NavigateToLogin : NavigationEvent()
    }
}