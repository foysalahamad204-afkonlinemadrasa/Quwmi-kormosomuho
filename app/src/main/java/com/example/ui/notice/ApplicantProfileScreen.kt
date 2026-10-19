package com.example.ui.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.ui.theme.HindSiliguriFamily
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.notice.ApplicantProfile

private val DeepEmeraldGreen = Color(0xFF123D32)
private val SoftGold = Color(0xFFF4D58A)
private val WarmIvoryCream = Color(0xFFF4F1E8)
private val NightDeepGreenBg = Color(0xFF0A1F1A)
private val NightIvoryText = Color(0xFFF4F1E8)

@Composable
fun ApplicantProfileScreen(
    isDarkMode: Boolean,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var fullName by remember(state.profile) { mutableStateOf(state.profile.fullName) }
    var education by remember(state.profile) { mutableStateOf(state.profile.education) }
    var experience by remember(state.profile) { mutableStateOf(state.profile.experience) }
    var preferredService by remember(state.profile) { mutableStateOf(state.profile.preferredService) }
    
    val screenBg = if (isDarkMode) NightDeepGreenBg else WarmIvoryCream
    val textColor = if (isDarkMode) NightIvoryText else DeepEmeraldGreen

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
            Text("আমার খেদমতপ্রত্যাশী প্রোফাইল", style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White))
        }

        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("পূর্ণ নাম") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = education, onValueChange = { education = it }, label = { Text("শিক্ষাগত যোগ্যতা") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = experience, onValueChange = { experience = it }, label = { Text("অভিজ্ঞতা") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = preferredService, onValueChange = { preferredService = it }, label = { Text("পছন্দের খেদমত") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.updateProfile(ApplicantProfile(fullName = fullName, education = education, experience = experience, preferredService = preferredService)) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) SoftGold else DeepEmeraldGreen)
            ) {
                Text("প্রোফাইল সংরক্ষণ করুন")
            }
            if (state.isLoading) CircularProgressIndicator()
        }
    }
}
