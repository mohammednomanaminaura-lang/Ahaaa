package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.rotate
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
fun AestheticsAdvancedScreen(viewModel: MonarchViewModel) {
    var subTab by remember { mutableIntStateOf(0) } // 0: Soundboard, 1: Aura & Theme Customizer, 2: Weather Gate, 3: Voice Commands, 4: Rune Translator, 5: AR Photo Mode

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
                "1. Soundboard",
                "2. Aura Customizer",
                "3. Weather Gates",
                "4. Voice Commands",
                "5. Rune Translator",
                "6. AR Photo Mode"
            )
            items(tabs.indices.toList()) { index ->
                val isSelected = subTab == index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) ElectricNeonBlue.copy(alpha = 0.2f) else VoidSurface)
                        .border(
                            1.dp,
                            if (isSelected) ElectricNeonBlue else VoidBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { subTab = index }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tabs[index],
                        color = if (isSelected) ElectricNeonBlue else TextMuted,
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
                0 -> SoundboardSection()
                1 -> AuraCustomizerSection(viewModel)
                2 -> WeatherGateSection(viewModel)
                3 -> VoiceCommandsSection(viewModel)
                4 -> RuneTranslatorSection(viewModel)
                5 -> ArPhotoModeSection(viewModel)
            }
        }
    }
}

/**
 * FEATURE 18: Necromancer Soundboard
 */
@Composable
fun SoundboardSection() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "NECROMANCER PROCEDURAL SOUNDBOARD") {
                Text(
                    text = "Zero-latency audio synthesizer generating authentic Solo Leveling sound effects on the fly.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        val sfxList = listOf(
            Triple("ARISE (일어나라)", "Sub-bass surge & ethereal monarch chime", ElectricNeonBlue) to { SoundSystem.playAriseSound() },
            Triple("SWORD SLASH", "High-frequency metallic dagger slice", CrimsonPenaltyRed) to { SoundSystem.playSwordSlash() },
            Triple("SYSTEM BELL", "Crisp triple harmonic level-up chime", MonarchGold) to { SoundSystem.playSystemBell() },
            Triple("PENALTY ZONE ALARM", "Crimson pulsing hazard warning buzz", CrimsonPenaltyRed) to { SoundSystem.playPenaltyAlarm() },
            Triple("RULER'S AUTHORITY", "Telekinetic sub-harmonic shockwave", NecromancerPurple) to { SoundSystem.playRulersAuthority() },
            Triple("MONARCH'S DOMAIN", "Void territory expansion hum & resonance", ElectricNeonBlue) to { SoundSystem.playMonarchDomainHum() }
        )

        items(sfxList) { (meta, action) ->
            val (name, desc, color) = meta
            GlassmorphicCard(
                borderColor = color.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = name, color = color, fontWeight = FontWeight.Black, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                        Text(text = desc, color = TextMuted, fontSize = 11.sp)
                    }

                    IconButton(
                        onClick = action,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.2f))
                            .border(1.dp, color, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play SFX", tint = color)
                    }
                }
            }
        }
    }
}

/**
 * FEATURE 20: Theme & Glow Customizer
 */
@Composable
fun AuraCustomizerSection(viewModel: MonarchViewModel) {
    val activeAura by viewModel.currentAuraColor.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "MONARCH AURA & NEON GLOW CUSTOMIZER") {
                Text(
                    text = "Modify the active system energy wavelength across the entire Monarch Realm HUD.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        val auras = listOf(
            "Electric Neon Blue (Pure Monarch Mana)" to ElectricNeonBlue,
            "Necromancer Purple (Shadow Army Domain)" to NecromancerPurple,
            "Crimson Penalty Red (Blood Monarch / Destruction)" to CrimsonPenaltyRed,
            "Monarch Radiant Gold (Divine Monarch Ascension)" to MonarchGold
        )

        items(auras) { (name, color) ->
            val isSelected = activeAura == color
            GlassmorphicCard(
                borderColor = if (isSelected) color else VoidBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(1.5.dp, Color.White, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = name, color = TextLight, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    NeonButton(
                        text = if (isSelected) "ACTIVE" else "SELECT",
                        onClick = { viewModel.setAuraColor(color) },
                        color = color
                    )
                }
            }
        }
    }
}

/**
 * FEATURE 21: Dungeon Gate Weather Theme
 */
@Composable
fun WeatherGateSection(viewModel: MonarchViewModel) {
    val activeWeather by viewModel.currentWeatherGate.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "DUNGEON GATE WEATHER ENVIRONMENT") {
                Text(
                    text = "Simulate dynamic environmental conditions inside the Monarch Realm.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        val gates = listOf(
            "Blue Ice Gate (Frost Blizzard & Sub-Zero Calm)",
            "Red Calamity Gate (Rain/Storm Mode & Blood Lightning)",
            "Void S-Rank Gate (Dark Matter & Ashborn Mana Vortex)",
            "Demon Castle Hell Gate (Molten Lava Embers & Heat Haze)"
        )

        items(gates) { gate ->
            val isSel = activeWeather == gate
            GlassmorphicCard(
                borderColor = if (isSel) ElectricNeonBlue else VoidBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = gate,
                        color = if (isSel) ElectricNeonBlue else TextLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    NeonButton(
                        text = if (isSel) "CURRENT" else "ACTIVATE",
                        onClick = {
                            viewModel.currentWeatherGate.value = gate
                            SoundSystem.playMonarchDomainHum()
                        },
                        color = if (isSel) ElectricNeonBlue else NecromancerPurple
                    )
                }
            }
        }
    }
}

