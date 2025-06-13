package com.app.hyo.presentation.mainActivity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hyo.domain.manger.LocalUserManger
import com.app.hyo.domain.usecases.app_entry.AppEntryUseCases
import com.app.hyo.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases,
    private val localUserManger: LocalUserManger // Inject LocalUserManger to check session
): ViewModel() {

    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    private val _startDestination = mutableStateOf(Route.AppStartNavigation.route)
    val startDestination: State<String> = _startDestination

    init {
        // Combine flows to check for both user session and onboarding status
        combine(
            localUserManger.readUserEmail(),
            appEntryUseCases.readAppEntry()
        ) { userEmail, hasSeenOnboarding ->
            when {
                // If user's email exists, they are logged in. Go to main app.
                !userEmail.isNullOrBlank() -> Route.HyoNavigation.route

                // If they have seen onboarding but are not logged in, go to registration.
                hasSeenOnboarding -> Route.RegistrationScreen.route

                // Otherwise, it's a first-time user. Start with onboarding.
                else -> Route.AppStartNavigation.route
            }
        }.onEach { startRoute ->
            _startDestination.value = startRoute
            // Delay to prevent the splash screen from disappearing too quickly
            delay(300)
            _splashCondition.value = false
        }.launchIn(viewModelScope)
    }
}