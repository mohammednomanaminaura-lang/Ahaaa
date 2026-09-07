package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.MonarchDao
import com.example.data.local.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        StatEntity::class,
        QuestEntity::class,
        ShadowSoldierEntity::class,
        InventoryEntity::class,
        AcademicGoalEntity::class,
        WorkoutDayEntity::class,
        VaultRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun monarchDao(): MonarchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "monarch_realm_db"
                ).addCallback(DatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.monarchDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: MonarchDao) {
                // Initial stats for Absolute Shadow Monarch Mohammad Noman Amin
                dao.updateMonarchStats(
                    StatEntity(
                        id = 1,
                        ownerName = "Mohammad Noman Amin",
                        title = "Absolute Shadow Monarch Mohammad Noman",
                        hunterRank = "Shadow Monarch (Rank EX)",
                        level = 120,
                        currentExp = 84500,
                        maxExp = 100000,
                        hp = 98500,
                        maxHp = 98500,
                        mp = 142000,
                        maxMp = 142000,
                        fatigue = 0,
                        str = 280,
                        agi = 275,
                        `int` = 320,
                        vit = 260,
                        unallocatedPoints = 15,
                        gold = 45000000L,
                        shadowArmyCount = 135000,
                        auraColorHex = "#00F0FF"
                    )
                )

                // Mandatory Daily Quests
                val quests = listOf(
                    QuestEntity(title = "Push-ups [Preparation to become strong]", category = "FITNESS", targetCount = 100, currentCount = 0, unit = "reps", expReward = 500, statRewardType = "STR"),
                    QuestEntity(title = "Sit-ups [Core Conditioning]", category = "FITNESS", targetCount = 100, currentCount = 0, unit = "reps", expReward = 500, statRewardType = "VIT"),
                    QuestEntity(title = "Squats [Lower Body Fortitude]", category = "FITNESS", targetCount = 100, currentCount = 0, unit = "reps", expReward = 500, statRewardType = "AGI"),
                    QuestEntity(title = "Running [Stamina & Speed]", category = "FITNESS", targetCount = 10, currentCount = 0, unit = "km", expReward = 1000, statRewardType = "AGI"),
                    QuestEntity(title = "SSC Mathematics: Calculus & Trigonometry", category = "SSC_ACADEMIC", targetCount = 2, currentCount = 0, unit = "hours", expReward = 800, statRewardType = "INT"),
                    QuestEntity(title = "SSC Science: Physics Mechanics & Chemical Bonds", category = "SSC_ACADEMIC", targetCount = 2, currentCount = 0, unit = "hours", expReward = 800, statRewardType = "INT"),
                    QuestEntity(title = "SSC English: Grammar & Vocabulary Drills", category = "SSC_ACADEMIC", targetCount = 40, currentCount = 0, unit = "questions", expReward = 600, statRewardType = "INT")
                )
                dao.insertQuests(quests)

                // Starting Shadow Soldiers
                val initialSoldiers = listOf(
                    ShadowSoldierEntity(name = "Igris (Blood-Red Commander)", rank = "Marshal", level = 110, powerRating = 95000, weapon = "Demon Monarch's Longsword", auraColor = "#9D00FF", isDeployed = true),
                    ShadowSoldierEntity(name = "Beru (Ant King)", rank = "Marshal", level = 115, powerRating = 98000, weapon = "Predator Claws & Poison", auraColor = "#00F0FF", isDeployed = true),
                    ShadowSoldierEntity(name = "Bellion (Grand Marshal)", rank = "Grand Marshal", level = 120, powerRating = 120000, weapon = "Centipede Greatsword", auraColor = "#9D00FF", isDeployed = true),
                    ShadowSoldierEntity(name = "Iron (Shield Knight)", rank = "Elite Knight", level = 85, powerRating = 45000, weapon = "Titan Tower Shield", auraColor = "#00F0FF", isDeployed = false),
                    ShadowSoldierEntity(name = "Tank (Shadow Bear)", rank = "Knight", level = 80, powerRating = 40000, weapon = "Shadow Claws", auraColor = "#00F0FF", isDeployed = false),
                    ShadowSoldierEntity(name = "Tusk (High Orc Shaman)", rank = "Commander", level = 95, powerRating = 72000, weapon = "Orb of Avarice", auraColor = "#FF0055", isDeployed = true)
                )
                initialSoldiers.forEach { dao.insertShadowSoldier(it) }

                // Inventory Items
                val items = listOf(
                    InventoryEntity(name = "Demon King's Shortsword", category = "WEAPON", rarity = "National-Level", description = "Dagger infused with white lightning and storms. Grants +150 Attack.", statBonusText = "+150 ATK, +50 AGI", isEquipped = true, iconKey = "dagger"),
                    InventoryEntity(name = "Kamish's Wrath", category = "WEAPON", rarity = "Mythic", description = "Carved from the dragon Kamish's sharpest fang. Grants colossal cutting power.", statBonusText = "+300 ATK, +80 STR", isEquipped = true, iconKey = "dagger"),
                    InventoryEntity(name = "Monarch's Cloak of Darkness", category = "ARMOR", rarity = "Mythic", description = "Woven from concentrated void shadows. Reduces physical damage by 50%.", statBonusText = "+200 VIT, +100 INT", isEquipped = true, iconKey = "armor"),
                    InventoryEntity(name = "Rasaka's Fang", category = "WEAPON", rarity = "Legendary", description = "Causes paralysis and bleeding effect upon impact.", statBonusText = "+45 ATK", isEquipped = false, iconKey = "dagger"),
                    InventoryEntity(name = "Orb of Avarice", category = "ARTIFACT", rarity = "Legendary", description = "Doubles all magic damage and mana regeneration speed.", statBonusText = "+200% MP Regen", isEquipped = true, iconKey = "orb"),
                    InventoryEntity(name = "Monarch Secret Manuscript", category = "VAULT_DOCUMENT", rarity = "Mythic", description = "Encrypted tactical notes for Absolute Shadow Monarch Mohammad Noman.", statBonusText = "Biometric Encrypted", isEquipped = false, isVaultLocked = true, iconKey = "scroll")
                )
                dao.insertInventoryItems(items)

                // 2-Year SSC Academic Plan
                val academicGoals = listOf(
                    AcademicGoalEntity(subject = "SSC Mathematics", chapterName = "Trigonometry & Higher Algebraic Formulas", targetHours = 30f, completedHours = 18.5f, targetQuestions = 300, completedQuestions = 210, mockTestScore = 94),
                    AcademicGoalEntity(subject = "SSC Physics", chapterName = "Kinematics, Work Energy & Power", targetHours = 25f, completedHours = 15.0f, targetQuestions = 200, completedQuestions = 140, mockTestScore = 91),
                    AcademicGoalEntity(subject = "SSC Chemistry", chapterName = "Chemical Reactions & Periodic Table", targetHours = 20f, completedHours = 12.0f, targetQuestions = 180, completedQuestions = 115, mockTestScore = 88),
                    AcademicGoalEntity(subject = "SSC English", chapterName = "Comprehension, Essay & Applied Grammar", targetHours = 20f, completedHours = 14.0f, targetQuestions = 250, completedQuestions = 190, mockTestScore = 95),
                    AcademicGoalEntity(subject = "SSC Biology/General", chapterName = "Cellular Biology & Genetics", targetHours = 18f, completedHours = 9.0f, targetQuestions = 150, completedQuestions = 80, mockTestScore = 86)
                )
                dao.insertAcademicGoals(academicGoals)

                // 30-Day Fat Loss & Six Pack Plan (Day 1 - 30)
                val workouts = (1..30).map { day ->
                    val focus = when (day % 4) {
                        1 -> "Fat Loss High-Intensity Circuit & 100 Jump Squats"
                        2 -> "Six-Pack Shred: Plank, Russian Twists & Hanging Leg Raises"
                        3 -> "Upper Body Monarch Sculpt: Push-ups & Dips"
                        else -> "Spine Decompression, Hamstring & Height Stretching"
                    }
                    WorkoutDayEntity(
                        dayNumber = day,
                        focusArea = focus,
                        exercises = "Warmup 5m, High Knees 3x30s, Mountain Climbers 4x25, Core Hold 3x60s, Cobra Stretch 3x45s",
                        targetMinutes = 45,
                        completed = day <= 7,
                        caloriesBurned = if (day <= 7) 420 else 0,
                        stretchRoutineCompleted = day <= 7
                    )
                }
                dao.insertWorkoutDays(workouts)
            }
        }
    }
}
