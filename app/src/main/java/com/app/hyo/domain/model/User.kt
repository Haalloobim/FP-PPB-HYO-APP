package com.app.hyo.domain.model

import kotlinx.serialization.Serializable

@Serializable // Add if you plan to serialize with Kotlinx Serialization for DataStore
data class User(
    val id: String = java.util.UUID.randomUUID().toString(), // Auto-generate ID
    val name: String,
    val telepon: String,
    val email: String,
    val hashedPassword: String // Store hashed password, NOT plain text
)