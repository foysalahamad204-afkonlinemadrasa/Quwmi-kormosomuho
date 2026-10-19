package com.example.ui.notice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.notice.Notice
import com.example.ui.theme.HindSiliguriFamily

private val DeepEmeraldGreen = Color(0xFF123D32)
private val SoftGold = Color(0xFFF4D58A)
private val WarmIvoryCream = Color(0xFFF4F1E8)
private val NightDeepGreenBg = Color(0xFF0A1F1A)
private val NightDarkTeal = Color(0xFF0E2E27)
private val NightHeader = Color(0xFF071814)

/**
 * NoticeListScreen:
 * বিজ্ঞপ্তি ও ঘোষণা মূল তালিকা পৃষ্ঠা।
 */
@Composable
fun NoticeListScreen(
    state: NoticeUiState,
    viewModel: NoticeViewModel,
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onOpenMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        if (state.selectedNotice != null) {
            viewModel.selectNotice(null)
        } else {
            onBack()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.statusMessage) {
        state.statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    // Notice Detail Navigation
    if (state.selectedNotice != null) {
        val selected = state.selectedNotice
        NoticeDetailScreen(
            notice = selected,
            isSaved = state.savedNoticeIds.contains(selected.id),
            isAdmin = state.isAdmin,
            isDarkMode = isDarkMode,
            onBack = { viewModel.selectNotice(null) },
            onToggleSave = { viewModel.toggleSaveNotice(selected) },
            onOpenBoost = { viewModel.openBoostDialog(true, selected) },
            onEditNotice = { viewModel.openCreateEditDialog(true, selected) },
            onDeleteNotice = { viewModel.deleteNotice(selected.id) },
            onAdminBoost = { viewModel.openAdminBoostDialog(true, selected) },
            onApply = { viewModel.openApplicationDialog(true, selected) },
            onOpenMenu = onOpenMenu,
            modifier = modifier
        )
        return
    }

    val currentBg = if (isDarkMode) NightDeepGreenBg else WarmIvoryCream
    val textColor = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen

    // Notices list to display based on mode and filters
    val baseList = if (state.isAdmin && !state.filterOnlySaved) {
        state.adminNotices.filter { it.status == state.adminFilterTab }
    } else if (state.filterOnlySaved) {
        state.publishedNotices.filter { state.savedNoticeIds.contains(it.id) }
    } else {
        state.publishedNotices
    }

    val filteredList = baseList.filter { notice ->
        val queryMatch = state.searchQuery.isBlank() ||
                notice.title.contains(state.searchQuery, ignoreCase = true) ||
                notice.instituteName.contains(state.searchQuery, ignoreCase = true) ||
                notice.role.contains(state.searchQuery, ignoreCase = true) ||
                notice.location.contains(state.searchQuery, ignoreCase = true)

        val locationMatch = state.locationFilter == "সব" || notice.location.contains(state.locationFilter, ignoreCase = true)

        queryMatch && locationMatch
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(currentBg)
    ) {
        Column(
            modifier = Modifier
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
                    // Left: Hamburger Menu (Consistent with Main Screen)
                    com.example.ui.menu.IslamicHamburgerButton(
                        onClick = onOpenMenu,
                        isDarkMode = isDarkMode,
                        testTag = "btn_notice_list_menu"
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Title
                    Text(
                        text = if (state.filterOnlySaved) "সংরক্ষিত বিজ্ঞপ্তি" else "বিজ্ঞপ্তি ও ঘোষণা",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    // Right actions: Back and Admin Add
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.isAdmin && !state.filterOnlySaved) {
                            IconButton(
                                onClick = { viewModel.openCreateEditDialog(true, null) },
                                modifier = Modifier.testTag("btn_admin_create_notice_header")
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "বিজ্ঞপ্তি যোগ", tint = SoftGold)
                            }
                        }

                        IconButton(onClick = onBack, modifier = Modifier.testTag("btn_notice_list_back")) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "ফিরে যান", tint = SoftGold)
                        }
                    }
                }
            }

            // Search Bar & Location Filters
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_notice_search"),
                    placeholder = {
                        Text(
                            text = "খেদমতের পদ, দায়িত্ব বা প্রতিষ্ঠান খুঁজুন...",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.sp, color = if (isDarkMode) Color(0xFF9FB2A8) else Color(0xFF64748B))
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = if (isDarkMode) SoftGold else DeepEmeraldGreen)
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotBlank()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "মুছুন", tint = Color.Gray)
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.5.sp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepEmeraldGreen,
                        unfocusedBorderColor = SoftGold.copy(alpha = 0.6f),
                        focusedContainerColor = if (isDarkMode) NightDarkTeal else Color(0xFFFFFFFF),
                        unfocusedContainerColor = if (isDarkMode) NightDarkTeal else Color(0xFFFFFFFF)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Location Filter Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val locations = listOf("সব", "ঢাকা", "চট্টগ্রাম", "সিলেট", "রাজশাহী", "খুলনা", "ময়মনসিংহ", "বরিশাল")
                    items(locations) { loc ->
                        val isSelected = state.locationFilter == loc
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) DeepEmeraldGreen else (if (isDarkMode) NightDarkTeal else Color(0xFFFFFFFF)))
                                .border(1.dp, if (isSelected) SoftGold else SoftGold.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .clickable { viewModel.updateLocationFilter(loc) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = loc,
                                style = TextStyle(
                                    fontFamily = HindSiliguriFamily,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SoftGold else textColor
                                )
                            )
                        }
                    }
                }
            }

            // Admin Filter Tabs
            if (state.isAdmin && !state.filterOnlySaved) {
                TabRow(
                    selectedTabIndex = when (state.adminFilterTab) {
                        "published" -> 0
                        "draft" -> 1
                        else -> 2
                    },
                    containerColor = if (isDarkMode) NightDarkTeal else Color(0xFFEBE6DA),
                    contentColor = DeepEmeraldGreen,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[when (state.adminFilterTab) {
                                "published" -> 0
                                "draft" -> 1
                                else -> 2
                            }]),
                            color = SoftGold
                        )
                    }
                ) {
                    Tab(
                        selected = state.adminFilterTab == "published",
                        onClick = { viewModel.setAdminFilterTab("published") },
                        text = { Text("প্রকাশিত", style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)) }
                    )
                    Tab(
                        selected = state.adminFilterTab == "draft",
                        onClick = { viewModel.setAdminFilterTab("draft") },
                        text = { Text("খসড়া", style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)) }
                    )
                    Tab(
                        selected = state.adminFilterTab == "expired",
                        onClick = { viewModel.setAdminFilterTab("expired") },
                        text = { Text("মেয়াদোত্তীর্ণ", style = TextStyle(fontFamily = HindSiliguriFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp)) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "অ্যাডমিন নিয়ন্ত্রণ ও পরিসংখ্যান:",
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    ),
                    modifier = Modifier.padding(horizontal = 14.dp)
                )

                NoticeAdminStatsRow(stats = state.stats, isDarkMode = isDarkMode)
            }

            // Notice List Content
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(DeepEmeraldGreen.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Filled.Mosque, contentDescription = null, tint = DeepEmeraldGreen, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (state.filterOnlySaved) "কোনো সংরক্ষিত বিজ্ঞপ্তি নেই" else "কোনো খেদমত বিজ্ঞপ্তি পাওয়া যায়নি",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (state.filterOnlySaved) "বিজ্ঞপ্তির বুকমার্ক আইকনে চাপ দিয়ে প্রয়োজনীয় খেদমতসমূহ এখানে সংরক্ষণ করতে পারেন।" else "অন্য কোনো এলাকা বা পদ লিখে খুঁজুন অথবা নতুন বিজ্ঞপ্তি প্রকাশের অপেক্ষা করুন।",
                            style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 13.sp, color = if (isDarkMode) Color(0xFF9FB2A8) else Color(0xFF475569)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList, key = { it.id }) { notice ->
                        NoticeCardItem(
                            notice = notice,
                            isSaved = state.savedNoticeIds.contains(notice.id),
                            isDarkMode = isDarkMode,
                            onClick = { viewModel.selectNotice(notice) },
                            onToggleSave = { viewModel.toggleSaveNotice(notice) }
                        )
                    }
                }
            }
        }

        // Floating Action Button for Admin
        if (state.isAdmin) {
            FloatingActionButton(
                onClick = { viewModel.openCreateEditDialog(true, null) },
                containerColor = DeepEmeraldGreen,
                contentColor = SoftGold,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .testTag("fab_admin_add_notice")
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "নতুন বিজ্ঞপ্তি প্রকাশ")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Boost Modal
        if (state.showBoostDialog) {
            NoticeBoostDialog(
                noticeTitle = state.selectedNotice?.title ?: "",
                isDarkMode = isDarkMode,
                onDismiss = { viewModel.openBoostDialog(false) }
            )
        }

        // Admin Create / Edit Modal
        if (state.showCreateEditDialog) {
            AdminCreateEditNoticeDialog(
                initialNotice = state.editingNotice,
                isDarkMode = isDarkMode,
                onDismiss = { viewModel.openCreateEditDialog(false) },
                onSave = { viewModel.saveNotice(it) }
            )
        }

        // Admin Boost Modal
        if (state.showAdminBoostDialog && state.boostingNotice != null) {
            AdminBoostManagementDialog(
                notice = state.boostingNotice,
                isDarkMode = isDarkMode,
                onDismiss = { viewModel.openAdminBoostDialog(false) },
                onApplyBoost = { id, boosted, days, notify -> viewModel.setNoticeBoost(id, boosted, days, notify) }
            )
        }

        // Admin Review Modal
        if (state.showAdminReviewDialog && state.reviewingNotice != null) {
            AdminStatusReviewDialog(
                notice = state.reviewingNotice,
                isDarkMode = isDarkMode,
                onDismiss = { viewModel.openAdminReviewDialog(false) },
                onApprove = { viewModel.approveNotice(it); viewModel.openAdminReviewDialog(false) },
                onReject = { id, reason -> viewModel.rejectNotice(id, reason); viewModel.openAdminReviewDialog(false) },
                onReturnForCorrection = { id, reason -> viewModel.returnForCorrection(id, reason); viewModel.openAdminReviewDialog(false) }
            )
        }

        // Application Modal
        if (state.showApplicationDialog && state.applyingNotice != null) {
            ApplicationFormDialog(
                notice = state.applyingNotice,
                isDarkMode = isDarkMode,
                onDismiss = { viewModel.openApplicationDialog(false) },
                onSubmit = { name, contact, quals, exp -> 
                    viewModel.applyForNotice(state.applyingNotice.id, name, contact, quals, exp)
                    viewModel.openApplicationDialog(false) 
                }
            )
        }
    }
}

