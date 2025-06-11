package com.app.hyo.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.hyo.presentation.Dimens
import com.app.hyo.presentation.dashboard.AppRoutes
import com.app.hyo.presentation.dashboard.HyoBottomNavigationBar
import com.app.hyo.ui.theme.Poppins
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onNavigateToRoute: (String) -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }

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
                            text = { Text("Edit Profile") },
                            onClick = {
                                viewModel.editProfile("New Name", "new.email@example.com")
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Log Out") },
                            onClick = {
                                viewModel.logout()
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

            Text(
                text = userProfile.name,
                fontSize = 24.sp,
                fontFamily = Poppins
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = userProfile.email,
                fontSize = 16.sp,
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, name = "Profile Screen Light")
@Composable
fun ProfileScreenPreviewLight() {
    com.app.hyo.ui.theme.HyoTheme(darkTheme = false) {
        ProfileScreen()
    }
}

@Preview(showBackground = true, name = "Profile Screen Dark")
@Composable
fun ProfileScreenPreviewDark() {
    com.app.hyo.ui.theme.HyoTheme(darkTheme = true) {
        ProfileScreen()
    }
}
