package com.example.ui.auth

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.example.ui.main.KaomiMainScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.example.ui.menu.IslamicSideMenu
import com.example.ui.menu.IslamicThreeDotButton
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicCard
import com.example.ui.components.NeumorphicCircularAvatar
import com.example.ui.components.NeumorphicGoogleButton
import com.example.ui.components.NeumorphicInputField
import com.example.ui.theme.BrandRed
import com.example.ui.theme.BrandRedDark
import com.example.ui.theme.BrandRedSurface
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.HindSiliguriFamily
import com.example.ui.theme.NeumorphicBg
import com.example.ui.theme.NeumorphicDarkShadow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    initialNoticeId: String? = null
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isIslamicMenuOpen by remember { mutableStateOf(false) }

    // 3D Flip Card Animation (preserving original code-with-nazia.css rotateY transition)
    val flipRotation by animateFloatAsState(
        targetValue = if (state.isFlippedToRegister) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "3d_card_flip"
    )

    val isBackFace = flipRotation > 90f
    val cardRotation = if (!isBackFace) flipRotation else flipRotation - 180f

    // Trigger Google Sign-In with Jetpack Credential Manager
    val startGoogleSignIn = {
        val activity = context as? Activity
        if (activity == null) {
            viewModel.onGoogleSignInError("অ্যাপ্লিকেশনের অ্যাক্টিভিটি পাওয়া যায়নি।")
        } else {
            val serverClientId = try {
                context.getString(R.string.default_web_client_id)
            } catch (e: Exception) {
                null
            } ?: "965861087688-ohi5fs9r30h7e57kg3u5vetvmkg9j48v.apps.googleusercontent.com"
                val credentialManager = CredentialManager.create(context)
                val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = serverClientId).build()
                val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

                coroutineScope.launch {
                    try {
                        val result = credentialManager.getCredential(activity, request)
                        val credential = result.credential
                        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                            viewModel.handleGoogleIdToken(googleIdToken)
                        } else {
                            viewModel.onGoogleSignInError("অপ্রত্যাশিত গুগল ক্রেডেনশিয়াল পাওয়া গেছে।")
                        }
                    } catch (e: GetCredentialCancellationException) {
                        Log.w("AuthScreen", "Google Sign-In flow cancelled by user: ${e.message}")
                        viewModel.onGoogleSignInCancelled()
                    } catch (e: Exception) {
                        Log.e("AuthScreen", "Google Sign-In failed", e)
                        viewModel.onGoogleSignInError("গুগল সাইন-ইন সম্পন্ন করা সম্ভব হয়নি: ${e.localizedMessage ?: "অজানা ত্রুটি"}")
                    }
                }
            }
        }

    // If user is currently authenticated with Firebase or in guest mode,
    // immediately show the dedicated, standalone Islamic Main Screen!
    if (state.currentUser != null || state.isGuestMode) {
        KaomiMainScreen(
            state = state,
            authViewModel = viewModel,
            modifier = modifier,
            initialNoticeId = initialNoticeId
        )
        return
    }

    val isDarkMode = state.isDarkMode
    val currentBg = if (isDarkMode) Color(0xFF0A1F1A) else NeumorphicBg
    val cardBg = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextPrimary
    val subTextColor = if (isDarkMode) Color(0xFF9FB2A8) else TextSecondary

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(currentBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Branding Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BrandRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mosque,
                                contentDescription = null,
                                tint = NeumorphicBg,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "কওমি কর্মসংস্থান বাংলাদেশ",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = if (isDarkMode) Color(0xFFF4D58A) else BrandRedDark
                            ),
                            modifier = Modifier.testTag("app_title_text")
                        )
                    }

                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "কওমি মাদরাসা ও দ্বীনি প্রতিষ্ঠানের কর্মসংস্থান প্ল্যাটফর্ম",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 13.sp,
                        color = if (isDarkMode) Color(0xFF9FB2A8) else TextSecondary,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )
            }

            // Neumorphic Card Container - graphicsLayer attached ONLY while actively animating
            val isActivelyFlipping = flipRotation > 1f && flipRotation < 179f
            val cardModifier = if (isActivelyFlipping && state.currentUser == null) {
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
                    .graphicsLayer {
                        rotationY = cardRotation
                        cameraDistance = 12f
                    }
                    .testTag("neumorphic_card_container")
            } else {
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
                    .testTag("neumorphic_card_container")
            }

            NeumorphicCard(
                modifier = cardModifier,
                cornerRadius = 38.dp,
                backgroundColor = cardBg,
                isDarkMode = isDarkMode
            ) {
                // If user is currently authenticated with Firebase
                if (state.currentUser != null) {
                    LoggedInSessionCard(
                        state = state,
                        isDarkMode = isDarkMode,
                        onSignOut = { viewModel.signOut(context) }
                    )
                } else {
                    val handleGoogleSignIn: () -> Unit = {
                        if (!state.isTermsAccepted) {
                            viewModel.setTermsError("অনুগ্রহ করে নিয়ম ও শর্তাবলি এবং গোপনীয়তা নীতিতে সম্মতি দিন।")
                        } else {
                            startGoogleSignIn()
                        }
                    }

                    if (isBackFace) {
                        // Back face: নতুন অ্যাকাউন্ট তৈরি করুন
                        CreateAccountForm(
                            state = state,
                            viewModel = viewModel,
                            onGoogleClick = handleGoogleSignIn,
                            isDarkMode = isDarkMode,
                            textColor = textColor,
                            subTextColor = subTextColor
                        )
                    } else {
                        // Front face: Login Form
                        LoginForm(
                            state = state,
                            viewModel = viewModel,
                            onGoogleClick = handleGoogleSignIn,
                            isDarkMode = isDarkMode,
                            textColor = textColor,
                            subTextColor = subTextColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Direct Main Page Preview / Guest Entry Button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = BrandRed),
                        onClick = { viewModel.enterGuestMode() }
                    )
                    .background(if (isDarkMode) Color(0xFF133B32) else NeumorphicBg)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("btn_guest_enter_main"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Mosque,
                    contentDescription = null,
                    tint = BrandRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "অতিথি হিসেবে সরাসরি মূল পেজ দেখুন",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkMode) com.example.ui.theme.IslamicGold else BrandRedDark
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer info
            Text(
                text = "কওমি কর্মসংস্থান বাংলাদেশ © ২০২৬",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 12.sp,
                    color = if (isDarkMode) Color(0xFF9FB2A8).copy(alpha = 0.7f) else TextMuted
                )
            )
        }

        // Terms & Conditions Modal
        if (state.showTermsModal) {
            TermsAndConditionsModal(
                onDismiss = { viewModel.showTermsModal(false) },
                onAcceptAndDismiss = {
                    viewModel.updateTermsAccepted(true)
                    viewModel.showTermsModal(false)
                },
                isDarkMode = isDarkMode
            )
        }

        // Privacy Policy Modal
        if (state.showPrivacyModal) {
            PrivacyPolicyModal(
                onDismiss = { viewModel.showPrivacyModal(false) },
                onAcceptAndDismiss = {
                    viewModel.updateTermsAccepted(true)
                    viewModel.showPrivacyModal(false)
                },
                isDarkMode = isDarkMode
            )
        }

        // Forgot Password Dialog
        if (state.showForgotPasswordDialog) {
            ForgotPasswordModal(
                state = state,
                onDismiss = { viewModel.showForgotPassword(false) },
                onEmailChange = { viewModel.updateForgotEmail(it) },
                onSendReset = { viewModel.sendPasswordResetEmail() },
                isDarkMode = isDarkMode
            )
        }

        // Islamic Side Menu Drawer
        IslamicSideMenu(
            isOpen = isIslamicMenuOpen,
            onDismiss = { isIslamicMenuOpen = false },
            currentUser = state.currentUser,
            isDarkMode = state.isDarkMode,
            onToggleNightMode = { viewModel.toggleDarkMode() },
            onTermsClick = { viewModel.showTermsModal(true) },
            onPrivacyClick = { viewModel.showPrivacyModal(true) },
            onSignOut = { viewModel.signOut(context) }
        )
    }
}

