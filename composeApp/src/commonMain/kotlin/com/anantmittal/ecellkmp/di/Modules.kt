package com.anantmittal.ecellkmp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.anantmittal.ecellkmp.data.database.DatabaseFactory
import com.anantmittal.ecellkmp.data.database.EcellAccountsDatabase
import com.anantmittal.ecellkmp.data.network.authenticationsource.EcellAuthSource
import com.anantmittal.ecellkmp.data.network.authenticationsource.FirebaseEcellAuthSource
import com.anantmittal.ecellkmp.data.network.datasource.KtorRemoteEcellDataSource
import com.anantmittal.ecellkmp.data.network.datasource.RemoteEcellDataSource
import com.anantmittal.ecellkmp.data.repository.DefaultEcellRepository
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.presentation.login_screen.LoginViewModel
import com.anantmittal.ecellkmp.presentation.signup_screen.SignupViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { Firebase.auth }
    singleOf(::FirebaseEcellAuthSource).bind<EcellAuthSource>()
    singleOf(::KtorRemoteEcellDataSource).bind<RemoteEcellDataSource>()
    singleOf(::DefaultEcellRepository).bind<EcellRepository>()

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<EcellAccountsDatabase>().ecellAccountsDao }

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
}