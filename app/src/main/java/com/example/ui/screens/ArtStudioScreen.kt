package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.SoundSystem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.DrawStroke
import com.example.viewmodel.MonarchViewModel

@Composable
fun ArtStudioScreen(viewModel: MonarchViewModel) {
    var subTab by remember { mutableIntStateOf(0) } // 0: Shadow Canvas, 1: AI Avatar Generator, 2: Voice Recorder, 3: Asset Store, 4: Manhwa Reader

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
                "1. Shadow Canvas",
                "2. AI Avatar",
                "3. Voice Recorder",
                "4. Asset Store",
                "5. Manhwa Reader"
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
                0 -> ShadowCanvasSection(viewModel)
                1 -> AiAvatarSection(viewModel)
                2 -> VoiceRecorderSection(viewModel)
                3 -> DungeonAssetStoreSection(viewModel)
                4 -> ManhwaReaderSection(viewModel)
            }
        }
    }
}

/**
 * FEATURE 1 & 4: Shadow Canvas with Neon Brushes & Shadow Transformation
 */
@Composable
fun ShadowCanvasSection(viewModel: MonarchViewModel) {
    val strokes by viewModel.canvasStrokes.collectAsStateWithLifecycle()
    val brushColor by viewModel.selectedBrushColor.collectAsStateWithLifecycle()
    val brushWidth by viewModel.selectedBrushWidth.collectAsStateWithLifecycle()
    val isTransforming by viewModel.isTransformingShadow.collectAsStateWithLifecycle()

    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

    val infiniteTransition = rememberInfiniteTransition(label = "trans")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutBounce),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "NEON SHADOW CANVAS",
                color = ElectricNeonBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NeonButton(
                    text = "CLEAR",
                    onClick = { viewModel.clearCanvas() },
                    color = CrimsonPenaltyRed
                )
                NeonButton(
                    text = if (isTransforming) "EXTRACTING..." else "TRANSFORM SHADOW",
                    onClick = { viewModel.triggerShadowTransformation() },
                    color = NecromancerPurple
                )
            }
        }

        // Color Palette & Brush Size
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val palette = listOf(ElectricNeonBlue, NecromancerPurple, CrimsonPenaltyRed, MonarchGold, Color.White)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                palette.forEach { color ->
                    val isSel = brushColor == color
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSel) 2.5.dp else 1.dp,
                                color = if (isSel) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { viewModel.selectedBrushColor.value = color }
                    )
                }
            }

            // Brush Width selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Width: ", color = TextMuted, fontSize = 11.sp)
                listOf(4f, 8f, 16f).forEach { width ->
                    Text(
                        text = "${width.toInt()}px",
                        color = if (brushWidth == width) ElectricNeonBlue else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.selectedBrushWidth.value = width }
                            .padding(horizontal = 4.dp)
                    )
                }
            }
        }

        // Drawing Area (Canvas)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isTransforming) VoidDark else VoidSurface)
                .border(
                    2.dp,
                    if (isTransforming) NecromancerPurple else ElectricNeonBlue.copy(alpha = 0.5f),
                    RoundedCornerShape(12.dp)
                )
                .pointerInput(brushColor, brushWidth) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPoints = listOf(offset)
                        },
                        onDrag = { change, _ ->
                            currentPoints = currentPoints + change.position
                        },
                        onDragEnd = {
                            if (currentPoints.isNotEmpty()) {
                                viewModel.addStroke(
                                    DrawStroke(
                                        points = currentPoints,
                                        color = brushColor,
                                        strokeWidth = brushWidth
                                    )
                                )
                                currentPoints = emptyList()
                            }
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw existing strokes with double-pass neon glow
                strokes.forEach { stroke ->
                    if (stroke.points.size > 1) {
                        val path = Path().apply {
                            moveTo(stroke.points.first().x, stroke.points.first().y)
                            for (i in 1 until stroke.points.size) {
                                lineTo(stroke.points[i].x, stroke.points[i].y)
                            }
                        }
                        // Outer Glow
                        drawPath(
                            path = path,
                            color = stroke.color.copy(alpha = 0.35f),
                            style = Stroke(
                                width = stroke.strokeWidth * 2.5f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                        // Inner Core
                        drawPath(
                            path = path,
                            color = stroke.color,
                            style = Stroke(
                                width = stroke.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                // Draw active stroke
                if (currentPoints.size > 1) {
                    val activePath = Path().apply {
                        moveTo(currentPoints.first().x, currentPoints.first().y)
                        for (i in 1 until currentPoints.size) {
                            lineTo(currentPoints[i].x, currentPoints[i].y)
                        }
                    }
                    drawPath(
                        path = activePath,
                        color = brushColor.copy(alpha = 0.35f),
                        style = Stroke(width = brushWidth * 2.5f, cap = StrokeCap.Round)
                    )
                    drawPath(
                        path = activePath,
                        color = brushColor,
                        style = Stroke(width = brushWidth, cap = StrokeCap.Round)
                    )
                }
            }

            // Transformation Particle FX Overlay
            if (isTransforming) {
                ParticleBackground(
                    particleColor = NecromancerPurple,
                    particleCount = 50
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SHADOW EXTRACTION: ARISE!",
                        color = ElectricNeonBlue,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}

/**
 * FEATURE 2: AI Avatar Generator (Converts sketch description to Solo Leveling Manhwa Art)
 */
@Composable
fun AiAvatarSection(viewModel: MonarchViewModel) {
    val prompt by viewModel.aiAvatarPrompt.collectAsStateWithLifecycle()
    val artResult by viewModel.generatedManhwaArtText.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGeneratingAvatar.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "AI MANHWA ART GENERATOR") {
                Text(
                    text = "Synthesizes character sketches and user descriptions into authentic Solo Leveling S-Rank Manhwa artwork concepts for Master Mohammad Noman Amin.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "CHARACTER PROMPT / SKETCH LORE:",
                        color = ElectricNeonBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { viewModel.aiAvatarPrompt.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextLight,
                            unfocusedTextColor = TextLight,
                            focusedBorderColor = ElectricNeonBlue,
                            unfocusedBorderColor = VoidBorder,
                            focusedContainerColor = VoidDark,
                            unfocusedContainerColor = VoidDark
                        ),
                        placeholder = { Text("Describe weapon, armor, rank, and eye glow color...", color = TextMuted) }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NeonButton(
                            text = if (isGenerating) "GENERATING..." else "SYNTHESIZE MANHWA ART",
                            onClick = { viewModel.generateAiAvatar(prompt) },
                            enabled = !isGenerating,
                            color = NecromancerPurple,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Generated Artwork Card
        if (artResult != null) {
            item {
                GlassmorphicCard(
                    borderColor = ElectricNeonBlue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ElectricNeonBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SYNTHESIZED MANHWA AVATAR PROFILE",
                                color = ElectricNeonBlue,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = artResult ?: "",
                            color = TextLight,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        NeonButton(
                            text = "SAVE TO SHADOW ARMY VAULT",
                            onClick = {
                                SoundSystem.playSystemBell()
                            },
                            color = ElectricNeonBlue
                        )
                    }
                }
            }
        }
    }
}

/**
 * FEATURE 3: Character Voice Recorder
 */
@Composable
fun VoiceRecorderSection(viewModel: MonarchViewModel) {
    val isRecording by viewModel.isRecordingVoice.collectAsStateWithLifecycle()
    val voiceNotes by viewModel.recordedVoiceNotes.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "SHADOW VOICE RECORDER") {
                Text(
                    text = "Record monarch battle cries, extraction commands, and character dialogues for your shadow soldiers.",
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(if (isRecording) CrimsonPenaltyRed.copy(alpha = 0.2f) else ElectricNeonBlue.copy(alpha = 0.2f))
                            .border(2.dp, if (isRecording) CrimsonPenaltyRed else ElectricNeonBlue, CircleShape)
                            .clickable { viewModel.toggleVoiceRecording() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = null,
                            tint = if (isRecording) CrimsonPenaltyRed else ElectricNeonBlue,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = if (isRecording) "RECORDING IN PROGRESS... TAP TO STOP" else "TAP TO RECORD MONARCH COMMAND",
                        color = if (isRecording) CrimsonPenaltyRed else TextLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        item {
            Text(
                text = "SAVED SHADOW AUDIO DIALOGUES (${voiceNotes.size})",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        items(voiceNotes) { note ->
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = note, color = TextLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Waveform Bitrate: 44.1kHz PCM", color = TextMuted, fontSize = 10.sp)
                    }
                    IconButton(onClick = { SoundSystem.playAriseSound() }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = ElectricNeonBlue
                        )
                    }
                }
            }
        }
    }
}

/**
 * FEATURE 5: Dungeon Asset Store
 */
@Composable
fun DungeonAssetStoreSection(viewModel: MonarchViewModel) {
    val items by viewModel.inventory.collectAsStateWithLifecycle()
    val equippedWeapon by viewModel.equippedWeapon.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "DUNGEON ASSET STORE & ARMORY") {
                Text(
                    text = "Equip authentic Solo Leveling mythic blades, demon monarch armor, and glowing void mantles to boost combat power.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        items(items) { item ->
            val isEquipped = item.name in equippedWeapon || item.isEquipped
            GlassmorphicCard(
                borderColor = if (isEquipped) ElectricNeonBlue else NecromancerPurple.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = item.name,
                                color = TextLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "RARITY: ${item.rarity} | BONUS: ${item.statBonusText}",
                                color = ElectricNeonBlue,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        NeonButton(
                            text = if (isEquipped) "EQUIPPED" else "EQUIP",
                            onClick = {
                                viewModel.equippedWeapon.value = item.name
                                SoundSystem.playSwordSlash()
                            },
                            color = if (isEquipped) ElectricNeonBlue else NecromancerPurple
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = item.description, color = TextMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

/**
 * FEATURE 19: Interactive Manhwa Comic Reader & Panel Creator
 */
@Composable
fun ManhwaReaderSection(viewModel: MonarchViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SystemNotificationBox(title = "MANHWA COMIC STRIP CREATOR") {
                Text(
                    text = "Create interactive vertical webtoon panels using created shadow soldiers, speech bubbles, and sound effects.",
                    color = TextLight,
                    fontSize = 12.sp
                )
            }
        }

        // Panel 1
        item {
            GlassmorphicCard(
                borderColor = ElectricNeonBlue,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "[PANEL 1: THE MONARCH'S AWAKENING]", color = ElectricNeonBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(VoidDark)
                            .border(1.dp, VoidBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "“You are no longer an E-Rank hunter, Mohammad Noman. You are the Monarch.”",
                                color = TextLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "💥 *FWHOOOOSH*", color = NecromancerPurple, fontSize = 14.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Panel 2
        item {
            GlassmorphicCard(
                borderColor = NecromancerPurple,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "[PANEL 2: THE SACRED COMMAND]", color = NecromancerPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(VoidDark)
                            .border(1.dp, VoidBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "“Arise (일어나라)... and protect the Monarch Realm.”",
                                color = ElectricNeonBlue,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "⚡ *KABOOOOM*", color = CrimsonPenaltyRed, fontSize = 14.sp, fontWeight = FontWeight.Black)
                                IconButton(onClick = { SoundSystem.playAriseSound() }) {
                                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play SFX", tint = ElectricNeonBlue)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
