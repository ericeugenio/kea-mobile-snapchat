package com.example.snapchat

import android.app.Application
import com.example.snapchat.data.AppContainer
import com.example.snapchat.data.DefaultAppContainer

class SnapchatApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}