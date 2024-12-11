package com.diegobonnin.ejemplo_login.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val _credentials = MutableStateFlow<Credentials?>(null)
    val credentials: StateFlow<Credentials?> = _credentials

    data class Credentials(
        val username: String,
        val password: String
    )

    fun setCredentials(username: String, password: String) {
        _credentials.value = Credentials(username, password)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AuthViewModel()
            }
        }
    }
}