/**
 * Authenticated Session Card:
 * Handles Login State Management and Logout directly in the Neumorphic Card
 * without adding Dashboard, Profile, or extra pages.
 */
@Composable
private fun LoggedInSessionCard(
    state: AuthUiState,
    isDarkMode: Boolean,
    onSignOut: () -> Unit
) {
    val user = state.currentUser ?: return
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextPrimary
    val subTextColor = if (isDarkMode) Color(0xFF9FB2A8) else TextSecondary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp, vertical = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NeumorphicCircularAvatar(
            sizeDp = 92.dp,
            icon = Icons.Filled.VerifiedUser,
            isDarkMode = isDarkMode,
            testTag = "authenticated_avatar"
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "লগইন সম্পন্ন হয়েছে",
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = textColor
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "কওমি কর্মসংস্থান বাংলাদেশ-এ স্বাগতম",
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontSize = 14.sp,
                color = EmeraldGreen,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // User Account Details Pill
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(EmeraldGreen.copy(alpha = 0.08f))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!user.displayName.isNullOrBlank()) {
                    Text(
                        text = user.displayName!!,
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = user.email ?: "ইমেইল তথ্য সংযুক্ত",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 13.sp,
                        color = subTextColor
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Firebase Authentication সক্রিয়",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EmeraldGreen
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            CircularProgressIndicator(color = BrandRed, modifier = Modifier.size(36.dp))
        } else {
            NeumorphicButton(
                text = "লগআউট করুন",
                onClick = onSignOut,
                leadingIcon = Icons.Filled.ExitToApp,
                testTag = "btn_sign_out"
            )
        }
    }
}

