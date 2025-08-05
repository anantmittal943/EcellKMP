package com.anantmittal.ecellkmp.domain.models

data class AccountModel(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val kietLibId: String,
    val phoneNumber: Int? = null
)
