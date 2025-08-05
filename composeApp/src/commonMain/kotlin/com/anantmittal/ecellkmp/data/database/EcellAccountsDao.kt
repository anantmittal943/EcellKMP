package com.anantmittal.ecellkmp.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EcellAccountsDao {
    @Upsert
    suspend fun upsert(account: EcellAccountsEntity)

    @Update
    suspend fun update(account: EcellAccountsEntity)

//    @Query("SELECT * FROM EcellAccountsEntity LIMIT 1")
//    fun getAccountDetail() : Flow<EcellAccountsEntity>

    @Query("SELECT * FROM EcellAccountsEntity WHERE id = :uid")
    suspend fun getAccountById(uid: String): EcellAccountsEntity?

    @Query("DELETE FROM EcellAccountsEntity WHERE id = :uid")
    suspend fun logout(uid: String)
}