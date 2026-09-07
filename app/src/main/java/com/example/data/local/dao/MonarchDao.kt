package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MonarchDao {
    // Stats
    @Query("SELECT * FROM monarch_stats WHERE id = 1")
    fun getMonarchStats(): Flow<StatEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMonarchStats(stats: StatEntity)

    // Quests
    @Query("SELECT * FROM daily_quests ORDER BY isCompleted ASC, id ASC")
    fun getAllQuests(): Flow<List<QuestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: QuestEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(quests: List<QuestEntity>)

    @Update
    suspend fun updateQuest(quest: QuestEntity)

    @Query("DELETE FROM daily_quests WHERE id = :id")
    suspend fun deleteQuest(id: Long)

    // Shadow Soldiers
    @Query("SELECT * FROM shadow_soldiers ORDER BY level DESC, id DESC")
    fun getAllShadowSoldiers(): Flow<List<ShadowSoldierEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShadowSoldier(soldier: ShadowSoldierEntity): Long

    @Update
    suspend fun updateShadowSoldier(soldier: ShadowSoldierEntity)

    @Delete
    suspend fun deleteShadowSoldier(soldier: ShadowSoldierEntity)

    // Inventory
    @Query("SELECT * FROM inventory_items ORDER BY id ASC")
    fun getAllInventoryItems(): Flow<List<InventoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItem(item: InventoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItems(items: List<InventoryEntity>)

    @Update
    suspend fun updateInventoryItem(item: InventoryEntity)

    // Academic Goals
    @Query("SELECT * FROM academic_goals ORDER BY isCompleted ASC, id ASC")
    fun getAllAcademicGoals(): Flow<List<AcademicGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAcademicGoal(goal: AcademicGoalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAcademicGoals(goals: List<AcademicGoalEntity>)

    @Update
    suspend fun updateAcademicGoal(goal: AcademicGoalEntity)

    // 30-Day Workout
    @Query("SELECT * FROM workout_days ORDER BY dayNumber ASC")
    fun getAllWorkoutDays(): Flow<List<WorkoutDayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutDays(days: List<WorkoutDayEntity>)

    @Update
    suspend fun updateWorkoutDay(day: WorkoutDayEntity)

    // Vault
    @Query("SELECT * FROM vault_records ORDER BY timestamp DESC")
    fun getAllVaultRecords(): Flow<List<VaultRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaultRecord(record: VaultRecordEntity): Long

    @Delete
    suspend fun deleteVaultRecord(record: VaultRecordEntity)
}
