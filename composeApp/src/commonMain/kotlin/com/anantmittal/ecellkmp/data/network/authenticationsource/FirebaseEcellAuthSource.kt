package com.anantmittal.ecellkmp.data.network.authenticationsource

import com.anantmittal.ecellkmp.data.mappers.toUser
import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseEcellAuthSource(
    private val firebaseAuth: FirebaseAuth
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

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

}