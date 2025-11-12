package com.anantmittal.ecellkmp.data.mappers

import com.anantmittal.ecellkmp.data.database.EcellAccountsEntity
import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import dev.gitlive.firebase.auth.FirebaseUser

fun AccountModel.toAccountEntity(): EcellAccountsEntity {
    AppLogger.d(AppConfig.TAG, "Mapper: Converting AccountModel to Entity - email: $email, name: $name")
    return EcellAccountsEntity(
        id = id,
        name = name,
        email = email,
        password = password,
        kietLibId = kietLibId,
        branch = branch,
        phoneNumber = phoneNumber,
        profilePic = profilePic,
        accessType = accessType,
        accountType = accountType,
        portfolioUrl = portfolioUrl,
        linkedinUrl = linkedinUrl,
        instagramUrl = instagramUrl,
        designation = designation,
        status = status,
        universityRollNumber = universityRollNumber,
        kietEmail = kietEmail,
        accommodationType = accommodationType,
        city = city,
        domain = domain,
        year = year,
        dob = dob.seconds,
        shirtSize = shirtSize,
        createdOn = createdOn.seconds
    )
}

fun EcellAccountsEntity.toAccountModel(): AccountModel {
    AppLogger.d(AppConfig.TAG, "Mapper: Converting Entity to AccountModel - email: $email, name: $name")
    return AccountModel(
        id = id,
        name = name,
        email = email,
        password = password,
        kietLibId = kietLibId,
        branch = branch,
        phoneNumber = phoneNumber,
        profilePic = profilePic,
        accessType = accessType,
        accountType = accountType,
        portfolioUrl = portfolioUrl,
        linkedinUrl = linkedinUrl,
        instagramUrl = instagramUrl,
        designation = designation,
        status = status,
        universityRollNumber = universityRollNumber,
        kietEmail = kietEmail,
        accommodationType = accommodationType,
        city = city,
        domain = domain,
        year = year,
        dob = dev.gitlive.firebase.firestore.Timestamp(dob, 0),
        shirtSize = shirtSize,
        createdOn = dev.gitlive.firebase.firestore.Timestamp(createdOn, 0)
    )
}

fun AccountDTO.toAccountModel(): AccountModel {
    AppLogger.d(AppConfig.TAG, "Mapper: Converting DTO to AccountModel - email: $email, name: $name, id: $id")
    return AccountModel(
        id = id,
        name = name,
        email = email,
        password = password!!,
        kietLibId = library_id,
        branch = "",
        phoneNumber = phone_number,
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
        phone_number = phoneNumber,
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