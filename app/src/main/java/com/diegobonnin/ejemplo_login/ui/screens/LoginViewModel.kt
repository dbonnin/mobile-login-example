package com.diegobonnin.ejemplo_login.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.diegobonnin.ejemplo_login.LoginApplication
import com.diegobonnin.ejemplo_login.data.ApiResult
import com.diegobonnin.ejemplo_login.data.LoginRepository
import com.diegobonnin.ejemplo_login.model.LoginInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val loginInfo = LoginInfo(
                    username = _uiState.value.username,
                    password = _uiState.value.password
                )
                when (val result = loginRepository.login(loginInfo)) {
                    is ApiResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoggedIn = true,
                            error = null
                        )
                    }
                    is ApiResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            error = result.message
                        )
                    }
                    null -> TODO()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Login failed: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LoginApplication)
                val loginRepository = application.container.loginRepository
                LoginViewModel(loginRepository = loginRepository)
            }
        }
    }

}