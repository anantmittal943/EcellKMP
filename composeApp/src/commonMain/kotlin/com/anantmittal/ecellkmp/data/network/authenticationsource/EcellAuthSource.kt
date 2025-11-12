package com.anantmittal.ecellkmp.data.network.authenticationsource

import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import kotlinx.coroutines.flow.Flow

interface EcellAuthSource {
    val currentUser: Flow<User?>
    suspend fun login(loginModel: LoginModel): EmptyResult<DataError.Remote>
    suspend fun signup(signupModel: SignupModel): EmptyResult<DataError.Remote>
    suspend fun signOut()
}