package com.diegobonnin.ejemplo_login.data

import com.diegobonnin.ejemplo_login.network.UsersApiService
import com.diegobonnin.ejemplo_login.model.LoginInfo
import com.diegobonnin.ejemplo_login.model.TokenResponse
import com.diegobonnin.ejemplo_login.model.UserDetails
import com.diegobonnin.ejemplo_login.network.AuthService
import com.diegobonnin.ejemplo_login.network.TokenManager
import retrofit2.HttpException
import okhttp3.ResponseBody
import retrofit2.Response

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

class RemoteLoginRepository(
    private val authService: AuthService,
    private val apiService: UsersApiService,
    private val tokenManager: TokenManager
) : LoginRepository {

    override suspend fun login(loginInfo: LoginInfo): ApiResult<TokenResponse> {
        return try {
            // Get token
            val response = authService.getToken()

            // Log the response for debugging
            println("Token Response: ${response.accessToken}")
            println("Token Type: ${response.tokenType}")
            println("Expires In: ${response.expiresIn}")
            println("Scope: ${response.scope}")

            // Store token
            tokenManager.setToken(response.accessToken)

            ApiResult.Success(response)

        } catch (e: HttpException) {
            // Handle HTTP errors
            val errorBody = e.response()?.errorBody()?.string()
            println("HTTP Error ${e.code()}: $errorBody")
            ApiResult.Error("Login failed: HTTP ${e.code()}")

        } catch (e: Exception) {
            // Handle other errors
            println("Error during login: ${e.message}")
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }


    override suspend fun getUserDetails(username: String): UserDetails? = apiService.getUserDetails(username)

}