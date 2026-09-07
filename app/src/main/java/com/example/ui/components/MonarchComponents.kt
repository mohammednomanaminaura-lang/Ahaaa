package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Immersive UI Glassmorphic Card Container with subtle backdrop depth & glowing radial aura
 */
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.White.copy(alpha = 0.10f),
    backgroundColor: Color = Color.White.copy(alpha = 0.05f),
    shape: Shape = RoundedCornerShape(20.dp),
    borderWidth: Dp = 1.dp,
    glowAuraColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(borderWidth, borderColor, shape)
            .padding(16.dp)
    ) {
        if (glowAuraColor != null) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-30).dp)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                glowAuraColor.copy(alpha = 0.25f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
        }
        content()
    }
}

/**
 * Immersive UI System Pill Badge
 */
@Composable
fun MonarchPillBadge(
    text: String,
    color: Color = ElectricNeonBlue,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(100.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text.uppercase(),
            color = color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Immersive UI Horizontal Stat Bar with sleek track & glowing fill
 */
@Composable
fun MonarchStatBar(
    label: String,
    valueText: String,
    progress: Float,
    barColor: Color = ElectricNeonBlue,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.uppercase(),
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = valueText,
                color = barColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(Color(0xFF1E293B))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(100.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                barColor.copy(alpha = 0.8f),
                                barColor
                            )
                        )
                    )
            )
        }
    }
}

/**
 * Iconic Solo Leveling [SYSTEM ALERT] Notification Window
 */
@Composable
fun SystemNotificationBox(
    title: String = "SYSTEM NOTIFICATION",
    isPenalty: Boolean = false,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val mainColor = if (isPenalty) CrimsonPenaltyRed else ElectricNeonBlue
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(VoidDark.copy(alpha = 0.92f))
            .border(1.dp, mainColor.copy(alpha = alpha), RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("system_notification_box")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(mainColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "[ $title ]",
                        color = mainColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (onDismiss != null) {
                    Text(
                        text = "✕",
                        color = TextMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(4.dp)
                            .testTag("dismiss_system_box")
                    )
                }
            }

            // Decorative separator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(mainColor, mainColor.copy(alpha = 0.15f), Color.Transparent)
                        )
                    )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Body Content
            content()
        }
    }
}

/**
 * Immersive UI Gradient Action Button (e.g. Arise Primary Button)
 */
@Composable
fun ImmersiveGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(NecromancerPurple, Color(0xFF4B0082)),
    textColor: Color = Color.White,
    testTag: String = "immersive_gradient_button",
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (enabled) Brush.horizontalGradient(gradientColors)
                else Brush.horizontalGradient(listOf(VoidSurface, VoidDark))
            )
            .border(
                width = 1.dp,
                color = if (enabled) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            color = if (enabled) textColor else TextMuted,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

/**
 * Neon Glowing Action Button with Immersive glassmorphism
 */
@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = ElectricNeonBlue,
    testTag: String = "neon_button",
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (enabled) Color.White.copy(alpha = 0.05f) else VoidSurface)
            .border(
                width = 1.dp,
                color = if (enabled) color.copy(alpha = 0.6f) else VoidBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            color = if (enabled) color else TextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            letterSpacing = 1.5.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

