package com.example.clicker.data.network

object SessionCookieHolder {
    @Volatile
    var accessCookie: String? = null

    @Volatile
    var refreshCookie: String? = null
}