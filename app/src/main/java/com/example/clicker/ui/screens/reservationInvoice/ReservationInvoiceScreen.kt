package com.example.clicker.ui.screens.reservationInvoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.invoice.InvoiceDto
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.theme.ButtonGreen
import com.example.clicker.ui.viewmodel.AppViewModelProvider
import java.util.Locale

@Composable
fun ReservationInvoiceScreen(
    reservation: ReservationDto,
    modifier: Modifier = Modifier,
    viewModel: ReservationInvoiceViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(reservation.id) {
        viewModel.loadInvoiceData(reservation)
    }

    val uiState = viewModel.state.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is ReservationInvoiceUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is ReservationInvoiceUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ReservationInvoiceUiState.Success -> {
                val currentInvoice = state.invoices.firstOrNull()

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Facturation",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    InvoiceRow("Tables", "${formatEuro(state.tablesCost)} €")
                    InvoiceRow("Chaises", "${formatEuro(state.chairsCost)} €")
                    InvoiceRow("Prises", "${formatEuro(state.electricCost)} €")
                    InvoiceRow("Montant total TTC", "${formatEuro(state.totalPrice)} €")

                    Spacer(modifier = Modifier.height(8.dp))

                    if (currentInvoice == null) {
                        Text(
                            text = "Aucune facture créée.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )

                        Button(
                            onClick = { viewModel.createInvoiceIfNeeded(reservation) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonBlue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text("Créer une facture (${formatEuro(state.totalPrice)} € TTC)")
                        }
                    } else {
                        InvoiceItem(
                            invoice = currentInvoice,
                            onMarkPaid = {
                                viewModel.markPaid(currentInvoice, reservation)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InvoiceItem(
    invoice: InvoiceDto,
    onMarkPaid: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        InvoiceRow("Facture", "#${invoice.number}")
        InvoiceRow("Montant TTC", "${formatEuro(invoice.amountTtc)} €")
        InvoiceRow("TVA", "${formatEuro(invoice.vatRate)} %")
        InvoiceRow("Statut", invoice.status)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (invoice.status != "PAID") {
                Button(
                    onClick = onMarkPaid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Marquer payé")
                }
            } else {
                Text(
                    text = "Déjà payé",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun InvoiceRow(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    }
}

private fun formatEuro(value: Double): String {
    return String.format(Locale.US, "%.2f", value)
}