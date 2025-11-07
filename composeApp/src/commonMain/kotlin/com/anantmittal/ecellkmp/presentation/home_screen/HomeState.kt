package com.anantmittal.ecellkmp.presentation.home_screen

import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.DomainModel
import com.anantmittal.ecellkmp.domain.models.EventsGlimpsesModel
import com.anantmittal.ecellkmp.utility.presentation.UiText

data class HomeState(
    val eventsGlimpses: List<EventsGlimpsesModel> = emptyList(),
    val domainsList: List<DomainModel> = emptyList(),
    val teamMembers: List<AccountModel> = listOf(
        AccountModel(
            id = "1",
            name = "name 1",
            email = "name@gmail.com",
            password = "password",
            kietLibId = "TODO()",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4"
        ),
        AccountModel(
            id = "2",
            name = "name 2",
            email = "name@gmail.com",
            password = "password",
            kietLibId = "TODO()",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4"
        ),
        AccountModel(
            id = "3",
            name = "name 3",
            email = "name@gmail.com",
            password = "password",
            kietLibId = "TODO()",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4"
        ),
        AccountModel(
            id = "4",
            name = "name 4",
            email = "name@gmail.com",
            password = "password",
            kietLibId = "TODO()",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4"
        ),
        AccountModel(
            id = "4",
            name = "name 4",
            email = "name@gmail.com",
            password = "password",
            kietLibId = "TODO()",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4"
        ),
        AccountModel(
            id = "4",
            name = "name 4",
            email = "name@gmail.com",
            password = "password",
            kietLibId = "TODO()",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4"
        ),
        AccountModel(
            id = "4",
            name = "name 4",
            email = "name@gmail.com",
            password = "password",
            kietLibId = "TODO()",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4"
        ),
        AccountModel(
            id = "4",
            name = "name 4",
            email = "name@gmail.com",
            password = "password",
            kietLibId = "TODO()",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4"
        ),
    ),
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)
