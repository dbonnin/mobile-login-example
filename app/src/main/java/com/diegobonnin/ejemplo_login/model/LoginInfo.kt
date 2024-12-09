package com.diegobonnin.ejemplo_login.model


import kotlinx.serialization.Serializable

@Serializable
data class LoginInfo(
    val username: String,
    val password: String
)
