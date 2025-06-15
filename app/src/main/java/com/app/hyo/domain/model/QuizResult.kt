package com.app.hyo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val score: Int,
    val totalQuestions: Int,
    val timestamp: Long = System.currentTimeMillis()
)