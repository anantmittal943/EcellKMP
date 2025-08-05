package com.anantmittal.ecellkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.anantmittal.ecellkmp.app.App
import com.anantmittal.ecellkmp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }