package com.anantmittal.ecellkmp.data.repository

import androidx.sqlite.SQLiteException
import com.anantmittal.ecellkmp.data.database.EcellAccountsDao
import com.anantmittal.ecellkmp.data.mappers.toAccountDTO
import com.anantmittal.ecellkmp.data.mappers.toAccountEntity
import com.anantmittal.ecellkmp.data.mappers.toAccountModel
import com.anantmittal.ecellkmp.data.network.authenticationsource.EcellAuthSource
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.DomainModel
import com.anantmittal.ecellkmp.domain.models.EventsGlimpsesModel
import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import com.anantmittal.ecellkmp.utility.domain.Variables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DefaultEcellRepository(
    private val ecellAuthSource: EcellAuthSource, private val ecellAccountsDao: EcellAccountsDao
) : EcellRepository {
    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override val currentUser: Flow<User?>
        get() = ecellAuthSource.currentUser

    override suspend fun login(loginModel: LoginModel): Result<AccountModel, DataError.Remote> {
        return when (val authResult = ecellAuthSource.login(loginModel)) {
            is Result.Success -> {
                when (val accountResult = loadAccountRemotely(loginModel.email)) {
                    is Result.Success -> {
                        Result.Success(accountResult.data)
                    }

                    is Result.Error -> {
                        AppLogger.e(Variables.TAG, "Login auth succeeded but failed to load account: ${accountResult.error}")
                        Result.Error(accountResult.error)
                    }
                }
            }

            is Result.Error -> {
                AppLogger.e(Variables.TAG, "Login authentication failed: ${authResult.error}")
                Result.Error(authResult.error)
            }
        }
    }

    override suspend fun signup(signupModel: SignupModel): Result<AccountModel, DataError.Remote> {
        return when (val authResult = ecellAuthSource.signup(signupModel)) {
            is Result.Success -> {
                val user = currentUser.first()
                val uid = user?.uid
                val accountToCreate = signupModel.toAccountModel(uid ?: "error_uid_not_found")

                when (val createResult = createAccountDbRemotely(accountToCreate)) {
                    is Result.Success -> {
                        when (val loadResult = loadAccountRemotely(signupModel.email)) {
                            is Result.Success -> {
                                Result.Success(loadResult.data)
                            }

                            is Result.Error -> {
                                AppLogger.e(Variables.TAG, "Signup succeeded but failed to load account: ${loadResult.error}")
                                Result.Error(loadResult.error)
                            }
                        }
                    }

                    is Result.Error -> {
                        AppLogger.e(Variables.TAG, "Signup succeeded but failed to create account DB: ${createResult.error}")
                        Result.Error(createResult.error)
                    }
                }
            }

            is Result.Error -> {
                AppLogger.e(Variables.TAG, "Signup authentication failed: ${authResult.error}")
                Result.Error(authResult.error)
            }
        }
    }

    private suspend fun createAccountDbRemotely(accountModel: AccountModel): EmptyResult<DataError.Remote> {
        return try {
            ecellAuthSource.createAccountDb(accountModel.toAccountDTO())
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "Error in createAccountDbRemotely: $e")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    private suspend fun loadAccountRemotely(email: String): Result<AccountModel, DataError.Remote> {
        return when (val result = ecellAuthSource.getAccountDb(email)) {
            is Result.Success -> {
                val accountModel = result.data.toAccountModel()
                repoScope.launch {
                    cacheLoggedInAccount(accountModel)
                }
                AppLogger.d(Variables.TAG, "Account loaded remotely: $accountModel")
                Result.Success(accountModel)
            }

            is Result.Error -> {
                AppLogger.e(Variables.TAG, "Error loading account remotely: ${result.error}")
                Result.Error(result.error)
            }
        }
    }

    private suspend fun cacheLoggedInAccount(accountModel: AccountModel): EmptyResult<DataError.Local> {
        return try {
            ecellAccountsDao.upsert(accountModel.toAccountEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            AppLogger.d(Variables.TAG, "Error in cacheLoggedInAccount: $e")
            Result.Error(DataError.Local.UNKNOWN)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun loadAccountLocally(email: String): Result<AccountModel, DataError.Local> {
        return try {
            Result.Success(ecellAccountsDao.getAccountById(email)!!.toAccountModel())
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Result.Error(DataError.Local.UNKNOWN)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Result.Error(DataError.Local.NULL)
        }
    }

    override suspend fun loadEventsRemotely(): Result<List<EventsGlimpsesModel>, DataError.Remote> {
        TODO("Not yet implemented")
    }

    private suspend fun cacheEvents(): EmptyResult<DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun loadEventsLocally(): Result<List<EventsGlimpsesModel>, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun loadDomainsRemotely(): Result<List<DomainModel>, DataError.Remote> {
        TODO("Not yet implemented")
    }

    private suspend fun cacheDomains(): EmptyResult<DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun loadDomainsLocally(): Result<List<DomainModel>, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun loadTeamAccountsRemotely(): Result<List<AccountModel>, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun editDetails(accountModel: AccountModel): EmptyResult<DataError.Local> {
        return try {
            ecellAccountsDao.update(accountModel.toAccountEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun logout(uid: String) {
        ecellAccountsDao.logout(uid)
        ecellAuthSource.signOut()
    }
}
