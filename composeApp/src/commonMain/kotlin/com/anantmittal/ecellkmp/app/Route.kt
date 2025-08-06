package com.anantmittal.ecellkmp.app

import kotlinx.serialization.Serializable

sealed interface Route {

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

}