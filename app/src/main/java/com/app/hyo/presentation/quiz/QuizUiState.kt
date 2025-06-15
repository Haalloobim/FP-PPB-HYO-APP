package com.app.hyo.presentation.quiz

import com.app.hyo.presentation.dictionary.AlphabetSign

data class QuizUiState(
    val currentQuestion: AlphabetSign? = null,
    val options: List<Char> = emptyList(),
    val score: Int = 0,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean? = null,
    val quizFinished: Boolean = false,
    val questionNumber: Int = 0,
    val totalQuestions: Int = 0
)