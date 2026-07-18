package com.example.data.database

import androidx.room.*
import com.example.data.model.DiceSet
import com.example.data.model.RollLog
import kotlinx.coroutines.flow.Flow

@Dao
interface DiceDao {
    // DiceSet Queries
    @Query("SELECT * FROM dice_sets ORDER BY timestamp DESC")
    fun getAllDiceSets(): Flow<List<DiceSet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiceSet(diceSet: DiceSet)

    @Update
    suspend fun updateDiceSet(diceSet: DiceSet)

    @Delete
    suspend fun deleteDiceSet(diceSet: DiceSet)

    @Query("DELETE FROM dice_sets WHERE id = :id")
    suspend fun deleteDiceSetById(id: Int)

    // RollLog Queries
    @Query("SELECT * FROM roll_logs ORDER BY timestamp DESC")
    fun getAllRollLogs(): Flow<List<RollLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRollLog(rollLog: RollLog)

    @Query("DELETE FROM roll_logs")
    suspend fun clearAllRollLogs()
}
