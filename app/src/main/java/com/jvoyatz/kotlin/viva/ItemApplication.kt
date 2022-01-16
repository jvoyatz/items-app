package com.jvoyatz.kotlin.viva

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

//generates a component which is attached to application's lifecycle
//moreover, it provides dependencies to this compenent
@HiltAndroidApp
class ItemApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}