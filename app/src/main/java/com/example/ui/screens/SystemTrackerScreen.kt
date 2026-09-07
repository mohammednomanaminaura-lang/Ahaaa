package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.SoundSystem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.MonarchViewModel

@Composable
fun SystemTrackerScreen(viewModel: MonarchViewModel) {
    var subTab by remember { mutableIntStateOf(0) } // 0: Daily Quests & Penalty, 1: Stat Points, 2: 30-Day Fat Loss, 3: 2-Year SSC Plan, 4: Encrypted Vault

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .padding(top = 16.dp)
    ) {
        // Sub-Navigation Tabs
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf(
                "1. Daily Quests",
                "2. Stat Allocator",
                "3. 30-Day Fat Loss",
                "4. 2-Year SSC Plan",
                "5. System Vault"
            )
            items(tabs.indices.toList()) { index ->
                val isSelected = subTab == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) MonarchGold.copy(alpha = 0.2f) else VoidSurface)
                        .border(
                            1.dp,
                            if (isSelected) MonarchGold else VoidBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { subTab = index }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tabs[index],
                        color = if (isSelected) MonarchGold else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (subTab) {
                0 -> DailyQuestSection(viewModel)
                1 -> StatAllocatorSection(viewModel)
                2 -> WorkoutMissionSection(viewModel)
                3 -> SscAcademicSection(viewModel)
                4 -> SystemVaultSection(viewModel)
            }
        }
    }
}

/**
 * FEATURE 10: Daily Quest System & System Penalty Zone
 */
@Composable
fun DailyQuestSection(viewModel: MonarchViewModel) {
    val quests by viewModel.quests.collectAsStateWithLifecycle()
    val isPenalty by viewModel.isPenaltyActive.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(
                title = if (isPenalty) "WARNING: PENALTY QUEST ACTIVE" else "DAILY QUEST: PREPARATION FOR THE STRONG",
                isPenalty = isPenalty
            ) {
                Text(
                    text = if (isPenalty) "You have failed to complete your daily fitness & study goals! Survival Quest in the Centipede Wastelands activated for 4 hours." else "Complete all 4 physical exercises and SSC study tasks before midnight to claim stat rewards and avoid the Penalty Zone.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MANDATORY TASKS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "STATUS: ${quests.count { it.isCompleted }} / ${quests.size} DONE",
                    color = ElectricNeonBlue,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        items(quests) { quest ->
            GlassmorphicCard(
                borderColor = if (quest.isCompleted) ElectricNeonBlue else VoidBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = quest.title,
                            color = if (quest.isCompleted) ElectricNeonBlue else TextLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Target: ${quest.targetCount} ${quest.unit} | Reward: +${quest.expReward} EXP, +${quest.statRewardType}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Checkbox(
                        checked = quest.isCompleted,
                        onCheckedChange = { viewModel.toggleQuest(quest) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = ElectricNeonBlue,
                            uncheckedColor = VoidBorder,
                            checkmarkColor = VoidBlack
                        )
                    )
                }
            }
        }

        item {
            NeonButton(
                text = if (isPenalty) "EXIT PENALTY ZONE" else "SIMULATE PENALTY ZONE",
                onClick = {
                    viewModel.isPenaltyActive.value = !viewModel.isPenaltyActive.value
                    if (viewModel.isPenaltyActive.value) SoundSystem.playPenaltyAlarm() else SoundSystem.playSystemBell()
                },
                color = CrimsonPenaltyRed,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * FEATURE 11: Stat Point Allocator
 */
@Composable
fun StatAllocatorSection(viewModel: MonarchViewModel) {
    val stats by viewModel.stats.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "STAT POINT ALLOCATION MATRIX") {
                Text(
                    text = "Allocate points earned through daily workouts, SSC study milestones, and dungeon victories.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AVAILABLE POINTS:",
                        color = TextLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "${stats?.unallocatedPoints ?: 0} PTS",
                        color = ElectricNeonBlue,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                StatRow(
                    name = "STR (Strength)",
                    desc = "Physical striking power & muscle density",
                    value = stats?.str ?: 280,
                    color = CrimsonPenaltyRed,
                    canAdd = (stats?.unallocatedPoints ?: 0) > 0,
                    onAdd = { viewModel.allocateStat("STR") }
                )
                StatRow(
                    name = "AGI (Agility)",
                    desc = "Speed, reflexes, and evasion dynamics",
                    value = stats?.agi ?: 275,
                    color = ElectricNeonBlue,
                    canAdd = (stats?.unallocatedPoints ?: 0) > 0,
                    onAdd = { viewModel.allocateStat("AGI") }
                )
                StatRow(
                    name = "INT (Intelligence)",
                    desc = "Mana capacity, SSC recall & tactical acumen",
                    value = stats?.`int` ?: 320,
                    color = NecromancerPurple,
                    canAdd = (stats?.unallocatedPoints ?: 0) > 0,
                    onAdd = { viewModel.allocateStat("INT") }
                )
                StatRow(
                    name = "VIT (Vitality)",
                    desc = "Health points, stamina & fatigue recovery",
                    value = stats?.vit ?: 260,
                    color = MonarchGold,
                    canAdd = (stats?.unallocatedPoints ?: 0) > 0,
                    onAdd = { viewModel.allocateStat("VIT") }
                )
            }
        }
    }
}

@Composable
fun StatRow(
    name: String,
    desc: String,
    value: Int,
    color: Color,
    canAdd: Boolean,
    onAdd: () -> Unit
) {
    GlassmorphicCard(
        borderColor = color.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = desc, color = TextMuted, fontSize = 10.sp)
                Text(text = "CURRENT: $value", color = TextLight, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (canAdd) color.copy(alpha = 0.2f) else VoidSurface)
                    .border(1.dp, if (canAdd) color else VoidBorder, CircleShape)
                    .clickable(enabled = canAdd, onClick = onAdd),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = if (canAdd) color else TextMuted,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

/**
 * FEATURE 12: 30-Day Fat Loss & Six-Pack Mission
 */
@Composable
fun WorkoutMissionSection(viewModel: MonarchViewModel) {
    val days by viewModel.workoutDays.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "30-DAY FAT LOSS & SIX-PACK SHRED") {
                Text(
                    text = "Structured high-intensity bodyweight protocol targeting core definition, fat loss, posture alignment, and height stretching.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        // Progress Overview
        item {
            val completedCount = days.count { it.completed }
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "30-DAY MONARCH ASCENSION PROGRESS",
                        color = TextLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    StatusBar(
                        label = "MISSION COMPLETION",
                        current = completedCount,
                        max = 30,
                        fillColor = ElectricNeonBlue
                    )
                }
            }
        }

        items(days) { day ->
            GlassmorphicCard(
                borderColor = if (day.completed) ElectricNeonBlue else VoidBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DAY ${day.dayNumber}: ${day.focusArea}",
                            color = if (day.completed) ElectricNeonBlue else TextLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = day.exercises,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Burn: ~${day.caloriesBurned} kcal | Stretch: ${if (day.stretchRoutineCompleted) "Completed" else "Pending"}",
                            color = MonarchGold,
                            fontSize = 10.sp
                        )
                    }

                    Checkbox(
                        checked = day.completed,
                        onCheckedChange = { viewModel.toggleWorkoutDay(day) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = ElectricNeonBlue,
                            uncheckedColor = VoidBorder,
                            checkmarkColor = VoidBlack
                        )
                    )
                }
            }
        }
    }
}

