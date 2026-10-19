package com.example.ui.notice

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
private val DarkTeal = Color(0xFF0E2E27)

/**
 * Admin Create / Edit Notice Modal
 */
@Composable
fun AdminCreateEditNoticeDialog(
    initialNotice: Notice?,
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onSave: (Notice) -> Unit
) {
    val cardBg = if (isDarkMode) DarkTeal else WarmIvoryCream
    val textColor = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen

    var title by remember { mutableStateOf(initialNotice?.title ?: "") }
    var instituteName by remember { mutableStateOf(initialNotice?.instituteName ?: "") }
    var instituteDescription by remember { mutableStateOf(initialNotice?.instituteDescription ?: "") }
    var role by remember { mutableStateOf(initialNotice?.role ?: "") }
    var qualifications by remember { mutableStateOf(initialNotice?.qualifications ?: "") }
    var location by remember { mutableStateOf(initialNotice?.location ?: "") }
    var remuneration by remember { mutableStateOf(initialNotice?.remuneration ?: "") }
    var deadline by remember { mutableStateOf(initialNotice?.deadline ?: "") }
    var applicationInstructions by remember { mutableStateOf(initialNotice?.applicationInstructions ?: "") }
    var contactPhone by remember { mutableStateOf(initialNotice?.contactPhone ?: "") }
    var contactEmail by remember { mutableStateOf(initialNotice?.contactEmail ?: "") }
    var description by remember { mutableStateOf(initialNotice?.description ?: "") }
    var status by remember { mutableStateOf(initialNotice?.status ?: "published") }
    var isImportant by remember { mutableStateOf(initialNotice?.isImportant ?: false) }
    var attachmentUrl by remember { mutableStateOf(initialNotice?.attachmentUrl ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(cardBg)
                .border(1.5.dp, SoftGold, RoundedCornerShape(22.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(DeepEmeraldGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = null,
                                tint = SoftGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (initialNotice == null) "নতুন খেদমত বিজ্ঞপ্তি প্রকাশ" else "বিজ্ঞপ্তি সম্পাদনা",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = textColor
                            )
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "বন্ধ", tint = textColor)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            color = Color.Red,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                AdminInputField(label = "বিজ্ঞপ্তির শিরোনাম *", value = title, onValueChange = { title = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "প্রতিষ্ঠানের নাম *", value = instituteName, onValueChange = { instituteName = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "প্রতিষ্ঠানের সংক্ষিপ্ত পরিচিতি", value = instituteDescription, onValueChange = { instituteDescription = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "খেদমতের দায়িত্ব বা পদের নাম *", value = role, onValueChange = { role = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "যোগ্যতা ও অভিজ্ঞতা", value = qualifications, onValueChange = { qualifications = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "কর্মস্থল *", value = location, onValueChange = { location = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "হাদিয়া বা সুযোগ-সুবিধা", value = remuneration, onValueChange = { remuneration = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "আবেদনের শেষ তারিখ *", value = deadline, onValueChange = { deadline = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "মোবাইল নম্বর", value = contactPhone, onValueChange = { contactPhone = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "ইমেইল", value = contactEmail, onValueChange = { contactEmail = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "আবেদনের নিয়ম ও যোগাযোগের তথ্য", value = applicationInstructions, onValueChange = { applicationInstructions = it }, isDarkMode = isDarkMode)
                AdminInputField(label = "সংযুক্ত ছবি বা পিডিএফ লিংক (ঐচ্ছিক)", value = attachmentUrl, onValueChange = { attachmentUrl = it }, isDarkMode = isDarkMode)

                AdminInputField(
                    label = "বিজ্ঞপ্তির সম্পূর্ণ বিবরণ *",
                    value = description,
                    onValueChange = { description = it },
                    isDarkMode = isDarkMode,
                    singleLine = false,
                    lines = 4
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Important Flag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isImportant = !isImportant }
                ) {
                    Checkbox(
                        checked = isImportant,
                        onCheckedChange = { isImportant = it },
                        colors = CheckboxDefaults.colors(checkedColor = DeepEmeraldGreen, checkmarkColor = SoftGold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "গুরুত্বপূর্ণ বিজ্ঞপ্তি হিসেবে চিহ্নিত করুন",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Publication Status
                Text(
                    text = "বিজ্ঞপ্তির প্রকাশনা অবস্থা:",
                    style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textColor)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("published" to "প্রকাশিত", "draft" to "খসড়া", "expired" to "মেয়াদোত্তীর্ণ").forEach { (key, label) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { status = key }
                        ) {
                            RadioButton(
                                selected = status == key,
                                onClick = { status = key },
                                colors = RadioButtonDefaults.colors(selectedColor = DeepEmeraldGreen)
                            )
                            Text(
                                text = label,
                                style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.5.sp, color = textColor)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (title.isBlank() || instituteName.isBlank() || role.isBlank() || location.isBlank() || deadline.isBlank() || description.isBlank()) {
                            errorMessage = "অনুগ্রহ করে সকল আবশ্যকীয় (*) তথ্য পূরণ করুন।"
                            return@Button
                        }
                        val finalNotice = (initialNotice ?: Notice()).copy(
                            title = title.trim(),
                            instituteName = instituteName.trim(),
                            instituteDescription = instituteDescription.trim(),
                            role = role.trim(),
                            qualifications = qualifications.trim(),
                            location = location.trim(),
                            remuneration = remuneration.trim(),
                            deadline = deadline.trim(),
                            applicationInstructions = applicationInstructions.trim(),
                            contactPhone = contactPhone.trim(),
                            contactEmail = contactEmail.trim(),
                            description = description.trim(),
                            status = status,
                            isImportant = isImportant,
                            attachmentUrl = attachmentUrl.trim(),
                            attachmentType = if (attachmentUrl.isBlank()) "none" else if (attachmentUrl.endsWith(".pdf", ignoreCase = true)) "pdf" else "image",
                            publishedDate = initialNotice?.publishedDate?.takeIf { it.isNotBlank() } ?: com.example.ui.main.IslamicDateUtils.getCurrentDates().english
                        )
                        onSave(finalNotice)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepEmeraldGreen, contentColor = SoftGold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (initialNotice == null) "সংরক্ষণ ও প্রকাশ করুন" else "বিজ্ঞপ্তি হালনাগাদ করুন",
                        style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    )
                }
            }
        }
    }
}

/**
 * Admin Boost Management Dialog:
 * Manage boost status, duration, starting & stopping.
 */
@Composable
fun AdminBoostManagementDialog(
    notice: Notice,
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onApplyBoost: (noticeId: String, isBoosted: Boolean, durationDays: Int, sendNotification: Boolean) -> Unit
) {
    val cardBg = if (isDarkMode) DarkTeal else WarmIvoryCream
    val textColor = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen

    var isBoosted by remember { mutableStateOf(notice.isCurrentlyBoosted) }
    var durationDays by remember { mutableIntStateOf(notice.boostDurationDays ?: 7) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(cardBg)
                .border(1.5.dp, SoftGold, RoundedCornerShape(22.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(DeepEmeraldGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = null, tint = SoftGold, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "অ্যাডমিন বুস্ট নিয়ন্ত্রণ",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = textColor)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "বন্ধ", tint = textColor)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = notice.title,
                    style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
                )
                Text(
                    text = notice.instituteName,
                    style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.sp, color = if (isDarkMode) Color(0xFF9FB2A8) else Color(0xFF475569))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Boost Status Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isBoosted) DeepEmeraldGreen.copy(alpha = 0.12f) else Color.Transparent)
                        .border(1.dp, SoftGold.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .clickable { isBoosted = !isBoosted }
                        .padding(12.dp)
                ) {
                    Checkbox(
                        checked = isBoosted,
                        onCheckedChange = { isBoosted = it },
                        colors = CheckboxDefaults.colors(checkedColor = DeepEmeraldGreen, checkmarkColor = SoftGold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isBoosted) "বুস্ট সক্রিয় রয়েছে" else "বুস্ট নিষ্ক্রিয়",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textColor)
                        )
                        Text(
                            text = if (isBoosted) "শীর্ষস্থানে সোনালি বুস্টেড ব্যাজসহ প্রদর্শিত হবে" else "তালিকার স্বাভাবিক ক্রমে প্রদর্শিত হবে",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, color = if (isDarkMode) Color(0xFF9FB2A8) else Color(0xFF475569))
                        )
                    }
                }

                if (isBoosted) {
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "বুস্টের মেয়াদ নির্ধারণ করুন:",
                        style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = textColor),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3, 7, 15, 30).forEach { days ->
                            val isSelected = durationDays == days
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) DeepEmeraldGreen else Color.Transparent)
                                    .border(1.dp, if (isSelected) SoftGold else Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                    .clickable { durationDays = days }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$days দিন",
                                    style = TextStyle(
                                        fontFamily = HindSiliguriFamily,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = if (isSelected) SoftGold else textColor
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Push Notification Toggle
                var sendNotification by remember { mutableStateOf(isBoosted && !notice.notificationSent) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (sendNotification) DeepEmeraldGreen.copy(alpha = 0.08f) else Color.Transparent)
                        .border(1.dp, if (sendNotification) SoftGold.copy(alpha = 0.5f) else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { sendNotification = !sendNotification }
                        .padding(10.dp)
                ) {
                    Checkbox(
                        checked = sendNotification,
                        onCheckedChange = { sendNotification = it },
                        colors = CheckboxDefaults.colors(checkedColor = DeepEmeraldGreen, checkmarkColor = SoftGold)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "পুশ নোটিফিকেশন পাঠান",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = textColor)
                        )
                        Text(
                            text = "সকল ব্যবহারকারীর ফোনে বিজ্ঞপ্তিটি পৌঁছে যাবে",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 11.5.sp, color = if (isDarkMode) Color(0xFF9FB2A8) else Color(0xFF475569))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onApplyBoost(notice.id, isBoosted, durationDays, sendNotification)
                        // Note: Real notification would be triggered by Firestore update on server side.
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepEmeraldGreen, contentColor = SoftGold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "বুস্ট সংরক্ষণ করুন",
                        style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isDarkMode: Boolean,
    singleLine: Boolean = true,
    lines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(
            text = label,
            style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = if (isDarkMode) SoftGold else DeepEmeraldGreen)
        )
        Spacer(modifier = Modifier.height(2.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = lines,
            maxLines = if (singleLine) 1 else 6,
            textStyle = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.5.sp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DeepEmeraldGreen,
                unfocusedBorderColor = SoftGold.copy(alpha = 0.5f),
                focusedContainerColor = if (isDarkMode) Color(0xFF133B32) else Color(0xFFFFFFFF),
                unfocusedContainerColor = if (isDarkMode) Color(0xFF133B32) else Color(0xFFFFFFFF)
            )
        )
    }
}

/**
 * Admin Status Review Modal (Approve / Reject / Correction)
 */
@Composable
fun AdminStatusReviewDialog(
    notice: Notice,
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String, String) -> Unit,
    onReturnForCorrection: (String, String) -> Unit
) {
    val cardBg = if (isDarkMode) DarkTeal else WarmIvoryCream
    val textColor = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen
    
    var feedback by remember { mutableStateOf("") }
    var selectedAction by remember { mutableStateOf("approve") } // "approve", "reject", "correction"

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(cardBg)
                .border(1.5.dp, SoftGold, RoundedCornerShape(22.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "বিজ্ঞপ্তি পর্যালোচনা",
                        style = TextStyle(
                            color = SoftGold,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = HindSiliguriFamily
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = SoftGold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "বিজ্ঞপ্তি: ${notice.title}",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = HindSiliguriFamily
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "সিদ্ধান্ত নিন:",
                    style = TextStyle(color = SoftGold, fontSize = 14.sp, fontFamily = HindSiliguriFamily)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedAction == "approve",
                        onClick = { selectedAction = "approve" },
                        colors = RadioButtonDefaults.colors(selectedColor = SoftGold)
                    )
                    Text("অনুমোদন ও প্রকাশ", style = TextStyle(color = textColor, fontFamily = HindSiliguriFamily))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedAction == "correction",
                        onClick = { selectedAction = "correction" },
                        colors = RadioButtonDefaults.colors(selectedColor = SoftGold)
                    )
                    Text("সংশোধনের জন্য ফেরত", style = TextStyle(color = textColor, fontFamily = HindSiliguriFamily))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedAction == "reject",
                        onClick = { selectedAction = "reject" },
                        colors = RadioButtonDefaults.colors(selectedColor = SoftGold)
                    )
                    Text("প্রত্যাখ্যান করুন", style = TextStyle(color = textColor, fontFamily = HindSiliguriFamily))
                }

                if (selectedAction != "approve") {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = feedback,
                        onValueChange = { feedback = it },
                        label = { Text("মন্তব্য / কারণ লিখুন", color = textColor.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = textColor, fontFamily = HindSiliguriFamily),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoftGold,
                            unfocusedBorderColor = textColor.copy(alpha = 0.3f),
                            focusedLabelColor = SoftGold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        when (selectedAction) {
                            "approve" -> onApprove(notice.id)
                            "reject" -> onReject(notice.id, feedback)
                            "correction" -> onReturnForCorrection(notice.id, feedback)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoftGold),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "নিশ্চিত করুন",
                        style = TextStyle(
                            color = DeepEmeraldGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = HindSiliguriFamily
                        )
                    )
                }
            }
        }
    }
}
