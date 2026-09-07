package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
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
fun BattleZoneScreen(viewModel: MonarchViewModel) {
    var subTab by remember { mutableIntStateOf(0) } // 0: Live Battle & Skills, 1: Rank Evaluation Gate, 2: Shadow Extraction "Arise", 3: Shadow Army Roster

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
                "1. Live Shadow Combat",
                "2. Rank Evaluation Gate",
                "3. Shadow Extraction (ARISE)",
                "4. Shadow Army Roster"
            )
            items(tabs.indices.toList()) { index ->
                val isSelected = subTab == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) CrimsonPenaltyRed.copy(alpha = 0.2f) else VoidSurface)
                        .border(
                            1.dp,
                            if (isSelected) CrimsonPenaltyRed else VoidBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { subTab = index }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tabs[index],
                        color = if (isSelected) CrimsonPenaltyRed else TextMuted,
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
                0 -> LiveBattleSection(viewModel)
                1 -> RankEvaluationGateSection(viewModel)
                2 -> ShadowExtractionSection(viewModel)
                3 -> ShadowArmyRosterSection(viewModel)
            }
        }
    }
}

/**
 * FEATURE 6 & 9: Live Shadow Battle & Monarch Skills Showcase
 */
@Composable
fun LiveBattleSection(viewModel: MonarchViewModel) {
    val battleState by viewModel.battleState.collectAsStateWithLifecycle()
    val activeAura by viewModel.currentAuraColor.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Boss Encounter Card
        item {
            GlassmorphicCard(
                borderColor = if (battleState.isVictory) ElectricNeonBlue else CrimsonPenaltyRed,
                backgroundColor = VoidDark.copy(alpha = 0.9f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = battleState.bossName,
                                color = TextLight,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = battleState.bossRank,
                                color = CrimsonPenaltyRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        if (battleState.isVictory) {
                            Text(
                                text = "BOSS DEFEATED",
                                color = ElectricNeonBlue,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Boss HP Bar
                    StatusBar(
                        label = "BOSS HP",
                        current = battleState.bossHp,
                        max = battleState.bossMaxHp,
                        fillColor = CrimsonPenaltyRed
                    )

                    // Player Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            StatusBar(
                                label = "MONARCH HP",
                                current = battleState.playerHp,
                                max = battleState.playerMaxHp,
                                fillColor = ElectricNeonBlue
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            StatusBar(
                                label = "MANA (MP)",
                                current = battleState.playerMp,
                                max = battleState.playerMaxMp,
                                fillColor = NecromancerPurple
                            )
                        }
                    }
                }
            }
        }

        // Active Skill FX Notification
        if (battleState.activeSkillAnimation != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NecromancerPurple.copy(alpha = 0.3f))
                        .border(1.5.dp, ElectricNeonBlue, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡ CASTING [ ${battleState.activeSkillAnimation} ] ⚡",
                        color = ElectricNeonBlue,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Primary Combat Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeonButton(
                    text = "TWIN DAGGER STRIKE",
                    onClick = { viewModel.executePlayerAttack() },
                    color = CrimsonPenaltyRed,
                    enabled = battleState.bossHp > 0 && battleState.playerHp > 0,
                    modifier = Modifier.weight(1f)
                )

                if (battleState.isVictory) {
                    NeonButton(
                        text = "NEXT DUNGEON GATE",
                        onClick = {
                            val nextBosses = listOf(
                                "Ant King (Beru S-Rank Gate)",
                                "Frost Monarch Silad",
                                "Cerberus: Gatekeeper of Hell",
                                "Baruka: Ice Elf Warlord"
                            )
                            val next = nextBosses.random()
                            viewModel.resetBattle(bossName = next, bossHp = 20000, bossRank = "Red Gate Calamity Boss")
                        },
                        color = ElectricNeonBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Feature 9: Monarch Skills Showcase Buttons
        item {
            Text(
                text = "MONARCH EXCLUSIVE SKILLS (1000 MP)",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MonarchSkillCard(
                        name = "RULER'S AUTHORITY",
                        desc = "Telekinetic gravity crush.",
                        color = ElectricNeonBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.castMonarchSkill("RULER'S AUTHORITY") }
                    )
                    MonarchSkillCard(
                        name = "DRAGON'S FEAR",
                        desc = "Stuns and terrifies foes.",
                        color = CrimsonPenaltyRed,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.castMonarchSkill("DRAGON'S FEAR") }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MonarchSkillCard(
                        name = "SHADOW EXCHANGE",
                        desc = "Instant teleport strike.",
                        color = NecromancerPurple,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.castMonarchSkill("SHADOW EXCHANGE") }
                    )
                    MonarchSkillCard(
                        name = "MONARCH'S DOMAIN",
                        desc = "+50% damage to all shadows.",
                        color = MonarchGold,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.castMonarchSkill("MONARCH'S DOMAIN") }
                    )
                }
            }
        }

        // Real-Time Combat Log
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "DUNGEON COMBAT LOG",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    battleState.battleLogs.forEach { log ->
                        Text(
                            text = "> $log",
                            color = if ("Devastating" in log || "slashed" in log) ElectricNeonBlue else if ("fell" in log || "struck" in log) CrimsonPenaltyRed else TextLight,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonarchSkillCard(
    name: String,
    desc: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(VoidDark)
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column {
            Text(text = name, color = color, fontSize = 11.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
            Text(text = desc, color = TextMuted, fontSize = 9.sp)
        }
    }
}

/**
 * FEATURE 7: Rank Evaluation Gate Test Mini-Game
 */
@Composable
fun RankEvaluationGateSection(viewModel: MonarchViewModel) {
    val score by viewModel.rankResonanceScore.collectAsStateWithLifecycle()
    val rank by viewModel.evaluatedRank.collectAsStateWithLifecycle()
    val isTesting by viewModel.isTestingRank.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "RANK EVALUATION GATE") {
                Text(
                    text = "Hunter Association Mana Resonance Apparatus: Tap rapidly to channel maximum shadow mana and determine your hunter rank.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "MANA RESONANCE: $score",
                        color = ElectricNeonBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )

                    // Big Resonance Orb
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(NecromancerPurple.copy(alpha = 0.25f))
                            .border(3.dp, ElectricNeonBlue, CircleShape)
                            .clickable { viewModel.tapRankResonance() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "TAP TO\nCHANNEL",
                            color = TextLight,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    if (rank != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(VoidSurfaceVariant)
                                .border(1.dp, MonarchGold, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "EVALUATION RESULT", color = MonarchGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(text = rank ?: "", color = TextLight, fontSize = 15.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * FEATURE 8: Shadow Extraction System ("ARISE" 일어나라)
 */
@Composable
fun ShadowExtractionSection(viewModel: MonarchViewModel) {
    val attemptsLeft by viewModel.extractionAttemptsLeft.collectAsStateWithLifecycle()
    val isExtracting by viewModel.isExtracting.collectAsStateWithLifecycle()
    val extractionSuccess by viewModel.extractionSuccess.collectAsStateWithLifecycle()
    val battleState by viewModel.battleState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "SHADOW EXTRACTION MATRIX") {
                Text(
                    text = "Command defeated dungeon boss shadows to awaken under Absolute Shadow Monarch Mohammad Noman. You have 3 attempts per boss.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        item {
            GlassmorphicCard(
                borderColor = NecromancerPurple,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "TARGET: ${battleState.bossName}",
                        color = TextLight,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = "EXTRACTION CHANCES LEFT: $attemptsLeft / 3",
                        color = if (attemptsLeft > 1) ElectricNeonBlue else CrimsonPenaltyRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    // Big "ARISE" Extraction Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NecromancerPurple.copy(alpha = 0.3f))
                            .border(2.dp, ElectricNeonBlue, RoundedCornerShape(12.dp))
                            .clickable(enabled = attemptsLeft > 0 && !isExtracting) {
                                viewModel.performShadowExtraction()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isExtracting) "EXTRACTING SHADOW..." else "ARISE (일어나라)",
                            color = ElectricNeonBlue,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 3.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    if (extractionSuccess == true) {
                        Text(
                            text = "✨ EXTRACTION SUCCESSFUL! Shadow soldier added to Monarch Army.",
                            color = ElectricNeonBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    } else if (extractionSuccess == false) {
                        Text(
                            text = "❌ EXTRACTION FAILED. Shadow resistance encountered.",
                            color = CrimsonPenaltyRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Feature: Shadow Army Roster
 */
@Composable
fun ShadowArmyRosterSection(viewModel: MonarchViewModel) {
    val soldiers by viewModel.shadowSoldiers.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "MONARCH'S SHADOW LEGION (${soldiers.size})",
                color = ElectricNeonBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        items(soldiers) { soldier ->
            GlassmorphicCard(
                borderColor = NecromancerPurple.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = soldier.name,
                            color = TextLight,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "RANK: ${soldier.rank} | LVL: ${soldier.level} | PWR: ${soldier.powerRating}",
                            color = ElectricNeonBlue,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Weapon: ${soldier.weapon}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = { SoundSystem.playAriseSound() }) {
                        Icon(imageVector = Icons.Default.FlashOn, contentDescription = "Aura", tint = NecromancerPurple)
                    }
                }
            }
        }
    }
}
