package com.app.hyo.data.manger

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.hyo.domain.manger.Result
import com.app.hyo.domain.manger.UserRepository
import com.app.hyo.domain.model.User
import com.app.hyo.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mindrot.jbcrypt.BCrypt

// Top-level DataStore delegate for user data
private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.USER_DATA_STORE_NAME // Make sure this constant is defined in Constants.kt
)

// Top-level private object for keys specific to this user DataStore
// This is a good place for it, similar to your LocalUserMangerImpl pattern.
private object UserDataStoreKeys {
    val USERS_LIST = stringPreferencesKey(Constants.USERS_LIST_KEY) // Make sure USERS_LIST_KEY is in Constants.kt
}

class UserDataStoreRepositoryImpl(private val context: Context) : UserRepository {

    override suspend fun registerUser(user: User): Result<Unit> {
        return try {
            val currentUsersJson = context.userDataStore.data.first()[UserDataStoreKeys.USERS_LIST] ?: "[]"
            val users = Json.decodeFromString<MutableList<User>>(currentUsersJson)

            if (users.any { it.email == user.email }) {
                Result.Error(IllegalArgumentException("Email already exists."))
            } else {
                users.add(user)
                context.userDataStore.edit { preferences ->
                    preferences[UserDataStoreKeys.USERS_LIST] = Json.encodeToString(users)
                }
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            // Log.e("UserDataStoreRepo", "Error registering user", e) // Good to add logging
            Result.Error(e)
        }
    }

    override suspend fun loginUser(email: String, providedRawPassword: String): Result<User> {
        return try {
            val currentUsersJson = context.userDataStore.data.first()[UserDataStoreKeys.USERS_LIST] ?: "[]"
            val users = Json.decodeFromString<List<User>>(currentUsersJson)
            val user = users.firstOrNull { it.email == email }

            if (user != null && BCrypt.checkpw(providedRawPassword, user.hashedPassword)) {
                Result.Success(user)
            } else {
                Result.Error(SecurityException("Invalid email or password."))
            }
        } catch (e: Exception) {
            // Log.e("UserDataStoreRepo", "Error logging in user", e)
            Result.Error(e)
        }
    }

    override fun getUserByEmail(email: String): Flow<User?> {
        return context.userDataStore.data.map { preferences ->
            val usersJson = preferences[UserDataStoreKeys.USERS_LIST] ?: "[]"
            try {
                val users = Json.decodeFromString<List<User>>(usersJson)
                users.firstOrNull { it.email == email }
            } catch (e: Exception) {
                // Log.e("UserDataStoreRepo", "Error parsing users for getUserByEmail", e)
                null // Or handle error appropriately
            }
        }
    }

    override fun getAllUsers(): Flow<List<User>> {
        return context.userDataStore.data.map { preferences ->
            val usersJson = preferences[UserDataStoreKeys.USERS_LIST] ?: "[]"
            try {
                Json.decodeFromString<List<User>>(usersJson)
            } catch (e: Exception) {
                // Log.e("UserDataStoreRepo", "Error parsing users for getAllUsers", e)
                emptyList() // Or handle error appropriately
            }
        }
    }

    // This private suspend fun is a bit redundant if you're already calling .first() in each method.
    // However, if you keep it, ensure it uses UserDataStoreKeys.USERS_LIST
    private suspend fun getUsersList(): List<User> { // Not directly used by public methods anymore if they fetch directly
        return try {
            val preferences = context.userDataStore.data.first()
            val usersJson = preferences[UserDataStoreKeys.USERS_LIST] ?: "[]"
            Json.decodeFromString<List<User>>(usersJson)
        } catch (e: Exception) {
            // Log.e("UserDataStoreRepo", "Error in getUsersList", e)
            emptyList()
        }
    }

    override suspend fun addQuizResultToUser(email: String, quizResult: com.app.hyo.domain.model.QuizResult): Result<Unit> {
        return try {
            val currentUsersJson = context.userDataStore.data.first()[UserDataStoreKeys.USERS_LIST] ?: "[]"
            val users = Json.decodeFromString<MutableList<User>>(currentUsersJson)

            val userIndex = users.indexOfFirst { it.email == email }
            if (userIndex != -1) {
                val oldUser = users[userIndex]
                val updatedHistory = oldUser.quizHistory + quizResult
                val updatedUser = oldUser.copy(quizHistory = updatedHistory)
                users[userIndex] = updatedUser

                context.userDataStore.edit { preferences ->
                    preferences[UserDataStoreKeys.USERS_LIST] = Json.encodeToString(users)
                }
                Result.Success(Unit)
            } else {
                Result.Error(Exception("User not found."))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}