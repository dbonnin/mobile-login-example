package com.diegobonnin.ejemplo_login.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegobonnin.ejemplo_login.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log

// States
sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val result = authRepository.login(username, password)
                result.fold(
                    onSuccess = {
                        _loginState.value = LoginState.Success
                        _isLoggedIn.value = true
                        _username.value = username
                    },
                    onFailure = {
                        _loginState.value = LoginState.Error(it.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _isLoggedIn.value = false
                _username.value = null
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Logout failed", e)
            }
        }
    }
}
