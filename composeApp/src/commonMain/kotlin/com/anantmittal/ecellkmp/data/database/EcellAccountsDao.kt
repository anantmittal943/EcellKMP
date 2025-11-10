package com.anantmittal.ecellkmp.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface EcellAccountsDao {
    @Upsert
    suspend fun upsert(account: EcellAccountsEntity)

    @Update
    suspend fun update(account: EcellAccountsEntity)

//    @Query("SELECT * FROM EcellAccountsEntity LIMIT 1")
//    fun getAccountDetail() : Flow<EcellAccountsEntity>

    @Query("SELECT * FROM EcellAccountsEntity WHERE email = :email")
    suspend fun getAccountById(email: String): EcellAccountsEntity?

    @Query("DELETE FROM EcellAccountsEntity WHERE email = :email")
    suspend fun logout(email: String)
}