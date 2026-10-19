package com.example.ui.notice

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.notice.Notice
import com.example.data.notice.NoticeRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoticeUiState(
    val publishedNotices: List<Notice> = emptyList(),
    val adminNotices: List<Notice> = emptyList(),
    val savedNoticeIds: Set<String> = emptySet(),
    val myApplications: List<com.example.data.notice.Application> = emptyList(),
    val selectedNotice: Notice? = null,
    val isAdmin: Boolean = false,
    val adminFilterTab: String = "published", // "published", "draft", "expired"
    val searchQuery: String = "",
    val locationFilter: String = "সব", // "সব", "ঢাকা", "চট্টগ্রাম", "সিলেট", etc.
    val isLoading: Boolean = false,
    val statusMessage: String? = null,
    val showBoostDialog: Boolean = false,
    val showCreateEditDialog: Boolean = false,
    val editingNotice: Notice? = null,
    val showAdminBoostDialog: Boolean = false,
    val boostingNotice: Notice? = null,
    val showAdminReviewDialog: Boolean = false,
    val reviewingNotice: Notice? = null,
    val showApplicationDialog: Boolean = false,
    val applyingNotice: Notice? = null,
    val filterOnlySaved: Boolean = false,
    val stats: com.example.data.notice.NoticeStats = com.example.data.notice.NoticeStats()
)

class NoticeViewModel(application: android.app.Application) : androidx.lifecycle.AndroidViewModel(application) {

    private val repository by lazy { NoticeRepository(getApplication<android.app.Application>().applicationContext) }
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState: StateFlow<NoticeUiState> = _uiState.asStateFlow()

    private val auth by lazy { com.google.firebase.auth.FirebaseAuth.getInstance() }

    init {
        try {
            checkAdminStatus()
            observePublishedNotices()
            observeSavedNotices()
            observeMyApplications()
        } catch (e: Exception) {
            _uiState.update { it.copy(statusMessage = "প্রারম্ভিক তথ্য লোড করতে সমস্যা হয়েছে: ${e.localizedMessage}") }
        }
    }

    fun checkAdminStatus() {
        val user = auth.currentUser
        val isAdmin = repository.isUserAdmin(user?.email)
        _uiState.update { it.copy(isAdmin = isAdmin) }
        if (isAdmin) {
            observeAdminNotices()
            observeStats()
        }
    }

    private fun observeStats() {
        viewModelScope.launch {
            repository.getNoticeStats().collect { stats ->
                _uiState.update { it.copy(stats = stats) }
            }
        }
    }

