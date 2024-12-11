package com.diegobonnin.ejemplo_login.data

import android.util.Base64
import com.diegobonnin.ejemplo_login.network.ApiService
import android.util.Log


class AuthRepository(private val apiService: ApiService) {
    suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val basicAuth = "Basic " + Base64.encodeToString(
                "$username:$password".toByteArray(),
                Base64.NO_WRAP
            )

            // Log.d("AuthRepository", "Attempting login with auth header: $basicAuth")

            val request = apiService.login(username, basicAuth).raw().request
            Log.d("AuthRepository", "Request URL: ${request.url}")

            val response = apiService.login(username, basicAuth)

            Log.d("AuthRepository", "Response code: ${response.code()}")
            Log.d("AuthRepository", "Response headers: ${response.headers()}")
            Log.d("AuthRepository", "Response body: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                Log.d("AuthRepository", "Login successful")
                Result.success(Unit)
            } else {
                Log.e("AuthRepository", "Login failed with code: ${response.code()}")
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login exception", e)
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            Log.d("AuthRepository", "Attempting logout")
            val response = apiService.logout()

            if (response.isSuccessful) {
                Log.d("AuthRepository", "Logout successful")
                Result.success(Unit)
            } else {
                Log.e("AuthRepository", "Logout failed with code: ${response.code()}")
                Result.failure(Exception("Logout failed"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Logout exception", e)
            Result.failure(e)
        }
    }
}