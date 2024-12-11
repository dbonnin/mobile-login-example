package com.diegobonnin.ejemplo_login.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegobonnin.ejemplo_login.data.Car
import com.diegobonnin.ejemplo_login.data.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class CarsState {
    object Initial : CarsState()
    object Loading : CarsState()
    data class Success(val cars: List<Car>) : CarsState()
    data class Error(val message: String) : CarsState()
}


class CarsViewModel(private val carRepository: CarRepository) : ViewModel() {
    private val _carsState = MutableStateFlow<CarsState>(CarsState.Initial)
    val carsState: StateFlow<CarsState> = _carsState.asStateFlow()

    fun loadCars() {
        viewModelScope.launch {
            _carsState.value = CarsState.Loading
            try {
                val result = carRepository.getCars()
                result.fold(
                    onSuccess = { cars ->
                        _carsState.value = CarsState.Success(cars)
                    },
                    onFailure = {
                        _carsState.value = CarsState.Error(it.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _carsState.value = CarsState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
