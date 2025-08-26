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
import com.anantmittal.ecellkmp.utility.domain.onError
import com.anantmittal.ecellkmp.utility.domain.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DefaultEcellRepository(
    private val ecellAuthSource: EcellAuthSource,
    private val ecellAccountsDao: EcellAccountsDao
) : EcellRepository {
    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override val currentUser: Flow<User?>
        get() = ecellAuthSource.currentUser

    override suspend fun login(loginModel: LoginModel): EmptyResult<DataError.Remote> {
        return ecellAuthSource.login(loginModel).onSuccess {
//            loadAccountRemotely()
            // TODO: Setup cacheLoggedInAccount method for caching account data to room db local
//            cacheLoggedInAccount(
//                AccountModel(
//                    id = currentUser.collect { it?.uid }.toString(),
//                    name = signupModel.name,
//                    email = signupModel.email,
//                    password = signupModel.cnfmPassword,
//                    kietLibId = signupModel.kietLibId
//                )
//            )
        }
            .onError {
                // TODO: Tell user about error
            }
    }

    override suspend fun signup(signupModel: SignupModel): EmptyResult<DataError.Remote> {
        return ecellAuthSource.signup(signupModel).onSuccess {
            val user = currentUser.first()
            val uid = user?.uid
            val accountToCreate = signupModel.toAccountModel(uid ?: "error_uid_not_found")

            repoScope.launch {
                createAccountDbRemotely(accountToCreate)
                cacheLoggedInAccount(accountToCreate)
            }
        }
    }

    override suspend fun createAccountDbRemotely(accountModel: AccountModel): EmptyResult<DataError.Remote> {
        return try {
            ecellAuthSource.createAccountDb(accountModel.toAccountDTO())
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            AppLogger.d(Variables.TAG, "Error in createAccountDbRemotely: $e")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun loadAccountRemotely(uid: String): Result<AccountModel, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun cacheLoggedInAccount(accountModel: AccountModel): EmptyResult<DataError.Local> {
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

    override suspend fun loadAccountLocally(uid: String): Result<AccountModel, DataError.Local> {
        return try {
            Result.Success(ecellAccountsDao.getAccountById(uid)!!.toAccountModel())
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

    override suspend fun cacheEvents(): EmptyResult<DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun loadEventsLocally(): Result<List<EventsGlimpsesModel>, DataError.Local> {
        TODO("Not yet implemented")
    }

    override suspend fun loadDomainsRemotely(): Result<List<DomainModel>, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun cacheDomains(): EmptyResult<DataError.Local> {
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
