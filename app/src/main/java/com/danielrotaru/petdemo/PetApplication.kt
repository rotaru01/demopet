package com.danielrotaru.petdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}