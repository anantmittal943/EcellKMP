package com.anantmittal.ecellkmp

import android.app.Application
import com.anantmittal.ecellkmp.di.initKoin

class EcellApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { this@EcellApplication }
    }
}