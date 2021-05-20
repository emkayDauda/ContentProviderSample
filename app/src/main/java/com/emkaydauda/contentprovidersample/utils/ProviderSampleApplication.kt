package com.emkaydauda.contentprovidersample.utils

import android.app.Application
import com.google.firebase.FirebaseApp

class ProviderSampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}