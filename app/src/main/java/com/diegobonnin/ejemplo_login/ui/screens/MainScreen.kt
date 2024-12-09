package com.diegobonnin.ejemplo_login.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// MainScreen.kt
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    authViewModel: AuthViewModel
) {

    // Collect credentials from AuthViewModel
    val credentials by authViewModel.credentials.collectAsState()

    // Effect to fetch user details when credentials are available
    LaunchedEffect(credentials) {
        credentials?.let { creds ->
            // Call getUserDetails with the username from credentials
            viewModel.loadUserDetails(creds.username)
        }
    }

    // UI State from MainViewModel
    val userDetails by viewModel.userDetails.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            userDetails == null -> CircularProgressIndicator()
            else -> {
                userDetails?.let { user ->
                    Text("Username: ${user.username}")
                    Text("Email: ${user.email}")
                    Text("Name: ${user.name}")
                }
            }
        }
    }
}
