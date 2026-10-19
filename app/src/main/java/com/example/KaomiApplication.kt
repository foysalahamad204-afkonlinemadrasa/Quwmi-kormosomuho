package com.example

import android.app.Application
import com.google.firebase.FirebaseApp

class KaomiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        android.util.Log.d("KaomiApplication", "Initializing Firebase")
        FirebaseApp.initializeApp(this)
    }
}
