package com.app.hyo.presentation.navgraph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.app.hyo.presentation.camerax.CameraScreen
import com.app.hyo.presentation.dashboard.AppRoutes
import com.app.hyo.presentation.dashboard.DashboardScreen
import com.app.hyo.presentation.dictionary.DictionaryScreen
// import com.app.hyo.presentation.dashboard.DashboardViewModel // Uncomment if needed directly
import com.app.hyo.presentation.login.LoginScreen
import com.app.hyo.presentation.onboarding.OnBoardingScreen
import com.app.hyo.presentation.onboarding.OnBoardingViewModel
import com.app.hyo.presentation.onboarding.OnBoardingNavigationEvent
import com.app.hyo.presentation.profile.ProfileScreen
import com.app.hyo.presentation.register.RegisterScreen


@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Route.RegistrationScreen.route) {
            RegisterScreen(
                onRegisterClick = {
                    // Navigate to Login or Dashboard/Home after registration
                    navController.navigate(Route.LoginScreen.route) {
                        popUpTo(Route.RegistrationScreen.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Route.LoginScreen.route)
                }
            )
        }

        composable(route = Route.LoginScreen.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(AppRoutes.HOME_SCREEN) {
                        popUpTo(Route.LoginScreen.route) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onRegisterNavigateClick = {
                    navController.navigate(Route.RegistrationScreen.route)
                }
            )
        }

        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                LaunchedEffect(key1 = true) {
                    viewModel.navigationEvents.collect { event ->
                        when (event) {
                            is OnBoardingNavigationEvent.NavigateToRegistration -> {
                                // Navigate to Registration after onboarding
                                navController.navigate(Route.RegistrationScreen.route) {
                                    popUpTo(Route.AppStartNavigation.route) { inclusive = true }
                                }
                            }
                        }
                    }
                }
                OnBoardingScreen(onEvent = viewModel::onEvent)
            }
        }

        // Main application navigation (after login/onboarding)
        navigation(
            route = Route.HyoNavigation.route,
            startDestination = Route.DashboardScreen.route // Default to Dashboard
        ) {
            composable(route = AppRoutes.HOME_SCREEN) {
                // val viewModel: DashboardViewModel = hiltViewModel() // If you need ViewModel here
                DashboardScreen(
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(AppRoutes.HOME_SCREEN) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onProfileClick = {
                        navController.navigate(AppRoutes.PROFILE_SCREEN) {
                            popUpTo(AppRoutes.HOME_SCREEN) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(route = AppRoutes.DICTIONARY_SCREEN) {
                DictionaryScreen(
                    onBackClick = {
                        navController.navigate(AppRoutes.HOME_SCREEN) {
                            popUpTo(AppRoutes.HOME_SCREEN) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(AppRoutes.HOME_SCREEN) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(route = "sign_language_camera_screen_route") {
                CameraScreen(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                )
            }
//            composable(route = Route.HomeScreen.route) { // Keep if you have a separate HomeScreen
//                // TODO: Add HomeScreen content or redirect to Dashboard
//                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.error)) {
//                    Text("Old Home Screen - Should navigate to Dashboard")
//                }
//            }
            composable(route = AppRoutes.PROFILE_SCREEN) {
                ProfileScreen(
                    onBackClick = {
                        navController.navigate(AppRoutes.HOME_SCREEN) {
                            popUpTo(AppRoutes.HOME_SCREEN) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(AppRoutes.HOME_SCREEN) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            // Add other destinations within HyoNavigation here
            // e.g., composable("profile_screen_route") { ProfileScreen(...) }
            // composable("search_screen_route") { SearchScreen(...) }
        }
    }
}

// Extension function to find the start destination of the graph
// This is useful for bottom navigation to pop up to the correct start destination.
fun NavHostController.findStartDestination(): androidx.navigation.NavDestination? {
    return this.graph.findNode(this.graph.startDestinationId)
}

