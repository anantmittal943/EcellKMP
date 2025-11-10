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
    val branch: String = "",
    val phoneNumber: String = "",
    val profilePic: String = "",
    val accessType: String = "",
    val accountType: String = "",
    val portfolioUrl: String = "",
    val linkedinUrl: String = "",
    val instagramUrl: String = "",
    val designation: String = "",
    val status: String = "",
    val universityRollNumber: String = "",
    val kietEmail: String = "",
    val accommodationType: String = "",
    val city: String = "",
    val domain: String = "",
    val year: String = "",
    val dob: Long = 0L, // Store timestamp as Long
    val shirtSize: String = "",
    val createdOn: Long = 0L // Store timestamp as Long
)
