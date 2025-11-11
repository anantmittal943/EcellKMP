package com.anantmittal.ecellkmp.data.network.datasource

import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import com.anantmittal.ecellkmp.utility.domain.Variables
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException

class FirebaseRemoteEcellDataSource(
    private val firebaseFirestore: FirebaseFirestore
) : RemoteEcellDataSource {
    override suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote> {
        try {
            AppLogger.d(Variables.TAG, "Firestore: Creating account DB for email: ${accountDTO.email}, library_id: ${accountDTO.library_id}")
            val emailQuery = firebaseFirestore.collection(Variables.TEAM_MEMBERS_TAG).where { "email" equalTo accountDTO.email }.get()
            AppLogger.d(Variables.TAG, "Firestore: Email query returned ${emailQuery.documents.size} documents")

            val kietLibIdQuery = firebaseFirestore.collection(Variables.TEAM_MEMBERS_TAG).where { "library_id" equalTo accountDTO.library_id }.get()
            AppLogger.d(Variables.TAG, "Firestore: Library ID query returned ${kietLibIdQuery.documents.size} documents")

            when {
                emailQuery.documents.isNotEmpty() -> {
                    AppLogger.e(Variables.TAG, "Firestore: Email already exists in database: ${accountDTO.email}")
                    return Result.Error(DataError.Remote.UNKNOWN)
                }

                kietLibIdQuery.documents.isNotEmpty() -> {
                    AppLogger.e(Variables.TAG, "Firestore: Library ID already exists in database: ${accountDTO.library_id}")
                    return Result.Error(DataError.Remote.UNKNOWN)
                }

                else -> {
                    val docName = accountDTO.name.replace(" ", "_") + "_" + accountDTO.id
                    AppLogger.d(Variables.TAG, "Firestore: Creating document with name: $docName")
                    firebaseFirestore.collection(Variables.TEAM_MEMBERS_TAG).document(docName).set(accountDTO)
                    AppLogger.d(Variables.TAG, "Firestore: Account DB created successfully for: ${accountDTO.email}")
                    return Result.Success(Unit)
                }
            }
        } catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "Firestore: FirebaseFirestoreException creating account DB for ${accountDTO.email}: ${e.message}")
            return Result.Error(DataError.Remote.UNKNOWN)
        } catch (e: Exception) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "Firestore: Exception creating account DB for ${accountDTO.email}: ${e.message}")
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        return try {
            AppLogger.d(Variables.TAG, "Firestore: Getting account DB for email: $email")
            val querySnapshot = firebaseFirestore.collection(Variables.TEAM_MEMBERS_TAG)
                .where { "email" equalTo email }
                .get()

            AppLogger.d(Variables.TAG, "Firestore: Query returned ${querySnapshot.documents.size} documents")

            if (querySnapshot.documents.isEmpty()) {
                AppLogger.e(Variables.TAG, "Firestore: No account found with email: $email")
                Result.Error(DataError.Remote.UNKNOWN)
            } else {
                val document = querySnapshot.documents.first()
                AppLogger.d(Variables.TAG, "Firestore: Document ID: ${document.id}")
                val accountDTO = document.data<AccountDTO>()
                AppLogger.d(Variables.TAG, "Firestore: Account found - email: ${accountDTO.email}, name: ${accountDTO.name}, id: ${accountDTO.id}")
                Result.Success(accountDTO)
            }
        } catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "Firestore: Error getting account for $email: ${e.message}")
            Result.Error(DataError.Remote.UNKNOWN)
        } catch (e: Exception) {
            e.printStackTrace()
            AppLogger.e(Variables.TAG, "Firestore: Unexpected error getting account for $email: ${e.message}")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getTeamMembers(): Result<List<AccountDTO>, DataError.Remote> {
        TODO("Not yet implemented")
    }
}