/**
 * Front Face: Login Form
 */
@Composable
private fun LoginForm(
    state: AuthUiState,
    viewModel: AuthViewModel,
    onGoogleClick: () -> Unit,
    isDarkMode: Boolean,
    textColor: Color,
    subTextColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular Avatar with Inner Ring & soft dual shadow
        NeumorphicCircularAvatar(
            sizeDp = 92.dp,
            icon = Icons.Filled.Mosque,
            testTag = "login_circular_avatar"
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Heading: লগইন
        Text(
            text = "লগইন",
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = textColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("login_heading")
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Subtitle: আপনার অ্যাকাউন্টে প্রবেশ করুন
        Text(
            text = "আপনার অ্যাকাউন্টে প্রবেশ করুন",
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = subTextColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("login_subtitle")
        )

        // Status Feedback Banner
        AnimatedVisibility(
            visible = state.authStatusMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (state.authStatusMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (state.isSuccessMessage) EmeraldGreen.copy(alpha = 0.12f)
                            else BrandRedSurface
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (state.isSuccessMessage) Icons.Filled.CheckCircle else Icons.Filled.Info,
                            contentDescription = null,
                            tint = if (state.isSuccessMessage) EmeraldGreen else BrandRed,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.authStatusMessage!!,
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (state.isSuccessMessage) EmeraldGreen else BrandRedDark,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Email or username input (Inset neumorphic field)
        NeumorphicInputField(
            value = state.identifier,
            onValueChange = { viewModel.updateIdentifier(it) },
            placeholder = "ইমেইল বা ইউজারনেম",
            leadingIcon = Icons.Filled.Email,
            errorMessage = state.identifierError,
            isDarkMode = isDarkMode,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            testTag = "input_email_or_username"
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Password input with show/hide password control (Inset neumorphic field)
        NeumorphicInputField(
            value = state.password,
            onValueChange = { viewModel.updatePassword(it) },
            placeholder = "পাসওয়ার্ড",
            leadingIcon = Icons.Filled.Lock,
            isPassword = true,
            errorMessage = state.passwordError,
            isDarkMode = isDarkMode,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onLoginClick() }
            ),
            testTag = "input_password"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // "পাসওয়ার্ড ভুলে গেছেন?" (Forgot password link)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "পাসওয়ার্ড ভুলে গেছেন?",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandRed
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = BrandRed.copy(alpha = 0.25f))
                    ) { viewModel.showForgotPassword(true) }
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .testTag("link_forgot_password")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Terms and Privacy Policy Consent Checkbox
        TermsAndPrivacyConsentCheckbox(
            checked = state.isTermsAccepted,
            onCheckedChange = { viewModel.updateTermsAccepted(it) },
            onTermsClick = { viewModel.showTermsModal(true) },
            onPrivacyClick = { viewModel.showPrivacyModal(true) },
            isDarkMode = isDarkMode,
            testTag = "checkbox_terms_login"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Login button labeled "প্রবেশ করুন" (Red gradient neumorphic button)
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrandRed, strokeWidth = 3.dp, modifier = Modifier.size(32.dp))
            }
        } else {
            NeumorphicButton(
                text = "প্রবেশ করুন",
                onClick = { viewModel.onLoginClick() },
                testTag = "btn_login"
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Divider with "অথবা"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(NeumorphicDarkShadow.copy(alpha = 0.5f))
            )
            Text(
                text = "অথবা",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 12.5.sp,
                    color = TextMuted
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(NeumorphicDarkShadow.copy(alpha = 0.5f))
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Google Sign-In Button labeled "গুগল দিয়ে এগিয়ে যান"
        NeumorphicGoogleButton(
            onClick = onGoogleClick,
            isDarkMode = isDarkMode,
            testTag = "btn_google_sign_in"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // "নতুন অ্যাকাউন্ট তৈরি করুন" Link with Flip Animation (100% Bengali)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "অ্যাকাউন্ট নেই? ",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.5.sp,
                    color = subTextColor
                )
            )
            Text(
                text = "নতুন অ্যাকাউন্ট তৈরি করুন",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color(0xFFF4D58A) else BrandRed
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = if (isDarkMode) Color(0xFFF4D58A).copy(alpha = 0.25f) else BrandRed.copy(alpha = 0.25f))
                    ) { viewModel.toggleFlip(true) }
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .testTag("link_create_account")
            )
        }
    }
}

