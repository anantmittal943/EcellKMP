package com.anantmittal.ecellkmp.data.network.datasource

import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result

/**
 * Hybrid implementation that intelligently uses Firebase or Ktor based on the operation.
 *
 * Strategy:
 * - Firebase for: Account creation/retrieval (auth-related operations)
 * - Ktor API for: Team members, events, other content (non-auth operations)
 *
 * This allows you to gradually migrate to API without breaking existing auth flows.
 */
class HybridRemoteEcellDataSource(
    private val firebaseSource: RemoteEcellDataSource,
    private val ktorSource: RemoteEcellDataSource
) : RemoteEcellDataSource {

    /**
     * Create account - Always use Firebase (auth-related)
     * This keeps account creation tied to Firebase Auth
     */
    override suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote> {
        AppLogger.d(AppConfig.TAG, "Hybrid: Using Firebase for createAccountDb")
        return firebaseSource.createAccountDb(accountDTO)
    }

    /**
     * Get account - Use Firebase (auth-related)
     * Can add fallback to API if needed
     */
    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        AppLogger.d(AppConfig.TAG, "Hybrid: Using Firebase for getAccountDb")

        // Option 1: Firebase only (current behavior)
        return firebaseSource.getAccountDb(email)

        // Option 2: Firebase with API fallback (uncomment to enable)
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

    /**
     * Get team members - Use API if available, fallback to Firebase
     * This demonstrates using API for content operations
     */
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

