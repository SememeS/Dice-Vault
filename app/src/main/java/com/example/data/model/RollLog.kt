package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roll_logs")
data class RollLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val name: String, // e.g. "Fireball" or "Custom Roll"
    val formula: String, // e.g. "8d6" or "1d20"
    val results: String, // Comma-separated rolls, e.g. "4,5,6"
    val modifier: Int = 0,
    val total: Int,
    val rollType: String = "NORMAL" // "NORMAL", "ADVANTAGE", "DISADVANTAGE"
)
