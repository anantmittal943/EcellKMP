package com.anantmittal.ecellkmp.data.mappers

import com.anantmittal.ecellkmp.data.database.EcellAccountsEntity
import com.anantmittal.ecellkmp.domain.models.AccountModel

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