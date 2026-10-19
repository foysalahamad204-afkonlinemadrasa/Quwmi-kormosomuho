package com.example.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.auth.AuthUiState
import com.example.ui.auth.AuthViewModel
import com.example.ui.auth.PrivacyPolicyModal
import com.example.ui.auth.TermsAndConditionsModal
import com.example.ui.menu.IslamicHamburgerButton
import com.example.ui.menu.IslamicNightModeButton
import com.example.ui.menu.IslamicSideMenu
import com.example.ui.notice.NoticeListScreen
import com.example.ui.notice.MyApplicationsScreen
import com.example.ui.notice.SubmittedApplicationsScreen
import com.example.ui.notice.ApplicantProfileScreen
import com.example.ui.notice.NoticeViewModel
import com.example.ui.theme.HindSiliguriFamily
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

// MARK: - Exact Colors Specified
// - Main page background: #F4F1E8 (warm ivory/cream), not white (#FFFFFF)
// - Header: #123D32 (deep emerald green)
// - Main headings and text: #123D32
// - Important icons and borders: #F4D58A (soft gold)
// - Date section: deep green background with gold accents
// - Night mode: deep green and dark teal backgrounds with gold accents

private val DeepEmeraldGreen = Color(0xFF123D32)
private val SoftGold = Color(0xFFF4D58A)
private val WarmIvoryCream = Color(0xFFF4F1E8)

// Night Mode Colors:
private val NightDeepGreenBg = Color(0xFF0A1F1A) // deep green background
private val NightDarkTeal = Color(0xFF0E2E27)     // dark teal background
private val NightHeader = Color(0xFF071814)
private val NightIvoryText = Color(0xFFF4F1E8)
private val NightMutedText = Color(0xFF9FB2A8)

/**
 * KaomiMainScreen:
 * মূল পেজে সুনির্দিষ্ট রঙের সমন্বয়:
 * - সম্পূর্ণ মূল পেজের ব্যাকগ্রাউন্ড (স্বাগতম অংশের নিচের ফাঁকা জায়গাসহ): #F4F1E8 (ওয়ার্ম আইভরি), কোনো সাদা (#FFFFFF) নেই।
 * - হেডার: #123D32 (গাঢ় এমেরাল্ড সবুজ), লেখা সাদা, আইকন সোনালি (#F4D58A)।
 * - মূল শিরোনাম ও লেখা: #123D32 (গাঢ় সবুজ)।
 * - গুরুত্বপূর্ণ আইকন ও বর্ডার: #F4D58A (সোনালি)।
 * - তারিখের অংশ: deep green (#123D32) ব্যাকগ্রাউন্ড ও সোনালি (#F4D58A) অ্যাকসেন্ট।
 * - নাইট মোড: deep green (#0A1F1A) ও dark teal (#0E2E27) ব্যাকগ্রাউন্ড এবং সোনালি অ্যাকসেন্ট।
 */
