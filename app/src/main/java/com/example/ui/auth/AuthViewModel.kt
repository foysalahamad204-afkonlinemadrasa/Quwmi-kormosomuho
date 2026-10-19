package com.example.ui.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.example.data.notice.NoticeRepository
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

data class AuthUiState(
    val currentUser: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val isFlippedToRegister: Boolean = false,

    // Login Form
    val identifier: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val identifierError: String? = null,
    val passwordError: String? = null,

    // Registration Form (নতুন অ্যাকাউন্ট তৈরি)
    val registerFullName: String = "",
    val registerIdentifier: String = "",
    val registerPassword: String = "",
    val registerConfirmPassword: String = "",
    val isRegisterPasswordVisible: Boolean = false,
    val isRegisterConfirmPasswordVisible: Boolean = false,
    val registerNameError: String? = null,
    val registerIdentifierError: String? = null,
    val registerPasswordError: String? = null,
    val registerConfirmPasswordError: String? = null,

    // Terms & Privacy Consent
    val isTermsAccepted: Boolean = false,
    val showTermsModal: Boolean = false,
    val showPrivacyModal: Boolean = false,

    // Forgot Password
    val showForgotPasswordDialog: Boolean = false,
    val forgotEmail: String = "",
    val forgotEmailError: String? = null,
    val forgotSuccessMessage: String? = null,

    // Main Screen & Appearance
    val isGuestMode: Boolean = false,
    val isDarkMode: Boolean = false,

    // Status messaging
    val authStatusMessage: String? = null,
    val isSuccessMessage: Boolean = false
)

class AuthViewModel(
    application: android.app.Application,
    private val authRepo: com.example.data.FirebaseAuthRepository = com.example.data.FirebaseAuthRepository()
) : androidx.lifecycle.AndroidViewModel(application) {

    private val noticeRepo by lazy { com.example.data.notice.NoticeRepository(getApplication<android.app.Application>().applicationContext) }

    private val _uiState = MutableStateFlow(AuthUiState(currentUser = authRepo.currentUser))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepo.authStateFlow.collect { user ->
                _uiState.update { it.copy(currentUser = user) }
            }
        }
    }

    fun toggleFlip(toRegister: Boolean) {
        _uiState.update {
            it.copy(
                isFlippedToRegister = toRegister,
                authStatusMessage = null,
                identifierError = null,
                passwordError = null,
                registerNameError = null,
                registerIdentifierError = null,
                registerPasswordError = null,
                registerConfirmPasswordError = null
            )
        }
    }

    // Terms & Privacy Handlers
    fun updateTermsAccepted(accepted: Boolean) {
        _uiState.update { it.copy(isTermsAccepted = accepted, authStatusMessage = null) }
    }

    fun showTermsModal(show: Boolean) {
        _uiState.update { it.copy(showTermsModal = show) }
    }

    fun showPrivacyModal(show: Boolean) {
        _uiState.update { it.copy(showPrivacyModal = show) }
    }

    fun setTermsError(message: String) {
        _uiState.update {
            it.copy(
                authStatusMessage = message,
                isSuccessMessage = false
            )
        }
    }

    // Login Form Handlers
    fun updateIdentifier(value: String) {
        _uiState.update { it.copy(identifier = value, identifierError = null, authStatusMessage = null) }
    }

    fun updatePassword(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, authStatusMessage = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        val state = _uiState.value

        if (!state.isTermsAccepted) {
            _uiState.update {
                it.copy(
                    authStatusMessage = "অনুগ্রহ করে নিয়ম ও শর্তাবলি এবং গোপনীয়তা নীতিতে সম্মতি দিন।",
                    isSuccessMessage = false
                )
            }
            return
        }

        val rawInput = state.identifier.trim()
        val pass = state.password

        var hasError = false
        var idError: String? = null
        var passError: String? = null

        if (rawInput.isEmpty()) {
            idError = "ইমেইল বা ইউজারনেম লিখুন"
            hasError = true
        } else if (rawInput.contains("@") && !android.util.Patterns.EMAIL_ADDRESS.matcher(rawInput).matches()) {
            idError = "সঠিক ইমেইল ফরম্যাট দিন (যেমন: name@example.com)"
            hasError = true
        }

        if (pass.isEmpty()) {
            passError = "পাসওয়ার্ড লিখুন"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    identifierError = idError,
                    passwordError = passError,
                    authStatusMessage = null
                )
            }
            return
        }

        val resolvedEmail = if (rawInput.contains("@")) {
            rawInput
        } else {
            "${rawInput.replace(" ", "").lowercase()}@kaomi.com"
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, authStatusMessage = null) }
            try {
                withTimeout(10000L) {
                    val result = authRepo.signInWithEmail(resolvedEmail, pass)
                    result.fold(
                        onSuccess = { user ->
                            registerFcmToken(user.uid)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    currentUser = user,
                                    authStatusMessage = "সফলভাবে লগইন হয়েছে!",
                                    isSuccessMessage = true
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    authStatusMessage = error.message ?: "লগইন ব্যর্থ হয়েছে।",
                                    isSuccessMessage = false
                                )
                            }
                        }
                    )
                }
            } catch (e: TimeoutCancellationException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        authStatusMessage = "Firebase সার্ভার সংযোগের সময় শেষ হয়েছে। ইন্টারনেট বা নেটওয়ার্ক চেক করে আবার চেষ্টা করুন।",
                        isSuccessMessage = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        authStatusMessage = e.localizedMessage ?: "লগইন ব্যর্থ হয়েছে।",
                        isSuccessMessage = false
                    )
                }
            }
        }
    }

    // Registration Form Handlers
    fun updateRegisterFullName(value: String) {
        _uiState.update { it.copy(registerFullName = value, registerNameError = null, authStatusMessage = null) }
    }

    fun updateRegisterIdentifier(value: String) {
        _uiState.update { it.copy(registerIdentifier = value, registerIdentifierError = null, authStatusMessage = null) }
    }

    fun updateRegisterPassword(value: String) {
        _uiState.update { it.copy(registerPassword = value, registerPasswordError = null, authStatusMessage = null) }
    }

    fun updateRegisterConfirmPassword(value: String) {
        _uiState.update { it.copy(registerConfirmPassword = value, registerConfirmPasswordError = null, authStatusMessage = null) }
    }

    fun toggleRegisterPasswordVisibility() {
        _uiState.update { it.copy(isRegisterPasswordVisible = !it.isRegisterPasswordVisible) }
    }

    fun toggleRegisterConfirmPasswordVisibility() {
        _uiState.update { it.copy(isRegisterConfirmPasswordVisible = !it.isRegisterConfirmPasswordVisible) }
    }

    fun onCreateAccountClick() {
        val state = _uiState.value

        if (!state.isTermsAccepted) {
            _uiState.update {
                it.copy(
                    authStatusMessage = "অনুগ্রহ করে নিয়ম ও শর্তাবলি এবং গোপনীয়তা নীতিতে সম্মতি দিন।",
                    isSuccessMessage = false
                )
            }
            return
        }

        val fullName = state.registerFullName.trim()
        val rawInput = state.registerIdentifier.trim()
        val pass = state.registerPassword
        val confirmPass = state.registerConfirmPassword

        var hasError = false
        var nameErr: String? = null
        var idErr: String? = null
        var passErr: String? = null
        var confirmErr: String? = null

        if (fullName.isEmpty()) {
            nameErr = "আপনার পূর্ণ নাম লিখুন"
            hasError = true
        }

        if (rawInput.isEmpty()) {
            idErr = "ইমেইল বা ইউজারনেম লিখুন"
            hasError = true
        } else if (rawInput.contains("@") && !android.util.Patterns.EMAIL_ADDRESS.matcher(rawInput).matches()) {
            idErr = "সঠিক ইমেইল ফরম্যাট দিন (যেমন: name@example.com)"
            hasError = true
        }

        if (pass.length < 6) {
            passErr = "পাসওয়ার্ড কমপক্ষে ৬ অক্ষরের হতে হবে"
            hasError = true
        }

        if (pass != confirmPass) {
            confirmErr = "পাসওয়ার্ড দুটি মেলেনি"
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    registerNameError = nameErr,
                    registerIdentifierError = idErr,
                    registerPasswordError = passErr,
                    registerConfirmPasswordError = confirmErr,
                    authStatusMessage = null
                )
            }
            return
        }

        val resolvedEmail = if (rawInput.contains("@")) {
            rawInput
        } else {
            "${rawInput.replace(" ", "").lowercase()}@kaomi.com"
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, authStatusMessage = null) }
            try {
                withTimeout(10000L) {
                    val result = authRepo.createUserWithEmail(resolvedEmail, pass, fullName)
                    result.fold(
                        onSuccess = { user ->
                            registerFcmToken(user.uid)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    currentUser = user,
                                    authStatusMessage = "অ্যাকাউন্ট সফলভাবে তৈরি হয়েছে!",
                                    isSuccessMessage = true
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    authStatusMessage = error.message ?: "অ্যাকাউন্ট তৈরি ব্যর্থ হয়েছে।",
                                    isSuccessMessage = false
                                )
                            }
                        }
                    )
                }
            } catch (e: TimeoutCancellationException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        authStatusMessage = "অ্যাকাউন্ট তৈরির সময় শেষ হয়েছে। নেটওয়ার্ক চেক করে আবার চেষ্টা করুন।",
                        isSuccessMessage = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        authStatusMessage = e.localizedMessage ?: "অ্যাকাউন্ট তৈরি ব্যর্থ হয়েছে।",
                        isSuccessMessage = false
                    )
                }
            }
        }
    }

    // Google Sign-In Handler
    fun handleGoogleIdToken(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, authStatusMessage = null) }
            try {
                withTimeout(12000L) {
                    val result = authRepo.signInWithGoogleCredential(idToken)
                    result.fold(
                        onSuccess = { user ->
                            registerFcmToken(user.uid)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    currentUser = user,
                                    authStatusMessage = "গুগল অ্যাকাউন্ট দিয়ে সফলভাবে প্রবেশ করা হয়েছে!",
                                    isSuccessMessage = true
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    authStatusMessage = error.message ?: "গুগল সাইন-ইন ব্যর্থ হয়েছে।",
                                    isSuccessMessage = false
                                )
                            }
                        }
                    )
                }
            } catch (e: TimeoutCancellationException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        authStatusMessage = "গুগল সাইন-ইন সংযোগের সময় শেষ হয়েছে।",
                        isSuccessMessage = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        authStatusMessage = e.localizedMessage ?: "গুগল সাইন-ইন ব্যর্থ হয়েছে।",
                        isSuccessMessage = false
                    )
                }
            }
        }
    }

    fun onGoogleSignInError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                authStatusMessage = message,
                isSuccessMessage = false
            )
        }
    }

    fun onGoogleSignInCancelled() {
        _uiState.update { it.copy(isLoading = false) }
    }

    // Forgot Password Handlers
    fun showForgotPassword(show: Boolean) {
        _uiState.update {
            it.copy(
                showForgotPasswordDialog = show,
                forgotEmail = it.identifier,
                forgotEmailError = null,
                forgotSuccessMessage = null
            )
        }
    }

    fun updateForgotEmail(value: String) {
        _uiState.update { it.copy(forgotEmail = value, forgotEmailError = null) }
    }

    fun sendPasswordResetEmail() {
        val email = _uiState.value.forgotEmail.trim()
        if (email.isEmpty()) {
            _uiState.update { it.copy(forgotEmailError = "ইমেইল ঠিকানা লিখুন") }
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(forgotEmailError = "সঠিক ইমেইল ফরম্যাট দিন") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, forgotEmailError = null) }
            try {
                withTimeout(10000L) {
                    val result = authRepo.sendPasswordResetEmail(email)
                    result.fold(
                        onSuccess = {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    forgotSuccessMessage = "পাসওয়ার্ড রিসেট লিঙ্কটি আপনার ইমেইলে পাঠানো হয়েছে। অনুগ্রহ করে ইনবক্স অথবা স্প্যাম ফোল্ডার চেক করুন।"
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    forgotEmailError = error.message ?: "পাসওয়ার্ড রিসেট ব্যর্থ হয়েছে।"
                                )
                            }
                        }
                    )
                }
            } catch (e: TimeoutCancellationException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        forgotEmailError = "অনুরোধের সময় শেষ হয়েছে। পুনরায় চেষ্টা করুন।"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        forgotEmailError = e.localizedMessage ?: "পাসওয়ার্ড রিসেট ব্যর্থ হয়েছে।"
                    )
                }
            }
        }
    }

    // Main Screen & Appearance Handlers
    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun enterGuestMode() {
        _uiState.update { it.copy(isGuestMode = true, authStatusMessage = null) }
    }

    fun exitGuestMode() {
        _uiState.update { it.copy(isGuestMode = false) }
    }

    // Sign Out
    fun signOut(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Optional: Clear token on server if needed
            authRepo.signOut(context)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentUser = null,
                    isGuestMode = false,
                    authStatusMessage = "সফলভাবে লগআউট হয়েছে।",
                    isSuccessMessage = true,
                    isFlippedToRegister = false
                )
            }
        }
    }

    private fun registerFcmToken(userId: String) {
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000L) // Wait for Firebase to warm up
            android.util.Log.d("AuthViewModel", "Attempting to register FCM token for user: $userId")
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    android.util.Log.d("AuthViewModel", "FCM token retrieved successfully")
                    if (token != null) {
                        viewModelScope.launch {
                            val result = noticeRepo.updateFcmToken(userId, token)
                            if (result.isSuccess) {
                                android.util.Log.d("AuthViewModel", "FCM token updated in Firestore successfully")
                            } else {
                                android.util.Log.e("AuthViewModel", "FCM token update in Firestore failed", result.exceptionOrNull())
                            }
                        }
                    }
                } else {
                    android.util.Log.e("AuthViewModel", "FCM token retrieval failed: ${task.exception?.message}", task.exception)
                }
            }
        }
    }

    fun dismissMessage() {
        _uiState.update { it.copy(authStatusMessage = null) }
    }
}

class AuthViewModelFactory(
    private val application: android.app.Application
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
