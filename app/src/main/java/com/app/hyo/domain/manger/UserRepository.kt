package com.app.hyo.domain.manger

import com.app.hyo.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun registerUser(user: User): Result<Unit>
    suspend fun loginUser(email: String, providedRawPassword: String): Result<User>
    fun getUserByEmail(email: String): Flow<User?>
    fun getAllUsers(): Flow<List<User>> // For simplicity with DataStore

}

// In a utility file or common domain location
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}