/**
 * FEATURE 22: Voice Command System
 */
@Composable
fun VoiceCommandsSection(viewModel: MonarchViewModel) {
    val lastCommand by viewModel.lastExecutedVoiceCommand.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "MONARCH VOICE COMMAND MATRIX") {
                Text(
                    text = "Issue vocal or quick-action commands to control the app interface hands-free.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        if (lastCommand != null) {
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
                        text = "Last Executed Command: [ $lastCommand ]",
                        color = ElectricNeonBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        val commands = listOf(
            "ARISE" to "Extracts shadows & opens Battle Zone",
            "OPEN SYSTEM" to "Returns to Monarch Throne Master Matrix",
            "DAILY QUEST" to "Opens Fitness & SSC Academic Checklist",
            "BATTLE" to "Launches Live S-Rank Dungeon Combat",
            "ART STUDIO" to "Opens Neon Shadow Canvas & Manhwa Studio"
        )

        items(commands) { (cmd, desc) ->
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "“$cmd”", color = ElectricNeonBlue, fontWeight = FontWeight.Black, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                        Text(text = desc, color = TextMuted, fontSize = 11.sp)
                    }

                    NeonButton(
                        text = "TRIGGER",
                        onClick = { viewModel.executeVoiceCommand(cmd) },
                        color = ElectricNeonBlue
                    )
                }
            }
        }
    }
}

/**
 * FEATURE 23: Xeno-Language Rune Translator
 */
@Composable
fun RuneTranslatorSection(viewModel: MonarchViewModel) {
    val runeInput by viewModel.runeInputText.collectAsStateWithLifecycle()
    val translatedRune by viewModel.translatedRuneText.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "XENO-LANGUAGE RUNE & GLYPH TRANSLATOR") {
                Text(
                    text = "Translate English or Latin text into ancient Monarch Runes and Runic Glyphs from the Shadow Realm.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "TEXT INPUT:",
                        color = ElectricNeonBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    OutlinedTextField(
                        value = runeInput,
                        onValueChange = { viewModel.translateToRunes(it) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextLight,
                            unfocusedTextColor = TextLight,
                            focusedBorderColor = ElectricNeonBlue,
                            unfocusedBorderColor = VoidBorder,
                            focusedContainerColor = VoidDark,
                            unfocusedContainerColor = VoidDark
                        )
                    )
                }
            }
        }

        item {
            GlassmorphicCard(
                borderColor = NecromancerPurple,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "TRANSLATED RUNIC GLYPHS:",
                        color = NecromancerPurple,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(VoidDark)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = translatedRune,
                            color = ElectricNeonBlue,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 4.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * FEATURE 24: AR Photo Mode
 */
@Composable
fun ArPhotoModeSection(viewModel: MonarchViewModel) {
    val selectedSoldier by viewModel.arSelectedSoldier.collectAsStateWithLifecycle()
    val scale by viewModel.arSoldierScale.collectAsStateWithLifecycle()
    val captured by viewModel.arCapturedSnapshot.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "AR 3D SHADOW SOLDIER OVERLAY") {
                Text(
                    text = "Project life-sized 3D holographic shadow soldier silhouettes and aura projections into the real world.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        // Camera Simulated Viewport
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VoidDark)
                    .border(2.dp, ElectricNeonBlue, RoundedCornerShape(12.dp))
            ) {
                ParticleBackground(particleColor = NecromancerPurple, particleCount = 40)

                // Projected Holographic Soldier Representation
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Accessibility,
                            contentDescription = "Soldier Hologram",
                            tint = ElectricNeonBlue,
                            modifier = Modifier.size((110 * scale).dp)
                        )
                        Text(
                            text = "[ 3D AR PROJECTION: $selectedSoldier ]",
                            color = ElectricNeonBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // AR Reticle HUD
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(text = "+ HUD GRID LOCK", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }

        // AR Controls
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeonButton(
                    text = "SCALE UP (+)",
                    onClick = { viewModel.arSoldierScale.value = (scale + 0.2f).coerceAtMost(2.0f) },
                    color = ElectricNeonBlue,
                    modifier = Modifier.weight(1f)
                )
                NeonButton(
                    text = "CAPTURE AR PHOTO",
                    onClick = {
                        viewModel.arCapturedSnapshot.value = true
                        SoundSystem.playSystemBell()
                    },
                    color = CrimsonPenaltyRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
