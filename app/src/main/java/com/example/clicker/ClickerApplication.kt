package com.example.clicker

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.example.clicker.data.AppContainer
import com.example.clicker.data.AppDataContainer
import com.example.clicker.data.datastore.SessionPreferencesRepository
import okhttp3.OkHttpClient

private const val SESSION_PREFERENCES_NAME = "session_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SESSION_PREFERENCES_NAME
)

class ClickerApplication : Application(), SingletonImageLoader.Factory {

    lateinit var container: AppContainer
    lateinit var sessionPreferencesRepository: SessionPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(applicationContext)
        sessionPreferencesRepository = SessionPreferencesRepository(dataStore)
    }

    override fun newImageLoader(context: Context): ImageLoader {
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "ClickerApp/1.0")
                    .build()
                chain.proceed(request)
            }
            .build()

        return ImageLoader.Builder(context)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = { okHttpClient }
                    )
                )
            }
            .build()
    }
}