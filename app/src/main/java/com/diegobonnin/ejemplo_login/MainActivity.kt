package com.diegobonnin.ejemplo_login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.diegobonnin.ejemplo_login.data.AppContainer
import com.diegobonnin.ejemplo_login.ui.screens.AppNavigation
import com.diegobonnin.ejemplo_login.ui.theme.EjemplologinTheme

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = (application as LoginApplication).container
        enableEdgeToEdge()
        setContent {
            EjemplologinTheme {
                AppNavigation(
                    loginViewModel = appContainer.loginViewModel,
                    carsViewModel = appContainer.carsViewModel
                )
            }
        }
    }
}

