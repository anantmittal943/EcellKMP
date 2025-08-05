package com.anantmittal.ecellkmp.data.repository

import androidx.sqlite.SQLiteException
import com.anantmittal.ecellkmp.data.database.EcellAccountsDao
import com.anantmittal.ecellkmp.data.mappers.toAccountEntity
import com.anantmittal.ecellkmp.data.mappers.toAccountModel
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.repository.EcellRepository
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result

class DefaultEcellRepository(
    private val ecellAccountsDao: EcellAccountsDao
) : EcellRepository {
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
    }
}