package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BrandRed
import com.example.ui.theme.BrandRedDark
import com.example.ui.theme.BrandRedLight
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.HindSiliguriFamily
import com.example.ui.theme.NeumorphicBg
import com.example.ui.theme.NeumorphicDarkShadow
import com.example.ui.theme.NeumorphicDeepShadow
import com.example.ui.theme.NeumorphicLightShadow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicEmerald

/**
 * Neumorphic Card container with soft dual shadows (light on top-left, dark on bottom-right)
 * precisely matching code-with-nazia.css design.
 */
@Composable
fun NeumorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 36.dp,
    backgroundColor: Color = NeumorphicBg,
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val lightShadow = if (isDarkMode) Color(0xFF133B32).copy(alpha = 0.3f) else NeumorphicLightShadow
    val darkShadow = if (isDarkMode) Color(0xFF071814).copy(alpha = 0.7f) else NeumorphicDarkShadow
    val deepShadow = if (isDarkMode) Color(0xFF000000).copy(alpha = 0.6f) else NeumorphicDeepShadow

    Box(
        modifier = modifier
            .drawBehind {
                val r = cornerRadius.toPx()
                // Outer dark shadow (bottom-right)
                drawRoundRect(
                    color = deepShadow.copy(alpha = 0.40f),
                    topLeft = Offset(8.dp.toPx(), 8.dp.toPx()),
                    size = size,
                    cornerRadius = CornerRadius(r, r)
                )
                drawRoundRect(
                    color = darkShadow.copy(alpha = 0.50f),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    size = size,
                    cornerRadius = CornerRadius(r, r)
                )
                // Outer light highlight (top-left)
                drawRoundRect(
                    color = lightShadow.copy(alpha = if (isDarkMode) 0.4f else 0.95f),
                    topLeft = Offset(-6.dp.toPx(), -6.dp.toPx()),
                    size = size,
                    cornerRadius = CornerRadius(r, r)
                )
                drawRoundRect(
                    color = lightShadow.copy(alpha = if (isDarkMode) 0.3f else 0.70f),
                    topLeft = Offset(-3.dp.toPx(), -3.dp.toPx()),
                    size = size,
                    cornerRadius = CornerRadius(r, r)
                )
            }
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = lightShadow.copy(alpha = 0.4f),
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}

/**
 * Iconic circular avatar with an inner ring and outer soft shadow,
 * matching code-with-nazia.css header element.
 */
@Composable
fun NeumorphicCircularAvatar(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 92.dp,
    icon: ImageVector? = null,
    isDarkMode: Boolean = false,
    testTag: String = "neumorphic_avatar"
) {
    val bgColor = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val lightShadow = if (isDarkMode) Color(0xFF133B32).copy(alpha = 0.35f) else NeumorphicLightShadow
    val darkShadow = if (isDarkMode) Color(0xFF071814).copy(alpha = 0.65f) else NeumorphicDarkShadow
    val deepShadow = if (isDarkMode) Color(0xFF000000).copy(alpha = 0.6f) else NeumorphicDeepShadow
    val highlightColor = if (isDarkMode) Color(0xFFF4D58A) else BrandRed

    Box(
        modifier = modifier
            .size(sizeDp)
            .testTag(testTag)
            .drawBehind {
                val radius = size.minDimension / 2
                // Outer dark shadow
                drawCircle(
                    color = deepShadow.copy(alpha = 0.45f),
                    radius = radius,
                    center = center + Offset(6.dp.toPx(), 6.dp.toPx())
                )
                // Outer light shadow
                drawCircle(
                    color = lightShadow.copy(alpha = 0.95f),
                    radius = radius,
                    center = center + Offset(-6.dp.toPx(), -6.dp.toPx())
                )
            }
            .clip(CircleShape)
            .background(bgColor)
            .border(width = 3.dp, color = lightShadow.copy(alpha = 0.7f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Inner Ring
        Box(
            modifier = Modifier
                .size(sizeDp - 20.dp)
                .clip(CircleShape)
                .background(bgColor)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(darkShadow, lightShadow)
                    ),
                    shape = CircleShape
                )
                .drawWithContent {
                    drawContent()
                    // Inner shadow effect
                    drawCircle(
                        color = darkShadow.copy(alpha = 0.25f),
                        radius = size.minDimension / 2,
                        center = center + Offset(1.5.dp.toPx(), 1.5.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx())
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Avatar Emblem",
                    tint = highlightColor,
                    modifier = Modifier.size(sizeDp - 46.dp)
                )
            }
        }
    }
}

