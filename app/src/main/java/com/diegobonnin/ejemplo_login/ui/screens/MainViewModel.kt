package com.diegobonnin.ejemplo_login.ui.screens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.diegobonnin.ejemplo_login.LoginApplication
import com.diegobonnin.ejemplo_login.data.LoginRepository
import com.diegobonnin.ejemplo_login.model.UserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _userDetails = MutableStateFlow<UserDetails?>(null)
    val userDetails: StateFlow<UserDetails?> = _userDetails

    fun loadUserDetails(username: String) {
        viewModelScope.launch {
            try {
                val details = loginRepository.getUserDetails(username)
                _userDetails.value = details
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LoginApplication)
                MainViewModel(loginRepository = application.container.loginRepository)
            }
        }
    }

}