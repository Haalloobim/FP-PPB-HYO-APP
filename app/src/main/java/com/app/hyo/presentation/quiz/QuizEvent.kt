package com.app.hyo.presentation.quiz

sealed class QuizEvent {
    data class SelectAnswer(val answer: Char) : QuizEvent()
    object NextQuestion : QuizEvent()
    object RestartQuiz : QuizEvent()
}