package com.anantmittal.ecellkmp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.anantmittal.ecellkmp.data.database.DatabaseFactory
import com.anantmittal.ecellkmp.data.database.EcellAccountsDatabase
import com.anantmittal.ecellkmp.data.network.authenticationsource.EcellAuthSource
import com.anantmittal.ecellkmp.data.network.authenticationsource.FirebaseEcellAuthSource
import com.anantmittal.ecellkmp.data.network.datasource.FirebaseRemoteEcellDataSource
import com.anantmittal.ecellkmp.data.network.datasource.HybridRemoteEcellDataSource
import com.anantmittal.ecellkmp.data.network.datasource.KtorRemoteEcellDataSource
import com.anantmittal.ecellkmp.data.network.datasource.RemoteEcellDataSource
import com.anantmittal.ecellkmp.data.repository.DefaultEcellRepository
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.presentation.account_screen.AccountViewModel
import com.anantmittal.ecellkmp.presentation.home_screen.HomeViewModel
import com.anantmittal.ecellkmp.presentation.login_screen.LoginViewModel
import com.anantmittal.ecellkmp.presentation.signup_screen.SignupViewModel
import com.anantmittal.ecellkmp.presentation.team_detail_screen.TeamDetailViewModel
import com.anantmittal.ecellkmp.presentation.team_shared.TeamSharedViewModel
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

// Define qualifiers for different data source implementations
object DataSourceQualifiers {
    val FIREBASE = named("firebase")
    val KTOR = named("ktor")
    val HYBRID = named("hybrid")
}

val sharedModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    singleOf(::FirebaseEcellAuthSource).bind<EcellAuthSource>()

    // Register all implementations with qualifiers
    single<RemoteEcellDataSource>(DataSourceQualifiers.FIREBASE) {
        FirebaseRemoteEcellDataSource(get())
    }
    single<RemoteEcellDataSource>(DataSourceQualifiers.KTOR) {
        KtorRemoteEcellDataSource()
    }
    single<RemoteEcellDataSource>(DataSourceQualifiers.HYBRID) {
        HybridRemoteEcellDataSource(
            firebaseSource = get(DataSourceQualifiers.FIREBASE),
            ktorSource = get(DataSourceQualifiers.KTOR)
        )
    }

    // Use AppConfig to select implementation dynamically
    single<RemoteEcellDataSource> {
        when (AppConfig.CURRENT_DATA_SOURCE) {
            AppConfig.DataSourceType.FIREBASE -> get(DataSourceQualifiers.FIREBASE)
            AppConfig.DataSourceType.KTOR_API -> get(DataSourceQualifiers.KTOR)
            AppConfig.DataSourceType.HYBRID -> get(DataSourceQualifiers.HYBRID)
        }
    }

    singleOf(::DefaultEcellRepository).bind<EcellRepository>()

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build()
    }
    single { get<EcellAccountsDatabase>().ecellAccountsDao }

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::TeamSharedViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::TeamDetailViewModel)
}