package com.example.data.notice

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun saveProfile(profile: ApplicantProfile) {
        val user = auth.currentUser ?: return
        firestore.collection("profiles").document(user.uid).set(profile.copy(userId = user.uid)).await()
    }

    suspend fun getProfile(): ApplicantProfile? {
        val user = auth.currentUser ?: return null
        return firestore.collection("profiles").document(user.uid).get().await().toObject(ApplicantProfile::class.java)
    }
}
