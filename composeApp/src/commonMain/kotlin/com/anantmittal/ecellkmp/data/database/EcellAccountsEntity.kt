package com.anantmittal.ecellkmp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EcellAccountsEntity(
    @PrimaryKey(autoGenerate = false) val id: String = "",
    val name: String = "",
    val email: String,
    val password: String,
    val kietLibId: String = "",
    val phoneNumber: Int? = null
)