@Composable
fun KaomiMainScreen(
    state: AuthUiState,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    initialNoticeId: String? = null
) {
    val noticeViewModel: NoticeViewModel = viewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isSideMenuOpen by remember { mutableStateOf(false) }
    var currentScreen by remember { mutableStateOf("home") }

    // Notification Permission Request
    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Handle permission result if needed
    }
    LaunchedEffect(Unit) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val isDarkMode = state.isDarkMode
    val noticeState by noticeViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(initialNoticeId) {
        if (!initialNoticeId.isNullOrBlank()) {
            currentScreen = "notices"
            noticeViewModel.selectNoticeById(initialNoticeId)
        }
    }

    // ব্যাক হ্যান্ডলার: সাইড মেনু খোলা থাকলে আগে সেটি বন্ধ করবে, অন্যথায় স্ক্রিন অনুযায়ী কাজ করবে
    androidx.activity.compose.BackHandler(enabled = isSideMenuOpen) {
        isSideMenuOpen = false
    }

    // Main page background: WarmIvoryCream (#F4F1E8) in light mode, NightDeepGreenBg (#0A1F1A) in night mode
    val currentBg = if (isDarkMode) NightDeepGreenBg else WarmIvoryCream

    // Real-time calculated dates
    val dateInfo = remember { IslamicDateUtils.getCurrentDates() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(currentBg)
    ) {
        if (currentScreen == "notices") {
            NoticeListScreen(
                state = noticeState,
                viewModel = noticeViewModel,
                isDarkMode = isDarkMode,
                onBack = { currentScreen = "home" },
                onOpenMenu = { isSideMenuOpen = true },
                modifier = Modifier.fillMaxSize()
            )
        } else if (currentScreen == "home") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(currentBg)
            ) {
                // Header: #123D32 (deep emerald green) with white title & #F4D58A icons
                MainTopAppBar(
                    isDarkMode = isDarkMode,
                    onOpenMenu = { isSideMenuOpen = true },
                    onToggleNightMode = { authViewModel.toggleDarkMode() }
                )

                // Entire scrollable area and empty space below welcome section uses #F4F1E8
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(currentBg)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(currentBg)
                            .testTag("kaomi_main_scroll_content"),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // তারিখের অংশ: deep green background (#123D32) with gold accents (#F4D58A)
                        item {
                            UnifiedDateBanner(
                                dates = dateInfo,
                                isDarkMode = isDarkMode
                            )
                        }

                        // মূল স্বাগতম ও শিরোনাম অংশ: background #F4F1E8, text #123D32, border #F4D58A
                        item {
                            MainWelcomeSection(
                                isDarkMode = isDarkMode
                            )
                        }
                    }

                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }

        // হ্যামবার্গার মেনু: নির্ধারিত ১০টি আইটেম ও নিচে লগআউট (অপরিবর্তিত)
        // This is now always at the bottom of the Box, so it's drawn OVER any screen content!
        IslamicSideMenu(
            isOpen = isSideMenuOpen,
            onDismiss = { isSideMenuOpen = false },
            currentUser = state.currentUser,
            isDarkMode = isDarkMode,
            onToggleNightMode = { authViewModel.toggleDarkMode() },
            onTermsClick = { authViewModel.showTermsModal(true) },
            onPrivacyClick = { authViewModel.showPrivacyModal(true) },
            onSignOut = { authViewModel.signOut(context) },
            onMenuItemClick = { itemTitle ->
                when (itemTitle) {
                    "আমার আবেদন" -> {
                        currentScreen = "my_applications"
                    }
                    "বিজ্ঞপ্তি ও ঘোষণা" -> {
                        noticeViewModel.setFilterOnlySaved(false)
                        noticeViewModel.selectNotice(null)
                        currentScreen = "notices"
                    }
                    "সংরক্ষিত বিজ্ঞপ্তি" -> {
                        noticeViewModel.setFilterOnlySaved(true)
                        noticeViewModel.selectNotice(null)
                        currentScreen = "notices"
                    }
                    else -> {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(itemTitle)
                        }
                    }
                }
            }
        )

        // Navigation screens
        when (currentScreen) {
            "notices" -> {
                NoticeListScreen(
                    state = noticeState,
                    viewModel = noticeViewModel,
                    isDarkMode = isDarkMode,
                    onBack = { currentScreen = "home" },
                    onOpenMenu = { isSideMenuOpen = true },
                    modifier = Modifier.fillMaxSize()
                )
            }
            "my_applications" -> {
                MyApplicationsScreen(
                    isDarkMode = isDarkMode,
                    onBack = { currentScreen = "home" },
                    onNavigateToNotices = { currentScreen = "submitted_applications" },
                    onNavigateToProfile = { currentScreen = "applicant_profile" }
                )
            }
            "submitted_applications" -> {
                SubmittedApplicationsScreen(
                    applications = noticeState.myApplications,
                    isDarkMode = isDarkMode,
                    onBack = { currentScreen = "my_applications" }
                )
            }
            "applicant_profile" -> {
                ApplicantProfileScreen(
                    isDarkMode = isDarkMode,
                    onBack = { currentScreen = "my_applications" }
                )
            }
            else -> {
                // Home content (existing Column)
            }
        }

        // Terms and Conditions Modal
        if (state.showTermsModal) {
            TermsAndConditionsModal(
                onDismiss = { authViewModel.showTermsModal(false) },
                onAcceptAndDismiss = { authViewModel.showTermsModal(false) },
                isDarkMode = isDarkMode
            )
        }

        // Privacy Policy Modal
        if (state.showPrivacyModal) {
            PrivacyPolicyModal(
                onDismiss = { authViewModel.showPrivacyModal(false) },
                onAcceptAndDismiss = { authViewModel.showPrivacyModal(false) },
                isDarkMode = isDarkMode
            )
        }
    }
}

/**
 * Top App Bar:
 * - হেডার ব্যাকগ্রাউন্ড: #123D32 (deep emerald green)
 * - লেখা: সাদা (#FFFFFF)
 * - আইকন: সোনালি (#F4D58A)
 */