/**
 * FEATURE 13: 2-Year SSC Academic Plan
 */
@Composable
fun SscAcademicSection(viewModel: MonarchViewModel) {
    val goals by viewModel.academicGoals.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "2-YEAR SSC ACADEMIC DUNGEON PLAN") {
                Text(
                    text = "Subject-wise mastery tracking for SSC Examination preparation for Master Mohammad Noman Amin.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        items(goals) { goal ->
            GlassmorphicCard(
                borderColor = if (goal.isCompleted) ElectricNeonBlue else NecromancerPurple.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = goal.subject, color = TextLight, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            Text(text = goal.chapterName, color = TextMuted, fontSize = 11.sp)
                        }

                        Text(
                            text = "Score: ${goal.mockTestScore}%",
                            color = MonarchGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    StatusBar(
                        label = "STUDY HOURS: ${goal.completedHours.toInt()}h / ${goal.targetHours.toInt()}h",
                        current = goal.completedHours.toInt(),
                        max = goal.targetHours.toInt(),
                        fillColor = ElectricNeonBlue
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        NeonButton(
                            text = "+1 HOUR / +20 QS",
                            onClick = { viewModel.incrementAcademicProgress(goal) },
                            color = NecromancerPurple
                        )
                    }
                }
            }
        }
    }
}

/**
 * FEATURE 14: System Inventory & Vault (Biometric / PIN Locked)
 */
@Composable
fun SystemVaultSection(viewModel: MonarchViewModel) {
    val isUnlocked by viewModel.isVaultUnlocked.collectAsStateWithLifecycle()
    val records by viewModel.vaultRecords.collectAsStateWithLifecycle()
    var pinText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "SYSTEM INVENTORY & BIOMETRIC VAULT") {
                Text(
                    text = "Encrypted storage for tactical plans, voice recordings, sketches, and private monarch documents.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        if (!isUnlocked) {
            item {
                GlassmorphicCard(
                    borderColor = CrimsonPenaltyRed,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
                            tint = CrimsonPenaltyRed,
                            modifier = Modifier.size(48.dp)
                        )

                        Text(
                            text = "ENTER MASTER SECURITY PIN (e.g. 7777)",
                            color = TextLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        OutlinedTextField(
                            value = pinText,
                            onValueChange = { pinText = it },
                            modifier = Modifier.width(180.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextLight,
                                unfocusedTextColor = TextLight,
                                focusedBorderColor = ElectricNeonBlue,
                                unfocusedBorderColor = VoidBorder,
                                focusedContainerColor = VoidDark,
                                unfocusedContainerColor = VoidDark
                            )
                        )

                        if (errorMessage != null) {
                            Text(text = errorMessage ?: "", color = CrimsonPenaltyRed, fontSize = 11.sp)
                        }

                        NeonButton(
                            text = "AUTHENTICATE VAULT",
                            onClick = {
                                val ok = viewModel.unlockVault(pinText)
                                if (!ok) {
                                    errorMessage = "Invalid Master PIN. Access Denied."
                                }
                            },
                            color = ElectricNeonBlue
                        )
                    }
                }
            }
        } else {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(VoidSurfaceVariant)
                        .border(1.dp, ElectricNeonBlue, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "🔓 VAULT UNLOCKED: Welcome, Absolute Shadow Monarch Mohammad Noman Amin.",
                        color = ElectricNeonBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            items(listOf(
                "Document: SSC Examination Master Strategy 2026",
                "Voice Archive: 'Army Deployment Command 01'",
                "Tactical Note: 30-Day Six-Pack Nutrition Protocol",
                "Drawing Blueprint: Kamish Dragon Armor Synthesis"
            )) { doc ->
                GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = doc, color = TextLight, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        IconButton(onClick = { SoundSystem.playSystemBell() }) {
                            Icon(imageVector = Icons.Default.Visibility, contentDescription = "View", tint = ElectricNeonBlue)
                        }
                    }
                }
            }
        }
    }
}
