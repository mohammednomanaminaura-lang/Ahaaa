package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monarch_stats")
data class StatEntity(
    @PrimaryKey val id: Int = 1,
    val ownerName: String = "Mohammad Noman Amin",
    val title: String = "Absolute Shadow Monarch Mohammad Noman",
    val hunterRank: String = "Shadow Monarch (Rank EX)",
    val level: Int = 120,
    val currentExp: Int = 84500,
    val maxExp: Int = 100000,
    val hp: Int = 98500,
    val maxHp: Int = 98500,
    val mp: Int = 142000,
    val maxMp: Int = 142000,
    val fatigue: Int = 0,
    val str: Int = 280,
    val agi: Int = 275,
    val `int`: Int = 320,
    val vit: Int = 260,
    val unallocatedPoints: Int = 15,
    val gold: Long = 45000000L,
    val shadowArmyCount: Int = 135000,
    val auraColorHex: String = "#00F0FF"
)

@Entity(tableName = "daily_quests")
data class QuestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "FITNESS", "SSC_ACADEMIC", "MONARCH_HABIT"
    val targetCount: Int,
    val currentCount: Int,
    val unit: String,
    val isCompleted: Boolean = false,
    val expReward: Int = 500,
    val statRewardType: String = "STR",
    val isPenalty: Boolean = false
)

@Entity(tableName = "shadow_soldiers")
data class ShadowSoldierEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val rank: String, // "Marshal", "General", "Commander", "Elite Knight", "Knight"
    val level: Int = 1,
    val powerRating: Int = 5000,
    val weapon: String = "Dark Mana Blade",
    val auraColor: String = "#9D00FF",
    val voiceClipPath: String? = null,
    val sketchDrawingData: String? = null,
    val manhwaArtPrompt: String = "",
    val isExtracted: Boolean = true,
    val isDeployed: Boolean = false
)

@Entity(tableName = "inventory_items")
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // "WEAPON", "ARMOR", "ARTIFACT", "VAULT_DOCUMENT"
    val rarity: String, // "National-Level", "Mythic", "Legendary", "S-Rank"
    val description: String,
    val statBonusText: String,
    val isEquipped: Boolean = false,
    val isVaultLocked: Boolean = false,
    val iconKey: String = "dagger"
)

@Entity(tableName = "academic_goals")
data class AcademicGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String, // "SSC Mathematics", "SSC Physics/Chemistry", "SSC English", "SSC General Science"
    val chapterName: String,
    val targetHours: Float = 2.0f,
    val completedHours: Float = 0.0f,
    val targetQuestions: Int = 50,
    val completedQuestions: Int = 0,
    val mockTestScore: Int = 0,
    val isCompleted: Boolean = false
)

@Entity(tableName = "workout_days")
data class WorkoutDayEntity(
    @PrimaryKey val dayNumber: Int, // 1 to 30
    val focusArea: String, // "Fat Loss & Core", "Full Body Circuit", "Six-Pack Shred", "Spine & Height Stretch"
    val exercises: String, // Comma separated
    val targetMinutes: Int = 45,
    val completed: Boolean = false,
    val caloriesBurned: Int = 0,
    val stretchRoutineCompleted: Boolean = false
)

@Entity(tableName = "vault_records")
data class VaultRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: String, // "VOICE_NOTE", "SKETCH", "SECRET_FILE"
    val content: String,
    val audioDurationSeconds: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
