package com.anantmittal.ecellkmp.data.repository

import androidx.sqlite.SQLiteException
import com.anantmittal.ecellkmp.data.database.EcellAccountsDao
import com.anantmittal.ecellkmp.data.mappers.toAccountDTO
import com.anantmittal.ecellkmp.data.mappers.toAccountEntity
import com.anantmittal.ecellkmp.data.mappers.toAccountModel
import com.anantmittal.ecellkmp.data.network.authenticationsource.EcellAuthSource
import com.anantmittal.ecellkmp.data.network.datasource.RemoteEcellDataSource
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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DefaultEcellRepository(
    private val ecellAuthSource: EcellAuthSource,
    private val remoteEcellDataSource: RemoteEcellDataSource,
    private val ecellAccountsDao: EcellAccountsDao
) : EcellRepository {
    override val currentUser: Flow<User?>
        get() = ecellAuthSource.currentUser


    override suspend fun login(loginModel: LoginModel): Result<AccountModel, DataError.Remote> {
        AppLogger.d(Variables.TAG, "Starting login for email: ${loginModel.email}")

        return withContext(NonCancellable) {
            when (val authResult = ecellAuthSource.login(loginModel)) {
                is Result.Success -> {
                    AppLogger.d(Variables.TAG, "Login auth successful, loading account...")
                    when (val accountResult = loadAccount(loginModel.email)) {
                        is Result.Success -> {
                            AppLogger.d(Variables.TAG, "Login completed successfully for: ${accountResult.data.name}")
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
    }

    override suspend fun signup(signupModel: SignupModel): Result<AccountModel, DataError.Remote> {
        AppLogger.d(Variables.TAG, "Starting signup for email: ${signupModel.email}, name: ${signupModel.name}")

        return withContext(NonCancellable) {
            when (val authResult = ecellAuthSource.signup(signupModel)) {
                is Result.Success -> {
                    AppLogger.d(Variables.TAG, "Signup auth successful, getting current user...")
                    val user = currentUser.first()
                    val uid = user?.uid
                    AppLogger.d(Variables.TAG, "Current user UID: $uid")
                    val accountToCreate = signupModel.toAccountModel(uid ?: "error_uid_not_found")

                    when (val createResult = createAccountDbRemotely(accountToCreate)) {
                        is Result.Success -> {
                            AppLogger.d(Variables.TAG, "Account DB created remotely, loading account...")
                            when (val loadResult = loadAccount(signupModel.email)) {
                                is Result.Success -> {
                                    AppLogger.d(Variables.TAG, "Signup completed successfully for: ${loadResult.data.name}")
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
    }

    override suspend fun loadAccount(email: String): Result<AccountModel, DataError.Remote> {
        AppLogger.d(Variables.TAG, "loadAccount: Starting for email: $email")

        when (val localResult = loadAccountLocally(email)) {
            is Result.Success -> {
                AppLogger.d(Variables.TAG, "loadAccount: Found in local cache: ${localResult.data.name}")
                return Result.Success(localResult.data)
            }

            is Result.Error -> {
                AppLogger.d(Variables.TAG, "loadAccount: Not in local cache (${localResult.error}), fetching from remote...")
            }
        }

        when (val remoteResult = loadAccountRemotely(email)) {
            is Result.Success -> {
                AppLogger.d(Variables.TAG, "loadAccount: Successfully loaded and cached from remote")
                return Result.Success(remoteResult.data)
            }

            is Result.Error -> {
                AppLogger.e(Variables.TAG, "loadAccount: Failed to load from remote: ${remoteResult.error}")
                return Result.Error(remoteResult.error)
            }
        }
    }

    private suspend fun createAccountDbRemotely(accountModel: AccountModel): EmptyResult<DataError.Remote> {
        return try {
            AppLogger.d(Variables.TAG, "Creating account DB remotely for: ${accountModel.email}")
            remoteEcellDataSource.createAccountDb(accountModel.toAccountDTO())
            AppLogger.d(Variables.TAG, "Account DB created successfully for: ${accountModel.email}")
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "Error in createAccountDbRemotely for ${accountModel.email}: ${e.message}")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    private suspend fun loadAccountRemotely(email: String): Result<AccountModel, DataError.Remote> {
        AppLogger.d(Variables.TAG, "Loading account remotely for email: $email")
        return when (val result = remoteEcellDataSource.getAccountDb(email)) {
            is Result.Success -> {
                AppLogger.d(Variables.TAG, "Account DTO retrieved from remote: ${result.data}")
                val accountModel = result.data.toAccountModel()
                AppLogger.d(Variables.TAG, "Account DTO converted to model: ${accountModel.email}, name: ${accountModel.name}")

                AppLogger.d(Variables.TAG, "Caching account locally...")
                when (val cacheResult = cacheLoggedInAccount(accountModel)) {
                    is Result.Success -> {
                        AppLogger.d(Variables.TAG, "Account cached successfully")
                    }

                    is Result.Error -> {
                        AppLogger.e(Variables.TAG, "Failed to cache account: ${cacheResult.error}")
                    }
                }

                AppLogger.d(Variables.TAG, "Account loaded remotely successfully: ${accountModel.email}")
                Result.Success(accountModel)
            }

            is Result.Error -> {
                AppLogger.e(Variables.TAG, "Error loading account remotely for $email: ${result.error}")
                Result.Error(result.error)
            }
        }
    }

    private suspend fun cacheLoggedInAccount(accountModel: AccountModel): EmptyResult<DataError.Local> {
        return try {
            AppLogger.d(Variables.TAG, "Attempting to cache account: ${accountModel.email}")
            val entity = accountModel.toAccountEntity()
            AppLogger.d(Variables.TAG, "Account entity created: ${entity.email}")
            ecellAccountsDao.upsert(entity)
            AppLogger.d(Variables.TAG, "Account upserted successfully: ${accountModel.email}")
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "SQLiteException in cacheLoggedInAccount for ${accountModel.email}: ${e.message}")
            Result.Error(DataError.Local.UNKNOWN)
        } catch (e: Exception) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "Exception in cacheLoggedInAccount for ${accountModel.email}: ${e.message}")
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    private suspend fun loadAccountLocally(email: String): Result<AccountModel, DataError.Local> {
        return try {
            AppLogger.d(Variables.TAG, "Loading account locally for email: $email")
            val entity = ecellAccountsDao.getAccountById(email)
            AppLogger.d(Variables.TAG, "Account entity retrieved: ${entity?.email ?: "NULL"}")

            if (entity == null) {
                AppLogger.e(Variables.TAG, "Account entity is NULL for email: $email")
                Result.Error(DataError.Local.NULL_RESULT)
            } else {
                val accountModel = entity.toAccountModel()
                AppLogger.d(Variables.TAG, "Account loaded locally successfully: ${accountModel.email}, name: ${accountModel.name}")
                Result.Success(accountModel)
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "SQLiteException in loadAccountLocally for $email: ${e.message}")
            Result.Error(DataError.Local.UNKNOWN)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "NullPointerException in loadAccountLocally for $email: ${e.message}")
            Result.Error(DataError.Local.NULL_RESULT)
        } catch (e: Exception) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "Exception in loadAccountLocally for $email: ${e.message}")
            Result.Error(DataError.Local.UNKNOWN)
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

    override suspend fun logout(email: String) {
        ecellAccountsDao.logout(email)
        ecellAuthSource.signOut()
    }
}
