package com.anantmittal.ecellkmp.data.network.datasource

import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result

interface RemoteEcellDataSource {
    suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote>
    suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote>
    suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote>
}