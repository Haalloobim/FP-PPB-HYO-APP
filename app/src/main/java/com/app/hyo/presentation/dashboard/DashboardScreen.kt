package com.app.hyo.presentation.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowBack // Placeholder, consider a more appropriate icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hyo.R // Assuming you have a placeholder image in drawable
import com.app.hyo.presentation.Dimens
import com.app.hyo.ui.theme.HyoTheme
import com.app.hyo.ui.theme.Poppins // Assuming Poppins is your app's font

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    // viewModel: DashboardViewModel = hiltViewModel() // Uncomment when ViewModel is ready
    onProfileClick: () -> Unit,
    onNavigateToRoute: (String) -> Unit // For bottom navigation
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "TUMBAS",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp // Adjust as per your theme
                        // Color is now inherited from TopAppBarDefaults
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp) // Adjust size
                            // Tint is now inherited from TopAppBarDefaults
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors( // Changed from smallTopAppBarColors
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), // Light purpleish
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            HyoBottomNavigationBar(
                onItemSelected = { route ->
                    onNavigateToRoute(route) // Handle navigation
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(horizontal = Dimens.MediumPadding1) // Overall horizontal padding
                .background(MaterialTheme.colorScheme.background)
        ) {
            item { Spacer(modifier = Modifier.height(Dimens.MediumPadding1)) }

            // Current News Section
            item {
                Text(
                    text = "Current News",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins
                    ),
                    modifier = Modifier.padding(bottom = Dimens.SmallPadding2)
                )
                // Placeholder for News Item
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Adjust height as needed
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant) // Placeholder color
                ) {
                    // You can add an Image or a more complex composable here
                    // For now, a simple text placeholder
                    Text(
                        text = "News Placeholder",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(Dimens.MediumPadding2)) }

            // Puskesmas Terdekat Section
            item {
                Text(
                    text = "Puskesmas Terdekat",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins
                    ),
                    modifier = Modifier.padding(bottom = Dimens.SmallPadding2)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding1)
                ) {
                    // Placeholder for Puskesmas Item 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp) // Adjust height as needed
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant) // Placeholder color
                    ) {
                        Text(
                            text = "Puskesmas 1",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // Placeholder for Puskesmas Item 2
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp) // Adjust height as needed
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant) // Placeholder color
                    ) {
                        Text(
                            text = "Puskesmas 2",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            // item { Spacer(modifier = Modifier.height(Dimens.MediumPadding2)) } // Optional: Space at the bottom
        }
    }
}

@Composable
fun HyoBottomNavigationBar(
    onItemSelected: (String) -> Unit,
    currentRoute: String? = null // Optional: to highlight the current active item
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, "home_screen_route"), // Replace with actual routes
        BottomNavItem("Search", Icons.Filled.Search, "search_screen_route"),
        BottomNavItem("Chat", Icons.Filled.MailOutline, "chat_screen_route"), // Changed to Icons.Filled.Chat
        BottomNavItem("Back", Icons.Filled.ArrowBack, "back_action_or_route"), // Or another relevant icon/route
        BottomNavItem("Menu", Icons.Filled.Menu, "menu_screen_route")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface, // Or your desired color
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, fontSize = 10.sp, fontFamily = Poppins) },
                selected = currentRoute == item.route, // Highlight if current route matches
                onClick = { onItemSelected(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                )
            )
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)

@Preview(showBackground = true, name = "Dashboard Screen Light")
@Composable
fun DashboardScreenPreviewLight() {
    HyoTheme(darkTheme = false) {
        DashboardScreen(onProfileClick = {}, onNavigateToRoute = {})
    }
}

@Preview(showBackground = true, name = "Dashboard Screen Dark")
@Composable
fun DashboardScreenPreviewDark() {
    HyoTheme(darkTheme = true) {
        DashboardScreen(onProfileClick = {}, onNavigateToRoute = {})
    }
}