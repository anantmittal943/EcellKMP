package com.anantmittal.ecellkmp.data.mappers

import com.anantmittal.ecellkmp.data.database.EcellAccountsEntity
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.User
import dev.gitlive.firebase.auth.FirebaseUser

fun AccountModel.toAccountEntity() : EcellAccountsEntity {
    return EcellAccountsEntity(
        id = id,
        name = name,
        email = email,
        password = password,
        kietLibId = kietLibId,
        phoneNumber = phoneNumber
    )
}

fun EcellAccountsEntity.toAccountModel() : AccountModel {
    return AccountModel(
        id = id,
        name = name,
        email = email,
        password = password,
        kietLibId = kietLibId,
        phoneNumber = phoneNumber
    )
}

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid
    )
}