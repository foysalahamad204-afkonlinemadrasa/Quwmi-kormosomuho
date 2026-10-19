package com.example.ui.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.HindSiliguriFamily

private val DeepEmeraldGreen = Color(0xFF123D32)
private val SoftGold = Color(0xFFF4D58A)
private val WarmIvoryCream = Color(0xFFF4F1E8)
private val NightDeepGreenBg = Color(0xFF0A1F1A)
private val NightDarkTeal = Color(0xFF0E2E27)
private val NightIvoryText = Color(0xFFF4F1E8)

@Composable
fun MyApplicationsScreen(
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onNavigateToNotices: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val screenBg = if (isDarkMode) NightDeepGreenBg else WarmIvoryCream
    val textColor = if (isDarkMode) NightIvoryText else DeepEmeraldGreen
    val cardBg = if (isDarkMode) NightDarkTeal else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(screenBg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isDarkMode) Color(0xFF071814) else DeepEmeraldGreen)
                .statusBarsPadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "আমার আবেদন",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "আপনার খেদমতের আবেদন ও খেদমতপ্রত্যাশী প্রোফাইল পরিচালনা করুন।",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 15.sp,
                    color = textColor.copy(alpha = 0.8f)
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Card 1: Application List
            ApplicationCard(
                title = "বিজ্ঞপ্তির বিপরীতে আবেদন",
                description = "আপনার জমা দেওয়া আবেদন, আবেদনের বর্তমান অবস্থা এবং সংশ্লিষ্ট আপডেট দেখুন।",
                buttonText = "আমার জমা দেওয়া আবেদন",
                isDarkMode = isDarkMode,
                onClick = onNavigateToNotices
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card 2: Profile
            ApplicationCard(
                title = "আমার খেদমতপ্রত্যাশী প্রোফাইল",
                description = "নিজের ছবি, শিক্ষাগত যোগ্যতা, সনদ, অভিজ্ঞতা ও পছন্দের খেদমতের তথ্য দিয়ে প্রোফাইল তৈরি করুন, যাতে উপযুক্ত মাদ্রাসা আপনার সঙ্গে যোগাযোগ করতে পারে।",
                buttonText = "প্রোফাইল তৈরি করুন",
                isDarkMode = isDarkMode,
                onClick = onNavigateToProfile
            )
        }
    }
}

@Composable
private fun ApplicationCard(
    title: String,
    description: String,
    buttonText: String,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    val cardBg = if (isDarkMode) NightDarkTeal else Color.White
    val textColor = if (isDarkMode) NightIvoryText else DeepEmeraldGreen

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, SoftGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) SoftGold else DeepEmeraldGreen
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontSize = 14.sp,
                color = textColor.copy(alpha = 0.7f)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) SoftGold else DeepEmeraldGreen),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = buttonText,
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) DeepEmeraldGreen else Color.White
                )
            )
        }
    }
}
