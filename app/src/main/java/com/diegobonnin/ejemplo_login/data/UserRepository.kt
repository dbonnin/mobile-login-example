package com.diegobonnin.ejemplo_login.data

import com.diegobonnin.ejemplo_login.network.ApiService

class UserRepository(private val apiService: ApiService) {
    suspend fun getUser(username: String): Result<User> {
        return try {
            val response = apiService.getUser(username)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
