package com.example.data

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), pass).await()
            val user = authResult.user ?: throw Exception("ব্যবহারকারী পাওয়া যায়নি")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    suspend fun createUserWithEmail(email: String, pass: String, fullName: String): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email.trim(), pass).await()
            val user = authResult.user ?: throw Exception("অ্যাকাউন্ট তৈরি ব্যর্থ হয়েছে")
            if (fullName.isNotBlank()) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName.trim())
                    .build()
                user.updateProfile(profileUpdates).await()
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email.trim()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    suspend fun signInWithGoogleCredential(idToken: String): Result<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user ?: throw Exception("গুগল সাইন-ইন ব্যর্থ হয়েছে")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception(mapFirebaseError(e)))
        }
    }

    suspend fun signOut(context: Context) {
        auth.signOut()
        try {
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (_: Exception) {
        }
    }

    private fun mapFirebaseError(e: Exception): String {
        return when (e) {
            is FirebaseAuthInvalidUserException -> "এই ইমেইল ঠিকানায় কোনো অ্যাকাউন্ট খুঁজে পাওয়া যায়নি।"
            is FirebaseAuthInvalidCredentialsException -> "ইমেইল বা পাসওয়ার্ড সঠিক নয়।"
            is FirebaseAuthUserCollisionException -> "এই ইমেইল দিয়ে ইতিমধ্যে একটি অ্যাকাউন্ট নিবন্ধিত আছে।"
            is FirebaseAuthWeakPasswordException -> "পাসওয়ার্ডটি দুর্বল। অনুগ্রহ করে কমপক্ষে ৬ অক্ষরের শক্তিশালী পাসওয়ার্ড দিন।"
            is FirebaseNetworkException -> "ইন্টারনেট সংযোগ নেই। অনুগ্রহ করে আপনার ডেটা বা ওয়াইফাই চেক করুন।"
            is FirebaseAuthException -> when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "ইমেইল ঠিকানাটির ফরম্যাট সঠিক নয়।"
                "ERROR_WRONG_PASSWORD" -> "পাসওয়ার্ড সঠিক নয়।"
                "ERROR_USER_NOT_FOUND" -> "এই অ্যাকাউন্টের অস্তিত্ব নেই।"
                "ERROR_USER_DISABLED" -> "এই অ্যাকাউন্টটি নিষ্ক্রিয় করা হয়েছে।"
                "ERROR_TOO_MANY_REQUESTS" -> "অতিরিক্ত অনুরোধের কারণে সাময়িকভাবে বন্ধ। কিছুক্ষণ পর চেষ্টা করুন।"
                "ERROR_OPERATION_NOT_ALLOWED" -> "Firebase Console-এ এই লগইন পদ্ধতিটি সক্রিয় (Enable) করা নেই।"
                else -> e.localizedMessage ?: "একটি ত্রুটি ঘটেছে। পুনরায় চেষ্টা করুন।"
            }
            else -> e.localizedMessage ?: "অনাকাঙ্ক্ষিত ত্রুটি ঘটেছে। দয়া করে পুনরায় চেষ্টা করুন।"
        }
    }
}
