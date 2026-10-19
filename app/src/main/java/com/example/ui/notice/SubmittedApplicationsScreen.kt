package com.example.ui.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.notice.Application
import com.example.ui.theme.HindSiliguriFamily

private val DeepEmeraldGreen = Color(0xFF123D32)
private val WarmIvoryCream = Color(0xFFF4F1E8)
private val NightDeepGreenBg = Color(0xFF0A1F1A)
private val NightDarkTeal = Color(0xFF0E2E27)
private val NightIvoryText = Color(0xFFF4F1E8)
@Composable
fun SubmittedApplicationsScreen(
    applications: List<Application>,
    isDarkMode: Boolean,
    onBack: () -> Unit
) {
    val screenBg = if (isDarkMode) NightDeepGreenBg else WarmIvoryCream
    val textColor = if (isDarkMode) NightIvoryText else DeepEmeraldGreen
    val cardBg = if (isDarkMode) NightDarkTeal else Color.White

    Column(modifier = Modifier.fillMaxSize().background(screenBg)) {
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
            Text("আমার জমা দেওয়া আবেদন", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
        }
        
        if (applications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("আপনি এখনো কোনো অনলাইন আবেদন জমা দেননি।", style = TextStyle(fontFamily = HindSiliguriFamily, color = textColor))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(applications) { app ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("আবেদন নম্বর: ${app.id}", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, color = textColor.copy(alpha = 0.6f)))
                            Text("অবস্থা: ${app.status}", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (app.status == Application.STATUS_SELECTED) Color(0xFF10B981) else textColor))
                        }
                    }
                }
            }
        }
    }
}
