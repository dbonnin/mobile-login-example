package com.diegobonnin.ejemplo_login.data

import android.util.Log
import com.diegobonnin.ejemplo_login.network.ApiService

class CarRepository(private val apiService: ApiService) {

    suspend fun getCars(): Result<List<Car>> {
        return try {
            Log.d("CarRepository", "Requesting cars list")
            val response = apiService.getCars()

            Log.d("CarRepository", "Response code: ${response.code()}")
            Log.d("CarRepository", "Response headers: ${response.headers()}")

            if (response.isSuccessful) {
                val cars = response.body()!!
                Log.d("CarRepository", "Cars retrieved successfully. Number of cars: ${cars.size}")
                Log.d("CarRepository", "Cars data: $cars")
                Result.success(cars)
            } else {
                Log.e("CarRepository", "Failed to get cars. Code: ${response.code()}")
                Log.e("CarRepository", "Error body: ${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to get cars"))
            }
        } catch (e: Exception) {
            Log.e("CarRepository", "Exception while getting cars", e)
            Result.failure(e)
        }
    }

}