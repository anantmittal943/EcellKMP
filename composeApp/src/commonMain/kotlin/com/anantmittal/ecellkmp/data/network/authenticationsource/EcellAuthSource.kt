package com.anantmittal.ecellkmp.data.network.authenticationsource

import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import kotlinx.coroutines.flow.Flow

interface EcellAuthSource {
    val currentUser: Flow<User?>
    suspend fun login(loginModel: LoginModel): EmptyResult<DataError.Remote>
    suspend fun signup(signupModel: SignupModel): EmptyResult<DataError.Remote>
    suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote>
    suspend fun getAccountDb(uid: String): Result<AccountDTO, DataError.Remote>
    suspend fun signOut()
}