package com.anantmittal.ecellkmp.domain.models

import dev.gitlive.firebase.firestore.Timestamp

data class AccountModel(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val kietLibId: String,
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
    val dob: Timestamp = Timestamp.now(),
    val shirtSize: String = "",
    val createdOn: Timestamp = Timestamp.now(),
)
