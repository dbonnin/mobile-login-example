package com.diegobonnin.ejemplo_login.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*


@Composable
fun CarsScreen(
    viewModel: CarsViewModel,
    loginViewModel: LoginViewModel,
    onLogoutSuccess: () -> Unit
) {
    val carsState by viewModel.carsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCars()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top bar with logout button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    loginViewModel.logout()
                    onLogoutSuccess()
                }
            ) {
                Text("Logout")
            }
        }

        // Existing content
        when (val state = carsState) {
            is CarsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is CarsState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.cars) { car ->
                        Text(
                            text = "${car.brand} - ${car.model}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
            is CarsState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> Unit
        }
    }
}