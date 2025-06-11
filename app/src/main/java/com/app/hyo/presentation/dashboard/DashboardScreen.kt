package com.app.hyo.presentation.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt// Icon for camera/recognition
import androidx.compose.material.icons.filled.Book // Icon for dictionary/learn
import androidx.compose.material.icons.filled.School // Icon for learn/practice
import androidx.compose.material.icons.filled.People // Icon for community
import androidx.compose.material.icons.filled.Mic // Icon for practice/speech
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hyo.R // Assuming you have a placeholder image in drawable
import com.app.hyo.presentation.Dimens
import com.app.hyo.ui.theme.HyoTheme
import com.app.hyo.ui.theme.Poppins // Assuming Poppins is your app's font

// Define routes for navigation
object AppRoutes {
    const val HOME_SCREEN = "home_screen_route"
    const val SIGN_LANGUAGE_CAMERA_SCREEN = "sign_language_camera_screen_route"
    const val LEARN_ISL_SCREEN = "learn_isl_screen_route"
    const val PRACTICE_ISL_SCREEN = "practice_isl_screen_route"
    const val DICTIONARY_SCREEN = "dictionary_screen_route"
    const val COMMUNITY_SCREEN = "community_screen_route"
    const val PROFILE_SCREEN = "profile_screen_route"
    const val CHAT_SCREEN = "chat_screen_route" // Keeping this from original bottom nav
    const val MENU_SCREEN = "menu_screen_route" // Keeping this from original bottom nav
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    onProfileClick: () -> Unit,
    onNavigateToRoute: (String) -> Unit // For bottom navigation
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "HYO",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer // Explicit tint for clarity
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            HyoBottomNavigationBar(
                onItemSelected = { route ->
                    onNavigateToRoute(route)
                },
                currentRoute = AppRoutes.HOME_SCREEN // Highlight Home as current for dashboard
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = Dimens.MediumPadding1) // Overall horizontal padding
        ) {
            item { Spacer(modifier = Modifier.height(Dimens.MediumPadding1)) }

            // Welcome Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimens.MediumPadding1))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f))
                        .padding(Dimens.MediumPadding2),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Welcome to HYO!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(Dimens.SmallPadding2))
                    Text(
                        text = "Your helper for Indonesian Sign Language.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = Poppins
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(Dimens.MediumPadding2)) }

            // Live Sign Language Recognition Section (Call to Action)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp) // Make it prominent
                        .clickable { onNavigateToRoute(AppRoutes.SIGN_LANGUAGE_CAMERA_SCREEN) },
                    shape = RoundedCornerShape(Dimens.MediumPadding1),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary // Use primary color for CTA
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.MediumPadding2),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Live Recognition",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(Dimens.SmallPadding2))
                        Text(
                            text = "Start Live Sign Recognition",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(Dimens.MediumPadding2)) }

            // Learn & Practice Section
            item {
                Text(
                    text = "Learn & Practice",
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
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Search, // Or a specific dictionary icon
                        title = "ISL Dictionary",
                        onClick = { onNavigateToRoute(AppRoutes.DICTIONARY_SCREEN) }
                    )
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Mic, // Or a hand icon for practice
                        title = "Practice Mode",
                        onClick = { onNavigateToRoute(AppRoutes.PRACTICE_ISL_SCREEN) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(Dimens.MediumPadding2)) }

            // ISL Dictionary & Community Section
//            item {
//                Text(
//                    text = "Resources",
//                    style = MaterialTheme.typography.headlineSmall.copy(
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = Poppins
//                    ),
//                    modifier = Modifier.padding(bottom = Dimens.SmallPadding2)
//                )
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding1)
//                ) {
//                    DashboardCard(
//                        modifier = Modifier.weight(1f),
//                        icon = Icons.Filled.Search, // Or a specific dictionary icon
//                        title = "ISL Dictionary",
//                        onClick = { onNavigateToRoute(AppRoutes.DICTIONARY_SCREEN) }
//                    )
//                    DashboardCard(
//                        modifier = Modifier.weight(1f),
//                        icon = Icons.Filled.People,
//                        title = "Community",
//                        onClick = { onNavigateToRoute(AppRoutes.COMMUNITY_SCREEN) }
//                    )
//                }
//            }
            item { Spacer(modifier = Modifier.height(Dimens.MediumPadding2)) } // Space at the bottom
        }
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.SmallPadding2),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Placeholder color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.MediumPadding1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding1))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontFamily = Poppins,
                    fontSize = 14.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HyoBottomNavigationBar(
    onItemSelected: (String) -> Unit,
    currentRoute: String? = null // Optional: to highlight the current active item
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, AppRoutes.HOME_SCREEN),
        BottomNavItem("Camera", Icons.Filled.CameraAlt, AppRoutes.SIGN_LANGUAGE_CAMERA_SCREEN), // Added Camera route
        BottomNavItem("Dictionary", Icons.Filled.Book, AppRoutes.DICTIONARY_SCREEN), // <-- CHANGE THIS LINE
        BottomNavItem("Menu", Icons.Filled.Menu, AppRoutes.MENU_SCREEN)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, fontSize = 10.sp, fontFamily = Poppins) },
                selected = currentRoute == item.route,
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