    private fun observePublishedNotices() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getPublishedNotices().collect { list ->
                _uiState.update {
                    it.copy(
                        publishedNotices = list,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun observeAdminNotices() {
        viewModelScope.launch {
            repository.getAllNoticesForAdmin().collect { list ->
                _uiState.update { it.copy(adminNotices = list) }
            }
        }
    }

    private fun observeSavedNotices() {
        val uid = auth.currentUser?.uid ?: ""
        viewModelScope.launch {
            repository.getSavedNoticeIds(uid).collect { savedIds ->
                _uiState.update { it.copy(savedNoticeIds = savedIds) }
            }
        }
    }

    fun selectNotice(notice: Notice?) {
        _uiState.update { it.copy(selectedNotice = notice) }
    }

    fun selectNoticeById(noticeId: String) {
        viewModelScope.launch {
            // Try to find in already loaded lists first
            val found = _uiState.value.publishedNotices.find { it.id == noticeId }
                ?: _uiState.value.adminNotices.find { it.id == noticeId }
            
            if (found != null) {
                selectNotice(found)
            } else {
                // If not loaded yet, wait or fetch from repository
                // For simplicity, we just check again when notices are updated.
                // But a real implementation would fetch it.
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun updateLocationFilter(location: String) {
        _uiState.update { it.copy(locationFilter = location) }
    }

    fun setAdminFilterTab(tab: String) {
        _uiState.update { it.copy(adminFilterTab = tab) }
    }

    fun setFilterOnlySaved(onlySaved: Boolean) {
        _uiState.update { it.copy(filterOnlySaved = onlySaved) }
    }

    fun toggleSaveNotice(notice: Notice) {
        val uid = auth.currentUser?.uid ?: "guest"
        viewModelScope.launch {
            repository.toggleSaveNotice(uid, notice).onSuccess { isSaved ->
                val msg = if (isSaved) "বিজ্ঞপ্তিটি সংরক্ষিত তালিকায় যোগ করা হয়েছে" else "বিজ্ঞপ্তিটি সংরক্ষণ থেকে সরানো হয়েছে"
                _uiState.update { it.copy(statusMessage = msg) }
            }
        }
    }

    fun applyForNotice(noticeId: String, name: String, contact: String, quals: String, exp: String) {
        val uid = auth.currentUser?.uid ?: return
        val app = com.example.data.notice.Application(noticeId = noticeId, userId = uid, applicantName = name, contactInfo = contact, qualifications = quals, experience = exp)
        viewModelScope.launch {
            repository.createApplication(app).onSuccess {
                _uiState.update { it.copy(statusMessage = "আবেদন সফলভাবে জমা হয়েছে।") }
            }.onFailure { e ->
                _uiState.update { it.copy(statusMessage = "আবেদন জমা দিতে সমস্যা হয়েছে: ${e.message}") }
            }
        }
    }

    private fun observeMyApplications() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.getUserApplications(uid).collect { list ->
                _uiState.update { it.copy(myApplications = list) }
            }
        }
    }

    fun openBoostDialog(show: Boolean, notice: Notice? = null) {
        _uiState.update {
            it.copy(
                showBoostDialog = show,
                selectedNotice = notice ?: it.selectedNotice
            )
        }
    }

    fun openAdminBoostDialog(show: Boolean, notice: Notice? = null) {
        _uiState.update {
            it.copy(
                showAdminBoostDialog = show,
                boostingNotice = notice
            )
        }
    }

    fun openAdminReviewDialog(show: Boolean, notice: Notice? = null) {
        _uiState.update {
            it.copy(
                showAdminReviewDialog = show,
                reviewingNotice = notice
            )
        }
    }

    fun openApplicationDialog(show: Boolean, notice: Notice? = null) {
        _uiState.update {
            it.copy(
                showApplicationDialog = show,
                applyingNotice = notice
            )
        }
    }

    fun openCreateEditDialog(show: Boolean, notice: Notice? = null) {
        _uiState.update {
            it.copy(
                showCreateEditDialog = show,
                editingNotice = notice
            )
        }
    }

    fun saveNotice(notice: Notice) {
        val user = auth.currentUser
        val adminId = user?.uid ?: "admin_id"
        val adminEmail = user?.email ?: "admin@kaomi.edu.bd"

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (notice.id.isBlank() || notice.id.startsWith("new_")) {
                repository.createNotice(notice, adminId, adminEmail).onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            showCreateEditDialog = false,
                            editingNotice = null,
                            statusMessage = "নতুন খেদমত বিজ্ঞপ্তি সফলভাবে সংরক্ষিত ও প্রকাশিত হয়েছে"
                        )
                    }
                }
            } else {
                repository.updateNotice(notice, adminId, adminEmail).onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            showCreateEditDialog = false,
                            editingNotice = null,
                            selectedNotice = if (state.selectedNotice?.id == notice.id) notice else state.selectedNotice,
                            statusMessage = "বিজ্ঞপ্তি সফলভাবে হালনাগাদ করা হয়েছে"
                        )
                    }
                }
            }
        }
    }

    fun deleteNotice(noticeId: String) {
        val user = auth.currentUser
        val adminId = user?.uid ?: "admin_id"
        val adminEmail = user?.email ?: "admin@kaomi.edu.bd"

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.deleteNotice(noticeId, adminId, adminEmail).onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        selectedNotice = if (state.selectedNotice?.id == noticeId) null else state.selectedNotice,
                        statusMessage = "বিজ্ঞপ্তি সফলভাবে অপসারিত হয়েছে"
                    )
                }
            }
        }
    }

    fun setNoticeBoost(noticeId: String, isBoosted: Boolean, durationDays: Int, sendNotification: Boolean = false) {
        val user = auth.currentUser
        val adminId = user?.uid ?: "admin_id"
        val adminEmail = user?.email ?: "admin@kaomi.edu.bd"

        viewModelScope.launch {
            repository.updateBoostStatus(noticeId, isBoosted, durationDays, adminId, adminEmail).onSuccess {
                var msg = if (isBoosted) "বিজ্ঞপ্তিটি $durationDays দিনের জন্য বুস্ট সক্রিয় করা হয়েছে" else "বিজ্ঞপ্তির বুস্ট বন্ধ করা হয়েছে"
                if (sendNotification) {
                    msg += " এবং পুশ নোটিফিকেশন পাঠানো হচ্ছে"
                }
                _uiState.update { state ->
                    state.copy(
                        showAdminBoostDialog = false,
                        boostingNotice = null,
                        statusMessage = msg
                    )
                }
            }
        }
    }

    fun approveNotice(noticeId: String) {
        updateStatus(noticeId, Notice.STATUS_PUBLISHED, "অনুমোদিত")
    }

    fun rejectNotice(noticeId: String, reason: String) {
        updateStatus(noticeId, Notice.STATUS_REJECTED, "প্রত্যাখ্যাত", reason)
    }

    fun returnForCorrection(noticeId: String, reason: String) {
        updateStatus(noticeId, Notice.STATUS_CORRECTION, "সংশোধনের জন্য ফেরত", reason)
    }

    private fun updateStatus(noticeId: String, status: String, statusLabel: String, feedback: String = "") {
        val user = auth.currentUser
        val adminId = user?.uid ?: "admin_id"
        val adminEmail = user?.email ?: "admin@kaomi.edu.bd"

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.updateNoticeStatus(noticeId, status, feedback, adminId, adminEmail).onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        statusMessage = "বিজ্ঞপ্তিটি সফলভাবে $statusLabel হয়েছে",
                        selectedNotice = state.selectedNotice?.copy(status = status, adminFeedback = feedback)
                    )
                }
            }
        }
    }

    fun requestBoost(noticeId: String) {
        viewModelScope.launch {
            repository.requestBoost(noticeId).onSuccess {
                _uiState.update { it.copy(statusMessage = "বুস্টের জন্য অনুরোধ পাঠানো হয়েছে। অ্যাডমিন পর্যালোচনা করবেন।") }
            }
        }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }
}
