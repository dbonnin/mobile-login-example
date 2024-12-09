package com.diegobonnin.ejemplo_login.network


import com.diegobonnin.ejemplo_login.model.UserDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApiService {

    @GET("users/{username}")
    suspend fun getUserDetails(@Path("username") username: String): UserDetails?

}