package com.example.clicker.ui.viewmodel


import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clicker.ClickerApplication
import com.example.clicker.ui.screens.gameCreate.GameCreateViewModel
import com.example.clicker.ui.screens.gameDetail.GameDetailViewModel
import com.example.clicker.ui.screens.gameEdit.GameEditViewModel
import com.example.clicker.ui.screens.games.GamesViewModel
import com.example.clicker.ui.screens.login.LoginViewModel
import com.example.clicker.ui.screens.register.RegisterViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(
                clickerApplication().container.authRepository,
                clickerApplication().sessionPreferencesRepository
            )
        }
        initializer {
            RegisterViewModel(
                clickerApplication().container.authRepository
            )
        }
        initializer {
            GamesViewModel(
                clickerApplication().container.gamesRepository
            )
        }

        initializer {
            GameDetailViewModel(
                clickerApplication().container.gamesRepository
            )
        }

        initializer {
            GameEditViewModel(
                clickerApplication().container.gamesRepository
            )
        }

        initializer {
            GameCreateViewModel(
                clickerApplication().container.gamesRepository
            )
        }
    }
}

fun CreationExtras.clickerApplication(): ClickerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ClickerApplication)