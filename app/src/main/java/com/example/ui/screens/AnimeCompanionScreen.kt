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
fun AnimeCompanionScreen(viewModel: MonarchViewModel) {
    var subTab by remember { mutableIntStateOf(0) } // 0: Multilingual AI Chat, 1: Anime Lore Knowledge Base, 2: AI Advisor & Reminders

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
                "1. Multilingual Anime AI Chat",
                "2. Anime Knowledge Base",
                "3. Shadow AI Advisor"
            )
            items(tabs.indices.toList()) { index ->
                val isSelected = subTab == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) NecromancerPurple.copy(alpha = 0.2f) else VoidSurface)
                        .border(
                            1.dp,
                            if (isSelected) NecromancerPurple else VoidBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { subTab = index }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tabs[index],
                        color = if (isSelected) NecromancerPurple else TextMuted,
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
                0 -> MultilingualChatSection(viewModel)
                1 -> AnimeKnowledgeSection(viewModel)
                2 -> ShadowAdvisorSection(viewModel)
            }
        }
    }
}

/**
 * FEATURE 15: Multilingual Anime AI Chat (Hindi & English with Sung Jin-Woo, Naruto, Goku)
 */
@Composable
fun MultilingualChatSection(viewModel: MonarchViewModel) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val character by viewModel.selectedAnimeCharacter.collectAsStateWithLifecycle()
    val isThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Character Selector
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chars = listOf("Sung Jin-Woo", "Naruto", "Goku", "Igris", "Beru")
            items(chars) { c ->
                val isSel = character == c
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) ElectricNeonBlue.copy(alpha = 0.2f) else VoidDark)
                        .border(1.dp, if (isSel) ElectricNeonBlue else VoidBorder, RoundedCornerShape(6.dp))
                        .clickable { viewModel.selectedAnimeCharacter.value = c }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = c,
                        color = if (isSel) ElectricNeonBlue else TextLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Log
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.isUser
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isUser) ElectricNeonBlue.copy(alpha = 0.15f) else VoidSurface)
                            .border(
                                1.dp,
                                if (isUser) ElectricNeonBlue.copy(alpha = 0.5f) else NecromancerPurple.copy(alpha = 0.4f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = msg.sender,
                                color = if (isUser) ElectricNeonBlue else NecromancerPurple,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = msg.text,
                                color = TextLight,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    Text(
                        text = "$character is channeling mana...",
                        color = NecromancerPurple,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Chat in Hindi or English (e.g. 'Kaise ho?', 'Level up tips')", color = TextMuted, fontSize = 11.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextLight,
                    unfocusedTextColor = TextLight,
                    focusedBorderColor = ElectricNeonBlue,
                    unfocusedBorderColor = VoidBorder,
                    focusedContainerColor = VoidDark,
                    unfocusedContainerColor = VoidDark
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        val txt = inputText
                        inputText = ""
                        viewModel.sendChatMessage(txt)
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ElectricNeonBlue)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = VoidBlack)
            }
        }
    }
}

/**
 * FEATURE 16: Ultimate Movie & Anime Knowledge Base
 */
@Composable
fun AnimeKnowledgeSection(viewModel: MonarchViewModel) {
    val result by viewModel.triviaResult.collectAsStateWithLifecycle()
    val isThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    var queryText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "MULTIVERSE ANIME & MOVIE KNOWLEDGE ARCHIVE") {
                Text(
                    text = "Query deep trivia, lore, power scaling calculations, and story arcs across Solo Leveling, Dragon Ball, Naruto, Jujutsu Kaisen, and MCU.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "ENTER ANIME / MOVIE TOPIC:",
                        color = ElectricNeonBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    OutlinedTextField(
                        value = queryText,
                        onValueChange = { queryText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. 'Who is the strongest Shadow Soldier?', 'Ultra Instinct lore'", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextLight,
                            unfocusedTextColor = TextLight,
                            focusedBorderColor = ElectricNeonBlue,
                            unfocusedBorderColor = VoidBorder,
                            focusedContainerColor = VoidDark,
                            unfocusedContainerColor = VoidDark
                        )
                    )

                    NeonButton(
                        text = if (isThinking) "SCANNING ARCHIVES..." else "SEARCH LORE ARCHIVE",
                        onClick = { viewModel.searchAnimeTrivia(queryText) },
                        enabled = !isThinking && queryText.isNotBlank(),
                        color = ElectricNeonBlue,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        if (result != null) {
            item {
                GlassmorphicCard(
                    borderColor = NecromancerPurple,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "ARCHIVE DECRYPTION RESULTS",
                            color = NecromancerPurple,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = result ?: "",
                            color = TextLight,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * FEATURE 17: AI Personal Advisor & Motivation
 */
@Composable
fun ShadowAdvisorSection(viewModel: MonarchViewModel) {
    val advisor by viewModel.advisorSoldier.collectAsStateWithLifecycle()
    val advice by viewModel.advisorAdvice.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "SHADOW ADVISOR COUNSEL") {
                Text(
                    text = "Personal voice and strategic reminders from your shadow marshals to maintain discipline in workouts and SSC examination preparation.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        item {
            GlassmorphicCard(
                borderColor = ElectricNeonBlue,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "COUNSEL FROM: $advisor",
                            color = ElectricNeonBlue,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        IconButton(onClick = { SoundSystem.playAriseSound() }) {
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play", tint = ElectricNeonBlue)
                        }
                    }

                    Text(
                        text = "“$advice”",
                        color = TextLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NeonButton(
                            text = "SWITCH TO BERU",
                            onClick = {
                                viewModel.advisorSoldier.value = "Beru"
                                viewModel.refreshAdvisorAdvice()
                            },
                            color = NecromancerPurple,
                            modifier = Modifier.weight(1f)
                        )
                        NeonButton(
                            text = "SWITCH TO IGRIS",
                            onClick = {
                                viewModel.advisorSoldier.value = "Igris"
                                viewModel.refreshAdvisorAdvice()
                            },
                            color = CrimsonPenaltyRed,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
