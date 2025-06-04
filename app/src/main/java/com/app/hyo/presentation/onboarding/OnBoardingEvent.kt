package com.app.hyo.presentation.onboarding

sealed class OnBoardingEvent {

    object SaveAppEntry: OnBoardingEvent()
    object NavigateToRegistration: OnBoardingEvent()
}