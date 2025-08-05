package com.anantmittal.ecellkmp.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<EcellAccountsDatabase>
}