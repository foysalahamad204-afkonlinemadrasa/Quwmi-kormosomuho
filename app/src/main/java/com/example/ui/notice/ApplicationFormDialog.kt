package com.example.ui.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.notice.Notice
import com.example.ui.theme.HindSiliguriFamily

private val DeepEmeraldGreen = Color(0xFF123D32)
private val SoftGold = Color(0xFFF4D58A)
private val WarmIvoryCream = Color(0xFFF4F1E8)
private val NightDeepGreenBg = Color(0xFF0A1F1A)
private val NightDarkTeal = Color(0xFF0E2E27)

@Composable
fun ApplicationFormDialog(
    notice: Notice,
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String) -> Unit
) {
    val cardBg = if (isDarkMode) NightDarkTeal else WarmIvoryCream
    val textColor = if (isDarkMode) Color.White else DeepEmeraldGreen

    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var quals by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(cardBg)
                .border(1.5.dp, SoftGold, RoundedCornerShape(22.dp))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("আবেদন ফর্ম", style = TextStyle(color = SoftGold, fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = HindSiliguriFamily))
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Close", tint = SoftGold) }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("বিজ্ঞপ্তি: ${notice.title}", style = TextStyle(color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = HindSiliguriFamily))
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("আপনার নাম") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("যোগাযোগের নম্বর") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = quals, onValueChange = { quals = it }, label = { Text("শিক্ষাগত যোগ্যতা") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = experience, onValueChange = { experience = it }, label = { Text("অভিজ্ঞতা") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { onSubmit(name, contact, quals, experience) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoftGold)
                ) {
                    Text("জমা দিন", style = TextStyle(color = DeepEmeraldGreen, fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
