package com.app.hyo.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hyo.presentation.Dimens
import com.app.hyo.presentation.dashboard.AppRoutes
import com.app.hyo.presentation.dashboard.HyoBottomNavigationBar
import com.app.hyo.ui.theme.Poppins
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onNavigateToRoute: (String) -> Unit,
    onLogout: () -> Unit, // Callback for logout navigation
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ProfileViewModel.NavigationEvent.NavigateToLogin -> {
                    onLogout()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Profile",
                        fontFamily = Poppins,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Log Out") },
                            leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = "Log Out")},
                            onClick = {
                                viewModel.onLogoutClick()
                                menuExpanded = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            )
        },
        bottomBar = {
            HyoBottomNavigationBar(
                onItemSelected = onNavigateToRoute,
                currentRoute = AppRoutes.PROFILE_SCREEN
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.MediumPadding2)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (user != null) {
                Text(
                    text = user!!.name,
                    fontSize = 24.sp,
                    fontFamily = Poppins
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = user!!.email,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true, name = "Profile Screen")
@Composable
fun ProfileScreenPreview() {
    com.app.hyo.ui.theme.HyoTheme {
        ProfileScreen(onBackClick = {}, onNavigateToRoute = {}, onLogout = {})
    }
}