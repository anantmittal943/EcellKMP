package com.anantmittal.ecellkmp.app

import kotlinx.serialization.Serializable

sealed interface Route {

    val path: String
        get() = this::class.qualifiedName ?: ""

    @Serializable
    data object AuthNavGraph : Route

    @Serializable
    data object NormalNavGraph : Route

    @Serializable
    data object TeamNavGraph : Route

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Signup : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object Explore : Route

    @Serializable
    data object Meetings : Route

    @Serializable
    data object CreateMeeting : Route

    @Serializable
    data object Account : Route

    @Serializable
    data object ViewProfile : Route

}