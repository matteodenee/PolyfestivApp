package com.example.clicker.ui.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.clicker.ClickerApplication
import com.example.clicker.ui.screens.exposants.ExposantViewModel
import com.example.clicker.ui.screens.gameCreate.GameCreateViewModel
import com.example.clicker.ui.screens.gameDetail.GameDetailViewModel
import com.example.clicker.ui.screens.gameEdit.GameEditViewModel
import com.example.clicker.ui.screens.games.GamesViewModel
import com.example.clicker.ui.screens.login.LoginViewModel
import com.example.clicker.ui.screens.register.RegisterViewModel
import com.example.clicker.ui.screens.festivalList.FestivalListViewModel
import com.example.clicker.ui.screens.festivalList.FestivalCreateViewModel
import com.example.clicker.ui.screens.festivalDetail.FestivalDetailViewModel
import com.example.clicker.ui.screens.festivalEdit.ModifDetailViewModel
import com.example.clicker.ui.screens.festivalEdit.StockTablesViewModel
import com.example.clicker.ui.screens.festivalEdit.StockMaterielViewModel
import com.example.clicker.ui.screens.festivalEdit.ZonesTarifViewModel
import com.example.clicker.ui.screens.festivalEdit.ZonesPlanViewModel
import com.example.clicker.ui.screens.admin.AdminViewModel
import com.example.clicker.ui.screens.reservationPlacement.ReservationPlacementViewModel
import com.example.clicker.ui.screens.reservationDetail.ReservationDetailViewModel
import com.example.clicker.ui.screens.reservations.ReservationsViewModel
import com.example.clicker.ui.screens.reservationCreate.ReservationCreateViewModel
import com.example.clicker.ui.screens.reservationSupplies.ReservationSuppliesViewModel
import com.example.clicker.ui.screens.reservationNote.ReservationNoteViewModel
import com.example.clicker.ui.screens.reservationContact.ReservationContactViewModel
import com.example.clicker.ui.screens.reservationInvoice.ReservationInvoiceViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(
                clickerApplication().container.authRepository,
                clickerApplication().sessionPreferencesRepository,
                clickerApplication().applicationContext
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
                clickerApplication().container.gamesRepository,
                clickerApplication().applicationContext
            )
        }

        initializer {
            GameEditViewModel(
                clickerApplication().container.gamesRepository,
                clickerApplication().applicationContext
            )
        }

        initializer {
            GameCreateViewModel(
                clickerApplication().container.gamesRepository,
                clickerApplication().applicationContext
            )
        }

        initializer {
            AdminViewModel(
                clickerApplication().container.adminRepository
            )
        }

        initializer {
            ExposantViewModel(
                repository = clickerApplication().container.exposantRepository
            )
        }
        
        initializer {
            ReservationPlacementViewModel(
                clickerApplication().container.reservationsRepository,
                clickerApplication().container.reservationGameRepository,
                clickerApplication().container.reservationGamePlacementRepository,
                clickerApplication().container.reservationTariffzoneAllocationRepository,
                clickerApplication().container.tarifZoneRepository,
                clickerApplication().container.mapZoneRepository,
                clickerApplication().container.festivalTableRepository,
                clickerApplication().container.equipmentRepository,
                clickerApplication().container.festivalEquipmentStockRepository,
                clickerApplication().container.gamesRepository
            )
        }

        initializer {
            FestivalListViewModel(
                clickerApplication().container.festivalsRepository
            )
        }

        initializer {
            FestivalCreateViewModel(
                clickerApplication().container.festivalsRepository
            )
        }

        initializer {
            FestivalDetailViewModel(
                clickerApplication().container.festivalsRepository
            )
        }

        initializer {
            ModifDetailViewModel(
                clickerApplication().container.festivalsRepository
            )
        }

        initializer {
            StockTablesViewModel(
                clickerApplication().container.tablesRepository
            )
        }

        initializer {
            StockMaterielViewModel(
                clickerApplication().container.equipmentsRepository
            )
        }

        initializer {
            ZonesTarifViewModel(
                clickerApplication().container.tariffZonesRepository
            )
        }

        initializer {
            ZonesPlanViewModel(
                clickerApplication().container.mapZonesRepository
            )
        }

        initializer {
            ReservationsViewModel(
                clickerApplication().container.reservationsRepository,
                clickerApplication().container.festivalsRepository,
                clickerApplication().container.exposantRepository
            )
        }

        initializer {
            ReservationDetailViewModel(
                clickerApplication().container.reservationsRepository,
                clickerApplication().container.exposantRepository
            )
        }

        initializer {
            ReservationCreateViewModel(
                clickerApplication().container.exposantRepository,
                clickerApplication().container.reservationsRepository,
                clickerApplication().container.festivalsRepository
            )
        }

        initializer {
            ReservationSuppliesViewModel(
                clickerApplication().container.tariffZonesRepository,
                clickerApplication().container.reservationTariffzoneAllocationRepository,
                clickerApplication().container.reservationGameRepository,
                clickerApplication().container.gamesRepository,
                clickerApplication().container.reservationsRepository
            )
        }

        initializer {
            ReservationNoteViewModel(
                clickerApplication().container.reservationNoteRepository
            )
        }

        initializer {
            ReservationContactViewModel(
                clickerApplication().container.reservationContactRepository,
                clickerApplication().container.reservationsRepository
            )
        }

        initializer {
            ReservationInvoiceViewModel(
                clickerApplication().container.invoiceRepository,
                clickerApplication().container.tariffZonesRepository,
                clickerApplication().container.reservationTariffzoneAllocationRepository,
                clickerApplication().container.reservationGameRepository,
                clickerApplication().container.equipmentsRepository
            )
        }
    }
}

fun CreationExtras.clickerApplication(): ClickerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ClickerApplication)
