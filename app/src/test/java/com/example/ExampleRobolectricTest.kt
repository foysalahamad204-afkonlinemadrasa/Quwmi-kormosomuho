package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.auth.AuthViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Before
  fun setUp() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    if (FirebaseApp.getApps(context).isEmpty()) {
      FirebaseApp.initializeApp(
        context,
        FirebaseOptions.Builder()
          .setApplicationId("1:965861087688:android:47537524f75238591f08fd")
          .setApiKey("AIzaSyBN_CzzzgqyoX-8s8hNF0rpS4-sB6y5nDA")
          .setProjectId("kaomi-kormosongsthan")
          .build()
      )
    }
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("কওমি কর্মসংস্থান বাংলাদেশ", appName)
  }

  @Test
  fun `auth viewmodel initial state and text input handling`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = AuthViewModel(app)
    val initialState = viewModel.uiState.value
    assertFalse(initialState.isFlippedToRegister)
    assertFalse(initialState.isTermsAccepted)
    assertFalse(initialState.showTermsModal)
    assertFalse(initialState.showPrivacyModal)
    assertEquals("", initialState.identifier)
    assertEquals("", initialState.password)

    viewModel.updateIdentifier("teacher@kaomi.edu")
    viewModel.updatePassword("secret123")

    val updatedState = viewModel.uiState.value
    assertEquals("teacher@kaomi.edu", updatedState.identifier)
    assertEquals("secret123", updatedState.password)

    // Flip to register
    viewModel.toggleFlip(true)
    assertTrue(viewModel.uiState.value.isFlippedToRegister)

    // Update registration inputs
    viewModel.updateRegisterFullName("মুফতি আহমদ")
    viewModel.updateRegisterIdentifier("ahmad@kaomi.edu")
    viewModel.updateRegisterPassword("123456")
    viewModel.updateRegisterConfirmPassword("123456")

    val regState = viewModel.uiState.value
    assertEquals("মুফতি আহমদ", regState.registerFullName)
    assertEquals("ahmad@kaomi.edu", regState.registerIdentifier)
    assertEquals("123456", regState.registerPassword)

    // Flip back to login
    viewModel.toggleFlip(false)
    assertFalse(viewModel.uiState.value.isFlippedToRegister)
  }

  @Test
  fun `terms and privacy consent prevents login when unchecked`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = AuthViewModel(app)
    assertFalse(viewModel.uiState.value.isTermsAccepted)

    // Attempt login without accepting terms
    viewModel.updateIdentifier("test@kaomi.edu")
    viewModel.updatePassword("password123")
    viewModel.onLoginClick()

    val state = viewModel.uiState.value
    assertEquals("অনুগ্রহ করে নিয়ম ও শর্তাবলি এবং গোপনীয়তা নীতিতে সম্মতি দিন।", state.authStatusMessage)
    assertFalse(state.isSuccessMessage)

    // Now accept terms
    viewModel.updateTermsAccepted(true)
    assertTrue(viewModel.uiState.value.isTermsAccepted)
  }

  @Test
  fun `terms and privacy modals open and close correctly`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = AuthViewModel(app)

    viewModel.showTermsModal(true)
    assertTrue(viewModel.uiState.value.showTermsModal)

    viewModel.showTermsModal(false)
    assertFalse(viewModel.uiState.value.showTermsModal)

    viewModel.showPrivacyModal(true)
    assertTrue(viewModel.uiState.value.showPrivacyModal)

    viewModel.showPrivacyModal(false)
    assertFalse(viewModel.uiState.value.showPrivacyModal)
  }

  @Test
  fun `login validation checks required fields after accepting terms`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = AuthViewModel(app)
    viewModel.updateTermsAccepted(true)
    viewModel.onLoginClick()
    val errorState = viewModel.uiState.value
    assertNotNull(errorState.identifierError)
    assertNotNull(errorState.passwordError)
  }

  @Test
  fun `islamic date utils generates hijri bengali english dates and contains no live word`() {
    val dates = com.example.ui.main.IslamicDateUtils.getCurrentDates()
    assertTrue(dates.hijri.isNotBlank())
    assertTrue(dates.bengali.isNotBlank())
    assertTrue(dates.english.isNotBlank())

    // Must never contain the word "লাইভ" or "Live"
    assertFalse(dates.hijri.contains("লাইভ"))
    assertFalse(dates.bengali.contains("লাইভ"))
    assertFalse(dates.english.contains("লাইভ"))
    assertFalse(dates.hijri.contains("Live", ignoreCase = true))
    assertFalse(dates.bengali.contains("Live", ignoreCase = true))
    assertFalse(dates.english.contains("Live", ignoreCase = true))

    // Bengali numerals check
    val bengaliNum = com.example.ui.main.IslamicDateUtils.toBengaliDigits(2026)
    assertEquals("২০২৬", bengaliNum)
  }

  @Test
  fun `dark mode and guest mode toggle correctly in auth viewmodel`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = AuthViewModel(app)
    assertFalse(viewModel.uiState.value.isDarkMode)
    assertFalse(viewModel.uiState.value.isGuestMode)

    viewModel.toggleDarkMode()
    assertTrue(viewModel.uiState.value.isDarkMode)

    viewModel.toggleDarkMode()
    assertFalse(viewModel.uiState.value.isDarkMode)

    viewModel.enterGuestMode()
    assertTrue(viewModel.uiState.value.isGuestMode)

    viewModel.exitGuestMode()
    assertFalse(viewModel.uiState.value.isGuestMode)
  }

  @Test
  fun `notice viewmodel handles filtering, search, and saved notices`() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val noticeViewModel = com.example.ui.notice.NoticeViewModel(context)
    val state = noticeViewModel.uiState.value

    // Search query
    noticeViewModel.updateSearchQuery("মুহাদ্দিস")
    assertEquals("মুহাদ্দিস", noticeViewModel.uiState.value.searchQuery)

    // Location filter
    noticeViewModel.updateLocationFilter("ঢাকা")
    assertEquals("ঢাকা", noticeViewModel.uiState.value.locationFilter)

    // Saved filter toggle
    noticeViewModel.setFilterOnlySaved(true)
    assertTrue(noticeViewModel.uiState.value.filterOnlySaved)
    noticeViewModel.setFilterOnlySaved(false)
    assertFalse(noticeViewModel.uiState.value.filterOnlySaved)

    // Admin email check
    val repo = com.example.data.notice.NoticeRepository(context)
    assertTrue(repo.isUserAdmin("foysalahamad102@gmail.com"))
    assertTrue(repo.isUserAdmin("admin@kaomi.edu.bd"))
    assertFalse(repo.isUserAdmin("user@gmail.com"))
  }

  @Test
  fun `notice viewmodel can be instantiated by android viewmodel factory`() {
    val app = ApplicationProvider.getApplicationContext<Context>() as android.app.Application
    val factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(app)
    val vm = factory.create(com.example.ui.notice.NoticeViewModel::class.java)
    assertNotNull(vm)
  }
}
