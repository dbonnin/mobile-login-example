package com.diegobonnin.ejemplo_login.network

import com.diegobonnin.ejemplo_login.model.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("oauth2/token")
    suspend fun getToken(
        @Query("grant_type") grantType: String = "client_credentials",
        @Query("scope") scope: String = "CUSTOM"
    ): TokenResponse

}