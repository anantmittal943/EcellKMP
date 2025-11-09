package com.anantmittal.ecellkmp.data.network.authenticationsource

import com.anantmittal.ecellkmp.data.dto.AccountDTO
import com.anantmittal.ecellkmp.data.mappers.toUser
import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import com.anantmittal.ecellkmp.utility.domain.Variables
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseEcellAuthSource(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : EcellAuthSource {
    override val currentUser: Flow<User?>
        get() = firebaseAuth.authStateChanged.map { firebaseUser ->
            firebaseUser?.toUser()
        }

    override suspend fun login(loginModel: LoginModel): EmptyResult<DataError.Remote> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(
                email = loginModel.email,
                password = loginModel.password
            )
            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun signup(signupModel: SignupModel): EmptyResult<DataError.Remote> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(
                email = signupModel.email,
                password = signupModel.cnfmPassword
            )
            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun createAccountDb(accountDTO: AccountDTO): EmptyResult<DataError.Remote> {
        return try {
            val emailQuery = firebaseFirestore.collection(Variables.TEAM_MEMBERS_TAG).where { "email" equalTo accountDTO.email }.get()
            val kietLibIdQuery = firebaseFirestore.collection(Variables.TEAM_MEMBERS_TAG).where { "library_id" equalTo accountDTO.library_id }.get()
            if (emailQuery.documents.isNotEmpty()) {
                AppLogger.d(Variables.TAG, "emailQuery.documents.isNotEmpty()")
                Result.Error(DataError.Remote.UNKNOWN)
            } else if (kietLibIdQuery.documents.isNotEmpty()) {
                Result.Error(DataError.Remote.UNKNOWN)
                AppLogger.d(Variables.TAG, "kietLibIdQuery.documents.isNotEmpty()")
            } else {
                firebaseFirestore.collection(Variables.TEAM_MEMBERS_TAG).document(
                    accountDTO.name.replace(" ", "_") + "_" + accountDTO.id
                ).set(accountDTO)
            }
            Result.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            AppLogger.d(Variables.TAG, "$e")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun getAccountDb(email: String): Result<AccountDTO, DataError.Remote> {
        return try {
            val querySnapshot = firebaseFirestore.collection(Variables.TEAM_MEMBERS_TAG)
                .where { "email" equalTo email }
                .get()

            if (querySnapshot.documents.isEmpty()) {
                AppLogger.d(Variables.TAG, "No account found with email: $email")
                Result.Error(DataError.Remote.UNKNOWN)
            } else {
                val document = querySnapshot.documents.first()
                val accountDTO = document.data<AccountDTO>()
                AppLogger.d(Variables.TAG, "Account found: ${document.id} => $accountDTO")
                Result.Success(accountDTO)
            }
        } catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            AppLogger.d(Variables.TAG, "Error getting account: $e")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}