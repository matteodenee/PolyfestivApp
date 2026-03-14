package com.example.clicker

import android.app.Application
import com.example.clicker.data.AppContainer
import com.example.clicker.data.AppDataContainer

class ClickerApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer()
    }
}