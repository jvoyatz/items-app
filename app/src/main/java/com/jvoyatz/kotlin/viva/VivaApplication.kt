package com.jvoyatz.kotlin.viva

import android.app.Application
import timber.log.Timber

class VivaApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}