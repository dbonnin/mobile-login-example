package com.diegobonnin.ejemplo_login

import android.app.Application
import com.diegobonnin.ejemplo_login.data.AppContainer
import com.diegobonnin.ejemplo_login.data.DefaultAppContainer

class LoginApplication: Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }

}