package com.anantmittal.ecellkmp.di

import com.anantmittal.ecellkmp.data.database.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DatabaseFactory() }
    }