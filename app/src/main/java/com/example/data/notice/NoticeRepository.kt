package com.example.data.notice

import android.content.Context
import com.example.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * NoticeRepository:
 * Manages notice and announcement persistence in Firestore and local synchronization.
 * Uses named Firestore database ID from R.string.firestore_database_id.
 */
class NoticeRepository(
    private val context: Context
) {
    private val auth by lazy { FirebaseAuth.getInstance() }
    // Initial Seed Notices representing authentic Qawmi Madrasa খেদমত বিজ্ঞপ্তি
    private val initialSampleNotices = listOf(
        Notice(
            id = "sample_notice_01",
            title = "দাওরায়ে হাদিস ও কিতাব বিভাগ শিক্ষক আবশ্যক",
            instituteName = "দারুল উলুম কওমি মাদরাসা, ঢাকা",
            role = "মুহাদ্দিস ও কিতাব বিভাগের উস্তাদ",
            qualifications = "বেফাকুল মাদারিসিল আরাবিয়া থেকে দাওরায়ে হাদিস উত্তীর্ণ ও দরসে নেজামিতে ৩ বছরের সফল পাঠদানের অভিজ্ঞতা।",
            location = "মিরপুর-১, ঢাকা",
            remuneration = "আলোচনা সাপেক্ষে সম্মানজনক হাদিয়া ও মাদরাসায় একক আবাসন ব্যবস্থা।",
            deadline = "২৫ জিলকদ ১৪৪৭ / ১৫ জুন ২০২৬",
            applicationInstructions = "স্বহস্তে লিখিত আবেদনপত্র, শিক্ষাগত সনদের সত্যায়িত কপি ও প্রয়োজনীয় কাগজপত্রসহ সরাসরি যোগাযোগ করুন অথবা ইমেইলে প্রেরণ করুন।",
            contactPhone = "০১৭১১-৮৮৯৯০০",
            contactEmail = "darululoom.dhaka@kaomi.edu.bd",
            publishedDate = "১৮ এপ্রিল ২০২৬",
            timestamp = System.currentTimeMillis() - 3600000 * 24,
            description = "আসসালামু আলাইকুম। আমাদের প্রাচীন ও সুনামধন্য দ্বীনি শিক্ষা প্রতিষ্ঠান দারুল উলুম কওমি মাদরাসায় আসন্ন শিক্ষাবর্ষের জন্য দাওরায়ে হাদিস ও উচ্চতর কিতাব বিভাগে ছাত্রদের দরস প্রদানের লক্ষ্যে একজন নিষ্ঠাবান, সুন্নতের পাবন্দ ও অভিজ্ঞ মুহাদ্দিস নিয়োগের জন্য খেদমতপ্রত্যাশী উস্তাদগণের নিকট থেকে আবেদন আহ্বান করা হচ্ছে।\n\nদায়িত্বসমূহ:\n১. মিশকাত ও তিরমিযী শরীফের নিয়মিত দরস পরিচালনা।\n২. ছাত্রদের তাহযিব ও আখলাক তদারকি।\n৩. প্রতিষ্ঠান কর্তৃক অর্পিত অন্যান্য দ্বীনি দায়িত্ব নিষ্ঠার সাথে পালন।",
            status = Notice.STATUS_PUBLISHED,
            isImportant = true,
            isBoosted = true,
            boostStartedAt = System.currentTimeMillis() - 3600000 * 12,
            boostExpiresAt = System.currentTimeMillis() + 3600000 * 24 * 7,
            boostDurationDays = 7,
            attachmentUrl = "",
            attachmentType = "none",
            createdBy = "admin"
        ),
        Notice(
            id = "sample_notice_02",
            title = "হিফজুল কুরআন প্রধান শিক্ষক ও কারী আবশ্যক",
            instituteName = "জামিয়া ইসলামিয়া আরাবিয়া, চট্টগ্রাম",
            role = "হিফজ বিভাগের প্রধান শিক্ষক",
            qualifications = "হুফফাজে কেরামদের মধ্যে তাজবীদসহ হাফেজ এবং অভিজ্ঞ হিফজ শিক্ষক। কমপক্ষে ২ জামাত তাহফিজ সমাপ্তির অভিজ্ঞতা থাকতে হবে।",
            location = "আন্দরকিল্লা, চট্টগ্রাম",
            remuneration = "সম্মানজনক হাদিয়া, খাবার ও একক আবাসন সুবিধা।",
            deadline = "৩০ শাওয়াল ১৪৪৭ / ২০ মে ২০২৬",
            applicationInstructions = "মুহতামিম বরাবর আবেদনপত্র সরাসরি দফতরে জমা দিন অথবা নিম্নের নম্বরে যোগাযোগ করুন।",
            contactPhone = "০১৮১৯-৭৭৬৬৫৫",
            contactEmail = "jamia.ctg@kaomi.edu.bd",
            publishedDate = "১৭ এপ্রিল ২০২৬",
            timestamp = System.currentTimeMillis() - 3600000 * 48,
            description = "জামিয়া ইসলামিয়ার হিফজুল কুরআন বিভাগে তাজবীদের সাথে কুরআনুল কারিম শিক্ষাদানে পারদর্শী ও ছাত্রদের আন্তরিকভাবে তদারকিতে সক্ষম একজন দক্ষ হাফেজ ও ক্বারী প্রয়োজন।\n\nপ্রয়োজনীয় শর্তাবলি:\n- সুন্নতের পাবন্দি ও তাকওয়াবান জীবনযাপন।\n- ছাত্রদের প্রতি স্নেহশীল মনোভাব ও শৃঙ্খলার প্রতি বিশেষ যত্ন।",
            status = Notice.STATUS_PUBLISHED,
            isImportant = false,
            isBoosted = false,
            attachmentUrl = "",
            attachmentType = "none",
            createdBy = "admin"
        ),
        Notice(
            id = "sample_notice_03",
            title = "মসজিদের খতিব ও ইফতা বিভাগের সিনিয়র উস্তাদ",
            instituteName = "আল-জামেয়াতুল আহলিয়া দারুল মাআরিফ, সিলেট",
            role = "জুমা খতিব ও ইফতা শিক্ষক",
            qualifications = "তাকমিল ও তাখাসসুস ফিল ইফতা সনদধারী, কুরআন-সুন্নাহর সঠিক বিশ্লেষণে দক্ষ এবং সুমধুর বাচনভঙ্গিসম্পন্ন।",
            location = "দরগাহ মহল্লা, সিলেট",
            remuneration = "আলোচনা সাপেক্ষে সম্মানজনক হাদিয়া।",
            deadline = "১০ জিলকদ ১৪৪৭ / ৩১ মে ২০২৬",
            applicationInstructions = "সিভি ও প্রামাণ্য দলিলের অনুলিপি সরাসরি বা ইমেইলে প্রেরণ করুন।",
            contactPhone = "০১৭২০-৩৩৪৪৫৫",
            contactEmail = "darulmaarif.sylhet@kaomi.edu.bd",
            publishedDate = "১৬ এপ্রিল ২০২৬",
            timestamp = System.currentTimeMillis() - 3600000 * 72,
            description = "কেন্দ্রীয় জামে মসজিদের নিয়মিত জুমার খুতবা প্রদান ও সংশ্লিষ্ট কওমি মাদরাসার ফতোয়া ও গবেষণা বিভাগে পাঠদানের জন্য একজন বিজ্ঞ আলেম ও মুফতি আবশ্যক।",
            status = Notice.STATUS_PUBLISHED,
            isImportant = true,
            isBoosted = false,
            attachmentUrl = "",
            attachmentType = "none",
            createdBy = "admin"
        )
    )

    // In-memory fallback / cache in case Firestore is unreachable offline
    private val localNoticesCache = mutableListOf<Notice>().apply {
        addAll(initialSampleNotices)
    }
    private val localSavedNoticesCache = mutableSetOf<String>()

    /**
     * Get Firestore instance bound to the applet's provisioned named database ID
     */
    private fun getFirestore(): FirebaseFirestore? {
        return try {
            val dbId = context.getString(R.string.firestore_database_id)
            if (FirebaseApp.getApps(context).isNotEmpty() && dbId.isNotBlank()) {
                FirebaseFirestore.getInstance(FirebaseApp.getInstance(), dbId)
            } else null
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Check if a user is authorized as Admin.
     * Recognized: specific administrator email or existing in admins collection.
     */
    fun isUserAdmin(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        val adminEmails = listOf(
            "foysalahamad102@gmail.com",
            "admin@kaomi.edu.bd",
            "qawmi.admin@kaomi.edu.bd"
        )
        return adminEmails.any { it.equals(email.trim(), ignoreCase = true) }
    }

    /**
     * Flow of published notices for regular users
     */
    fun getPublishedNotices(): Flow<List<Notice>> = callbackFlow {
        val firestore = getFirestore()
        if (firestore == null) {
            trySend(localNoticesCache.filter { it.status == Notice.STATUS_PUBLISHED }
                .sortedWith(compareByDescending<Notice> { it.isCurrentlyBoosted }
                    .thenByDescending { it.isImportant }
                    .thenByDescending { it.timestamp }))
            awaitClose { }
            return@callbackFlow
        }

        val listener = firestore.collection("notices")
            .whereEqualTo("status", Notice.STATUS_PUBLISHED)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(localNoticesCache.filter { it.status == Notice.STATUS_PUBLISHED }
                        .sortedWith(compareByDescending<Notice> { it.isCurrentlyBoosted }
                            .thenByDescending { it.isImportant }
                            .thenByDescending { it.timestamp }))
                    return@addSnapshotListener
                }

                val list = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Notice::class.java)?.copy(id = doc.id)
                    } catch (_: Exception) {
                        null
                    }
                }

                if (list.isEmpty()) {
                    trySend(localNoticesCache.filter { it.status == Notice.STATUS_PUBLISHED }
                        .sortedWith(compareByDescending<Notice> { it.isCurrentlyBoosted }
                            .thenByDescending { it.isImportant }
                            .thenByDescending { it.timestamp }))
                } else {
                    val sorted = list.sortedWith(compareByDescending<Notice> { it.isCurrentlyBoosted }
                        .thenByDescending { it.isImportant }
                        .thenByDescending { it.timestamp })
                    trySend(sorted)
                }
            }

        awaitClose { listener.remove() }
    }

    /**
     * Flow of all notices (Published, Drafts, Expired) for Admins
     */
    fun getAllNoticesForAdmin(): Flow<List<Notice>> = callbackFlow {
        val firestore = getFirestore()
        if (firestore == null) {
            trySend(localNoticesCache.sortedByDescending { it.timestamp })
            awaitClose { }
            return@callbackFlow
        }

        val listener = firestore.collection("notices")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(localNoticesCache.sortedByDescending { it.timestamp })
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Notice::class.java)?.copy(id = doc.id)
                    } catch (_: Exception) {
                        null
                    }
                } ?: emptyList()

                if (list.isEmpty()) {
                    trySend(localNoticesCache.sortedByDescending { it.timestamp })
                } else {
                    trySend(list)
                }
            }

        awaitClose { listener.remove() }
    }

    /**
     * Flow of notices submitted by a specific user
     */
    fun getUserNotices(userEmail: String): Flow<List<Notice>> = callbackFlow {
        val firestore = getFirestore()
        if (firestore == null) {
            trySend(localNoticesCache.filter { it.createdBy == userEmail }
                .sortedByDescending { it.timestamp })
            awaitClose { }
            return@callbackFlow
        }

        val listener = firestore.collection("notices")
            .whereEqualTo("createdBy", userEmail)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(localNoticesCache.filter { it.createdBy == userEmail }
                        .sortedByDescending { it.timestamp })
                    return@addSnapshotListener
                }

                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Notice::class.java)?.copy(id = doc.id)
                }
                trySend(list)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Flow of notice statistics for Admin dashboard
     */
    fun getNoticeStats(): Flow<NoticeStats> = callbackFlow {
        val firestore = getFirestore()
        if (firestore == null) {
            trySend(calculateLocalStats())
            awaitClose { }
            return@callbackFlow
        }

        val listener = firestore.collection("notices")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(calculateLocalStats())
                    return@addSnapshotListener
                }

                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Notice::class.java)
                }

                val stats = NoticeStats(
                    total = list.size,
                    published = list.count { it.status == Notice.STATUS_PUBLISHED },
                    pending = list.count { it.status == Notice.STATUS_PENDING },
                    correction = list.count { it.status == Notice.STATUS_CORRECTION },
                    rejected = list.count { it.status == Notice.STATUS_REJECTED },
                    expired = list.count { it.status == Notice.STATUS_EXPIRED },
                    boosted = list.count { it.isCurrentlyBoosted }
                )
                trySend(stats)
            }

        awaitClose { listener.remove() }
    }

    private fun calculateLocalStats(): NoticeStats {
        return NoticeStats(
            total = localNoticesCache.size,
            published = localNoticesCache.count { it.status == Notice.STATUS_PUBLISHED },
            pending = localNoticesCache.count { it.status == Notice.STATUS_PENDING },
            correction = localNoticesCache.count { it.status == Notice.STATUS_CORRECTION },
            rejected = localNoticesCache.count { it.status == Notice.STATUS_REJECTED },
            expired = localNoticesCache.count { it.status == Notice.STATUS_EXPIRED },
            boosted = localNoticesCache.count { it.isCurrentlyBoosted }
        )
    }

    /**
     * Create notice (Admin or User)
     * Users submit for approval (Pending), Admins can publish directly.
     */
    suspend fun createNotice(notice: Notice, adminId: String, adminEmail: String): Result<String> {
        val firestore = getFirestore()
        val docId = "notice_${System.currentTimeMillis()}"
        val isAdmin = isUserAdmin(adminEmail)
        
        val finalNotice = notice.copy(
            id = docId,
            createdBy = adminEmail,
            timestamp = System.currentTimeMillis(),
            status = if (isAdmin) Notice.STATUS_PUBLISHED else Notice.STATUS_PENDING
        )

        localNoticesCache.add(0, finalNotice)

        if (firestore != null) {
            try {
                firestore.collection("notices").document(docId).set(finalNotice).await()
                // Record audit log
                val log = AdminNoticeLog(
                    id = "log_${System.currentTimeMillis()}",
                    adminId = adminId,
                    adminEmail = adminEmail,
                    action = if (isAdmin) "বিজ্ঞপ্তি তৈরি ও প্রকাশ" else "বিজ্ঞপ্তি জমা (অনুমোদনের জন্য)",
                    noticeId = docId,
                    timestamp = System.currentTimeMillis(),
                    details = "নতুন খেদমত বিজ্ঞপ্তি: ${notice.title} (অবস্থা: ${finalNotice.status})"
                )
                firestore.collection("admin_logs").document(log.id).set(log).await()
            } catch (e: Exception) {
                return Result.success(docId)
            }
        }
        return Result.success(docId)
    }

    /**
     * Update notice by Admin
     */
    suspend fun updateNotice(notice: Notice, adminId: String, adminEmail: String): Result<Unit> {
        val index = localNoticesCache.indexOfFirst { it.id == notice.id }
        if (index >= 0) {
            localNoticesCache[index] = notice
        }

        val firestore = getFirestore()
        if (firestore != null) {
            try {
                firestore.collection("notices").document(notice.id).set(notice).await()
                val log = AdminNoticeLog(
                    id = "log_${System.currentTimeMillis()}",
                    adminId = adminId,
                    adminEmail = adminEmail,
                    action = "বিজ্ঞপ্তি সম্পাদনা",
                    noticeId = notice.id,
                    timestamp = System.currentTimeMillis(),
                    details = "বিজ্ঞপ্তি হালনাগাদ: ${notice.title} (অবস্থা: ${notice.status})"
                )
                firestore.collection("admin_logs").document(log.id).set(log).await()
            } catch (_: Exception) { }
        }
        return Result.success(Unit)
    }

    /**
     * Delete notice by Admin
     */
    suspend fun deleteNotice(noticeId: String, adminId: String, adminEmail: String): Result<Unit> {
        localNoticesCache.removeAll { it.id == noticeId }

        val firestore = getFirestore()
        if (firestore != null) {
            try {
                firestore.collection("notices").document(noticeId).delete().await()
                val log = AdminNoticeLog(
                    id = "log_${System.currentTimeMillis()}",
                    adminId = adminId,
                    adminEmail = adminEmail,
                    action = "বিজ্ঞপ্তি মুছে ফেলা",
                    noticeId = noticeId,
                    timestamp = System.currentTimeMillis(),
                    details = "বিজ্ঞপ্তি অপসারিত হয়েছে"
                )
                firestore.collection("admin_logs").document(log.id).set(log).await()
            } catch (_: Exception) { }
        }
        return Result.success(Unit)
    }

    /**
     * Admin: Change notice status (Approve, Reject, Send Back)
     */
    suspend fun updateNoticeStatus(
        noticeId: String,
        newStatus: String,
        feedback: String,
        adminId: String,
        adminEmail: String
    ): Result<Unit> {
        val existing = localNoticesCache.find { it.id == noticeId } ?: return Result.failure(Exception("বিজ্ঞপ্তি পাওয়া যায়নি"))
        val updated = existing.copy(status = newStatus, adminFeedback = feedback)
        
        val res = updateNotice(updated, adminId, adminEmail)
        if (res.isSuccess) {
            val firestore = getFirestore()
            if (firestore != null) {
                try {
                    val log = AdminNoticeLog(
                        id = "log_${System.currentTimeMillis()}",
                        adminId = adminId,
                        adminEmail = adminEmail,
                        action = "অবস্থা পরিবর্তন: $newStatus",
                        noticeId = noticeId,
                        timestamp = System.currentTimeMillis(),
                        details = "বিজ্ঞপ্তির অবস্থা পরিবর্তিত: $newStatus. মন্তব্য: $feedback"
                    )
                    firestore.collection("admin_logs").document(log.id).set(log).await()
                } catch (_: Exception) { }
            }
        }
        return res
    }

    /**
     * Admin Boost Management: Update boost status, start time, expiration
     */
    suspend fun updateBoostStatus(
        noticeId: String,
        isBoosted: Boolean,
        durationDays: Int,
        adminId: String,
        adminEmail: String
    ): Result<Unit> {
        val existing = localNoticesCache.find { it.id == noticeId } ?: return Result.failure(Exception("বিজ্ঞপ্তি পাওয়া যায়নি"))
        val now = System.currentTimeMillis()
        val expiresAt = if (isBoosted) now + (durationDays.toLong() * 24 * 3600 * 1000) else null

        val updated = existing.copy(
            isBoosted = isBoosted,
            boostStartedAt = if (isBoosted) now else null,
            boostExpiresAt = expiresAt,
            boostDurationDays = if (isBoosted) durationDays else null,
            boostRequested = if (isBoosted) false else existing.boostRequested,
            notificationSent = if (isBoosted && !existing.isBoosted) false else existing.notificationSent
        )

        return updateNotice(updated, adminId, adminEmail)
    }

    /**
     * User: Request Boost for a notice
     */
    suspend fun requestBoost(noticeId: String): Result<Unit> {
        val existing = localNoticesCache.find { it.id == noticeId } ?: return Result.failure(Exception("বিজ্ঞপ্তি পাওয়া যায়নি"))
        val updated = existing.copy(boostRequested = true)
        
        // This is a minimal update that doesn't record an admin log
        val index = localNoticesCache.indexOfFirst { it.id == noticeId }
        if (index >= 0) {
            localNoticesCache[index] = updated
        }
        val firestore = getFirestore()
        if (firestore != null) {
            try {
                firestore.collection("notices").document(noticeId).set(updated).await()
            } catch (e: Exception) {
                return Result.failure(e)
            }
        }
        return Result.success(Unit)
    }

    /**
     * Toggle Save / Bookmark Notice for user
     */
    suspend fun toggleSaveNotice(userId: String, notice: Notice): Result<Boolean> {
        val isCurrentlySaved = localSavedNoticesCache.contains(notice.id)
        val newSavedState = !isCurrentlySaved

        if (newSavedState) {
            localSavedNoticesCache.add(notice.id)
        } else {
            localSavedNoticesCache.remove(notice.id)
        }

        val firestore = getFirestore()
        if (firestore != null && userId.isNotBlank()) {
            try {
                val ref = firestore.collection("users").document(userId).collection("saved_notices").document(notice.id)
                if (newSavedState) {
                    val item = SavedNoticeItem(
                        noticeId = notice.id,
                        savedAt = System.currentTimeMillis(),
                        noticeTitle = notice.title,
                        instituteName = notice.instituteName
                    )
                    ref.set(item).await()
                } else {
                    ref.delete().await()
                }
            } catch (_: Exception) { }
        }
        return Result.success(newSavedState)
    }

    /**
     * Flow of saved notice IDs
     */
    fun getSavedNoticeIds(userId: String): Flow<Set<String>> = callbackFlow {
        val firestore = getFirestore()
        if (firestore == null || userId.isBlank()) {
            trySend(localSavedNoticesCache.toSet())
            awaitClose { }
            return@callbackFlow
        }

        val listener = firestore.collection("users").document(userId).collection("saved_notices")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(localSavedNoticesCache.toSet())
                    return@addSnapshotListener
                }
                val set = snapshot.documents.map { it.id }.toSet()
                localSavedNoticesCache.clear()
                localSavedNoticesCache.addAll(set)
                trySend(set)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Store FCM token for the user to enable targeted push notifications.
     */
    suspend fun updateFcmToken(userId: String, token: String): Result<Unit> {
        val firestore = getFirestore() ?: return Result.failure(Exception("Firestore unavailable"))
        return try {
            firestore.collection("fcm_tokens").document(userId).set(
                mapOf(
                    "token" to token,
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Application Management
    suspend fun createApplication(application: com.example.data.notice.Application): Result<String> {
        val firestore = getFirestore() ?: return Result.failure(Exception("Firestore unavailable"))
        val appId = "app_${System.currentTimeMillis()}"
        val finalApp = application.copy(id = appId, timestamp = System.currentTimeMillis())
        return try {
            firestore.collection("applications").document(appId).set(finalApp).await()
            Result.success(appId)
        } catch (e: Exception) { Result.failure(e) }
    }

    fun getUserApplications(userId: String): Flow<List<com.example.data.notice.Application>> = callbackFlow {
        val firestore = getFirestore()
        if (firestore == null) { trySend(emptyList()); awaitClose { }; return@callbackFlow }
        val listener = firestore.collection("applications").whereEqualTo("userId", userId).addSnapshotListener { snapshot, _ ->
            trySend(snapshot?.documents?.mapNotNull { it.toObject(com.example.data.notice.Application::class.java)?.copy(id = it.id) } ?: emptyList())
        }
        awaitClose { listener.remove() }
    }

    suspend fun updateApplicationStatus(appId: String, status: String, feedback: String = ""): Result<Unit> {
        val firestore = getFirestore() ?: return Result.failure(Exception("Firestore unavailable"))
        return try {
            firestore.collection("applications").document(appId).update("status", status, "adminFeedback", feedback, "updatedAt", System.currentTimeMillis()).await()
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
}
