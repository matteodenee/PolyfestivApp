package com.example.clicker.data.network

import com.example.clicker.data.auth.AuthApiService
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

    // Interceptor qui va s'exécuter avant chaque requête envoyée au serveur
    private val authCookieInterceptor = Interceptor { chain ->
        // On récupère la requête qui allait être envoyée
        val originalRequest = chain.request()
        // On crée une copie modifiable de cette requête
        val requestBuilder = originalRequest.newBuilder()
        // Si un cookie de session existe
        SessionCookieHolder.cookie?.let { cookieValue ->
            // On ajoute le cookie dans les headers de la requête
            // Cela permet au serveur de reconnaître l'utilisateur
            requestBuilder.addHeader("Cookie", cookieValue.substringBefore(";"))
        }
        // On envoie la requête
        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authCookieInterceptor)
        .build()

    val api: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=utf-8".toMediaType())
            )
            .build()
            .create(AuthApiService::class.java)
    }
}