package com.example.data.local.repository

import com.example.data.local.dao.MonarchDao
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.Flow

class MonarchRepository(private val dao: MonarchDao) {
    val stats: Flow<StatEntity?> = dao.getMonarchStats()
    val quests: Flow<List<QuestEntity>> = dao.getAllQuests()
    val shadowSoldiers: Flow<List<ShadowSoldierEntity>> = dao.getAllShadowSoldiers()
    val inventory: Flow<List<InventoryEntity>> = dao.getAllInventoryItems()
    val academicGoals: Flow<List<AcademicGoalEntity>> = dao.getAllAcademicGoals()
    val workoutDays: Flow<List<WorkoutDayEntity>> = dao.getAllWorkoutDays()
    val vaultRecords: Flow<List<VaultRecordEntity>> = dao.getAllVaultRecords()

    suspend fun updateStats(stats: StatEntity) = dao.updateMonarchStats(stats)
    suspend fun insertQuest(quest: QuestEntity) = dao.insertQuest(quest)
    suspend fun updateQuest(quest: QuestEntity) = dao.updateQuest(quest)
    suspend fun deleteQuest(id: Long) = dao.deleteQuest(id)

    suspend fun insertShadowSoldier(soldier: ShadowSoldierEntity) = dao.insertShadowSoldier(soldier)
    suspend fun updateShadowSoldier(soldier: ShadowSoldierEntity) = dao.updateShadowSoldier(soldier)
    suspend fun deleteShadowSoldier(soldier: ShadowSoldierEntity) = dao.deleteShadowSoldier(soldier)

    suspend fun insertInventoryItem(item: InventoryEntity) = dao.insertInventoryItem(item)
    suspend fun updateInventoryItem(item: InventoryEntity) = dao.updateInventoryItem(item)

    suspend fun updateAcademicGoal(goal: AcademicGoalEntity) = dao.updateAcademicGoal(goal)
    suspend fun updateWorkoutDay(day: WorkoutDayEntity) = dao.updateWorkoutDay(day)

    suspend fun insertVaultRecord(record: VaultRecordEntity) = dao.insertVaultRecord(record)
    suspend fun deleteVaultRecord(record: VaultRecordEntity) = dao.deleteVaultRecord(record)
}
