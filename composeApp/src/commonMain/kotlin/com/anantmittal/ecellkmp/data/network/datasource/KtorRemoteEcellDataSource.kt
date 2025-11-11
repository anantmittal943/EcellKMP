package com.anantmittal.ecellkmp.data.network.datasource

import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result

class KtorRemoteEcellDataSource : RemoteEcellDataSource {
    override suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
        TODO("Not yet implemented")
    }
}