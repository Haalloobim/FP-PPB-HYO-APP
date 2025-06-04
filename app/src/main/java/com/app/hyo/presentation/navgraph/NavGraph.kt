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
import com.app.hyo.presentation.dashboard.DashboardScreen
// import com.app.hyo.presentation.dashboard.DashboardViewModel // Uncomment if needed directly
import com.app.hyo.presentation.login.LoginScreen
import com.app.hyo.presentation.onboarding.OnBoardingScreen
import com.app.hyo.presentation.onboarding.OnBoardingViewModel
import com.app.hyo.presentation.onboarding.OnBoardingNavigationEvent
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
                    // Navigate to Dashboard/Home after login
                    navController.navigate(Route.HyoNavigation.route) { // Or directly to DashboardScreen
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
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
            composable(route = Route.DashboardScreen.route) {
                // val viewModel: DashboardViewModel = hiltViewModel() // If you need ViewModel here
                DashboardScreen(
                    onProfileClick = {
                        // TODO: Navigate to Profile Screen
                        // navController.navigate("profile_screen_route")
                    },
                    onNavigateToRoute = { route ->
                        // Handle bottom navigation clicks
                        // This is a simplified example. You might want a more robust
                        // navigation setup for bottom bar items, potentially with its own NavHost.
                        navController.navigate(route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
//                            popUpTo(navController.graph.findStartDestination()?.id ?: Route.DashboardScreen.route) {
//                                saveState = true
//                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
            composable(route = Route.HomeScreen.route) { // Keep if you have a separate HomeScreen
                // TODO: Add HomeScreen content or redirect to Dashboard
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.error)) {
                    Text("Old Home Screen - Should navigate to Dashboard")
                }
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

