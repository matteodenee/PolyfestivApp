package com.example.clicker.data.network

import com.example.clicker.data.auth.AuthApiService
import com.example.clicker.data.game.GamesApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://api.polyfestival.axithem.fr/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    // Interceptor exécuté avant chaque requête envoyée au serveur
    private val authCookieInterceptor = Interceptor { chain ->
        // On récupère la requête originale qui va être envoyée
        val originalRequest = chain.request()
        // On crée une copie modifiable de cette requête
        val requestBuilder = originalRequest.newBuilder()
        // On récupère les cookies stockés en mémoire (access_token et refresh_token)
        val cookies = listOfNotNull(
            SessionCookieHolder.accessCookie,
            SessionCookieHolder.refreshCookie
        )
        // Si au moins un cookie existe
        if (cookies.isNotEmpty()) {
            // On ajoute les cookies dans le header HTTP "Cookie"
            requestBuilder.addHeader("Cookie", cookies.joinToString("; "))
        }
        // On envoie la requête au serveur
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authCookieInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=utf-8".toMediaType())
            )
            .build()
    }

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val gamesApi: GamesApiService by lazy {
        retrofit.create(GamesApiService::class.java)
    }
}