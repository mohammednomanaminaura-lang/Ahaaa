package com.example.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundSystem
import com.example.data.local.AppDatabase
import com.example.data.local.entities.*
import com.example.data.local.repository.MonarchRepository
import com.example.data.remote.GeminiService
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

// Drawing stroke data
data class DrawStroke(
    val points: List<androidx.compose.ui.geometry.Offset>,
    val color: Color,
    val strokeWidth: Float,
    val hasGlow: Boolean = true
)

// Battle state
data class BattleState(
    val playerHp: Int = 10000,
    val playerMaxHp: Int = 10000,
    val playerMp: Int = 5000,
    val playerMaxMp: Int = 5000,
    val bossName: String = "Blood-Red Commander Igris",
    val bossRank: String = "S-Rank Gate Boss",
    val bossHp: Int = 15000,
    val bossMaxHp: Int = 15000,
    val isVictory: Boolean = false,
    val isDefeat: Boolean = false,
    val canExtract: Boolean = false,
    val battleLogs: List<String> = listOf("System: Gate dungeon open. Defeat the boss to extract shadows!"),
    val activeSkillAnimation: String? = null
)

// Chat message
data class ChatMessage(
    val sender: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class MonarchViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = MonarchRepository(database.monarchDao())

    // Database Flows
    val stats: StateFlow<StatEntity?> = repository.stats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val quests: StateFlow<List<QuestEntity>> = repository.quests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val shadowSoldiers: StateFlow<List<ShadowSoldierEntity>> = repository.shadowSoldiers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inventory: StateFlow<List<InventoryEntity>> = repository.inventory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val academicGoals: StateFlow<List<AcademicGoalEntity>> = repository.academicGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workoutDays: StateFlow<List<WorkoutDayEntity>> = repository.workoutDays
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vaultRecords: StateFlow<List<VaultRecordEntity>> = repository.vaultRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // App Navigation & Selected Category
    val selectedTab = MutableStateFlow(0) // 0: Monarch Throne/Home, 1: Art Studio, 2: Battle Zone, 3: System Tracker, 4: Anime AI, 5: Aesthetics & Sound

    // Feature 1: Shadow Canvas State
    val canvasStrokes = MutableStateFlow<List<DrawStroke>>(emptyList())
    val selectedBrushColor = MutableStateFlow(ElectricNeonBlue)
    val selectedBrushWidth = MutableStateFlow(8f)

    // Feature 2: AI Avatar Generator State
    val aiAvatarPrompt = MutableStateFlow("S-Rank Shadow Monarch Mohammad Noman with glowing neon blue eyes and void mantle")
    val generatedManhwaArtText = MutableStateFlow<String?>(null)
    val isGeneratingAvatar = MutableStateFlow(false)

    // Feature 3: Character Voice Recorder State
    val isRecordingVoice = MutableStateFlow(false)
    val recordedVoiceNotes = MutableStateFlow<List<String>>(listOf("Default: 'Arise, my shadow soldiers!'", "Combat Cry: 'Monarch's Domain!'"))
    val isPlayingVoice = MutableStateFlow(false)

    // Feature 4: Shadow Transformation State
    val isTransformingShadow = MutableStateFlow(false)
    val shadowAuraType = MutableStateFlow("Electric Neon Blue")

    // Feature 5: Dungeon Asset Store
    val equippedWeapon = MutableStateFlow("Demon King's Shortsword (+150 ATK)")
    val equippedArmor = MutableStateFlow("Monarch's Cloak of Darkness (+200 VIT)")

    // Feature 6 & 9: Live Shadow Battle State
    val battleState = MutableStateFlow(BattleState())

    // Feature 7: Rank Evaluation Gate
    val rankResonanceScore = MutableStateFlow(0)
    val evaluatedRank = MutableStateFlow<String?>(null)
    val isTestingRank = MutableStateFlow(false)

    // Feature 8: Shadow Extraction System
    val extractionAttemptsLeft = MutableStateFlow(3)
    val extractionSuccess = MutableStateFlow<Boolean?>(null)
    val isExtracting = MutableStateFlow(false)

    // Feature 10: Penalty Zone System
    val isPenaltyActive = MutableStateFlow(false)
    val penaltyTimeRemainingSeconds = MutableStateFlow(14400) // 4 hours penalty

    // Feature 14: Vault Biometric / PIN Lock
    val isVaultUnlocked = MutableStateFlow(false)
    val vaultPinInput = MutableStateFlow("")

    // Feature 15 & 16: Multilingual Anime AI Chat
    val selectedAnimeCharacter = MutableStateFlow("Sung Jin-Woo")
    val selectedLanguage = MutableStateFlow("HINDI / ENGLISH")
    val chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("System", "Welcome, Absolute Shadow Monarch Mohammad Noman Amin. Sung Jin-Woo, Naruto, and Goku stand by your side. Ask anything in Hindi or English.", false)
        )
    )
    val isAiThinking = MutableStateFlow(false)

    // Feature 16: Anime Knowledge Base
    val triviaQuery = MutableStateFlow("")
    val triviaResult = MutableStateFlow<String?>(null)

    // Feature 17: AI Advisor
    val advisorSoldier = MutableStateFlow("Igris")
    val advisorAdvice = MutableStateFlow("My Liege Mohammad Noman, focus today on completing your SSC Mathematics goals and your 100 push-ups. Your shadows shall protect your realm.")

    // Feature 20: Theme & Glow Customizer
    val currentAuraColor = MutableStateFlow(ElectricNeonBlue)

    // Feature 21: Dynamic Weather Gate Theme
    val currentWeatherGate = MutableStateFlow("Red Calamity Gate (Rain/Storm Mode)")

    // Feature 22: Voice Command System
    val lastExecutedVoiceCommand = MutableStateFlow<String?>(null)
    val isListeningVoiceCommand = MutableStateFlow(false)

    // Feature 23: Xeno-Language Rune Translator
    val runeInputText = MutableStateFlow("ARISE MONARCH NOMAN")
    val translatedRuneText = MutableStateFlow("𐍈 𐍂 𐌉 𐍃 𐌴   𐌌 𐍈 𐌽 𐌀 𐍂 𐌂 𐌷   𐌽 𐍈 𐌌 𐌀 𐌽")

    // Feature 24: AR Photo Mode
    val arSelectedSoldier = MutableStateFlow("Igris (Shadow Marshal)")
    val arSoldierScale = MutableStateFlow(1.0f)
    val arCapturedSnapshot = MutableStateFlow(false)

    // Feature 25: Parallax 3D Throne Wallpaper
    val parallaxOffsetX = MutableStateFlow(0f)
    val parallaxOffsetY = MutableStateFlow(0f)

    // --- Actions ---

    // 1. Shadow Canvas Actions
    fun addStroke(stroke: DrawStroke) {
        canvasStrokes.value = canvasStrokes.value + stroke
    }

    fun clearCanvas() {
        canvasStrokes.value = emptyList()
    }

    // 2. AI Avatar Generation
    fun generateAiAvatar(prompt: String) {
        viewModelScope.launch {
            isGeneratingAvatar.value = true
            SoundSystem.playMonarchDomainHum()
            val result = GeminiService.askGemini(
                prompt = "Transform this sketch description into a Solo Leveling S-Rank Manhwa character for Absolute Shadow Monarch Mohammad Noman: $prompt",
                systemInstruction = "You are the Solo Leveling Manhwa Art Studio AI generator for Master Mohammad Noman Amin."
            )
            generatedManhwaArtText.value = result
            isGeneratingAvatar.value = false
        }
    }

    // 3. Voice Recording
    fun toggleVoiceRecording() {
        if (!isRecordingVoice.value) {
            isRecordingVoice.value = true
        } else {
            isRecordingVoice.value = false
            SoundSystem.playSystemBell()
            recordedVoiceNotes.value = recordedVoiceNotes.value + "Recorded Command: 'Shadow Soldiers, Assemble!' [0:04]"
        }
    }

    // 4. Shadow Transformation
    fun triggerShadowTransformation() {
        viewModelScope.launch {
            isTransformingShadow.value = true
            SoundSystem.playAriseSound()
            delay(1800)
            isTransformingShadow.value = false
        }
    }

    // 6 & 9. Battle Actions
    fun executePlayerAttack() {
        val current = battleState.value
        if (current.bossHp <= 0 || current.playerHp <= 0) return

        SoundSystem.playSwordSlash()
        val dmg = Random.nextInt(1200, 2400)
        val newBossHp = (current.bossHp - dmg).coerceAtLeast(0)
        val isVic = newBossHp == 0

        val bossCounterDmg = if (!isVic) Random.nextInt(400, 900) else 0
        val newPlayerHp = (current.playerHp - bossCounterDmg).coerceAtLeast(0)

        val log = "Mohammad Noman slashed with twin daggers for $dmg DMG!"
        val counterLog = if (!isVic) "${current.bossName} struck back for $bossCounterDmg DMG." else "${current.bossName} has fallen! Prepare Shadow Extraction!"

        battleState.value = current.copy(
            bossHp = newBossHp,
            playerHp = newPlayerHp,
            isVictory = isVic,
            canExtract = isVic,
            battleLogs = listOf(log, counterLog) + current.battleLogs.take(5)
        )

        if (isVic) {
            SoundSystem.playSystemBell()
        }
    }

    fun castMonarchSkill(skillName: String) {
        val current = battleState.value
        if (current.playerMp < 1000 || current.bossHp <= 0) return

        when (skillName) {
            "RULER'S AUTHORITY" -> SoundSystem.playRulersAuthority()
            "DRAGON'S FEAR" -> SoundSystem.playMonarchDomainHum()
            "SHADOW EXCHANGE" -> SoundSystem.playSwordSlash()
            "MONARCH'S DOMAIN" -> SoundSystem.playAriseSound()
        }

        val dmg = Random.nextInt(3500, 5500)
        val newBossHp = (current.bossHp - dmg).coerceAtLeast(0)
        val newPlayerMp = (current.playerMp - 1000).coerceAtLeast(0)
        val isVic = newBossHp == 0

        battleState.value = current.copy(
            bossHp = newBossHp,
            playerMp = newPlayerMp,
            isVictory = isVic,
            canExtract = isVic,
            activeSkillAnimation = skillName,
            battleLogs = listOf("Absolute Monarch Skill [$skillName] unleashed! Devastating $dmg DMG!") + current.battleLogs.take(5)
        )

        viewModelScope.launch {
            delay(1200)
            battleState.value = battleState.value.copy(activeSkillAnimation = null)
        }
    }

    fun resetBattle(bossName: String = "Blood-Red Commander Igris", bossHp: Int = 15000, bossRank: String = "S-Rank Gate Boss") {
        battleState.value = BattleState(
            bossName = bossName,
            bossHp = bossHp,
            bossMaxHp = bossHp,
            bossRank = bossRank
        )
        extractionAttemptsLeft.value = 3
        extractionSuccess.value = null
    }

    // 7. Rank Evaluation Gate Mini-Game
    fun tapRankResonance() {
        if (!isTestingRank.value) {
            isTestingRank.value = true
            rankResonanceScore.value = 0
            evaluatedRank.value = null
        }

        rankResonanceScore.value += 12
        SoundSystem.playSwordSlash()

        if (rankResonanceScore.value >= 100) {
            val rank = when {
                rankResonanceScore.value >= 180 -> "Shadow Monarch (Rank EX - Supreme Ruler)"
                rankResonanceScore.value >= 140 -> "National-Level Hunter (Rank SSS)"
                rankResonanceScore.value >= 120 -> "S-Rank Elite Hunter"
                rankResonanceScore.value >= 100 -> "A-Rank Guild Master"
                else -> "B-Rank Hunter"
            }
            evaluatedRank.value = rank
            isTestingRank.value = false
            SoundSystem.playSystemBell()
        }
    }

    // 8. Shadow Extraction System ("ARISE")
    fun performShadowExtraction() {
        if (extractionAttemptsLeft.value <= 0) return

        viewModelScope.launch {
            isExtracting.value = true
            SoundSystem.playAriseSound()
            delay(1500)
            isExtracting.value = false

            val successChance = 0.75f
            val isSuccess = Random.nextFloat() <= successChance

            if (isSuccess) {
                extractionSuccess.value = true
                SoundSystem.playSystemBell()
                // Insert new extracted soldier
                val currentBoss = battleState.value.bossName
                repository.insertShadowSoldier(
                    ShadowSoldierEntity(
                        name = "Shadow $currentBoss",
                        rank = "Commander",
                        level = 100,
                        powerRating = 85000,
                        weapon = "Extracted Void Blade",
                        auraColor = currentAuraColor.value.toHex()
                    )
                )
            } else {
                extractionAttemptsLeft.value -= 1
                extractionSuccess.value = false
                if (extractionAttemptsLeft.value <= 0) {
                    SoundSystem.playPenaltyAlarm()
                }
            }
        }
    }

    // 10. Quest Completion & Penalty Handling
    fun toggleQuest(quest: QuestEntity) {
        viewModelScope.launch {
            val updated = quest.copy(
                isCompleted = !quest.isCompleted,
                currentCount = if (!quest.isCompleted) quest.targetCount else 0
            )
            repository.updateQuest(updated)

            if (updated.isCompleted) {
                SoundSystem.playSystemBell()
                // Level up / XP reward
                stats.value?.let { currentStats ->
                    val newExp = currentStats.currentExp + quest.expReward
                    val didLevelUp = newExp >= currentStats.maxExp
                    val newLevel = if (didLevelUp) currentStats.level + 1 else currentStats.level
                    val finalExp = if (didLevelUp) newExp - currentStats.maxExp else newExp
                    val unallocated = if (didLevelUp) currentStats.unallocatedPoints + 5 else currentStats.unallocatedPoints

                    repository.updateStats(
                        currentStats.copy(
                            currentExp = finalExp,
                            level = newLevel,
                            unallocatedPoints = unallocated
                        )
                    )
                }
            }
        }
    }

    // 11. Stat Point Allocator
    fun allocateStat(statType: String) {
        val current = stats.value ?: return
        if (current.unallocatedPoints <= 0) return

        viewModelScope.launch {
            val updated = when (statType) {
                "STR" -> current.copy(str = current.str + 1, unallocatedPoints = current.unallocatedPoints - 1)
                "AGI" -> current.copy(agi = current.agi + 1, unallocatedPoints = current.unallocatedPoints - 1)
                "INT" -> current.copy(`int` = current.`int` + 1, unallocatedPoints = current.unallocatedPoints - 1)
                "VIT" -> current.copy(vit = current.vit + 1, unallocatedPoints = current.unallocatedPoints - 1)
                else -> current
            }
            repository.updateStats(updated)
            SoundSystem.playSystemBell()
        }
    }

    // 12. 30-Day Workout Toggle
    fun toggleWorkoutDay(day: WorkoutDayEntity) {
        viewModelScope.launch {
            val updated = day.copy(
                completed = !day.completed,
                caloriesBurned = if (!day.completed) 450 else 0,
                stretchRoutineCompleted = !day.completed
            )
            repository.updateWorkoutDay(updated)
            SoundSystem.playSystemBell()
        }
    }

    // 13. SSC Academic Goal Update
    fun incrementAcademicProgress(goal: AcademicGoalEntity) {
        viewModelScope.launch {
            val newHours = (goal.completedHours + 1.0f).coerceAtMost(goal.targetHours)
            val newQuestions = (goal.completedQuestions + 20).coerceAtMost(goal.targetQuestions)
            val isComp = newHours >= goal.targetHours && newQuestions >= goal.targetQuestions

            repository.updateAcademicGoal(
                goal.copy(
                    completedHours = newHours,
                    completedQuestions = newQuestions,
                    isCompleted = isComp
                )
            )
            SoundSystem.playSystemBell()
        }
    }

    // 14. Vault Unlock
    fun unlockVault(pin: String): Boolean {
        return if (pin == "7777" || pin == "0000" || pin == "1234") {
            isVaultUnlocked.value = true
            SoundSystem.playSystemBell()
            true
        } else {
            SoundSystem.playPenaltyAlarm()
            false
        }
    }

    // 15. Multilingual AI Anime Chat
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage("Mohammad Noman", text, true)
        chatMessages.value = chatMessages.value + userMsg

        viewModelScope.launch {
            isAiThinking.value = true
            val charName = selectedAnimeCharacter.value
            val response = GeminiService.askGemini(
                prompt = text,
                systemInstruction = "You are $charName from anime. You are speaking to your master, Absolute Shadow Monarch Mohammad Noman Amin. Respond enthusiastically in ${selectedLanguage.value} (Hindi/English mix or pure Hindi/English)."
            )
            chatMessages.value = chatMessages.value + ChatMessage(charName, response, false)
            isAiThinking.value = false
            SoundSystem.playSystemBell()
        }
    }

    // 16. Anime Knowledge Search
    fun searchAnimeTrivia(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            isAiThinking.value = true
            val result = GeminiService.askGemini(
                prompt = "Deep Anime Trivia & Power Analysis for: $query. Include power scaling, history, and Solo Leveling universe connection.",
                systemInstruction = "You are the Ultimate Anime & Movie Multiverse Lore Archivist serving Monarch Mohammad Noman."
            )
            triviaResult.value = result
            isAiThinking.value = false
        }
    }

    // 17. AI Advisor Refresh
    fun refreshAdvisorAdvice() {
        viewModelScope.launch {
            val soldier = advisorSoldier.value
            val advice = GeminiService.askGemini(
                prompt = "Give short, intense motivational advice for workout, SSC studies, and monarch leadership.",
                systemInstruction = "You are shadow soldier $soldier serving Absolute Shadow Monarch Mohammad Noman Amin."
            )
            advisorAdvice.value = advice
            SoundSystem.playMonarchDomainHum()
        }
    }

    // 20. Aura Color Customizer
    fun setAuraColor(color: Color) {
        currentAuraColor.value = color
        SoundSystem.playMonarchDomainHum()
    }

    // 22. Voice Command Execution
    fun executeVoiceCommand(command: String) {
        lastExecutedVoiceCommand.value = command
        when (command.lowercase().trim()) {
            "arise", "일어나라" -> {
                selectedTab.value = 2 // Battle
                performShadowExtraction()
            }
            "open system", "status" -> {
                selectedTab.value = 0 // Home
                SoundSystem.playSystemBell()
            }
            "daily quest", "quest" -> {
                selectedTab.value = 3 // Tracker
                SoundSystem.playSystemBell()
            }
            "battle", "dungeon" -> {
                selectedTab.value = 2 // Battle
                SoundSystem.playSwordSlash()
            }
            "inventory", "vault" -> {
                selectedTab.value = 3 // Tracker
                SoundSystem.playSystemBell()
            }
            "art studio", "canvas" -> {
                selectedTab.value = 1 // Studio
                SoundSystem.playMonarchDomainHum()
            }
        }
    }

    // 23. Translate Rune Text
    fun translateToRunes(input: String) {
        runeInputText.value = input
        val runeMap = mapOf(
            'A' to "𐌀", 'B' to "𐌁", 'C' to "𐌂", 'D' to "𐌃", 'E' to "𐌴", 'F' to "𐌅",
            'G' to "𐌆", 'H' to "𐌇", 'I' to "𐌉", 'J' to "𐌉", 'K' to "𐌊", 'L' to "𐌋",
            'M' to "𐌌", 'N' to "𐌽", 'O' to "𐍈", 'P' to "𐌐", 'Q' to "𐍁", 'R' to "𐍂",
            'S' to "𐍃", 'T' to "𐍄", 'U' to "𐍅", 'V' to "𐍅", 'W' to "𐍅", 'X' to "𐍇",
            'Y' to "𐍈", 'Z' to "𐌆"
        )
        val translated = input.uppercase().map { char ->
            runeMap[char] ?: char.toString()
        }.joinToString(" ")
        translatedRuneText.value = translated
    }

    private fun Color.toHex(): String {
        return String.format("#%02X%02X%02X", (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())
    }
}
