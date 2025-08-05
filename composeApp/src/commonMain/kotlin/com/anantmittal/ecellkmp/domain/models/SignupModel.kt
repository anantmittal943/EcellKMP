package com.anantmittal.ecellkmp.domain.models

data class SignupModel(
    val name: String,
    val email: String,
    val kietLibId: String = "",
    val cnfmPassword: String
)