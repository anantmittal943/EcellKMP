package com.anantmittal.ecellkmp.data.repository

import androidx.sqlite.SQLiteException
import com.anantmittal.ecellkmp.data.database.EcellAccountsDao
import com.anantmittal.ecellkmp.data.mappers.toAccountEntity
import com.anantmittal.ecellkmp.data.mappers.toAccountModel
import com.anantmittal.ecellkmp.data.network.authentication.EcellAuthSource
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import kotlinx.coroutines.flow.Flow

class DefaultEcellRepository(
    private val ecellAuthSource: EcellAuthSource,
    private val ecellAccountsDao: EcellAccountsDao
) : EcellRepository {
    override val currentUser: Flow<User?>
        get() = ecellAuthSource.currentUser

    override suspend fun login(loginModel: LoginModel): EmptyResult<DataError.Remote> {
        return ecellAuthSource.login(loginModel)
    }

    override suspend fun signup(signupModel: SignupModel): EmptyResult<DataError.Remote> {
        return ecellAuthSource.signup(signupModel)
    }

    override suspend fun loggedIn(accountModel: AccountModel): EmptyResult<DataError.Local> {
        return try {
            ecellAccountsDao.upsert(accountModel.toAccountEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            e.printStackTrace()
            Result.Error(DataError.Local.DISK_FULL)
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