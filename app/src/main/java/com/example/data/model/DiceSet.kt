package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dice_sets")
data class DiceSet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val d4Count: Int = 0,
    val d6Count: Int = 0,
    val d8Count: Int = 0,
    val d10Count: Int = 0,
    val d12Count: Int = 0,
    val d20Count: Int = 0,
    val d100Count: Int = 0,
    val modifier: Int = 0,
    val colorHex: String = "#D32F2F", // Default d20 red hex
    val isCustom: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getFormulaString(): String {
        val parts = mutableListOf<String>()
        if (d4Count > 0) parts.add("${d4Count}d4")
        if (d6Count > 0) parts.add("${d6Count}d6")
        if (d8Count > 0) parts.add("${d8Count}d8")
        if (d10Count > 0) parts.add("${d10Count}d10")
        if (d12Count > 0) parts.add("${d12Count}d12")
        if (d20Count > 0) parts.add("${d20Count}d20")
        if (d100Count > 0) parts.add("${d100Count}d100")
        
        var formulaStr = parts.joinToString(" + ")
        if (formulaStr.isEmpty()) {
            formulaStr = "0"
        }
        if (modifier > 0) {
            formulaStr += " + $modifier"
        } else if (modifier < 0) {
            formulaStr += " - ${kotlin.math.abs(modifier)}"
        }
        return formulaStr
    }

    // Check if empty dice set
    fun isEmpty(): Boolean {
        return d4Count == 0 && d6Count == 0 && d8Count == 0 && d10Count == 0 &&
               d12Count == 0 && d20Count == 0 && d100Count == 0 && modifier == 0
    }
}
