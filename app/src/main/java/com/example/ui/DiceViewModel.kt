package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.DiceSet
import com.example.data.model.RollLog
import com.example.data.repository.DiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DiceRepository

    val diceSets: StateFlow<List<DiceSet>>
    val rollLogs: StateFlow<List<RollLog>>

    private val _currentRollTotal = MutableStateFlow<Int?>(null)
    val currentRollTotal: StateFlow<Int?> = _currentRollTotal.asStateFlow()

    private val _currentRollResults = MutableStateFlow<List<Pair<Int, Int>>>(emptyList()) // Pair(sides, rolled)
    val currentRollResults: StateFlow<List<Pair<Int, Int>>> = _currentRollResults.asStateFlow()

    private val _currentRollFormula = MutableStateFlow("")
    val currentRollFormula: StateFlow<String> = _currentRollFormula.asStateFlow()

    private val _currentRollName = MutableStateFlow("")
    val currentRollName: StateFlow<String> = _currentRollName.asStateFlow()

    private val _currentRollType = MutableStateFlow("NORMAL")
    val currentRollType: StateFlow<String> = _currentRollType.asStateFlow()

    private val _isRolling = MutableStateFlow(false)
    val isRolling: StateFlow<Boolean> = _isRolling.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = DiceRepository(database.diceDao())

        diceSets = repository.allDiceSets.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        rollLogs = repository.allRollLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Pre-populate database with cool standard D&D rolls if none exist yet
        viewModelScope.launch {
            repository.allDiceSets.collect { list ->
                if (list.isEmpty()) {
                    val defaults = listOf(
                        DiceSet(
                            name = "D20 Check",
                            description = "Standard d20 roll for initiative, attacks, and ability checks.",
                            d20Count = 1,
                            modifier = 0,
                            colorHex = "#E53935", // Red d20 color
                            isCustom = false
                        ),
                        DiceSet(
                            name = "Greatsword Attack",
                            description = "Two-handed weapon hit (2d6 + 3 damage).",
                            d6Count = 2,
                            modifier = 3,
                            colorHex = "#1E88E5", // Blue sword color
                            isCustom = false
                        ),
                        DiceSet(
                            name = "Fireball (Lvl 3)",
                            description = "Level 3 evocation spell (8d6 fire damage).",
                            d6Count = 8,
                            modifier = 0,
                            colorHex = "#F4511E", // Deep Orange
                            isCustom = false
                        ),
                        DiceSet(
                            name = "Cure Wounds",
                            description = "Healing touch spell (1d8 + 4 healing).",
                            d8Count = 1,
                            modifier = 4,
                            colorHex = "#43A047", // Green
                            isCustom = false
                        ),
                        DiceSet(
                            name = "Magic Missile",
                            description = "Darts that automatically hit (3d4 + 3 force damage).",
                            d4Count = 3,
                            modifier = 3,
                            colorHex = "#8E24AA", // Purple
                            isCustom = false
                        )
                    )
                    for (set in defaults) {
                        repository.insertDiceSet(set)
                    }
                }
            }
        }
    }

    fun rollDiceSet(diceSet: DiceSet, rollType: String = "NORMAL") {
        rollDice(
            name = diceSet.name,
            d4 = diceSet.d4Count,
            d6 = diceSet.d6Count,
            d8 = diceSet.d8Count,
            d10 = diceSet.d10Count,
            d12 = diceSet.d12Count,
            d20 = diceSet.d20Count,
            d100 = diceSet.d100Count,
            modifier = diceSet.modifier,
            rollType = rollType
        )
    }

    fun rollDice(
        name: String,
        d4: Int,
        d6: Int,
        d8: Int,
        d10: Int,
        d12: Int,
        d20: Int,
        d100: Int,
        modifier: Int,
        rollType: String = "NORMAL"
    ) {
        viewModelScope.launch {
            _isRolling.value = true
            // Play quick shake delay
            kotlinx.coroutines.delay(600)

            val resultsList = mutableListOf<Pair<Int, Int>>() // Pair(sides, rolled)

            fun rollDie(sides: Int): Int = Random.nextInt(1, sides + 1)

            // Roll non-d20 dice
            if (d4 > 0) repeat(d4) { resultsList.add(4 to rollDie(4)) }
            if (d6 > 0) repeat(d6) { resultsList.add(6 to rollDie(6)) }
            if (d8 > 0) repeat(d8) { resultsList.add(8 to rollDie(8)) }
            if (d10 > 0) repeat(d10) { resultsList.add(10 to rollDie(10)) }
            if (d12 > 0) repeat(d12) { resultsList.add(12 to rollDie(12)) }

            // Handle d20 separately for Advantage/Disadvantage
            if (d20 > 0) {
                repeat(d20) {
                    if (rollType == "ADVANTAGE") {
                        val roll1 = rollDie(20)
                        val roll2 = rollDie(20)
                        val chosen = maxOf(roll1, roll2)
                        // Add both results conceptually or add the chosen one with special tracking
                        resultsList.add(20 to chosen)
                    } else if (rollType == "DISADVANTAGE") {
                        val roll1 = rollDie(20)
                        val roll2 = rollDie(20)
                        val chosen = minOf(roll1, roll2)
                        resultsList.add(20 to chosen)
                    } else {
                        resultsList.add(20 to rollDie(20))
                    }
                }
            }

            if (d100 > 0) repeat(d100) { resultsList.add(100 to rollDie(100)) }

            val sumOfRolls = resultsList.sumOf { it.second }
            val totalResult = sumOfRolls + modifier

            _currentRollTotal.value = totalResult
            _currentRollResults.value = resultsList

            // Format formula string
            val formulaParts = mutableListOf<String>()
            if (d4 > 0) formulaParts.add("${d4}d4")
            if (d6 > 0) formulaParts.add("${d6}d6")
            if (d8 > 0) formulaParts.add("${d8}d8")
            if (d10 > 0) formulaParts.add("${d10}d10")
            if (d12 > 0) formulaParts.add("${d12}d12")
            if (d20 > 0) formulaParts.add("${d20}d20")
            if (d100 > 0) formulaParts.add("${d100}d100")

            var formulaString = formulaParts.joinToString(" + ")
            if (formulaString.isEmpty()) formulaString = "0"
            if (modifier > 0) formulaString += " + $modifier"
            else if (modifier < 0) formulaString += " - ${kotlin.math.abs(modifier)}"

            _currentRollFormula.value = formulaString
            _currentRollName.value = name
            _currentRollType.value = rollType
            _isRolling.value = false

            // Save log to db
            val resultsStr = resultsList.joinToString(", ") { "d${it.first}:${it.second}" }
            val log = RollLog(
                name = name,
                formula = formulaString,
                results = resultsStr,
                modifier = modifier,
                total = totalResult,
                rollType = rollType
            )
            repository.insertRollLog(log)
        }
    }

    fun saveDiceSet(diceSet: DiceSet) {
        viewModelScope.launch {
            repository.insertDiceSet(diceSet)
        }
    }

    fun deleteDiceSet(diceSet: DiceSet) {
        viewModelScope.launch {
            repository.deleteDiceSet(diceSet)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAllRollLogs()
        }
    }
}