/**
 * Notice Card Item in List
 */
@Composable
private fun NoticeCardItem(
    notice: Notice,
    isSaved: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit,
    onToggleSave: () -> Unit
) {
    val cardBg = if (isDarkMode) NightDarkTeal else Color(0xFFFFFFFF)
    val textColor = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen
    val subTextColor = if (isDarkMode) Color(0xFF9FB2A8) else Color(0xFF475569)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(
                width = if (notice.isCurrentlyBoosted) 1.5.dp else 1.dp,
                color = if (notice.isCurrentlyBoosted) SoftGold else SoftGold.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
            .testTag("notice_item_${notice.id}")
    ) {
        Column {
            // Badges & Save Icon Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (notice.isCurrentlyBoosted) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(DeepEmeraldGreen)
                                .border(1.dp, SoftGold, RoundedCornerShape(20.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Filled.RocketLaunch, contentDescription = null, tint = SoftGold, modifier = Modifier.size(11.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "শীর্ষ খেদমত (বুস্টেড)",
                                    style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SoftGold)
                                )
                            }
                        }
                    }

                    if (notice.isImportant) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(SoftGold.copy(alpha = 0.2f))
                                .border(1.dp, SoftGold.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "জরুরি",
                                style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DeepEmeraldGreen)
                            )
                        }
                    }

                    if (notice.status == "expired") {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Red.copy(alpha = 0.1f))
                                .border(1.dp, Color.Red.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "মেয়াদোত্তীর্ণ",
                                style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                            )
                        }
                    }
                }

                // Bookmark toggle
                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "সংরক্ষণ",
                        tint = if (isSaved) SoftGold else (if (isDarkMode) SoftGold else DeepEmeraldGreen),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Notice Title
            Text(
                text = notice.title,
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Institute Name
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Mosque, contentDescription = null, tint = SoftGold, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = notice.instituteName,
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkMode) SoftGold else DeepEmeraldGreen
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.Filled.School, contentDescription = null, tint = if (isDarkMode) SoftGold else DeepEmeraldGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = notice.role,
                        style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, color = subTextColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null, tint = if (isDarkMode) SoftGold else DeepEmeraldGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = notice.location,
                        style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 12.sp, color = subTextColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Remuneration & Deadline Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.Filled.Payments, contentDescription = null, tint = SoftGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = notice.remuneration.ifBlank { "আলোচনা সাপেক্ষে" },
                        style = TextStyle(fontFamily = HindSiliguriFamily, fontSize = 11.5.sp, color = subTextColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null, tint = SoftGold, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "শেষ তারিখ: ${notice.deadline}",
                        style = TextStyle(
                            fontFamily = HindSiliguriFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkMode) SoftGold else DeepEmeraldGreen
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun NoticeAdminStatsRow(
    stats: com.example.data.notice.NoticeStats,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(label = "মোট", count = stats.total, color = DeepEmeraldGreen, isDarkMode = isDarkMode, modifier = Modifier.weight(1f))
        StatCard(label = "প্রকাশিত", count = stats.published, color = Color(0xFF10B981), isDarkMode = isDarkMode, modifier = Modifier.weight(1f))
        StatCard(label = "খসড়া", count = stats.pending, color = Color(0xFFF59E0B), isDarkMode = isDarkMode, modifier = Modifier.weight(1f))
        StatCard(label = "বুস্টেড", count = stats.boosted, color = SoftGold, isDarkMode = isDarkMode, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(
    label: String,
    count: Int,
    color: Color,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val cardBg = if (isDarkMode) NightDarkTeal else Color(0xFFFFFFFF)
    val textColor = if (isDarkMode) WarmIvoryCream else DeepEmeraldGreen

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg)
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = count.toString(),
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor.copy(alpha = 0.8f)
                )
            )
        }
    }
}
