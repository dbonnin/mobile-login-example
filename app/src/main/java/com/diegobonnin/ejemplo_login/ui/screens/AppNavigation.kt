package com.diegobonnin.ejemplo_login.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    loginViewModel: LoginViewModel,
    carsViewModel: CarsViewModel
) {
    val navController = rememberNavController()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "cars" else "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { navController.navigate("cars") }
            )
        }
        composable("cars") {
            CarsScreen(
                viewModel = carsViewModel,
                loginViewModel = loginViewModel,
                onLogoutSuccess = {
                    navController.navigate("login") {
                        popUpTo("cars") { inclusive = true }
                    }
                }
            )
        }
    }
}