/**
 * Back Face: নতুন অ্যাকাউন্ট তৈরি করুন Form
 */
@Composable
private fun CreateAccountForm(
    state: AuthUiState,
    viewModel: AuthViewModel,
    onGoogleClick: () -> Unit,
    isDarkMode: Boolean,
    textColor: Color,
    subTextColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular Avatar with Inner Ring & PersonAdd icon
        NeumorphicCircularAvatar(
            sizeDp = 86.dp,
            icon = Icons.Filled.PersonAdd,
            testTag = "create_account_avatar"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Heading: নতুন অ্যাকাউন্ট তৈরি করুন
        Text(
            text = "নতুন অ্যাকাউন্ট তৈরি করুন",
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = textColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("register_heading")
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Subtitle: আপনার তথ্য দিয়ে নিবন্ধন করুন
        Text(
            text = "আপনার তথ্য দিয়ে নিবন্ধন করুন",
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.5.sp,
                color = subTextColor
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.testTag("register_subtitle")
        )

        // Status Feedback Banner
        AnimatedVisibility(
            visible = state.authStatusMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (state.authStatusMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (state.isSuccessMessage) EmeraldGreen.copy(alpha = 0.12f)
                            else BrandRedSurface
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (state.isSuccessMessage) Icons.Filled.CheckCircle else Icons.Filled.Info,
                            contentDescription = null,
                            tint = if (state.isSuccessMessage) EmeraldGreen else BrandRed,
                            modifier = Modifier
                                .size(18.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.authStatusMessage!!,
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (state.isSuccessMessage) EmeraldGreen else BrandRedDark,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Full Name Input
        NeumorphicInputField(
            value = state.registerFullName,
            onValueChange = { viewModel.updateRegisterFullName(it) },
            placeholder = "পূর্ণ নাম",
            leadingIcon = Icons.Filled.Person,
            errorMessage = state.registerNameError,
            isDarkMode = isDarkMode,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            testTag = "input_register_full_name"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Email or Username Input
        NeumorphicInputField(
            value = state.registerIdentifier,
            onValueChange = { viewModel.updateRegisterIdentifier(it) },
            placeholder = "ইমেইল ঠিকানা",
            leadingIcon = Icons.Filled.Email,
            errorMessage = state.registerIdentifierError,
            isDarkMode = isDarkMode,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            testTag = "input_register_email"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Password Input
        NeumorphicInputField(
            value = state.registerPassword,
            onValueChange = { viewModel.updateRegisterPassword(it) },
            placeholder = "পাসওয়ার্ড (কমপক্ষে ৬ অক্ষর)",
            leadingIcon = Icons.Filled.Lock,
            isPassword = true,
            errorMessage = state.registerPasswordError,
            isDarkMode = isDarkMode,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            testTag = "input_register_password"
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Confirm Password Input
        NeumorphicInputField(
            value = state.registerConfirmPassword,
            onValueChange = { viewModel.updateRegisterConfirmPassword(it) },
            placeholder = "পাসওয়ার্ড নিশ্চিত করুন",
            leadingIcon = Icons.Filled.Lock,
            isPassword = true,
            errorMessage = state.registerConfirmPasswordError,
            isDarkMode = isDarkMode,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { viewModel.onCreateAccountClick() }
            ),
            testTag = "input_register_confirm_password"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Terms and Privacy Policy Consent Checkbox
        TermsAndPrivacyConsentCheckbox(
            checked = state.isTermsAccepted,
            onCheckedChange = { viewModel.updateTermsAccepted(it) },
            onTermsClick = { viewModel.showTermsModal(true) },
            onPrivacyClick = { viewModel.showPrivacyModal(true) },
            isDarkMode = isDarkMode,
            testTag = "checkbox_terms_register"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Create Account Button (Red gradient neumorphic button)
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BrandRed, strokeWidth = 3.dp, modifier = Modifier.size(32.dp))
            }
        } else {
            NeumorphicButton(
                text = "অ্যাকাউন্ট তৈরি করুন",
                onClick = { viewModel.onCreateAccountClick() },
                testTag = "btn_create_account"
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Divider with "অথবা"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(NeumorphicDarkShadow.copy(alpha = 0.5f))
            )
            Text(
                text = "অথবা",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 12.5.sp,
                    color = TextMuted
                ),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(NeumorphicDarkShadow.copy(alpha = 0.5f))
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Google Sign-In Button labeled "গুগল দিয়ে এগিয়ে যান"
        NeumorphicGoogleButton(
            onClick = onGoogleClick,
            isDarkMode = isDarkMode,
            testTag = "btn_google_sign_up"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Back to Login link with Flip Animation
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ইতিমধ্যে অ্যাকাউন্ট আছে? ",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.5.sp,
                    color = subTextColor
                )
            )
            Text(
                text = "প্রবেশ করুন",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color(0xFFF4D58A) else BrandRed
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = if (isDarkMode) Color(0xFFF4D58A).copy(alpha = 0.25f) else BrandRed.copy(alpha = 0.25f))
                    ) { viewModel.toggleFlip(false) }
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .testTag("link_back_to_login")
            )
        }
    }
}

/**
 * Forgot Password Dialog using Neumorphic card styling
 */
@Composable
private fun ForgotPasswordModal(
    state: AuthUiState,
    onDismiss: () -> Unit,
    onEmailChange: (String) -> Unit,
    onSendReset: () -> Unit,
    isDarkMode: Boolean = false
) {
    val cardBg = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextPrimary
    val subTextColor = if (isDarkMode) Color(0xFF9FB2A8) else TextSecondary

    Dialog(onDismissRequest = onDismiss) {
        NeumorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            cornerRadius = 28.dp,
            backgroundColor = cardBg,
            isDarkMode = isDarkMode
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "পাসওয়ার্ড পুনরুদ্ধার",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = textColor
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "বন্ধ", tint = if (isDarkMode) Color(0xFF9FB2A8) else TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "আপনার অ্যাকাউন্টের নিবন্ধিত ইমেইল ঠিকানাটি লিখুন। পাসওয়ার্ড রিসেট করার একটি লিঙ্ক আপনার ইমেইলে পাঠানো হবে।",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 13.sp,
                        color = subTextColor,
                        lineHeight = 18.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (!state.forgotSuccessMessage.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(EmeraldGreen.copy(alpha = 0.12f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = state.forgotSuccessMessage,
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 12.5.sp,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                NeumorphicInputField(
                    value = state.forgotEmail,
                    onValueChange = onEmailChange,
                    placeholder = "নিবন্ধিত ইমেইল ঠিকানা",
                    leadingIcon = Icons.Filled.Email,
                    errorMessage = state.forgotEmailError,
                    isDarkMode = isDarkMode,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSendReset() }),
                    testTag = "input_forgot_email"
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (state.isLoading) {
                    CircularProgressIndicator(color = BrandRed, modifier = Modifier.size(32.dp))
                } else {
                    NeumorphicButton(
                        text = "রিসেট লিঙ্ক পাঠান",
                        onClick = onSendReset,
                        testTag = "btn_send_reset_link"
                    )
                }
            }
        }
    }
}
