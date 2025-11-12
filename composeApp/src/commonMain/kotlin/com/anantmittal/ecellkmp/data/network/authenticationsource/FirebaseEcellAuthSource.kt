package com.anantmittal.ecellkmp.data.network.authenticationsource

import com.anantmittal.ecellkmp.data.mappers.toUser
import com.anantmittal.ecellkmp.domain.models.LoginModel
import com.anantmittal.ecellkmp.domain.models.SignupModel
import com.anantmittal.ecellkmp.domain.models.User
import com.anantmittal.ecellkmp.utility.domain.AppConfig
import com.anantmittal.ecellkmp.utility.domain.AppLogger
import com.anantmittal.ecellkmp.utility.domain.DataError
import com.anantmittal.ecellkmp.utility.domain.EmptyResult
import com.anantmittal.ecellkmp.utility.domain.Result
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseEcellAuthSource(
    private val firebaseAuth: FirebaseAuth,
//    private val firebaseFirestore: FirebaseFirestore
) : EcellAuthSource {
    override val currentUser: Flow<User?>
        get() = firebaseAuth.authStateChanged.map { firebaseUser ->
            firebaseUser?.toUser()
        }

    override suspend fun login(loginModel: LoginModel): EmptyResult<DataError.Remote> {
        return try {
            AppLogger.d(AppConfig.TAG, "FirebaseAuth: Attempting login for email: ${loginModel.email}")
            firebaseAuth.signInWithEmailAndPassword(
                email = loginModel.email,
                password = loginModel.password
            )
            AppLogger.d(AppConfig.TAG, "FirebaseAuth: Login successful for email: ${loginModel.email}")
            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()
            AppLogger.e(AppConfig.TAG, "FirebaseAuth: Login failed for ${loginModel.email}: ${e.message}")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun signup(signupModel: SignupModel): EmptyResult<DataError.Remote> {
        return try {
            AppLogger.d(AppConfig.TAG, "FirebaseAuth: Attempting signup for email: ${signupModel.email}")
            firebaseAuth.createUserWithEmailAndPassword(
                email = signupModel.email,
                password = signupModel.cnfmPassword
            )
            AppLogger.d(AppConfig.TAG, "FirebaseAuth: Signup successful for email: ${signupModel.email}")
            Result.Success(Unit)
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()
            AppLogger.e(AppConfig.TAG, "FirebaseAuth: Signup failed for ${signupModel.email}: ${e.message}")
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }
}