package com.example.clicker.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.clicker.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SessionPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val ACCESS_COOKIE = stringPreferencesKey("access_cookie")
        val REFRESH_COOKIE = stringPreferencesKey("refresh_cookie")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    val accessCookie: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Erreur lecture access cookie", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[ACCESS_COOKIE]
        }

    val refreshCookie: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Erreur lecture refresh cookie", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[REFRESH_COOKIE]
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

    suspend fun saveSession(accessCookie: String, refreshCookie: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_COOKIE] = accessCookie
            preferences[REFRESH_COOKIE] = refreshCookie
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun updateAccessCookie(accessCookie: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_COOKIE] = accessCookie
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_COOKIE)
            preferences.remove(REFRESH_COOKIE)
            preferences[IS_LOGGED_IN] = false
        }
    }
}