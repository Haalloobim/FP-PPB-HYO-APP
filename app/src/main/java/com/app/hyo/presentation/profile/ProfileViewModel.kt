package com.app.hyo.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String = "ADT",
    val email: String = "adt.adt@mail.co"
)

class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile


    fun logout() {
        // TODO: Implement logout logic (e.g. clear session, navigate to login)
    }

    fun editProfile(name: String, email: String) {
        _userProfile.value = UserProfile(name, email)
    }
}
