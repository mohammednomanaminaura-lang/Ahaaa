package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.audio.SoundSystem
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.MonarchViewModel

@Composable
fun MonarchApp(
    viewModel: MonarchViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val activeAura by viewModel.currentAuraColor.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = VoidBlack,
        bottomBar = {
            MonarchBottomNavigation(
                selectedTab = selectedTab,
                activeAura = activeAura,
                onTabSelected = { index ->
                    viewModel.selectedTab.value = index
                    SoundSystem.playSwordSlash()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(viewModel)
                1 -> ArtStudioScreen(viewModel)
                2 -> BattleZoneScreen(viewModel)
                3 -> SystemTrackerScreen(viewModel)
                4 -> AnimeCompanionScreen(viewModel)
                5 -> AestheticsAdvancedScreen(viewModel)
            }
        }
    }
}

@Composable
fun MonarchBottomNavigation(
    selectedTab: Int,
    activeAura: Color,
    onTabSelected: (Int) -> Unit
) {
    val navItems = listOf(
        Triple("SYSTEM", Icons.Default.Home, 0),
        Triple("STUDIO", Icons.Default.Brush, 1),
        Triple("GATE", Icons.Default.Security, 2),
        Triple("QUESTS", Icons.Default.FitnessCenter, 3),
        Triple("SHADOW AI", Icons.Default.Chat, 4),
        Triple("MONARCH", Icons.Default.AutoAwesome, 5)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0C101D).copy(alpha = 0.96f))
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.08f))
            .navigationBarsPadding()
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .testTag("monarch_bottom_navigation")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { (label, icon, index) ->
                val isSelected = selectedTab == index
                val itemColor = if (isSelected) ElectricNeonBlue else TextMuted
                val glowAlpha = if (isSelected) 0.15f else 0f

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ElectricNeonBlue.copy(alpha = 0.10f) else Color.Transparent)
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("nav_tab_$index")
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = itemColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = label,
                        color = itemColor,
                        fontSize = 8.5.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
