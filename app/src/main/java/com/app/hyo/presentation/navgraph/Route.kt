package com.app.hyo.presentation.navgraph

import androidx.navigation.NamedNavArgument

sealed class Route(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object OnBoardingScreen : Route(route = "onBoardingScreen")
    object RegistrationScreen : Route(route = "registrationScreen")
    object LoginScreen : Route(route = "loginScreen")
    object HomeScreen : Route(route = "homeScreen") // This might be your Dashboard
    object DashboardScreen : Route(route = "dashboardScreen")
    object DictionaryScreen : Route(route = "dictionaryScreen") // <-- ADD THIS LINE
    object ChatbotScreen : Route(route = "chatbotScreen")
    object SignLanguageCameraScreen : Route(route = "signLanguageCameraScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object HyoNavigation : Route(route = "hyoNavigation") }
