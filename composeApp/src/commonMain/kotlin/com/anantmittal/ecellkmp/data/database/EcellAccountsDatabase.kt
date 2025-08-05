package com.anantmittal.ecellkmp.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EcellAccountsEntity::class],
    version = 1
)
@ConstructedBy(EcellAccountsDatabaseConstructor::class)
abstract class EcellAccountsDatabase : RoomDatabase() {
    abstract val ecellAccountsDao: EcellAccountsDao

    companion object {
        const val DB_NAME = "EcellAccounts.db"
    }
}