package com.example.clicker.ui


import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clicker.ClickerApplication
import com.example.clicker.ui.screens.login.LoginViewModel
import com.example.clicker.ui.screens.register.RegisterViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(
                clickerApplication().container.authRepository
            )
        }
        initializer {
            RegisterViewModel(
                clickerApplication().container.authRepository
            )
        }
    }
}

fun CreationExtras.clickerApplication(): ClickerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ClickerApplication)