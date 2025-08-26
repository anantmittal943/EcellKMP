package com.anantmittal.ecellkmp.data.dto

import com.anantmittal.ecellkmp.utility.domain.Variables
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class AccountDTO(
    val id: String,
    val name: String = "",
    val email: String = "",
    val library_id: String = "",
    val password: String? = "",
    val profile_pic: String = "",
    val access_type: String = Variables.USER_ACCESS,
    val account_type: String = Variables.USER_ACCOUNT,
    val linkedin_url: String = "",
    val phone_number: String = "",
    val portfolio_url: String = "",
    val instagram_url: String = "",
    val position: String = "",
    val status: String = Variables.STATUS_PENDING,
    val university_roll_no: String = "",
    val kiet_email: String = "",
    val accommodation_type: String = "",
    val city: String = "",
    val domain: String = "",
    val year: String = "",
    val dob: Timestamp = Timestamp.now(),
    val fcm_token: String = "",
    val created_on: Timestamp = Timestamp.now(),
)
