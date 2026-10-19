package com.example.ui.notice

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RocketLaunch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.HindSiliguriFamily

private val DeepEmeraldGreen = Color(0xFF123D32)
private val SoftGold = Color(0xFFF4D58A)
private val WarmIvoryCream = Color(0xFFF4F1E8)
private val DarkTeal = Color(0xFF0E2E27)

/**
 * Notice Boost Dialog:
 * পরিষ্কার ও সহজবোধ্য উপায়ে ব্যবহারকারীকে বিজ্ঞপ্তি বুস্ট করার তথ্য ও অ্যাডমিনের সঙ্গে যোগাযোগের ব্যবস্থা প্রদান করে।
 */
@Composable
fun NoticeBoostDialog(
    noticeTitle: String,
    isDarkMode: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val cardBg = if (isDarkMode) DarkTeal else WarmIvoryCream
    val textColor = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen
    val subTextColor = if (isDarkMode) Color(0xFFCBD5E1) else Color(0xFF334A42)

    val copyToClipboard: (String, String) -> Unit = { label, text ->
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label কপি করা হয়েছে: $text", Toast.LENGTH_SHORT).show()
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(cardBg)
                .border(1.5.dp, SoftGold, RoundedCornerShape(22.dp))
                .padding(20.dp)
                .testTag("notice_boost_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DeepEmeraldGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RocketLaunch,
                                contentDescription = null,
                                tint = SoftGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "আপনার বিজ্ঞপ্তি বুস্ট করুন",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = textColor
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = textColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Notice title reminder
                if (noticeTitle.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DeepEmeraldGreen.copy(alpha = 0.08f))
                            .border(1.dp, SoftGold.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "বিজ্ঞপ্তি: $noticeTitle",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = textColor
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Clarification statement
                Text(
                    text = "বিজ্ঞপ্তি বুস্ট করলে তা কওমি কর্মসংস্থানের প্রধান তালিকায় শীর্ষস্থানে সোনালি ব্যাজসহ বিশেষভাবে উপস্থাপন করা হবে। এর ফলে অধিকসংখ্যক যোগ্য ও দ্বীনদার খেদমতপ্রত্যাশী দ্রুত আপনার প্রতিষ্ঠানের বিজ্ঞপ্তিতে দৃষ্টি আকর্ষণ করবেন।",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 13.sp,
                        color = subTextColor,
                        lineHeight = 19.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Important User Instructions
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isDarkMode) Color(0xFF133B32) else Color(0xFFEBE6DA))
                        .border(1.dp, SoftGold.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "📌 বুস্ট প্রক্রিয়ার নিয়মাবলি:",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = if (isDarkMode) SoftGold else DeepEmeraldGreen
                            )
                        )
                        Text(
                            text = "১. বুস্ট সক্রিয় করার জন্য প্রথমে অ্যাডমিনের সঙ্গে যোগাযোগ করতে হবে।\n" +
                                    "২. প্রতিষ্ঠান ও বিজ্ঞপ্তির তথ্য যাচাইপূর্বক অ্যাডমিন অনুমোদন দিলে বুস্ট কার্যকর হবে।\n" +
                                    "৩. বুস্টের সময়সীমা, ধরন ও হাদিয়ার শর্তাবলি অ্যাডমিন নিশ্চিত করবেন।\n" +
                                    "৪. নির্ধারিত মেয়াদ শেষে বিজ্ঞপ্তিটি স্বাভাবিক প্রদর্শনব্যবস্থায় ফিরে আসবে।\n" +
                                    "৫. বর্তমানে কোনো অনলাইন পেমেন্ট গেটওয়ে নেই; শর্ত ও হাদিয়া অ্যাডমিনের সাথে আলোচনা সাপেক্ষে নির্ধারিত হয়।",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 12.sp,
                                color = subTextColor,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Admin Contacts Section
                Text(
                    text = "অ্যাডমিনের সঙ্গে সরাসরি যোগাযোগের তথ্য:",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = textColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 1. WhatsApp Contact Row
                ContactChannelRow(
                    platform = "হোয়াটসঅ্যাপ (WhatsApp)",
                    handle = "@Foysal712",
                    isDarkMode = isDarkMode,
                    onCopy = { copyToClipboard("WhatsApp", "@Foysal712") },
                    onOpen = {
                        val uri = Uri.parse("https://api.whatsapp.com/send?text=${Uri.encode("আসসালামু আলাইকুম, আমি কওমি কর্মসংস্থান বাংলাদেশ অ্যাপে খেদমত বিজ্ঞপ্তি বুস্ট করতে চাই: $noticeTitle")}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            copyToClipboard("WhatsApp", "@Foysal712")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 2. Telegram Contact Row
                ContactChannelRow(
                    platform = "টেলিগ্রাম (Telegram)",
                    handle = "Q_kormosongsthan_Bangladesh",
                    isDarkMode = isDarkMode,
                    onCopy = { copyToClipboard("Telegram", "Q_kormosongsthan_Bangladesh") },
                    onOpen = {
                        val uri = Uri.parse("https://t.me/Q_kormosongsthan_Bangladesh")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            copyToClipboard("Telegram", "Q_kormosongsthan_Bangladesh")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepEmeraldGreen,
                        contentColor = SoftGold
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "ঠিক আছে, বুঝেছি",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactChannelRow(
    platform: String,
    handle: String,
    isDarkMode: Boolean,
    onCopy: () -> Unit,
    onOpen: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDarkMode) Color(0xFF133B32) else Color(0xFFFFFFFF))
            .border(1.dp, SoftGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = platform,
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) Color(0xFF9FB2A8) else Color(0xFF475569)
                    )
                )
                Text(
                    text = handle,
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen
                    )
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Copy Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepEmeraldGreen.copy(alpha = 0.12f))
                        .clickable(onClick = onCopy)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "কপি করুন",
                            tint = DeepEmeraldGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "কপি",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepEmeraldGreen
                            )
                        )
                    }
                }

                // Action Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DeepEmeraldGreen)
                        .clickable(onClick = onOpen)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "যোগাযোগ",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftGold
                        )
                    )
                }
            }
        }
    }
}
