package com.example.clicker

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.clicker.data.AppContainer
import com.example.clicker.data.AppDataContainer
import com.example.clicker.data.datastore.SessionPreferencesRepository

private const val SESSION_PREFERENCES_NAME = "session_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SESSION_PREFERENCES_NAME
)

class ClickerApplication : Application() {

    lateinit var container: AppContainer
    lateinit var sessionPreferencesRepository: SessionPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer()
        sessionPreferencesRepository = SessionPreferencesRepository(dataStore)
    }
}