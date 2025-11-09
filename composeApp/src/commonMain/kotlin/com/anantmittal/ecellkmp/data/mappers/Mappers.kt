package com.anantmittal.ecellkmp.data.mappers

import com.anantmittal.ecellkmp.data.database.EcellAccountsEntity
import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import dev.gitlive.firebase.auth.FirebaseUser

fun AccountModel.toAccountEntity(): EcellAccountsEntity {
    return EcellAccountsEntity(
        id = id,
        name = name,
        email = email,
        password = password,
        kietLibId = kietLibId,
        phoneNumber = phoneNumber
    )
}

fun EcellAccountsEntity.toAccountModel(): AccountModel {
    return AccountModel(
        id = id,
        name = name,
        email = email,
        password = password,
        kietLibId = kietLibId,
        phoneNumber = phoneNumber
    )
}

fun AccountDTO.toAccountModel(): AccountModel {
    return AccountModel(
        id = id,
        name = name,
        email = email,
        password = password!!,
        kietLibId = library_id,
        branch = "",
        phoneNumber = phone_number.toInt(),
        profilePic = profile_pic,
        accessType = access_type,
        accountType = account_type,
        portfolioUrl = portfolio_url,
        linkedinUrl = linkedin_url,
        instagramUrl = instagram_url,
        designation = position,
        status = status,
        universityRollNumber = university_roll_no,
        kietEmail = kiet_email,
        accommodationType = accommodation_type,
        city = city,
        domain = domain,
        year = year,
        dob = dob,
        shirtSize = "",
        createdOn = created_on
    )
}

fun AccountModel.toAccountDTO(): AccountDTO {
    return AccountDTO(
        id = id,
        name = name,
        email = email,
        library_id = kietLibId,
        password = password,
        profile_pic = profilePic,
        access_type = accessType,
        account_type = accountType,
        linkedin_url = linkedinUrl,
        phone_number = phoneNumber.toString(),
        portfolio_url = portfolioUrl,
        instagram_url = instagramUrl,
        position = designation,
        status = status,
        university_roll_no = universityRollNumber,
        kiet_email = kietEmail,
        accommodation_type = accommodationType,
        city = city,
        domain = domain,
        year = year,
        dob = dob,
        fcm_token = "",
        created_on = createdOn
    )
}

fun SignupModel.toAccountDTO(id: String): AccountDTO {
    return AccountDTO(
        id = id,
        name = name,
        email = email,
        password = cnfmPassword,
        library_id = kietLibId
    )
}

fun SignupModel.toAccountModel(id: String): AccountModel {
    return AccountModel(
        id = id,
        name = name,
        email = email,
        password = cnfmPassword,
        kietLibId = kietLibId
    )
}


fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        email = email!!
    )
}