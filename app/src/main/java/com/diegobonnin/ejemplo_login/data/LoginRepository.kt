package com.diegobonnin.ejemplo_login.data

import com.diegobonnin.ejemplo_login.model.LoginInfo
import com.diegobonnin.ejemplo_login.model.TokenResponse
import com.diegobonnin.ejemplo_login.model.UserDetails

interface LoginRepository {

    suspend fun login(loginInfo: LoginInfo): ApiResult<TokenResponse>?
    suspend fun getUserDetails(username: String): UserDetails?

}