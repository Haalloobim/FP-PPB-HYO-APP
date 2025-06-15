package com.app.hyo.presentation.quiz

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hyo.presentation.Dimens
import com.app.hyo.ui.theme.HyoTheme
import com.app.hyo.ui.theme.Poppins
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onBackClick: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mode Latihan", fontFamily = Poppins, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            )
        }
    ) { paddingValues ->
        if (uiState.quizFinished) {
            QuizResultScreen(
                score = uiState.score,
                totalQuestions = uiState.totalQuestions,
                onRestart = { viewModel.onEvent(QuizEvent.RestartQuiz) },
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            uiState.currentQuestion?.let { question ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()) // <<-- LAYAR DIBUAT SCROLLABLE
                        .padding(Dimens.MediumPadding1),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Skor: ${uiState.score}",
                            style = MaterialTheme.typography.headlineSmall.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Pertanyaan: ${uiState.questionNumber}/${uiState.totalQuestions}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontFamily = Poppins)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
                    Text(
                        text = "Pilih Abjad yang Sesuai",
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = Poppins, fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        val painter = remember(question.imageName) {
                            try {
                                val inputStream = context.assets.open("images/${question.imageName}")
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                BitmapPainter(bitmap.asImageBitmap())
                            } catch (e: IOException) {
                                null
                            }
                        }
                        if (painter != null) {
                            Image(
                                painter = painter,
                                contentDescription = "Tanda isyarat untuk ${question.letter}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Gambar tidak tersedia")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.MediumPadding2))

                    // Opsi Jawaban
                    uiState.options.chunked(2).forEach { rowOptions ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding1)
                        ) {
                            rowOptions.forEach { option ->
                                OptionButton(
                                    text = option.toString(),
                                    onClick = { viewModel.onEvent(QuizEvent.SelectAnswer(option)) },
                                    modifier = Modifier.weight(1f),
                                    isSelected = uiState.isAnswered,
                                    isCorrect = option == question.letter
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
                    }

                    if (uiState.isAnswered) {
                        Button(
                            onClick = { viewModel.onEvent(QuizEvent.NextQuestion) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Dimens.SmallPadding1)
                        ) {
                            Text("Berikutnya")
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
                }
            } ?: Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun OptionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    isCorrect: Boolean
) {
    val backgroundColor = when {
        !isSelected -> MaterialTheme.colorScheme.surfaceVariant
        isCorrect -> Color.Green.copy(alpha = 0.5f)
        else -> Color.Red.copy(alpha = 0.5f)
    }

    val borderColor = when {
        !isSelected -> MaterialTheme.colorScheme.outline
        isCorrect -> Color.Green
        else -> Color.Red
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = Dimens.MediumPadding1, horizontal = Dimens.SmallPadding1),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.MediumPadding2),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Kuis Selesai!",
            style = MaterialTheme.typography.displayMedium.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
        Text(
            text = "Skor Akhir Anda:",
            style = MaterialTheme.typography.headlineSmall.copy(fontFamily = Poppins)
        )
        Text(
            text = "$score / ${totalQuestions * 10}",
            style = MaterialTheme.typography.displaySmall.copy(fontFamily = Poppins, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(Dimens.MediumPadding3))
        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mulai Lagi")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    HyoTheme {
        QuizScreen(onBackClick = {})
    }
}