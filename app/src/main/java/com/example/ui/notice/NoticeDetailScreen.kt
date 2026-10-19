package com.example.ui.notice

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.notice.Notice
import com.example.ui.theme.HindSiliguriFamily
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

private val DeepEmeraldGreen = Color(0xFF123D32)
private val SoftGold = Color(0xFFF4D58A)
private val WarmIvoryCream = Color(0xFFF4F1E8)
private val NightDeepGreenBg = Color(0xFF0A1F1A)
private val NightDarkTeal = Color(0xFF0E2E27)
private val NightHeader = Color(0xFF071814)

/**
 * NoticeDetailScreen:
 * খেদমত বিজ্ঞপ্তির পূর্ণাঙ্গ বিবরণ পৃষ্ঠা।
 */
@Composable
fun NoticeDetailScreen(
    notice: Notice,
    isSaved: Boolean,
    isAdmin: Boolean,
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onToggleSave: () -> Unit,
    onOpenBoost: () -> Unit,
    onEditNotice: () -> Unit,
    onDeleteNotice: () -> Unit,
    onAdminBoost: () -> Unit,
    onApply: () -> Unit,
    onOpenMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val context = LocalContext.current
    val currentBg = if (isDarkMode) NightDeepGreenBg else WarmIvoryCream
    val cardBg = if (isDarkMode) NightDarkTeal else WarmIvoryCream
    val textColor = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen
    val subTextColor = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF334A42)

    var showDeleteConfirm by remember { mutableStateOf(false) }

    val copyToClipboard: (String, String) -> Unit = { label, text ->
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label কপি করা হয়েছে: $text", Toast.LENGTH_SHORT).show()
    }

    if (showDeleteConfirm) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showDeleteConfirm = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.dp, Color.Red.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "বিজ্ঞপ্তি মুছে ফেলুন",
                        style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "আপনি কি নিশ্চিতভাবে এই বিজ্ঞপ্তিটি মুছে ফেলতে চান? এটি আর ফিরিয়ে আনা সম্ভব হবে না।",
                        style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.sp, color = textColor),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { showDeleteConfirm = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.2f), contentColor = textColor),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("না", style = TextStyle(fontFamily = HindSiliguriFamily))
                        }
                        Button(
                            onClick = {
                                showDeleteConfirm = false
                                onDeleteNotice()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("হ্যাঁ, মুছুন", style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(currentBg)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .background(if (isDarkMode) NightHeader else DeepEmeraldGreen)
                .padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Hamburger Menu
                com.example.ui.menu.IslamicHamburgerButton(
                    onClick = onOpenMenu,
                    isDarkMode = isDarkMode,
                    testTag = "btn_notice_detail_menu"
                )

                Spacer(modifier = Modifier.width(4.dp))

                // Title
                Text(
                    text = "বিজ্ঞপ্তির বিবরণ",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Right actions: Bookmark and Back
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.testTag("btn_notice_detail_bookmark")
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "সংরক্ষণ",
                            tint = SoftGold
                        )
                    }

                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("btn_notice_detail_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান",
                            tint = SoftGold
                        )
                    }
                }
            }
        }

        // Scrollable Notice Details
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 14.dp)
        ) {
            // Badges row: Boosted & Important
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (notice.isCurrentlyBoosted) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(DeepEmeraldGreen)
                            .border(1.dp, SoftGold, RoundedCornerShape(30.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = null, tint = SoftGold, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "শীর্ষ খেদমত বিজ্ঞপ্তি (বুস্টেড)",
                                style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SoftGold)
                            )
                        }
                    }
                }

                if (notice.isImportant) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(SoftGold.copy(alpha = 0.2f))
                            .border(1.dp, SoftGold, RoundedCornerShape(30.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = SoftGold, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "জরুরি বিজ্ঞপ্তি",
                                style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepEmeraldGreen)
                            )
                        }
                    }
                }

                if (notice.status == "expired") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color.Red.copy(alpha = 0.1f))
                            .border(1.dp, Color.Red, RoundedCornerShape(30.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null, tint = Color.Red, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "মেয়াদোত্তীর্ণ বিজ্ঞপ্তি",
                                style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Institute Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(cardBg)
                    .border(1.2.dp, SoftGold, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = notice.title,
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.Mosque, contentDescription = null, tint = SoftGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = notice.instituteName,
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkMode) SoftGold else DeepEmeraldGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null, tint = if (isDarkMode) SoftGold else DeepEmeraldGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "কর্মস্থল: ${notice.location}",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 13.sp,
                                color = subTextColor
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Key Specification Grid
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDarkMode) Color(0xFF133B32) else Color(0xFFEBE6DA))
                    .border(1.dp, SoftGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SpecRow(icon = Icons.Filled.School, label = "খেদমতের দায়িত্ব:", value = notice.role, isDarkMode = isDarkMode)
                    SpecRow(icon = Icons.Filled.Star, label = "যোগ্যতা ও অভিজ্ঞতা:", value = notice.qualifications.ifBlank { "বিজ্ঞপ্তিতে উল্লেখিত নিয়ম অনুযায়ী" }, isDarkMode = isDarkMode)
                    SpecRow(icon = Icons.Filled.Payments, label = "হাদিয়া বা সুযোগ-সুবিধা:", value = notice.remuneration.ifBlank { "আলোচনা সাপেক্ষে" }, isDarkMode = isDarkMode)
                    SpecRow(icon = Icons.Filled.CalendarToday, label = "আবেদনের শেষ তারিখ:", value = notice.deadline, isDarkMode = isDarkMode, highlight = true)
                    SpecRow(icon = Icons.Filled.CalendarToday, label = "বিজ্ঞপ্তি প্রকাশের তারিখ:", value = notice.publishedDate, isDarkMode = isDarkMode)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Full Description
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.2.dp, SoftGold, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "বিজ্ঞপ্তির সম্পূর্ণ বিবরণ:",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = notice.description,
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 13.5.sp,
                            color = subTextColor,
                            lineHeight = 21.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // How to apply & Contact
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.2.dp, SoftGold, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "আবেদনের নিয়ম ও যোগাযোগের তথ্য:",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (notice.applicationInstructions.isNotBlank()) {
                        Text(
                            text = notice.applicationInstructions,
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 13.sp,
                                color = subTextColor,
                                lineHeight = 19.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Contact action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (notice.contactPhone.isNotBlank()) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${notice.contactPhone}"))
                                    try {
                                        context.startActivity(intent)
                                    } catch (_: Exception) {
                                        copyToClipboard("মোবাইল নম্বর", notice.contactPhone)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepEmeraldGreen, contentColor = SoftGold),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "কল করুন", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold))
                            }
                        }

                        if (notice.contactEmail.isNotBlank()) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${notice.contactEmail}"))
                                    try {
                                        context.startActivity(intent)
                                    } catch (_: Exception) {
                                        copyToClipboard("ইমেইল", notice.contactEmail)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepEmeraldGreen, contentColor = SoftGold),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "ইমেইল পাঠান", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Attachment Section (NEW)
            if (notice.attachmentUrl.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(cardBg)
                        .border(1.2.dp, SoftGold, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "সংযুক্ত তথ্য (ছবি বা পিডিএফ):",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(DeepEmeraldGreen.copy(alpha = 0.05f))
                                .border(1.dp, SoftGold.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.attachmentUrl))
                                    try {
                                        context.startActivity(intent)
                                    } catch (_: Exception) {
                                        Toast.makeText(context, "সংযুক্তিটি খোলা সম্ভব হয়নি", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (notice.attachmentType == "pdf") Icons.AutoMirrored.Outlined.Assignment else Icons.Filled.School, // Icons.Filled.Image or similar
                                contentDescription = null,
                                tint = DeepEmeraldGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (notice.attachmentType == "pdf") "পিডিএফ ডকুমেন্ট দেখুন" else "সংযুক্ত ছবি দেখুন",
                                    style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DeepEmeraldGreen)
                                )
                                Text(
                                    text = "ফাইলটি খুলতে এখানে চাপ দিন",
                                    style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, color = subTextColor)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Boost Option Card for User
            if (notice.isOnlineApplication) {
                Button(
                    onClick = onApply,
                    modifier = Modifier.fillMaxWidth().height(50.dp).padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoftGold, contentColor = DeepEmeraldGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("অনলাইনে আবেদন করুন", style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp))
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DeepEmeraldGreen)
                    .border(1.2.dp, SoftGold, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = null, tint = SoftGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "আপনার বিজ্ঞপ্তি বুস্ট করুন",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = SoftGold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "বিজ্ঞপ্তিটি তালিকার শীর্ষে সোনালি ব্যাজসহ দৃশ্যমান করতে এবং দ্রুত কাঙ্ক্ষিত খেদমতপ্রত্যাশী খুঁজে পেতে বিজ্ঞপ্তি বুস্ট করার অনুরোধ জানান।",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 12.5.sp,
                            color = WarmIvoryCream.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onOpenBoost,
                        colors = ButtonDefaults.buttonColors(containerColor = SoftGold, contentColor = DeepEmeraldGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_trigger_boost_dialog")
                    ) {
                        Text(
                            text = "বুস্ট করার বিস্তারিত ও যোগাযোগ",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                        )
                    }
                }
            }

            // Admin Management Panel
            if (isAdmin) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isDarkMode) Color(0xFF133B32) else Color(0xFFEBE6DA))
                        .border(1.5.dp, SoftGold, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "⚙️ অ্যাডমিন ব্যবস্থাপনা:",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textColor)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onEditNotice,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepEmeraldGreen, contentColor = SoftGold),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "সম্পাদনা", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold))
                            }

                            Button(
                                onClick = onAdminBoost,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = DeepEmeraldGreen, contentColor = SoftGold),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "বুস্ট কন্ট্রোল", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold))
                            }

                            Button(
                                onClick = onDeleteNotice,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626), contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "মুছুন", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SpecRow(
    icon: ImageVector,
    label: String,
    value: String,
    isDarkMode: Boolean,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (highlight) SoftGold else (if (isDarkMode) SoftGold else DeepEmeraldGreen),
            modifier = Modifier.size(17.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkMode) Color(0xFF9FB2A8) else Color(0xFF475569)
                )
            )
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.5.sp,
                    fontWeight = if (highlight) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (highlight) (if (isDarkMode) SoftGold else DeepEmeraldGreen) else (if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen)
                )
            )
        }
    }
}
