package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.components.NeumorphicCard
import com.example.ui.theme.BrandRed
import com.example.ui.theme.BrandRedDark
import com.example.ui.theme.BrandRedLight
import com.example.ui.theme.HindSiliguriFamily
import com.example.ui.theme.NeumorphicBg
import com.example.ui.theme.NeumorphicDarkShadow
import com.example.ui.theme.NeumorphicLightShadow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Mobile-friendly terms & privacy consent checkbox with independently clickable links.
 * Text: "আমি নিয়ম ও শর্তাবলি এবং গোপনীয়তা নীতি পড়েছি ও সম্মত হয়েছি।"
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TermsAndPrivacyConsentCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    testTag: String = "checkbox_terms_and_privacy"
) {
    val bgColor = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextSecondary
    val highlightColor = if (isDarkMode) Color(0xFFF4D58A) else BrandRed
    val darkShadow = if (isDarkMode) Color(0xFF071814) else NeumorphicDarkShadow
    val lightShadow = if (isDarkMode) Color(0xFF133B32).copy(alpha = 0.3f) else NeumorphicLightShadow
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Custom Neumorphic Checkbox indicator
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(22.dp)
                .testTag(testTag)
                .clip(RoundedCornerShape(7.dp))
                .background(bgColor)
                .drawWithContent {
                    drawContent()
                    val r = 7.dp.toPx()
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(darkShadow.copy(alpha = 0.6f), Color.Transparent),
                            start = Offset(0f, 0f),
                            end = Offset(4.dp.toPx(), 4.dp.toPx())
                        ),
                        cornerRadius = CornerRadius(r, r)
                    )
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Transparent, lightShadow.copy(alpha = 0.9f)),
                            start = Offset(size.width - 4.dp.toPx(), size.height - 4.dp.toPx()),
                            end = Offset(size.width, size.height)
                        ),
                        cornerRadius = CornerRadius(r, r)
                    )
                }
                .border(
                    width = 1.2.dp,
                    color = if (checked) highlightColor else darkShadow,
                    shape = RoundedCornerShape(7.dp)
                )
                .clickable(
                    role = Role.Checkbox,
                    onClick = { onCheckedChange(!checked) }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(highlightColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "নির্বাচিত",
                        tint = if (isDarkMode) Color(0xFF123D32) else Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // FlowRow text with independent clickable links
        FlowRow(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "আমি ",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.sp,
                    color = textColor,
                    lineHeight = 20.sp
                ),
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onCheckedChange(!checked) }
            )
            Text(
                text = "নিয়ম ও শর্তাবলি",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = highlightColor,
                    textDecoration = TextDecoration.Underline,
                    lineHeight = 20.sp
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = highlightColor.copy(alpha = 0.25f))
                    ) { onTermsClick() }
                    .padding(horizontal = 2.dp)
                    .testTag("link_terms_and_conditions")
            )
            Text(
                text = " এবং ",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.sp,
                    color = textColor,
                    lineHeight = 20.sp
                ),
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onCheckedChange(!checked) }
            )
            Text(
                text = "গোপনীয়তা নীতি",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = highlightColor,
                    textDecoration = TextDecoration.Underline,
                    lineHeight = 20.sp
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = highlightColor.copy(alpha = 0.25f))
                    ) { onPrivacyClick() }
                    .padding(horizontal = 2.dp)
                    .testTag("link_privacy_policy")
            )
            Text(
                text = " পড়েছি ও সম্মত হয়েছি।",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 13.sp,
                    color = textColor,
                    lineHeight = 20.sp
                ),
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onCheckedChange(!checked) }
            )
        }
    }
}

/**
 * Detailed Terms & Conditions Modal (নিয়ম ও শর্তাবলি)
 * Contains all 11 mandatory detailed sections in Bengali.
 */
