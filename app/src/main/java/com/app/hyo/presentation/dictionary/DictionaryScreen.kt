package com.app.hyo.presentation.dictionary

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hyo.presentation.dashboard.AppRoutes
import com.app.hyo.presentation.dashboard.BottomNavItem
import com.app.hyo.presentation.dashboard.HyoBottomNavigationBar
import com.app.hyo.ui.theme.HyoTheme
import com.app.hyo.ui.theme.Poppins
import java.io.IOException

// Data class to represent a sign language alphabet entry
data class AlphabetSign(val letter: Char, val imageName: String)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DictionaryScreen(
    onBackClick: () -> Unit,
    onNavigateToRoute: (String) -> Unit // For bottom navigation
) {
    // Generate the list of alphabet signs from A to Z
    val alphabetList = ('A'..'Z').map {
        AlphabetSign(it, "body_$it.jpg")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BISINDO Dictionary",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold
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
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            HyoBottomNavigationBar(
                onItemSelected = onNavigateToRoute,
                currentRoute = AppRoutes.DICTIONARY_SCREEN
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Two columns
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(alphabetList) { sign ->
                AlphabetGridItem(sign = sign)
            }
        }
    }
}

@Composable
fun AlphabetGridItem(sign: AlphabetSign) {
    val context = LocalContext.current

    val painter = remember(sign.imageName) {
        try {
            val inputStream = context.assets.open("images/${sign.imageName}")
            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            BitmapPainter(bitmap.asImageBitmap())
        } catch (e: IOException) {
            null
        }
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f) // Make it square
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display the image
            if (painter != null) {
                Image(
                    painter = painter,
                    contentDescription = "Sign for ${sign.letter}",
                    modifier = Modifier
                        .weight(1f) // Take up most of the space
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.Gray.copy(alpha = 0.5f))
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.White, textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Display the letter
            Text(
                text = sign.letter.toString(),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DictionaryScreenPreview() {
    HyoTheme {
        DictionaryScreen(onBackClick = {}, onNavigateToRoute = {})
    }
}