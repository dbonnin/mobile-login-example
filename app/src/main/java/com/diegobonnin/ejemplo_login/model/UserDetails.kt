package com.diegobonnin.ejemplo_login.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val username: String,
    val email: String,
    val name: String
)
