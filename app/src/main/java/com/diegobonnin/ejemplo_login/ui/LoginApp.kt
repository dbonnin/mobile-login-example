package com.diegobonnin.ejemplo_login.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.diegobonnin.ejemplo_login.ui.screens.LoginScreen
import com.diegobonnin.ejemplo_login.ui.screens.LoginViewModel
import com.diegobonnin.ejemplo_login.ui.screens.MainScreen
import com.diegobonnin.ejemplo_login.ui.screens.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.diegobonnin.ejemplo_login.ui.screens.AuthViewModel


object NavRoutes {
    const val LOGIN = "login"
    const val MAIN = "main"
}


@Composable
fun LoginApp() {

    var isLoggedIn by remember { mutableStateOf(false) }

    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.MAIN) {
                        // Optional: Clear back stack after successful login
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.MAIN) {
            MainScreen(
                viewModel = mainViewModel,
                authViewModel = authViewModel)
        }
    }

/*
    if (!isLoggedIn) {
        LoginScreen(
            viewModel = loginViewModel,
            onLoginSuccess = { isLoggedIn = true }
        )
    } else {
        MainScreen(viewModel = mainViewModel)
    }

 */
}