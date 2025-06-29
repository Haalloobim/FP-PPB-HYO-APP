package com.app.hyo.data.manger

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.hyo.domain.manger.LocalUserManger
import com.app.hyo.util.Constants
import com.app.hyo.util.Constants.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserMangerImpl(
    private val context: Context
) : LocalUserManger {

    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.APP_ENTRY] ?: false
        }
    }

    override suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.LOGGED_IN_USER_EMAIL] = email
        }
    }

    override fun readUserEmail(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.LOGGED_IN_USER_EMAIL]
        }
    }

    override suspend fun clearUserSession() {
        context.dataStore.edit { settings ->
            settings.remove(PreferenceKeys.LOGGED_IN_USER_EMAIL)
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = USER_SETTINGS)

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey(Constants.APP_ENTRY)
    val LOGGED_IN_USER_EMAIL = stringPreferencesKey(Constants.LOGGED_IN_USER_EMAIL)
}