@Composable
fun TermsAndConditionsModal(
    onDismiss: () -> Unit,
    onAcceptAndDismiss: () -> Unit,
    isDarkMode: Boolean = false
) {
    val cardBg = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextPrimary
    val subTextColor = if (isDarkMode) Color(0xFF9FB2A8) else TextMuted
    val highlightColor = if (isDarkMode) Color(0xFFF4D58A) else BrandRed

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        NeumorphicCard(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.86f)
                .widthIn(max = 520.dp),
            cornerRadius = 28.dp,
            backgroundColor = cardBg,
            isDarkMode = isDarkMode
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BrandRed.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Gavel,
                                contentDescription = null,
                                tint = BrandRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "নিয়ম ও শর্তাবলি",
                                style = TextStyle(
                                    fontFamily = HindSiliguriFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp,
                                    color = textColor
                                )
                            )
                            Text(
                                text = "কওমি কর্মসংস্থান বাংলাদেশ",
                                style = TextStyle(
                                    fontFamily = HindSiliguriFamily,
                                    fontSize = 12.sp,
                                    color = subTextColor
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_close_terms_modal")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Content with all 11 sections
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(end = 4.dp)
                ) {
                    LegalSectionItem(
                        number = "১",
                        title = "অ্যাপের উদ্দেশ্য ও কার্যক্রম",
                        content = "কওমি কর্মসংস্থান বাংলাদেশ একটি বিশেষায়িত প্ল্যাটফর্ম যা কওমি মাদরাসা ও সংশ্লিষ্ট ইসলামী শিক্ষা প্রতিষ্ঠানের শিক্ষক, মুহতামিম, কারী, হাফেজ, নাযেম এবং কওমি শিক্ষার্থীদের সঙ্গে সম্মানজনক কর্মসংস্থানের সুযোগ সৃষ্টি ও সংযোগ স্থাপনের উদ্দেশ্যে পরিচালিত হয়। এই প্ল্যাটফর্মের মাধ্যমে নিয়োগদাতা প্রতিষ্ঠানসমূহ খেদমত বিজ্ঞপ্তি প্রকাশ করতে পারে এবং যোগ্য প্রার্থীরা সরাসরি আবেদন করতে পারে।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "২",
                        title = "অ্যাকাউন্ট তৈরি ও সঠিক তথ্য প্রদানের নিয়ম",
                        content = "অ্যাপে নিবন্ধনের সময় ব্যবহারকারীকে অবশ্যই তার প্রকৃত নাম, সক্রিয় ও বৈধ ইমেইল বা ইউজারনেম এবং সঠিক তথ্য প্রদান করতে হবে। কোনো প্রকার কাল্পনিক নাম, বিভ্রান্তিকর পরিচয় বা অন্যের পরিচয় ব্যবহার করে অ্যাকাউন্ট খোলা সম্পূর্ণ নিষিদ্ধ। পাসওয়ার্ডের সুরক্ষা ও গোপনীয়তা বজায় রাখার সম্পূর্ণ দায় ব্যবহারকারীর।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৩",
                        title = "খেদমতপ্রত্যাশীদের দায়িত্ব",
                        content = "খেদমতপ্রত্যাশীদের তাদের শিক্ষাগত যোগ্যতা (যেমন: দাওরায়ে হাদিস, ইফতা, আদব, হিফজ ইত্যাদি), শিক্ষকতা বা কর্মক্ষেত্রের অভিজ্ঞতা ও ব্যক্তিগত তথ্যের ক্ষেত্রে শতভাগ সত্যতা নিশ্চিত করতে হবে। কোনো অসত্য বা ভুয়া সনদ/তথ্য প্রদর্শনের দায় খেদমতপ্রত্যাশীর নিজের। আবেদনের পূর্বে প্রতিষ্ঠানের নিয়মাবলি ভালোভাবে জেনে নেওয়া আবশ্যক।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৪",
                        title = "নিয়োগদাতা ও মাদরাসা কর্তৃপক্ষের দায়িত্ব",
                        content = "মাদরাসা বা প্রতিষ্ঠান কর্তৃপক্ষকে বাস্তবসম্মত ও স্বচ্ছ খেদমত বিজ্ঞপ্তি প্রকাশ করতে হবে। বেতন, আবাসন সুবিধা, দায়িত্ব ও কর্মস্থল সম্পর্কে সুস্পষ্ট তথ্য প্রদান করতে হবে। কোনো বিভ্রান্তিকর বা অবাস্তব সুযোগ-সুবিধার লোভ দেখিয়ে নিয়োগ বিজ্ঞপ্তি প্রকাশ করা যাবে না। প্রার্থীদের সাথে সম্মানজনক ও শরয়ী শিষ্টাচার বজায় রেখে আচরণ করতে হবে।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৫",
                        title = "ভুয়া খেদমত বিজ্ঞপ্তি, প্রতারণা ও নিষিদ্ধ কার্যক্রম",
                        content = "খেদমত দেওয়ার নামে কোনো প্রকার অর্থ দাবি করা, রেজিস্ট্রেশন ফি বা জামানতের নামে টাকা আত্মসাৎ করা সম্পূর্ণ নিষিদ্ধ। কওমি কর্মসংস্থান বাংলাদেশ প্ল্যাটফর্মে কোনো প্রকার জুয়া, অনৈতিক প্রচার, কপিরাইট লঙ্ঘন বা ইসলামি অনুশাসন পরিপন্থী কার্যকলাপ কঠোরভাবে নিষিদ্ধ। এমন কর্মকাণ্ডে জড়িতদের বিরুদ্ধে আইনি ও প্রাতিষ্ঠানিক ব্যবস্থা নেওয়া হবে।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৬",
                        title = "খেদমতের আবেদন ও নিয়োগের শর্ত",
                        content = "অ্যাপ কর্তৃপক্ষ কেবলমাত্র প্রার্থী ও নিয়োগদাতার মধ্যে সংযোগ স্থাপনকারী মাধ্যম হিসেবে কাজ করে। প্রার্থী বাছাই, সাক্ষাৎকার গ্রহণ, বেতন নির্ধারণ ও চূড়ান্ত নিয়োগের পূর্ণ এখতিয়ার নিয়োগকারী প্রতিষ্ঠানের। নিয়োগ-পরবর্তী যেকোনো চুক্তি বা মতবিরোধের দায় সংশ্লিষ্ট উভয় পক্ষের।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৭",
                        title = "ব্যবহারকারীর আচরণবিধি",
                        content = "অ্যাপ ব্যবহারকালে অন্য কোনো ব্যবহারকারী বা প্রতিষ্ঠানের প্রতি অসৌজন্যমূলক আচরণ, কটূক্তি বা বিভ্রান্তিকর মন্তব্য প্রকাশ করা যাবে না। অ্যাপের সার্ভার বা প্রযুক্তিতে বিঘ্ন সৃষ্টি করা বা অননুমোদিত অ্যাক্সেস নেওয়ার অপচেষ্টা থেকে বিরত থাকতে হবে।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৮",
                        title = "অ্যাকাউন্ট স্থগিত বা বন্ধ করার নীতি",
                        content = "শর্তাবলি লঙ্ঘন, ভুয়া তথ্য বা প্রতারণামূলক কার্যকলাপের প্রমাণ পাওয়া গেলে পূর্ব নোটিশ ছাড়াই সংশ্লিষ্ট ব্যবহারকারীর অ্যাকাউন্ট সাময়িক স্থগিত বা স্থায়ীভাবে বাতিল করার পূর্ণ অধিকার অ্যাপ কর্তৃপক্ষের সংরক্ষিত থাকবে।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৯",
                        title = "অ্যাপ কর্তৃপক্ষের দায়িত্ব ও সীমাবদ্ধতা",
                        content = "অ্যাপ কর্তৃপক্ষ যথাসম্ভব যাচাই-বাছাই করে তথ্য উপস্থাপনের চেষ্টা করে, তবে কোনো বিজ্ঞপ্তি বা ব্যবহারকারীর দেওয়া তথ্যের শতভাগ নির্ভুলতার গ্যারান্টি কর্তৃপক্ষ দেয় না। তৃতীয় পক্ষের আর্থিক লেনদেন বা বাহ্যিক চুক্তির জন্য অ্যাপ কর্তৃপক্ষ দায়ী থাকবে না।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "১০",
                        title = "নিয়ম পরিবর্তনের পদ্ধতি",
                        content = "কওমি কর্মসংস্থান বাংলাদেশ কর্তৃপক্ষ যেকোনো সময় প্রয়োজনানুসারে এই নিয়ম ও শর্তাবলি হালনাগাদ বা পরিবর্তন করার অধিকার রাখে। পরিবর্তিত নিয়মাবলি অ্যাপে প্রকাশের সাথে সাথে তা কার্যকর বলে গণ্য হবে।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "১১",
                        title = "অভিযোগ ও যোগাযোগের ব্যবস্থা",
                        content = "কোনো ভুয়া বিজ্ঞপ্তি, হয়রানি বা প্রতারণার শিকার হলে ব্যবহারকারীরা অ্যাপের সহায়তা বিভাগে যোগাযোগ করতে পারবেন। ইমেইল: support@kaomikormo.com এর মাধ্যমে সরাসরি অভিযোগ দায়ের করা যাবে। কর্তৃপক্ষ দ্রুততম সময়ে বিষয়টি পর্যালোচনা করে ব্যবস্থা গ্রহণ করবে।",
                        isDarkMode = isDarkMode
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_terms_dismiss"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "বন্ধ করুন",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = if (isDarkMode) subTextColor else TextSecondary
                            )
                        )
                    }

                    ElevatedButton(
                        onClick = onAcceptAndDismiss,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(46.dp)
                            .testTag("btn_terms_accept"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = highlightColor,
                            contentColor = if (isDarkMode) Color(0xFF123D32) else Color.White
                        )
                    ) {
                        Text(
                            text = "পড়েছি ও সম্মত আছি",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Detailed Privacy Policy Modal (গোপনীয়তা নীতি)
 * Contains all mandatory privacy points in Bengali.
 */
@Composable
fun PrivacyPolicyModal(
    onDismiss: () -> Unit,
    onAcceptAndDismiss: () -> Unit,
    isDarkMode: Boolean = false
) {
    val cardBg = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextPrimary
    val subTextColor = if (isDarkMode) Color(0xFF9FB2A8) else TextMuted
    val highlightColor = if (isDarkMode) Color(0xFFF4D58A) else BrandRed

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        NeumorphicCard(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.86f)
                .widthIn(max = 520.dp),
            cornerRadius = 28.dp,
            backgroundColor = cardBg,
            isDarkMode = isDarkMode
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BrandRed.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = BrandRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "গোপনীয়তা নীতি",
                                style = TextStyle(
                                    fontFamily = HindSiliguriFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp,
                                    color = textColor
                                )
                            )
                            Text(
                                text = "কওমি কর্মসংস্থান বাংলাদেশ",
                                style = TextStyle(
                                    fontFamily = HindSiliguriFamily,
                                    fontSize = 12.sp,
                                    color = subTextColor
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_close_privacy_modal")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(end = 4.dp)
                ) {
                    LegalSectionItem(
                        number = "১",
                        title = "কোন ব্যক্তিগত তথ্য সংগ্রহ করা হয়",
                        content = "ব্যবহারকারীর নিবন্ধন ও সেবা ব্যবহারের সময় আমরা আপনার নাম, ইমেইল ঠিকানা, প্রোফাইল ছবি (গুগল সাইন-ইন এর ক্ষেত্রে), শিক্ষাগত যোগ্যতা (কওমি মাদরাসা সনদ), অভিজ্ঞতা ও বায়োডাটার তথ্য সংগ্রহ করি। এছাড়াও নিরাপদ লগইন ও জালিয়াতি রোধে ডিভাইসের মৌলিক প্রযুক্তিগত তথ্য (যেমন: ডিভাইস মডেল ও ওএস সংস্করণ) সংগৃহীত হতে পারে।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "২",
                        title = "নাম, মোবাইল নম্বর, ইমেইল, শিক্ষাগত যোগ্যতা ও কর্মসংস্থান সংক্রান্ত তথ্যের ব্যবহার",
                        content = "সংগৃহীত সকল তথ্য কেবলমাত্র খেদমত ও শিক্ষকতার আবেদন প্রক্রিয়াকরণ, উপযুক্ত পদের বিজ্ঞপ্তি প্রদর্শন, নিয়োগকারী মাদরাসা কর্তৃপক্ষের সাথে প্রার্থীর যোগাযোগ স্থাপন, অ্যাকাউন্ট নিরাপত্তা যাচাই এবং প্ল্যাটফর্মের সেবার মানোন্নয়নের উদ্দেশ্যে ব্যবহার করা হয়। ব্যবহারকারীর লিখিত অনুমতি ছাড়া কোনো বাণিজ্যিক প্রচারণার কাজে এই তথ্য ব্যবহার করা হবে না।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৩",
                        title = "Firebase Authentication ও অন্যান্য সেবার মাধ্যমে তথ্য প্রক্রিয়াকরণ",
                        content = "নিরাপদ লগইন, ব্যবহারকারী প্রমাণীকরণ এবং পাসওয়ার্ড রিসেট প্রক্রিয়ার জন্য আমরা বিশ্বস্ত Google Firebase Authentication প্রযুক্তি ব্যবহার করি। আপনার পাসওয়ার্ড কখনোই সরাসরি দৃশ্যমান আকারে সংরক্ষিত হয় না, বরং আন্তর্জাতিক মানের ক্রিপ্টোগ্রাফিক হ্যাশিং এর মাধ্যমে সম্পূর্ণ সুরক্ষিত থাকে।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৪",
                        title = "তথ্য সংরক্ষণ ও নিরাপত্তা ব্যবস্থা",
                        content = "কওমি কর্মসংস্থান বাংলাদেশ আপনার তথ্যের সর্বোচ্চ নিরাপত্তা বিধানে অঙ্গীকারবদ্ধ। তথ্যের অননুমোদিত প্রবেশ, অনাকাঙ্ক্ষিত পরিবর্তন, চুরি বা ফাঁস রোধে আমরা উন্নত ফায়ারওয়াল, নিরাপদ এনক্রিপশন প্রোটোকল এবং কঠোর ডেটা অ্যাক্সেস নিয়ন্ত্রণ নীতি অনুসরণ করি।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৫",
                        title = "তৃতীয় পক্ষের সাথে তথ্য ভাগাভাগি নীতি",
                        content = "আমরা কোনো অবস্থাতেই ব্যবহারকারীর ব্যক্তিগত তথ্য তৃতীয় কোনো বিজ্ঞাপনদাতা বা বাণিজ্যিক প্রতিষ্ঠানের কাছে বিক্রি, ভাড়া বা হস্তান্তর করি না। কেবল যখন কোনো খেদমতপ্রত্যাশী কোনো খেদমত বিজ্ঞপ্তিতে স্বেচ্ছায় আবেদন করেন, তখন নিয়োগকারী প্রতিষ্ঠানকে আবেদনকারী প্রার্থীর সংশ্লিষ্ট শিক্ষাগত ও যোগাযোগের তথ্য প্রদর্শিত হয়।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৬",
                        title = "ব্যবহারকারীর তথ্য অধিকার ও নিয়ন্ত্রণ",
                        content = "ব্যবহারকারী যেকোনো সময় নিজের প্রোফাইলের তথ্য দেখার, পরিবর্তন করার বা সংশোধনের পূর্ণ অধিকার রাখেন। এছাড়াও ব্যবহারকারী চাইলে তার অ্যাকাউন্ট এবং সংরক্ষিত তথ্য স্থায়ীভাবে মুছে ফেলার (Data Deletion) জন্য কর্তৃপক্ষের সাথে যোগাযোগ করতে পারেন।",
                        isDarkMode = isDarkMode
                    )

                    LegalSectionItem(
                        number = "৭",
                        title = "গোপনীয়তা নীতি হালনাগাদ ও যোগাযোগ",
                        content = "আইনগত বা সেবার মানোন্নয়নের প্রয়োজনে এই গোপনীয়তা নীতি সময়ে সময়ে হালনাগাদ হতে পারে। যেকোনো পরিবর্তনের পর অ্যাপে বিজ্ঞপ্তি দিয়ে জানানো হবে। গোপনীয়তা ও ডেটা সুরক্ষা বিষয়ে যেকোনো প্রশ্নের জন্য আমাদের ডেটা সুরক্ষা টিমের সাথে যোগাযোগ করুন: privacy@kaomikormo.com।",
                        isDarkMode = isDarkMode
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_privacy_dismiss"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "বন্ধ করুন",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        )
                    }

                    ElevatedButton(
                        onClick = onAcceptAndDismiss,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(46.dp)
                            .testTag("btn_privacy_accept"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = BrandRed,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "পড়েছি ও সম্মত আছি",
                            style = TextStyle(
                                fontFamily = HindSiliguriFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual legal section card item with badge and formatted typography
 */
@Composable
private fun LegalSectionItem(
    number: String,
    title: String,
    content: String,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextPrimary
    val subTextColor = if (isDarkMode) Color(0xFF9FB2A8) else TextSecondary
    val highlightColor = if (isDarkMode) Color(0xFFF4D58A) else BrandRed
    val darkShadow = if (isDarkMode) Color(0xFF071814) else NeumorphicDarkShadow

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = 1.dp,
                color = darkShadow.copy(alpha = 0.35f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(highlightColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    style = TextStyle(
                        fontFamily = HindSiliguriFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = if (isDarkMode) Color(0xFF123D32) else Color.White
                    )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    color = if (isDarkMode) Color(0xFFF4D58A) else BrandRedDark
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = content,
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = textColor,
                lineHeight = 19.sp
            ),
            textAlign = TextAlign.Start
        )
    }
}
