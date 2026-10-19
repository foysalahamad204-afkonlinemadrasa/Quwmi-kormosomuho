package com.example.ui.menu

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HindSiliguriFamily
import com.google.firebase.auth.FirebaseUser
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicEmerald
import com.example.ui.theme.IslamicTeal
import com.example.ui.theme.IslamicNavy
import com.example.ui.theme.IslamicNavySurface
import com.example.ui.theme.IslamicNavyCard
import com.example.ui.theme.IslamicIvory
import com.example.ui.theme.IslamicIvorySecondary
import com.example.ui.theme.IslamicIvoryMuted
import com.example.ui.theme.IslamicGoldDivider
import com.example.ui.theme.IslamicLogoutRed
import com.example.ui.theme.IslamicLogoutBg

/**
 * Hamburger Menu Button (☰ হ্যামবার্গার মেনু বাটন)
 * Placed at the top-left of the authenticated main screen.
 * Icons are in gold (#F4D58A) on the dark emerald header (#123D32).
 */
@Composable
fun IslamicHamburgerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    testTag: String = "btn_hamburger_menu"
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isDarkMode) IslamicNavySurface else IslamicGold.copy(alpha = 0.15f)
                )
                .border(
                    width = 1.dp,
                    color = IslamicGold.copy(alpha = 0.65f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "মেনু খুলুন",
                tint = IslamicGold,
                modifier = Modifier.size(23.dp)
            )
        }
    }
}

/**
 * Functional Night Mode Toggle Button (নাইট মোড টগল বাটন)
 * Placed at the top-right of the authenticated main screen.
 * Icons are in gold (#F4D58A) on the dark emerald header (#123D32).
 */
