package com.anantmittal.ecellkmp.data.network.datasource

import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result

class HybridRemoteEcellDataSource(
    private val firebaseSource: RemoteEcellDataSource,
    private val ktorSource: RemoteEcellDataSource
) : RemoteEcellDataSource {

    override suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote> {
        AppLogger.d(AppConfig.TAG, "Hybrid: Using Firebase for createAccountDb")
        return firebaseSource.createAccountDb(accountDTO)
    }

    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        AppLogger.d(AppConfig.TAG, "Hybrid: Using Firebase for getAccountDb")
        return firebaseSource.getAccountDb(email)

        // return when (val result = firebaseSource.getAccountDb(email)) {
        //     is Result.Success -> {
        //         AppLogger.d(AppConfig.TAG, "Hybrid: Firebase getAccountDb succeeded")
        //         result
        //     }
        //     is Result.Error -> {
        //         AppLogger.w(AppConfig.TAG, "Hybrid: Firebase failed, trying API fallback")
        //         ktorSource.getAccountDb(email)
        //     }
        // }
    }

    override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
        return if (AppConfig.FeatureFlags.USE_API_FOR_TEAM_MEMBERS) {
            AppLogger.d(AppConfig.TAG, "Hybrid: Using API for getTeamMembers")

            when (val apiResult = ktorSource.getTeamMembers()) {
                is Result.Success -> {
                    AppLogger.d(AppConfig.TAG, "Hybrid: API getTeamMembers succeeded")
                    apiResult
                }

                is Result.Error -> {
                    AppLogger.w(AppConfig.TAG, "Hybrid: API failed, falling back to Firebase")
                    firebaseSource.getTeamMembers()
                }
            }
        } else {
            AppLogger.d(AppConfig.TAG, "Hybrid: Using Firebase for getTeamMembers")
            firebaseSource.getTeamMembers()
        }
    }
}

