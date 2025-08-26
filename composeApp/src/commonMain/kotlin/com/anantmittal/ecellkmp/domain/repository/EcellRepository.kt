package com.anantmittal.ecellkmp.domain.repository

import com.anantmittal.ecellkmp.domain.models.AccountModel
import com.anantmittal.ecellkmp.domain.models.DomainModel
import com.anantmittal.ecellkmp.domain.models.EventsGlimpsesModel
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
    suspend fun createAccountDbRemotely(accountModel: AccountModel): EmptyResult<DataError.Remote>
    suspend fun loadAccountRemotely(uid: String): Result<AccountModel, DataError.Remote>
    suspend fun cacheLoggedInAccount(accountModel: AccountModel): EmptyResult<DataError.Local>
    suspend fun loadAccountLocally(uid: String): Result<AccountModel, DataError.Local>
    suspend fun loadEventsRemotely(): Result<List<EventsGlimpsesModel>, DataError.Remote>
    suspend fun cacheEvents(): EmptyResult<DataError.Local>
    suspend fun loadEventsLocally(): Result<List<EventsGlimpsesModel>, DataError.Local>
    suspend fun loadDomainsRemotely(): Result<List<DomainModel>, DataError.Remote>
    suspend fun cacheDomains(): EmptyResult<DataError.Local>
    suspend fun loadDomainsLocally(): Result<List<DomainModel>, DataError.Local>
    suspend fun loadTeamAccountsRemotely(): Result<List<AccountModel>, DataError.Remote>
    suspend fun editDetails(accountModel: AccountModel): EmptyResult<DataError.Local>
    suspend fun logout(uid: String)
}