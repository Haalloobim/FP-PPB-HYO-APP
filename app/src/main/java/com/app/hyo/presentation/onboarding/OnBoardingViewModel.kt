package com.app.hyo.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hyo.domain.usecases.app_entry.AppEntryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val appEntryUseCases: AppEntryUseCases
) : ViewModel() {

    private val _navigationEvents = Channel<OnBoardingNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onEvent(event: OnBoardingEvent){
        when(event){
            is OnBoardingEvent.SaveAppEntry ->{
                saveUserEntry()
            }
            is OnBoardingEvent.NavigateToRegistration -> {
                viewModelScope.launch {
                    _navigationEvents.send(OnBoardingNavigationEvent.NavigateToRegistration)
                }
            }
        }
    }

    private fun saveUserEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }
    }
}

sealed class OnBoardingNavigationEvent {
    object NavigateToRegistration : OnBoardingNavigationEvent()
}