@Composable
private fun MainTopAppBar(
    isDarkMode: Boolean,
    onOpenMenu: () -> Unit,
    onToggleNightMode: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(if (isDarkMode) NightHeader else DeepEmeraldGreen)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag("main_top_app_bar")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // বাম ওপরে: ☰ হ্যামবার্গার মেনু বাটন (সোনালি #F4D58A আইকন)
            IslamicHamburgerButton(
                onClick = onOpenMenu,
                isDarkMode = isDarkMode,
                testTag = "btn_hamburger_menu"
            )

            // হেডারের কেন্দ্র: লেখা সাদা এবং সোনালি প্রতীক
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SoftGold.copy(alpha = 0.20f))
                        .border(1.dp, SoftGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Mosque,
                        contentDescription = null,
                        tint = SoftGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "কওমি কর্মসংস্থান বাংলাদেশ",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                )
            }

            // ডান ওপরে: কার্যকর নাইট মোড টগল বাটন (সোনালি #F4D58A আইকন)
            IslamicNightModeButton(
                isDarkMode = isDarkMode,
                onToggle = onToggleNightMode,
                testTag = "btn_night_mode_toggle"
            )
        }
    }
}

/**
 * Date section:
 * - Background: deep green background (#123D32)
 * - Accents, border, icons, dividers & text: soft gold (#F4D58A)
 */
@Composable
private fun UnifiedDateBanner(
    dates: IslamicDateUtils.FormattedDates,
    isDarkMode: Boolean
) {
    val dateBg = if (isDarkMode) NightDarkTeal else DeepEmeraldGreen

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(dateBg)
            .border(
                width = 1.5.dp,
                color = SoftGold,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 13.dp)
            .testTag("unified_date_banner")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // ১. হিজরি তারিখ
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    text = "🌙",
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = dates.hijri,
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SoftGold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // সোনালি বিভাজক ১
            Box(
                modifier = Modifier
                    .height(14.dp)
                    .width(1.dp)
                    .background(SoftGold.copy(alpha = 0.70f))
                    .padding(horizontal = 2.dp)
            )

            // ২. বাংলা তারিখ
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    text = "🌾",
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = dates.bengali,
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkMode) NightIvoryText else SoftGold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // সোনালি বিভাজক ২
            Box(
                modifier = Modifier
                    .height(14.dp)
                    .width(1.dp)
                    .background(SoftGold.copy(alpha = 0.70f))
                    .padding(horizontal = 2.dp)
            )

            // ৩. ইংরেজি তারিখ
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    text = "📅",
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = dates.english,
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkMode) NightMutedText else SoftGold.copy(alpha = 0.92f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Main Welcome Section:
 * - Background: #F4F1E8 (warm ivory/cream), not white (#FFFFFF)
 * - Border: #F4D58A (soft gold)
 * - Main headings and text: #123D32 (deep emerald green)
 * - Important icon: #F4D58A (soft gold) on #123D32 circular badge
 */
@Composable
private fun MainWelcomeSection(
    isDarkMode: Boolean
) {
    val sectionBg = if (isDarkMode) NightDarkTeal else WarmIvoryCream
    val textColor = if (isDarkMode) NightIvoryText else DeepEmeraldGreen
    val welcomeColor = if (isDarkMode) SoftGold else DeepEmeraldGreen

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(sectionBg)
            .border(
                width = 1.5.dp,
                color = SoftGold,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 22.dp, vertical = 32.dp)
            .testTag("islamic_hero_card"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // ইসলামিক প্রতীক (গাঢ় এমেরাল্ড বৃত্তে সোনালি #F4D58A আইকন ও সোনালি বর্ডার)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(if (isDarkMode) NightDeepGreenBg else DeepEmeraldGreen)
                    .border(1.5.dp, SoftGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Mosque,
                    contentDescription = "কওমি প্রতীক",
                    tint = SoftGold,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // সাধারণ স্বাগতম বার্তা (#123D32)
            Text(
                text = "স্বাগতম",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = welcomeColor
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("welcome_greeting_text")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // অ্যাপের নাম “কওমি কর্মসংস্থান বাংলাদেশ” (#123D32)
            Text(
                text = "কওমি কর্মসংস্থান বাংলাদেশ",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("main_app_title_text")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // কওমি অঙ্গনের কর্মসংস্থানের বিশ্বস্ত ঠিকানা
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        if (isDarkMode) {
                            NightDeepGreenBg
                        } else {
                            DeepEmeraldGreen.copy(alpha = 0.08f)
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = SoftGold,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 7.dp)
                    .testTag("tagline_badge")
            ) {
                Text(
                    text = "কওমি অঙ্গনের কর্মসংস্থানের বিশ্বস্ত ঠিকানা",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) SoftGold else DeepEmeraldGreen
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
