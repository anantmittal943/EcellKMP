package com.anantmittal.ecellkmp

import android.app.Application
import com.anantmittal.ecellkmp.di.initKoin
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

class EcellApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { this@EcellApplication }
        Firebase.initialize(
            applicationContext,
            options = FirebaseOptions(
                applicationId = "1:887068944759:android:4e2cd66ca8cab13e4b9972",
                apiKey = "AIzaSyCDtzKCV0PX_DUjoN8LfxoRlTXxi7hehrs",
                projectId = "e-cell-main-app-68d8c"
            )
        )
    }
}