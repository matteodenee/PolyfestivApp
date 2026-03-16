package com.example.clicker.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import com.example.clicker.TAG


class SessionPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val SESSION_COOKIE = stringPreferencesKey("session_cookie")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    val sessionCookie: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Erreur lecture session cookie", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[SESSION_COOKIE]
        }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Erreur lecture état session", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    suspend fun saveSession(cookie: String) {
        dataStore.edit { preferences ->
            preferences[SESSION_COOKIE] = cookie
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(SESSION_COOKIE)
            preferences[IS_LOGGED_IN] = false
        }
    }
}