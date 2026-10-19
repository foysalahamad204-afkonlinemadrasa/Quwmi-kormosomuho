package com.example.ui.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.notice.ApplicantProfile
import com.example.data.notice.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: ApplicantProfile = ApplicantProfile(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

class ProfileViewModel(private val repository: ProfileRepository = ProfileRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val profile = repository.getProfile()
            _uiState.value = _uiState.value.copy(profile = profile ?: ApplicantProfile(), isLoading = false)
        }
    }

    fun updateProfile(profile: ApplicantProfile) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.saveProfile(profile)
            _uiState.value = _uiState.value.copy(profile = profile, isLoading = false, isSuccess = true)
        }
    }
}
