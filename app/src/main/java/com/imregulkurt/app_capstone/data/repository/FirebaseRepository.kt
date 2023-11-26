package com.imregulkurt.app_capstone.data.repository

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.imregulkurt.app_capstone.common.Resource
import com.google.firebase.auth.FirebaseAuth
import com.imregulkurt.app_capstone.MainApplication
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun getUserId(): String {
        return firebaseAuth.currentUser?.uid.orEmpty()
    }

    fun getUserEmail(): String {
        return firebaseAuth.currentUser?.email.orEmpty()
    }

    // Async method
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<Unit> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {

                // TODO: temporary for debug, can be removed
                val message = "Signed in with email: $email and password $password"
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(MainApplication.appContext.applicationContext, message, Toast.LENGTH_SHORT).show()
                }
                Resource.Success(Unit)
            } else {
                Resource.Error("Something went wrong!")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    // Async method
    suspend fun signInAnonymously(): Resource<Unit> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            if (result.user != null) {

                // TODO: temporary for debug, can be removed
                val message = "Signed in with user id: ${firebaseAuth.uid}"
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(MainApplication.appContext.applicationContext, message, Toast.LENGTH_SHORT).show()
                }
                Resource.Success(Unit)
            } else {
                Resource.Error("Something went wrong!")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    // Async method
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Resource<Unit> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) {

                // TODO: temporary for debug, can be removed
                val message = "Signed up with email: $email and password $password"
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(MainApplication.appContext.applicationContext, message, Toast.LENGTH_SHORT).show()
                }
                Resource.Success(Unit)
            } else {
                Resource.Error("Something went wrong!")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
    }
}