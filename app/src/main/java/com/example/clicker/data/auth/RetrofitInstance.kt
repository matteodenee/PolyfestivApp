package com.example.clicker.data.auth


import kotlinx.serialization.json.Json
import okhttp3.JavaNetCookieJar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy

object RetrofitInstance {

    private const val BASE_URL = "https://api.polyfestival.axithem.fr/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val cookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(JavaNetCookieJar(cookieManager))
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