/**
 * Inset / Recessed Neumorphic Text Input Field.
 * Implements inner drop shadows and red accent focus indicator,
 * perfectly echoing `box-shadow: inset 6px 6px 6px #cbced1, inset -6px -6px 6px white;`
 */
@Composable
fun NeumorphicInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    errorMessage: String? = null,
    isDarkMode: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    testTag: String = "neumorphic_input"
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val bgColor = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val lightShadow = if (isDarkMode) Color(0xFF133B32).copy(alpha = 0.25f) else NeumorphicLightShadow
    val darkShadow = if (isDarkMode) Color(0xFF071814).copy(alpha = 0.65f) else NeumorphicDarkShadow
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextPrimary
    val mutedColor = if (isDarkMode) Color(0xFF9FB2A8) else TextMuted

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
                .onFocusChanged { isFocused = it.isFocused },
            textStyle = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            ),
            singleLine = true,
            cursorBrush = SolidColor(if (isDarkMode) IslamicGold else BrandRed),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(bgColor)
                        .border(
                            width = if (errorMessage != null) 1.5.dp else if (isFocused) 1.5.dp else 1.dp,
                            color = if (errorMessage != null) ErrorRed
                            else if (isFocused) (if (isDarkMode) IslamicGold else BrandRed)
                            else darkShadow.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(27.dp)
                        )
                        .drawBehind {
                            val r = 27.dp.toPx()
                            // Top-Left Inset Dark Shadow
                            drawRoundRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        darkShadow.copy(alpha = 0.55f),
                                        Color.Transparent
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(8.dp.toPx(), 8.dp.toPx())
                                ),
                                cornerRadius = CornerRadius(r, r)
                            )
                            // Bottom-Right Inset Light Shadow
                            drawRoundRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        lightShadow.copy(alpha = 0.85f)
                                    ),
                                    start = Offset(size.width - 8.dp.toPx(), size.height - 8.dp.toPx()),
                                    end = Offset(size.width, size.height)
                                ),
                                cornerRadius = CornerRadius(r, r)
                            )
                        }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        if (leadingIcon != null) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = null,
                                tint = if (errorMessage != null) ErrorRed else if (isFocused) (if (isDarkMode) IslamicGold else BrandRed) else mutedColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = TextStyle(
                                        fontFamily = HindSiliguriFamily,
                                        fontSize = 14.sp,
                                        color = mutedColor
                                    )
                                )
                            }
                            innerTextField()
                        }

                        if (isPassword) {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (passwordVisible) "পাসওয়ার্ড লুকান" else "পাসওয়ার্ড দেখুন",
                                    tint = if (passwordVisible) (if (isDarkMode) IslamicGold else BrandRed) else mutedColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        )

        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 12.sp,
                    color = ErrorRed,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

/**
 * Gradient Red Neumorphic Action Button with smooth tactile press animation.
 * Features dark red #a10f0f gradient style from code-with-nazia.css.
 */
