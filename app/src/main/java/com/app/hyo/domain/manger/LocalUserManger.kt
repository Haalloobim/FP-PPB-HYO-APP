package com.app.hyo.domain.manger

import kotlinx.coroutines.flow.Flow

interface LocalUserManger {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<Boolean>

    suspend fun saveUserEmail(email: String)
    fun readUserEmail(): Flow<String?>
    suspend fun clearUserSession()
}