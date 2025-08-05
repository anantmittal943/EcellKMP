package com.anantmittal.ecellkmp.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object NavGraph : Route

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Signup : Route

    @Serializable
    data object Home : Route

}