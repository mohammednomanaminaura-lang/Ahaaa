package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.audio.SoundSystem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.MonarchViewModel

/**
 * FEATURE 25: Dynamic 3D Monarch Throne Wallpaper & Master Dashboard
 * Dedicated to Master Mohammad Noman Amin (Absolute Shadow Monarch)
 */
@Composable
fun HomeScreen(viewModel: MonarchViewModel) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val activeAura by viewModel.currentAuraColor.collectAsStateWithLifecycle()
    val weatherGate by viewModel.currentWeatherGate.collectAsStateWithLifecycle()
    val soldiers by viewModel.shadowSoldiers.collectAsStateWithLifecycle()

    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val crownGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crownGlow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    dragOffsetX = (dragOffsetX + dragAmount.x * 0.05f).coerceIn(-30f, 30f)
                    dragOffsetY = (dragOffsetY + dragAmount.y * 0.05f).coerceIn(-30f, 30f)
                }
            }
    ) {
        // Feature 25: Live 3D Parallax Throne Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = dragOffsetX.dp, y = dragOffsetY.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.monarch_throne_bg),
                contentDescription = "Monarch Throne Wallpaper",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.15f),
                contentScale = ContentScale.Crop
            )

            // Dark void gradient overlay for glassmorphic readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                VoidBlack.copy(alpha = 0.5f),
                                VoidBlack.copy(alpha = 0.75f),
                                VoidBlack.copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            // Dynamic Mana Embers
            ParticleBackground(
                particleColor = activeAura,
                particleCount = 35
            )
        }

        // Foreground Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Immersive Header: Arise System & S-Rank Class
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ElectricNeonBlue)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ARISE SYSTEM",
                                color = ElectricNeonBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 3.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = "Monarch Realm",
                            color = TextLight,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Light,
                            letterSpacing = (-0.5).sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "SYSTEM RANK",
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.5.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "S",
                                color = ElectricNeonBlue,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.SansSerif
                            )
                            Text(
                                text = "[CLASS: MONARCH]",
                                color = NecromancerPurple,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // User Credentials Immersive Card
            item {
                GlassmorphicCard(
                    borderColor = Color.White.copy(alpha = 0.12f),
                    backgroundColor = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(20.dp),
                    glowAuraColor = NecromancerPurple,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("monarch_identity_card")
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "USER CREDENTIALS",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                            MonarchPillBadge(
                                text = "Authenticated",
                                color = ElectricNeonBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Absolute Shadow Monarch",
                            color = TextLight,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Mohammad Noman Amin",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Dual Stat Bars Grid (STR LVL 99 & INT LVL 120)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassmorphicCard(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        MonarchStatBar(
                            label = "STR",
                            valueText = "LVL ${stats?.str ?: 99}",
                            progress = 0.92f,
                            barColor = ElectricNeonBlue
                        )
                    }
                    GlassmorphicCard(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        MonarchStatBar(
                            label = "INT",
                            valueText = "LVL ${stats?.`int` ?: 120}",
                            progress = 0.98f,
                            barColor = NecromancerPurple
                        )
                    }
                }
            }

            // Active Quests & Tasks Section (Immersive Card with SSC & Fit tasks + Marshals preview)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ACTIVE QUESTS & TASKS",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "PENALTY ACTIVE",
                            color = CrimsonPenaltyRed,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Quest Item 1: SSC Physics Prep
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(ElectricNeonBlue.copy(alpha = 0.10f))
                                        .border(1.dp, ElectricNeonBlue.copy(alpha = 0.35f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "SSC",
                                        color = ElectricNeonBlue,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Physics Prep (2 Year Plan)",
                                            color = TextLight,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "75%",
                                            color = ElectricNeonBlue,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(100.dp))
                                            .background(Color(0xFF1E293B))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.75f)
                                                .fillMaxHeight()
                                                .background(ElectricNeonBlue)
                                        )
                                    }
                                }
                            }

                            // Quest Item 2: 10km Limit Break Run
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(NecromancerPurple.copy(alpha = 0.10f))
                                        .border(1.dp, NecromancerPurple.copy(alpha = 0.35f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "FIT",
                                        color = NecromancerPurple,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "10km Limit Break Run",
                                            color = TextLight,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "2.4km",
                                            color = NecromancerPurple,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(100.dp))
                                            .background(Color(0xFF1E293B))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.24f)
                                                .fillMaxHeight()
                                                .background(NecromancerPurple)
                                        )
                                    }
                                }
                            }

                            // Divider & Shadow Army Count Strip
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.White.copy(alpha = 0.08f))
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                                    listOf("IG", "BE", "TA").forEach { marshalInitial ->
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(VoidDark)
                                                .border(1.5.dp, Color.White.copy(alpha = 0.15f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = marshalInitial,
                                                color = ElectricNeonBlue,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "SHADOW ARMY SIZE: ${soldiers.size.coerceAtLeast(342)}",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Immersive Action Buttons (Arise & Skills)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ImmersiveGradientButton(
                        text = "ARISE",
                        onClick = {
                            SoundSystem.playAriseSound()
                            viewModel.selectedTab.value = 2
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "immersive_arise_button"
                    )
                    NeonButton(
                        text = "SKILLS",
                        onClick = {
                            SoundSystem.playRulersAuthority()
                            viewModel.selectedTab.value = 2
                        },
                        color = ElectricNeonBlue,
                        modifier = Modifier.weight(1f),
                        testTag = "immersive_skills_button"
                    )
                }
            }

            // System Status Matrix Window
            item {
                SystemNotificationBox(
                    title = "MONARCH STATUS MATRIX",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val s = stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LEVEL ${s?.level ?: 120}",
                                color = ElectricNeonBlue,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "CLASS: ${s?.hunterRank ?: "Shadow Monarch (Rank EX)"}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "GOLD: ${(s?.gold ?: 45000000L)} G",
                                color = MonarchGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // HP Bar
                    StatusBar(
                        label = "HP",
                        current = s?.hp ?: 98500,
                        max = s?.maxHp ?: 98500,
                        fillColor = CrimsonPenaltyRed
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // MP Bar
                    StatusBar(
                        label = "MP",
                        current = s?.mp ?: 142000,
                        max = s?.maxMp ?: 142000,
                        fillColor = ElectricNeonBlue
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // EXP Bar
                    StatusBar(
                        label = "EXP",
                        current = s?.currentExp ?: 84500,
                        max = s?.maxExp ?: 100000,
                        fillColor = NecromancerPurple
                    )
                }
            }

            // Quick Navigation Hub to 25 Features
            item {
                Text(
                    text = "SYSTEM REALM MODULES",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickModuleButton(
                        title = "ART STUDIO & MANHWA CREATOR",
                        subtitle = "Shadow Canvas, AI Avatar, Voice Recorder & Gear",
                        icon = Icons.Default.Brush,
                        color = ElectricNeonBlue,
                        onClick = { viewModel.selectedTab.value = 1 }
                    )
                    QuickModuleButton(
                        title = "BATTLE ZONE & SHADOW EXTRACTION",
                        subtitle = "Live Combat, Rank Test, 'ARISE' & Monarch Skills",
                        icon = Icons.Default.Security,
                        color = CrimsonPenaltyRed,
                        onClick = { viewModel.selectedTab.value = 2 }
                    )
                    QuickModuleButton(
                        title = "DAILY QUEST & SSC ACADEMIC TRACKER",
                        subtitle = "Fitness, 30-Day Fat Loss, 2-Year Plan & Vault",
                        icon = Icons.Default.FitnessCenter,
                        color = MonarchGold,
                        onClick = { viewModel.selectedTab.value = 3 }
                    )
                    QuickModuleButton(
                        title = "MULTILINGUAL ANIME AI COMPANION",
                        subtitle = "Hindi/English Chat, Trivia Base, Igris/Beru Advice",
                        icon = Icons.Default.Chat,
                        color = NecromancerPurple,
                        onClick = { viewModel.selectedTab.value = 4 }
                    )
                    QuickModuleButton(
                        title = "SOUNDBOARD, AURA & ADVANCED AR",
                        subtitle = "Necromancer Sounds, Weather Gates, Runes, AR",
                        icon = Icons.Default.AutoAwesome,
                        color = ElectricNeonBlue,
                        onClick = { viewModel.selectedTab.value = 5 }
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBar(
    label: String,
    current: Int,
    max: Int,
    fillColor: Color
) {
    val progress = if (max > 0) current.toFloat() / max else 0f
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = fillColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "$current / $max",
                color = TextLight,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(VoidSurfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(fillColor.copy(alpha = 0.7f), fillColor)
                        )
                    )
            )
        }
    }
}

@Composable
fun StatQuickChip(
    title: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(VoidSurface)
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "$value",
                color = TextLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun QuickModuleButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(VoidSurface)
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f))
                    .border(1.dp, color, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = color
            )
        }
    }
}
