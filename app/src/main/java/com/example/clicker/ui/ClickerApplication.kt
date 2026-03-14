package com.example.clicker.ui


import android.app.Application
import com.example.clicker.data.AppContainer
import com.example.clicker.data.AppDataContainer

class ClickerApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}