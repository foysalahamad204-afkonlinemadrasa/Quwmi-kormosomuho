package com.example.data.notice

/**
 * Application model for Job/Khedmat notice submissions.
 */
data class Application(
    val id: String = "",
    val noticeId: String = "",
    val userId: String = "",
    val applicantName: String = "",
    val contactInfo: String = "",
    val qualifications: String = "",
    val experience: String = "",
    val status: String = STATUS_SUBMITTED,
    val timestamp: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val adminFeedback: String = ""
) {
    companion object {
        const val STATUS_SUBMITTED = "submitted" // আবেদন জমা হয়েছে
        const val STATUS_REVIEWING = "reviewing" // পর্যালোচনাধীন
        const val STATUS_CONTACTED = "contacted" // যোগাযোগ করা হয়েছে
        const val STATUS_SELECTED = "selected" // নির্বাচিত
        const val STATUS_REJECTED = "rejected" // নির্বাচিত হয়নি
        const val STATUS_WITHDRAWN = "withdrawn" // আবেদন প্রত্যাহার করা হয়েছে
    }
}
