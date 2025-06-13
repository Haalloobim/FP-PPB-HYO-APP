package com.app.hyo.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.app.hyo.presentation.camerax.SignLanguageCameraScreen
import com.app.hyo.presentation.dashboard.AppRoutes
import com.app.hyo.presentation.dashboard.DashboardScreen
import com.app.hyo.presentation.dictionary.DictionaryScreen
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
                onRegisterSuccessNavigation = {
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
                onLoginSuccessNavigation = {
                    navController.navigate(Route.HyoNavigation.route) {
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

        navigation(
            route = Route.HyoNavigation.route,
            startDestination = AppRoutes.HOME_SCREEN
        ) {
            composable(route = AppRoutes.HOME_SCREEN) {
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
                    onBackClick = { navController.popBackStack() },
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(AppRoutes.HOME_SCREEN) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(route = AppRoutes.SIGN_LANGUAGE_CAMERA_SCREEN) {
                SignLanguageCameraScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = AppRoutes.PROFILE_SCREEN) {
                ProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(AppRoutes.HOME_SCREEN) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onLogout = {
                        navController.navigate(Route.LoginScreen.route) {
                            popUpTo(Route.HyoNavigation.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

fun NavHostController.findStartDestination(): androidx.navigation.NavDestination? {
    return this.graph.findNode(this.graph.startDestinationId)
}