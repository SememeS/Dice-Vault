package com.example.data.repository

import com.example.data.database.DiceDao
import com.example.data.model.DiceSet
import com.example.data.model.RollLog
import kotlinx.coroutines.flow.Flow

class DiceRepository(private val diceDao: DiceDao) {
    val allDiceSets: Flow<List<DiceSet>> = diceDao.getAllDiceSets()
    val allRollLogs: Flow<List<RollLog>> = diceDao.getAllRollLogs()

    suspend fun insertDiceSet(diceSet: DiceSet) {
        diceDao.insertDiceSet(diceSet)
    }

    suspend fun updateDiceSet(diceSet: DiceSet) {
        diceDao.updateDiceSet(diceSet)
    }

    suspend fun deleteDiceSet(diceSet: DiceSet) {
        diceDao.deleteDiceSet(diceSet)
    }

    suspend fun deleteDiceSetById(id: Int) {
        diceDao.deleteDiceSetById(id)
    }

    suspend fun insertRollLog(rollLog: RollLog) {
        diceDao.insertRollLog(rollLog)
    }

    suspend fun clearAllRollLogs() {
        diceDao.clearAllRollLogs()
    }
}
