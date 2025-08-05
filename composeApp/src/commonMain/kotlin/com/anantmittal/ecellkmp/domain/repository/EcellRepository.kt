package com.anantmittal.ecellkmp.domain.repository

import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import kotlinx.coroutines.flow.Flow

interface EcellRepository {
    val currentUser: Flow<User?>
    suspend fun login(loginModel: LoginModel): EmptyResult<DataError.Remote>
    suspend fun signup(signupModel: SignupModel): EmptyResult<DataError.Remote>
    suspend fun loggedIn(accountModel: AccountModel): EmptyResult<DataError.Local>
    suspend fun loadAccountLocally(uid: String): Result<AccountModel, DataError.Local>
    suspend fun editDetails(accountModel: AccountModel): EmptyResult<DataError.Local>
    suspend fun logout(uid: String)
}