@Composable
fun IslamicNightModeButton(
    isDarkMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "btn_night_mode_toggle"
) {
    IconButton(
        onClick = onToggle,
        modifier = modifier
            .size(48.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isDarkMode) IslamicNavySurface else IslamicGold.copy(alpha = 0.15f)
                )
                .border(
                    width = 1.dp,
                    color = IslamicGold.copy(alpha = 0.65f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                contentDescription = if (isDarkMode) "লাইট মোড চালু করুন" else "নাইট মোড চালু করুন",
                tint = IslamicGold,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * Three-Dot Menu Button (তিন ডট মেনু আইকন)
 */
@Composable
fun IslamicThreeDotButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    testTag: String = "btn_three_dot_menu"
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(48.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isDarkMode) IslamicNavySurface else IslamicEmerald.copy(alpha = 0.12f))
                .border(
                    width = 1.2.dp,
                    color = IslamicGold.copy(alpha = 0.45f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "ইসলামিক মেনু",
                tint = if (isDarkMode) IslamicGold else IslamicEmerald,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * Complete Islamic Side Menu Drawer (বাম পাশের সম্পূর্ণ ইসলামিক স্লাইডিং মেনু)
 * Slides in from the LEFT edge with an elegant Islamic color palette and motifs.
 */
@Composable
fun IslamicSideMenu(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentUser: FirebaseUser?,
    isDarkMode: Boolean,
    onToggleNightMode: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onSignOut: () -> Unit,
    onMenuItemClick: (String) -> Unit = {}
) {
    BackHandler(enabled = isOpen) {
        onDismiss()
    }

    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn(animationSpec = tween(220)),
        exit = fadeOut(animationSpec = tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.60f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        )
    }

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(durationMillis = 280)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(durationMillis = 240)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 330.dp)
                    .fillMaxWidth(0.85f)
                    .background(if (isDarkMode) IslamicNavy else Color(0xFFF0EBE0))
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            listOf(IslamicGold.copy(alpha = 0.6f), IslamicTeal.copy(alpha = 0.2f), Color.Transparent)
                        ),
                        shape = RoundedCornerShape(topEnd = 22.dp, bottomEnd = 22.dp)
                    )
                    .clip(RoundedCornerShape(topEnd = 22.dp, bottomEnd = 22.dp))
                    .testTag("islamic_side_menu_drawer")
            ) {
                // Header: Islamic Arch & Branding
                IslamicMenuHeader(
                    currentUser = currentUser,
                    isDarkMode = isDarkMode,
                    onToggleNightMode = onToggleNightMode,
                    onClose = onDismiss
                )

                // Scrollable Menu Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    // প্রথম বিভাগ (First Section)
                    IslamicMenuItemRow(
                        title = "১. আমার আবেদন",
                        icon = Icons.AutoMirrored.Outlined.Assignment,
                        iconTint = IslamicGold,
                        testTag = "menu_item_my_applications",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onMenuItemClick("আমার আবেদন")
                            onDismiss()
                        }
                    )

                    IslamicMenuItemRow(
                        title = "২. বিজ্ঞপ্তি ও ঘোষণা",
                        icon = Icons.Outlined.Campaign,
                        iconTint = IslamicTeal,
                        testTag = "menu_item_notices",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onMenuItemClick("বিজ্ঞপ্তি ও ঘোষণা")
                            onDismiss()
                        }
                    )

                    IslamicMenuItemRow(
                        title = "৩. সংরক্ষিত বিজ্ঞপ্তি",
                        icon = Icons.Outlined.BookmarkBorder,
                        iconTint = IslamicGold,
                        testTag = "menu_item_saved_notices",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onMenuItemClick("সংরক্ষিত বিজ্ঞপ্তি")
                            onDismiss()
                        }
                    )

                    IslamicMenuItemRow(
                        title = "৪. যোগাযোগ ও সহযোগিতা",
                        icon = Icons.Outlined.SupportAgent,
                        iconTint = IslamicTeal,
                        testTag = "menu_item_contact_support",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onMenuItemClick("যোগাযোগ ও সহযোগিতা")
                            onDismiss()
                        }
                    )

                    IslamicMenuItemRow(
                        title = "৫. আমার অ্যাকাউন্ট",
                        icon = Icons.Outlined.AccountCircle,
                        iconTint = IslamicGold,
                        testTag = "menu_item_my_account",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onMenuItemClick("আমার অ্যাকাউন্ট")
                            onDismiss()
                        }
                    )

                    IslamicMenuItemRow(
                        title = "৬. সেটিংস",
                        icon = Icons.Outlined.Settings,
                        iconTint = IslamicTeal,
                        testTag = "menu_item_settings",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onMenuItemClick("সেটিংস")
                            onDismiss()
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // দ্বিতীয় বিভাগ: অ্যাপের তথ্য ও নীতিমালা (Section Divider)
                    IslamicSectionDivider(title = "অ্যাপের তথ্য ও নীতিমালা")

                    Spacer(modifier = Modifier.height(8.dp))

                    IslamicMenuItemRow(
                        title = "৭. অ্যাপ সম্পর্কে",
                        icon = Icons.Outlined.Info,
                        iconTint = IslamicIvorySecondary,
                        testTag = "menu_item_about_app",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onMenuItemClick("অ্যাপ সম্পর্কে")
                            onDismiss()
                        }
                    )

                    IslamicMenuItemRow(
                        title = "৮. ডেভেলপার পরিচিতি",
                        icon = Icons.Outlined.Code,
                        iconTint = IslamicIvorySecondary,
                        testTag = "menu_item_developer_info",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onMenuItemClick("ডেভেলপার পরিচিতি")
                            onDismiss()
                        }
                    )

                    IslamicMenuItemRow(
                        title = "৯. গোপনীয়তা নীতি",
                        icon = Icons.Outlined.Security,
                        iconTint = IslamicGold,
                        testTag = "menu_item_privacy_policy",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onPrivacyClick()
                            onDismiss()
                        }
                    )

                    IslamicMenuItemRow(
                        title = "১০. নিয়মাবলি ও শর্তাবলি",
                        icon = Icons.Outlined.Gavel,
                        iconTint = IslamicGold,
                        testTag = "menu_item_terms_and_conditions",
                        isDarkMode = isDarkMode,
                        onClick = {
                            onTermsClick()
                            onDismiss()
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }

                // একেবারে নিচে: লগআউট (Bottom Section Logout)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(IslamicLogoutBg)
                            .border(1.dp, IslamicLogoutRed.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = IslamicLogoutRed.copy(alpha = 0.25f)),
                                onClick = {
                                    onDismiss()
                                    onSignOut()
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 13.dp)
                            .testTag("menu_item_logout"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(IslamicLogoutRed.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "লগআউট",
                                tint = IslamicLogoutRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "লগআউট",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicLogoutRed
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Islamic Header for the Side Menu Drawer
 */
@Composable
private fun IslamicMenuHeader(
    currentUser: FirebaseUser?,
    isDarkMode: Boolean,
    onToggleNightMode: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(IslamicEmerald, IslamicNavySurface)
                )
            )
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(IslamicNavy)
                            .border(1.2.dp, IslamicGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Mosque,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "কওমি কর্মসংস্থান",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = IslamicIvory
                            )
                        )
                        Text(
                            text = "দ্বীনি ও কর্মসংস্থান সেবা",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 11.5.sp,
                                color = IslamicGold
                              )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Night Mode Toggle inside Menu
                    IslamicNightModeButton(
                        isDarkMode = isDarkMode,
                        onToggle = onToggleNightMode,
                        testTag = "btn_night_mode_toggle_menu"
                    )

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_close_side_menu")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "মেনু বন্ধ করুন",
                            tint = IslamicIvorySecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // User Info Pill if logged in
            if (currentUser != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(IslamicNavy.copy(alpha = 0.55f))
                        .border(1.dp, IslamicTeal.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(IslamicTeal)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentUser.displayName ?: currentUser.email ?: "নিবন্ধিত অ্যাকাউন্ট",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = IslamicIvory
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * Single Row for an Islamic Menu Item
 */
@Composable
private fun IslamicMenuItemRow(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    testTag: String,
    isDarkMode: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = IslamicGold.copy(alpha = 0.20f)),
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 11.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isDarkMode) IslamicNavyCard else IslamicGold.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isDarkMode) IslamicIvory else Color(0xFF123D32)
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = if (isDarkMode) IslamicIvoryMuted else Color(0xFF123D32).copy(alpha = 0.4f),
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * Section Divider with Islamic motif styling
 */
@Composable
private fun IslamicSectionDivider(
    title: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(IslamicGoldDivider)
            )
            Text(
                text = "✦  $title  ✦",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IslamicGold
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(IslamicGoldDivider)
            )
        }
    }
}