@Composable
fun NeumorphicButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    testTag: String = "neumorphic_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        animationSpec = tween(120),
        label = "btn_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .scale(scale)
            .testTag(testTag)
            .drawBehind {
                val r = 26.dp.toPx()
                if (!isPressed) {
                    // Raised shadow when idle
                    drawRoundRect(
                        color = BrandRedDark.copy(alpha = 0.35f),
                        topLeft = Offset(4.dp.toPx(), 5.dp.toPx()),
                        size = size,
                        cornerRadius = CornerRadius(r, r)
                    )
                    drawRoundRect(
                        color = NeumorphicLightShadow.copy(alpha = 0.65f),
                        topLeft = Offset(-3.dp.toPx(), -3.dp.toPx()),
                        size = size,
                        cornerRadius = CornerRadius(r, r)
                    )
                }
            }
            .clip(RoundedCornerShape(26.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (enabled) {
                        listOf(BrandRedLight, BrandRed, BrandRedDark)
                    } else {
                        listOf(NeumorphicDarkShadow, NeumorphicDeepShadow)
                    }
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color.White.copy(alpha = 0.45f)),
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

/**
 * Neumorphic Google Button with official 4-color Google logo
 * labeled "গুগল দিয়ে এগিয়ে যান" (Continue with Google).
 */
@Composable
fun NeumorphicGoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    testTag: String = "btn_google_sign_in"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = tween(80),
        label = "google_btn_scale"
    )

    val bgColor = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val lightShadow = if (isDarkMode) Color(0xFF133B32).copy(alpha = 0.35f) else NeumorphicLightShadow
    val darkShadow = if (isDarkMode) Color(0xFF071814).copy(alpha = 0.65f) else NeumorphicDarkShadow
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextPrimary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .scale(scale)
            .testTag(testTag)
            .drawBehind {
                val r = 25.dp.toPx()
                if (!isPressed) {
                    drawRoundRect(
                        color = darkShadow.copy(alpha = 0.55f),
                        topLeft = Offset(3.dp.toPx(), 4.dp.toPx()),
                        size = size,
                        cornerRadius = CornerRadius(r, r)
                    )
                    drawRoundRect(
                        color = lightShadow.copy(alpha = 0.95f),
                        topLeft = Offset(-3.dp.toPx(), -3.dp.toPx()),
                        size = size,
                        cornerRadius = CornerRadius(r, r)
                    )
                }
            }
            .clip(RoundedCornerShape(25.dp))
            .background(if (isPressed) darkShadow.copy(alpha = 0.25f) else bgColor)
            .border(
                width = 1.dp,
                color = lightShadow.copy(alpha = 0.8f),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = (if (isDarkMode) IslamicGold else BrandRed).copy(alpha = 0.25f)),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google Logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "গুগল দিয়ে এগিয়ে যান",
                style = TextStyle(
                    fontFamily = HindSiliguriFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            )
        }
    }
}

/**
 * Neumorphic Checkbox / Toggle for "Remember Me" (আমাকে মনে রাখুন).
 */
@Composable
fun NeumorphicCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    testTag: String = "remember_me_checkbox"
) {
    val bgColor = if (isDarkMode) Color(0xFF0E2E27) else NeumorphicBg
    val lightShadow = if (isDarkMode) Color(0xFF133B32).copy(alpha = 0.35f) else NeumorphicLightShadow
    val darkShadow = if (isDarkMode) Color(0xFF071814).copy(alpha = 0.65f) else NeumorphicDarkShadow
    val textColor = if (isDarkMode) Color(0xFFF4F1E8) else TextSecondary

    Row(
        modifier = modifier
            .clickable(
                role = Role.Checkbox,
                onClick = { onCheckedChange(!checked) }
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .testTag(testTag)
                .clip(RoundedCornerShape(7.dp))
                .background(bgColor)
                .drawWithContent {
                    drawContent()
                    val r = 7.dp.toPx()
                    // Inset dark
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(darkShadow.copy(alpha = 0.6f), Color.Transparent),
                            start = Offset(0f, 0f),
                            end = Offset(4.dp.toPx(), 4.dp.toPx())
                        ),
                        cornerRadius = CornerRadius(r, r)
                    )
                    // Inset light
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
                    width = 1.dp,
                    color = if (checked) (if (isDarkMode) IslamicGold else BrandRed) else darkShadow,
                    shape = RoundedCornerShape(7.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(if (isDarkMode) IslamicGold else BrandRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "নির্বাচিত",
                        tint = if (isDarkMode) IslamicEmerald else Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = label,
            style = TextStyle(
                fontFamily = HindSiliguriFamily,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        )
    }
}
