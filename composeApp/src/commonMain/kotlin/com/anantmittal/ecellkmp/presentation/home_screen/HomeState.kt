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
            name = "Anant Mittal",
            email = "anantmittal943@gmail.com",
            password = "password",
            kietLibId = "2428CS2113",
            branch = "CS",
            phoneNumber = "9259386995",
            profilePic = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4",
            accessType = "ADMIN",
            accountType = "Team",
            portfolioUrl = "https://firebasestorage.googleapis.com/v0/b/e-cell-main-app-68d8c.appspot.com/o/anantmittal943%40gmail.com.jpg?alt=media&token=65d7784b-4352-403d-8da4-0ebab89c22a4",
            linkedinUrl = "https://www.linkedin.com/in/anantmittal943/",
            instagramUrl = "https://www.instagram.com/anantmittal943/#",
            designation = "Tech Member",
            status = "VERIFIED",
            universityRollNumber = "202401100100041",
            kietEmail = "anant.2428cs2113@kiet.edu",
            accommodationType = "Day Scholar",
            city = "Meerut",
            domain = "Technical",
            year = "2",
            shirtSize = "M"
        ),
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
