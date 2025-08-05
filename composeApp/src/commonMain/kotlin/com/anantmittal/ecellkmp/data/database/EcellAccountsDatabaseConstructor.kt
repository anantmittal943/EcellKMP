package com.anantmittal.ecellkmp.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object EcellAccountsDatabaseConstructor : RoomDatabaseConstructor<EcellAccountsDatabase> {
    override fun initialize(): EcellAccountsDatabase
}