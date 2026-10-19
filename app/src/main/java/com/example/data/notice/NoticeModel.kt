package com.example.data.notice

/**
 * Notice model for কওমি মাদরাসা ও দ্বীনি প্রতিষ্ঠানের খেদমতের বিজ্ঞপ্তি ও ঘোষণা
 * Strict Islamic terminology: no "চাকরি"/"চাকুরী", only "খেদমত", "দ্বীনি দায়িত্ব", "কর্মসংস্থান".
 */
data class Notice(
    val id: String = "",
    val title: String = "",
    val instituteName: String = "",
    val instituteDescription: String = "", // প্রতিষ্ঠানের পরিচিতি
    val role: String = "", // খেদমতের দায়িত্ব বা পদের নাম
    val qualifications: String = "", // যোগ্যতা ও অভিজ্ঞতা
    val location: String = "", // কর্মস্থল
    val remuneration: String = "", // হাদিয়া বা সুযোগ-সুবিধা
    val deadline: String = "", // আবেদনের শেষ তারিখ
    val applicationInstructions: String = "", // আবেদনের নিয়ম ও যোগাযোগের তথ্য
    val contactPhone: String = "", // মোবাইল নম্বর
    val contactEmail: String = "", // ইমেইল
    val publishedDate: String = "", // প্রকাশের তারিখ
    val timestamp: Long = System.currentTimeMillis(),
    val description: String = "", // বিজ্ঞপ্তির সম্পূর্ণ বিবরণ
    val status: String = STATUS_PENDING,
    val isImportant: Boolean = false, // গুরুত্বপূর্ণ বিজ্ঞপ্তি
    val isBoosted: Boolean = false, // বুস্টেড অবস্থা (অ্যাডমিন নিয়ন্ত্রিত)
    val boostRequested: Boolean = false, // ব্যবহারকারী বুস্টের অনুরোধ করেছেন কি না
    val boostStartedAt: Long? = null,
    val boostExpiresAt: Long? = null,
    val boostDurationDays: Int? = null,
    val attachmentUrl: String = "",
    val attachmentType: String = "none", // "image", "pdf", "none"
    val createdBy: String = "",
    val notificationSent: Boolean = false, // বুস্ট নোটিফিকেশন পাঠানো হয়েছে কি না
    val notificationSentAt: Long? = null,
    val adminFeedback: String = "", // সংশোধনের জন্য ফেরত বা প্রত্যাখ্যানের কারণ
    val isOnlineApplication: Boolean = false // অনলাইনে আবেদনযোগ্য কি না
) {
    val isCurrentlyBoosted: Boolean
        get() = isBoosted && (boostExpiresAt == null || boostExpiresAt > System.currentTimeMillis())

    companion object {
        const val STATUS_PENDING = "pending" // অনুমোদনের অপেক্ষায়
        const val STATUS_CORRECTION = "correction" // সংশোধনের জন্য ফেরত
        const val STATUS_REJECTED = "rejected" // প্রত্যাখ্যাত
        const val STATUS_PUBLISHED = "published" // অনুমোদিত ও প্রকাশিত
        const val STATUS_EXPIRED = "expired" // মেয়াদোত্তীর্ণ
    }
}

/**
 * Admin stats for notices
 */
data class NoticeStats(
    val total: Int = 0,
    val published: Int = 0,
    val pending: Int = 0,
    val correction: Int = 0,
    val rejected: Int = 0,
    val expired: Int = 0,
    val boosted: Int = 0
)

/**
 * Admin action audit log
 */
data class AdminNoticeLog(
    val id: String = "",
    val adminId: String = "",
    val adminEmail: String = "",
    val action: String = "", // CREATE, EDIT, BOOST, DELETE, STATUS_CHANGE
    val noticeId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val details: String = ""
)

/**
 * Saved notice reference for user bookmarks
 */
data class SavedNoticeItem(
    val noticeId: String = "",
    val savedAt: Long = System.currentTimeMillis(),
    val noticeTitle: String = "",
    val instituteName: String = ""
)
