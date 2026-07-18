package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DiceSet
import com.example.data.model.RollLog
import java.text.SimpleDateFormat
import java.util.*

// Styling Color Tokens
val DarkSlateBg = Color(0xFF1C1B1F) // Sophisticated Dark Bg
val DarkCardBg = Color(0xFF2B2930)  // Sophisticated Dark Surface/Card
val GoldAccent = Color(0xFFD0BCFF)  // Sophisticated Dark Pastel Violet Accent
val DarkGoldAccent = Color(0xFF4F378B) // Sophisticated Dark Container Purple
val AmberGlow = Color(0xFFE8DEF8)   // Sophisticated Dark Secondary Accent Lavender
val CrimsonAccent = Color(0xFFF2B8B5) // Sophisticated Dark Coral Red
val TextPrimary = Color(0xFFE6E1E5)   // Sophisticated Dark Primary Text
val TextSecondary = Color(0xFFCAC4D0) // Sophisticated Dark Secondary Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceRollerApp(
    viewModel: DiceViewModel,
    modifier: Modifier = Modifier
) {
    val diceSets by viewModel.diceSets.collectAsStateWithLifecycle()
    val rollLogs by viewModel.rollLogs.collectAsStateWithLifecycle()
    
    val currentTotal by viewModel.currentRollTotal.collectAsStateWithLifecycle()
    val currentResults by viewModel.currentRollResults.collectAsStateWithLifecycle()
    val currentFormula by viewModel.currentRollFormula.collectAsStateWithLifecycle()
    val currentName by viewModel.currentRollName.collectAsStateWithLifecycle()
    val currentType by viewModel.currentRollType.collectAsStateWithLifecycle()
    val isRolling by viewModel.isRolling.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf("sets") } // "sets", "quick", "history"
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedDiceSetForEdit by remember { mutableStateOf<DiceSet?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkSlateBg,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DarkSlateBg,
                    titleContentColor = TextPrimary
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(DarkGoldAccent, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Dice Vault",
                            fontWeight = FontWeight.Medium,
                            letterSpacing = (-0.5).sp,
                            fontSize = 20.sp
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (activeTab == "sets") {
                ExtendedFloatingActionButton(
                    onClick = {
                        selectedDiceSetForEdit = null
                        showCreateDialog = true
                    },
                    modifier = Modifier.testTag("add_set_button"),
                    containerColor = GoldAccent,
                    contentColor = DarkSlateBg,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Set")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Custom Set", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Active Rolling Tray Showcase (Always visible at the top to anchor actions)
            ActiveRollingTray(
                isRolling = isRolling,
                total = currentTotal,
                results = currentResults,
                formula = currentFormula,
                rollName = currentName,
                rollType = currentType
            )

            // Segmented Navigation Bar
            TabSelector(activeTab = activeTab, onTabSelected = { activeTab = it })

            Spacer(modifier = Modifier.height(12.dp))

            // Main Tab Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                when (activeTab) {
                    "sets" -> DiceSetsTab(
                        diceSets = diceSets,
                        onRoll = { set, type -> viewModel.rollDiceSet(set, type) },
                        onEdit = { set ->
                            selectedDiceSetForEdit = set
                            showCreateDialog = true
                        },
                        onDelete = { set -> viewModel.deleteDiceSet(set) }
                    )
                    "quick" -> QuickRollerTab(
                        onRoll = { name, d4, d6, d8, d10, d12, d20, d100, mod, type ->
                            viewModel.rollDice(name, d4, d6, d8, d10, d12, d20, d100, mod, type)
                        }
                    )
                    "history" -> RollHistoryTab(
                        rollLogs = rollLogs,
                        onClearHistory = { viewModel.clearHistory() }
                    )
                }
            }
        }
    }

    // Modal Create / Edit Dialog (Bulletproof Custom Overlay Dialog)
    if (showCreateDialog) {
        DiceSetDialog(
            diceSet = selectedDiceSetForEdit,
            onDismiss = { showCreateDialog = false },
            onSave = { savedSet ->
                viewModel.saveDiceSet(savedSet)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun TabSelector(
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(DarkCardBg, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val tabs = listOf(
            Triple("sets", "Dice Sets", Icons.Default.Casino),
            Triple("quick", "Quick Tray", Icons.Default.PlayArrow),
            Triple("history", "Roll Log", Icons.Default.History)
        )

        tabs.forEach { (key, label, icon) ->
            val isSelected = activeTab == key
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) GoldAccent else Color.Transparent,
                label = "tab_bg"
            )
            val contentColor by animateColorAsState(
                targetValue = if (isSelected) DarkSlateBg else TextSecondary,
                label = "tab_content"
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .clickable { onTabSelected(key) }
                    .padding(vertical = 10.dp, horizontal = 4.dp)
                    .testTag("tab_$key"),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    color = contentColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ActiveRollingTray(
    isRolling: Boolean,
    total: Int?,
    results: List<Pair<Int, Int>>,
    formula: String,
    rollName: String,
    rollType: String
) {
    // Rotation animation for active roll
    val infiniteTransition = rememberInfiniteTransition(label = "dice_rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Shake animation
    val shakeOffset by animateDpAsState(
        targetValue = if (isRolling) 8.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "shake"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(6.dp, RoundedCornerShape(32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2B2930), Color(0xFF1D1B20))
                ),
                shape = RoundedCornerShape(32.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFF49454F),
                shape = RoundedCornerShape(32.dp)
            )
            .padding(20.dp)
            .offset(x = shakeOffset),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isRolling) {
                // Rolling screen animation styled as a rotating active obsidian die
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .rotate(rotationAngle)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF4F378B), Color(0xFF381E72))
                                ),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .border(1.dp, Color(0xFFD0BCFF), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .size(48.dp)
                                .rotate(-rotationAngle)
                        ) {
                            drawD20Wireframe(Color(0xFFD0BCFF), size.width)
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Rolling Dice...",
                        color = Color(0xFFD0BCFF),
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.5.sp,
                        fontSize = 15.sp
                    )
                }
            } else if (total != null) {
                // Showing result
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ACTIVE ROLL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD0BCFF).copy(alpha = 0.7f),
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rollName.ifEmpty { "Custom Roll" },
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Formula: $formula",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            if (rollType != "NORMAL") {
                                Spacer(modifier = Modifier.width(6.dp))
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (rollType == "ADVANTAGE") Color(0xFF2E7D32) else Color(0xFFC62828)
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(2.dp)
                                ) {
                                    Text(
                                        text = rollType,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Giant active die total result matching "Obsidian Shard"
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .rotate(45f)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF4F378B), Color(0xFF381E72))
                                ),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .border(1.dp, Color(0xFFD0BCFF), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = total.toString(),
                            color = Color(0xFFD0BCFF),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier
                                .rotate(-45f)
                                .testTag("roll_result_total")
                        )
                    }
                }

                Divider(color = Color(0xFF2D2D35), modifier = Modifier.padding(vertical = 12.dp))

                // Individual Dice Rolls visual representation
                Text(
                    text = "DICE CAST",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (results.isEmpty()) {
                        Text(
                            text = "No dice rolled, only modifier added.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    } else {
                        // Display interactive dice cast items
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LazyRowScrollableContainer(results = results)
                        }
                    }
                }
            } else {
                // Empty Tray State
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The Dice Tray is Empty",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Choose a dice set or quick roll below to cast!",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun LazyRowScrollableContainer(results: List<Pair<Int, Int>>) {
    // Standard horizontal flow since we are on mobile, let's render up to 8 inline or wrap in scroll row.
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(results) { (sides, valRolled) ->
            Box(
                modifier = Modifier
                    .width(58.dp)
                    .background(DarkSlateBg, RoundedCornerShape(8.dp))
                    .border(0.5.dp, Color(0xFF3C3C46), RoundedCornerShape(8.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Custom drawn D&D die
                    DiceShape(sides = sides, value = valRolled, color = getDieColor(sides))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = valRolled.toString(),
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DiceSetsTab(
    diceSets: List<DiceSet>,
    onRoll: (DiceSet, String) -> Unit,
    onEdit: (DiceSet) -> Unit,
    onDelete: (DiceSet) -> Unit
) {
    if (diceSets.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = null,
                    tint = TextSecondary.copy(alpha = 0.3f),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "No Custom Dice Sets", color = TextPrimary, fontWeight = FontWeight.Bold)
                Text(text = "Create custom roll collections using the '+' button.", color = TextSecondary, fontSize = 12.sp)
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(diceSets) { set ->
                var expandedMenu by remember { mutableStateOf(false) }

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(16.dp))
                        .testTag("set_card_${set.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Dynamic color dot tag
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(android.graphics.Color.parseColor(set.colorHex)))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = set.name,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = set.getFormulaString(),
                                        fontWeight = FontWeight.SemiBold,
                                        color = GoldAccent,
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }

                            // Edit/Delete Context Menu Options
                            Box {
                                IconButton(
                                    onClick = { expandedMenu = true },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Menu",
                                        tint = TextSecondary
                                    )
                                }
                                DropdownMenu(
                                    expanded = expandedMenu,
                                    onDismissRequest = { expandedMenu = false },
                                    modifier = Modifier.background(DarkCardBg)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit Dice Set", color = TextPrimary) },
                                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = GoldAccent) },
                                        onClick = {
                                            expandedMenu = false
                                            onEdit(set)
                                        }
                                    )
                                    if (set.isCustom) {
                                        DropdownMenuItem(
                                            text = { Text("Delete Set", color = CrimsonAccent) },
                                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = CrimsonAccent) },
                                            onClick = {
                                                expandedMenu = false
                                                onDelete(set)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        if (set.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = set.description,
                                color = TextSecondary,
                                fontSize = 12.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Roller Actions Row (Advantage, Normal, Disadvantage)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Only enable Advantage/Disadvantage if there's d20 involved!
                            val hasD20 = set.d20Count > 0

                            if (hasD20) {
                                Button(
                                    onClick = { onRoll(set, "ADVANTAGE") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2E7D32),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("roll_adv_${set.id}"),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Adv", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = { onRoll(set, "NORMAL") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldAccent,
                                    contentColor = DarkSlateBg
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1.5f)
                                    .height(36.dp)
                                    .testTag("roll_normal_${set.id}"),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Roll", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            if (hasD20) {
                                Button(
                                    onClick = { onRoll(set, "DISADVANTAGE") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFC62828),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .testTag("roll_dis_${set.id}"),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Dis", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickRollerTab(
    onRoll: (name: String, d4: Int, d6: Int, d8: Int, d10: Int, d12: Int, d20: Int, d100: Int, modifier: Int, rollType: String) -> Unit
) {
    var d4Count by remember { mutableIntStateOf(0) }
    var d6Count by remember { mutableIntStateOf(0) }
    var d8Count by remember { mutableIntStateOf(0) }
    var d10Count by remember { mutableIntStateOf(0) }
    var d12Count by remember { mutableIntStateOf(0) }
    var d20Count by remember { mutableIntStateOf(0) }
    var d100Count by remember { mutableIntStateOf(0) }
    var modifierValue by remember { mutableIntStateOf(0) }

    var selectedRollType by remember { mutableStateOf("NORMAL") } // "NORMAL", "ADVANTAGE", "DISADVANTAGE"

    val hasSelectedDice = (d4Count + d6Count + d8Count + d10Count + d12Count + d20Count + d100Count) > 0

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "TRAY CONSTRUCTOR",
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "Tap dice to load up your quick roll collection.",
                color = TextSecondary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Dice selection grids
        item {
            val diceSelections = listOf(
                Quadruple(4, d4Count, { d4Count += 1 }, { if (d4Count > 0) d4Count -= 1 }),
                Quadruple(6, d6Count, { d6Count += 1 }, { if (d6Count > 0) d6Count -= 1 }),
                Quadruple(8, d8Count, { d8Count += 1 }, { if (d8Count > 0) d8Count -= 1 }),
                Quadruple(10, d10Count, { d10Count += 1 }, { if (d10Count > 0) d10Count -= 1 }), // d10 and d100
                Quadruple(12, d12Count, { d12Count += 1 }, { if (d12Count > 0) d12Count -= 1 }),
                Quadruple(20, d20Count, { d20Count += 1 }, { if (d20Count > 0) d20Count -= 1 }),
                Quadruple(100, d100Count, { d100Count += 1 }, { if (d100Count > 0) d100Count -= 1 })
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    diceSelections.chunked(3).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            rowItems.forEach { (sides, count, inc, dec) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(DarkSlateBg)
                                            .clickable { inc() }
                                            .border(
                                                1.dp,
                                                if (count > 0) GoldAccent else Color(0xFF32323C),
                                                RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        DiceShape(sides = sides, value = sides, color = getDieColor(sides))
                                        if (count > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .align(Alignment.TopEnd)
                                                    .background(GoldAccent, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = count.toString(),
                                                    color = DarkSlateBg,
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "d$sides",
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        IconButton(onClick = dec, modifier = Modifier.size(24.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = "Decrease d$sides",
                                                tint = if (count > 0) TextPrimary else TextSecondary.copy(alpha = 0.3f),
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        IconButton(onClick = inc, modifier = Modifier.size(24.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Increase d$sides",
                                                tint = TextPrimary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            // Fill blank weights to align grid
                            if (rowItems.size < 3) {
                                repeat(3 - rowItems.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Modifier Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "STATIC MODIFIER",
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { if (modifierValue > -20) modifierValue-- },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSlateBg),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = if (modifierValue >= 0) "+$modifierValue" else modifierValue.toString(),
                            color = if (modifierValue != 0) GoldAccent else TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )

                        Button(
                            onClick = { if (modifierValue < 20) modifierValue++ },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSlateBg),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Advantage / Disadvantage for d20 Checkbox if d20 is loaded!
        if (d20Count > 0) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "D20 ROLL RULES",
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val options = listOf("NORMAL", "ADVANTAGE", "DISADVANTAGE")
                            options.forEach { opt ->
                                val isChosen = selectedRollType == opt
                                val chosenColor = when (opt) {
                                    "ADVANTAGE" -> Color(0xFF2E7D32)
                                    "DISADVANTAGE" -> Color(0xFFC62828)
                                    else -> GoldAccent
                                }

                                Button(
                                    onClick = { selectedRollType = opt },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isChosen) chosenColor else DarkSlateBg,
                                        contentColor = if (isChosen) Color.White else TextSecondary
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = opt,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Big ROLL Button or reset
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Clear Tray
                OutlinedButton(
                    onClick = {
                        d4Count = 0
                        d6Count = 0
                        d8Count = 0
                        d10Count = 0
                        d12Count = 0
                        d20Count = 0
                        d100Count = 0
                        modifierValue = 0
                        selectedRollType = "NORMAL"
                    },
                    border = BorderStroke(1.dp, Color(0xFF32323C)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonAccent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp),
                    enabled = hasSelectedDice || modifierValue != 0
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Clear")
                }

                // Roll Button
                Button(
                    onClick = {
                        onRoll(
                            "Quick Tray Roll",
                            d4Count,
                            d6Count,
                            d8Count,
                            d10Count,
                            d12Count,
                            d20Count,
                            d100Count,
                            modifierValue,
                            if (d20Count > 0) selectedRollType else "NORMAL"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = DarkSlateBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("roll_tray_button"),
                    enabled = hasSelectedDice || modifierValue != 0
                ) {
                    Icon(imageVector = Icons.Default.Casino, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "ROLL TRAY", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun RollHistoryTab(
    rollLogs: List<RollLog>,
    onClearHistory: () -> Unit
) {
    var showConfirmClear by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LOGGED HISTORY",
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp
            )
            if (rollLogs.isNotEmpty()) {
                TextButton(onClick = { showConfirmClear = true }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = CrimsonAccent)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Clear Logs", color = CrimsonAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (rollLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "No Rolls Recorded", color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text(text = "Roll any dice set to record the logs.", color = TextSecondary, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 32.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(rollLogs) { log ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = log.name.ifEmpty { "Quick Roll" },
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = formatTime(log.timestamp),
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = log.formula,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(
                                                color = GoldAccent.copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .border(0.5.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = log.total.toString(),
                                            color = GoldAccent,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Serif
                                        )
                                    }
                                }
                            }

                            if (log.results.isNotEmpty()) {
                                Divider(color = Color(0xFF282830), modifier = Modifier.padding(vertical = 8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Rolls: ",
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = log.results,
                                        color = TextPrimary,
                                        fontSize = 11.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Clear confirmation dialog
    if (showConfirmClear) {
        AlertDialog(
            onDismissRequest = { showConfirmClear = false },
            title = { Text("Clear Log History?", color = TextPrimary) },
            text = { Text("This will permanently delete all historic roll logs from the database.", color = TextSecondary) },
            containerColor = DarkCardBg,
            dismissButton = {
                TextButton(onClick = { showConfirmClear = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onClearHistory()
                    showConfirmClear = false
                }) {
                    Text("Clear All", color = CrimsonAccent, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

// Dialog for customized dice set builder
@Composable
fun DiceSetDialog(
    diceSet: DiceSet?,
    onDismiss: () -> Unit,
    onSave: (DiceSet) -> Unit
) {
    var name by remember { mutableStateOf(diceSet?.name ?: "") }
    var description by remember { mutableStateOf(diceSet?.description ?: "") }
    var d4Count by remember { mutableIntStateOf(diceSet?.d4Count ?: 0) }
    var d6Count by remember { mutableIntStateOf(diceSet?.d6Count ?: 0) }
    var d8Count by remember { mutableIntStateOf(diceSet?.d8Count ?: 0) }
    var d10Count by remember { mutableIntStateOf(diceSet?.d10Count ?: 0) }
    var d12Count by remember { mutableIntStateOf(diceSet?.d12Count ?: 0) }
    var d20Count by remember { mutableIntStateOf(diceSet?.d20Count ?: 0) }
    var d100Count by remember { mutableIntStateOf(diceSet?.d100Count ?: 0) }
    var modifier by remember { mutableIntStateOf(diceSet?.modifier ?: 0) }
    var selectedColor by remember { mutableStateOf(diceSet?.colorHex ?: "#D32F2F") }

    val colors = listOf("#D32F2F", "#1976D2", "#388E3C", "#7B1FA2", "#F57C00", "#0097A7")

    val hasAnyDice = (d4Count + d6Count + d8Count + d10Count + d12Count + d20Count + d100Count) > 0

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCardBg),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFF3C3C46), RoundedCornerShape(20.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (diceSet == null) "CREATE CUSTOM SET" else "EDIT DICE SET",
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Text Fields
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Set Name (e.g., Fireball Lvl 3)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = GoldAccent,
                                unfocusedBorderColor = Color(0xFF3C3C46),
                                focusedLabelColor = GoldAccent,
                                unfocusedLabelColor = TextSecondary
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("set_name_input")
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Short Description") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = GoldAccent,
                                unfocusedBorderColor = Color(0xFF3C3C46),
                                focusedLabelColor = GoldAccent,
                                unfocusedLabelColor = TextSecondary
                            ),
                            singleLine = false,
                            maxLines = 2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("set_description_input")
                        )
                    }
                }

                // Grid of Counter controls
                item {
                    Text(
                        text = "SELECT DICE QUANTITIES",
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val diceCounters = listOf(
                        DiceCounterData("d4", d4Count, { d4Count++ }, { if (d4Count > 0) d4Count-- }, 4),
                        DiceCounterData("d6", d6Count, { d6Count++ }, { if (d6Count > 0) d6Count-- }, 6),
                        DiceCounterData("d8", d8Count, { d8Count++ }, { if (d8Count > 0) d8Count-- }, 8),
                        DiceCounterData("d10", d10Count, { d10Count++ }, { if (d10Count > 0) d10Count-- }, 10),
                        DiceCounterData("d12", d12Count, { d12Count++ }, { if (d12Count > 0) d12Count-- }, 12),
                        DiceCounterData("d20", d20Count, { d20Count++ }, { if (d20Count > 0) d20Count-- }, 20),
                        DiceCounterData("d100", d100Count, { d100Count++ }, { if (d100Count > 0) d100Count-- }, 100)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        diceCounters.chunked(2).forEach { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                pair.forEach { data ->
                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(DarkSlateBg, RoundedCornerShape(10.dp))
                                            .border(0.5.dp, Color(0xFF32323C), RoundedCornerShape(10.dp))
                                            .padding(6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            DiceShape(sides = data.sides, value = data.sides, color = getDieColor(data.sides), modifier = Modifier.size(28.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(text = data.label, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = data.dec, modifier = Modifier.size(24.dp)) {
                                                Icon(Icons.Default.Remove, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(12.dp))
                                            }
                                            Text(
                                                text = data.count.toString(),
                                                color = if (data.count > 0) GoldAccent else TextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                            IconButton(onClick = data.inc, modifier = Modifier.size(24.dp)) {
                                                Icon(Icons.Default.Add, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(12.dp))
                                            }
                                        }
                                    }
                                }
                                if (pair.size < 2) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                // Modifier Counter
                item {
                    Text(
                        text = "ADD MODIFIER",
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSlateBg, RoundedCornerShape(10.dp))
                            .border(0.5.dp, Color(0xFF32323C), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { if (modifier > -20) modifier-- },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkCardBg),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }

                        Text(
                            text = if (modifier >= 0) "+$modifier" else modifier.toString(),
                            color = if (modifier != 0) GoldAccent else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        Button(
                            onClick = { if (modifier < 20) modifier++ },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkCardBg),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }

                // Theme Color Palette Selection
                item {
                    Text(
                        text = "THEME COLOR",
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        colors.forEach { colHex ->
                            val isChosen = selectedColor == colHex
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(colHex)))
                                    .clickable { selectedColor = colHex }
                                    .border(
                                        width = if (isChosen) 2.5.dp else 0.dp,
                                        color = if (isChosen) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChosen) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Save or Cancel Buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            border = BorderStroke(1.dp, Color(0xFF3C3C46)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                if (name.isNotEmpty()) {
                                    val saved = DiceSet(
                                        id = diceSet?.id ?: 0,
                                        name = name,
                                        description = description,
                                        d4Count = d4Count,
                                        d6Count = d6Count,
                                        d8Count = d8Count,
                                        d10Count = d10Count,
                                        d12Count = d12Count,
                                        d20Count = d20Count,
                                        d100Count = d100Count,
                                        modifier = modifier,
                                        colorHex = selectedColor,
                                        isCustom = diceSet?.isCustom ?: true
                                    )
                                    onSave(saved)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = DarkSlateBg),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("save_set_button"),
                            enabled = name.isNotEmpty() && hasAnyDice
                        ) {
                            Text("Save Set", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// Helper Class definitions for counters
data class DiceCounterData(
    val label: String,
    val count: Int,
    val inc: () -> Unit,
    val dec: () -> Unit,
    val sides: Int
)

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
data class Quadruple5<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)

fun getDieColor(sides: Int): Color {
    return when (sides) {
        4 -> Color(0xFFBB86FC)   // Pastel Purple
        6 -> Color(0xFF80B3FF)   // Pastel Blue
        8 -> Color(0xFF81C784)   // Pastel Green
        10 -> Color(0xFFFFD54F)  // Pastel Amber
        12 -> Color(0xFF4DD0E1)  // Pastel Teal
        20 -> Color(0xFFD0BCFF)  // Pastel Violet (D20 Hero Color)
        100 -> Color(0xFFF48FB1) // Pastel Pink
        else -> GoldAccent
    }
}

fun formatTime(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
        sdf.format(Date(timestamp))
    } catch (e: Exception) {
        ""
    }
}

// Custom canvas drawing helpers for D&D dice shapes
fun DrawScope.drawD20Wireframe(color: Color, width: Float) {
    val centerX = width / 2
    val centerY = width / 2
    val radius = width * 0.45f

    // Outer Hexagon Points
    val outerPoints = (0 until 6).map { i ->
        val angle = Math.toRadians((i * 60 - 90).toDouble())
        Offset(
            centerX + radius * Math.cos(angle).toFloat(),
            centerY + radius * Math.sin(angle).toFloat()
        )
    }

    // Inner Triangle Points
    val innerRadius = radius * 0.5f
    val innerPoints = (0 until 3).map { i ->
        val angle = Math.toRadians((i * 120 - 90).toDouble())
        Offset(
            centerX + innerRadius * Math.cos(angle).toFloat(),
            centerY + innerRadius * Math.sin(angle).toFloat()
        )
    }

    // 1. Draw Outer Hexagon path
    val hexPath = Path().apply {
        moveTo(outerPoints[0].x, outerPoints[0].y)
        for (i in 1 until 6) {
            lineTo(outerPoints[i].x, outerPoints[i].y)
        }
        close()
    }
    drawPath(path = hexPath, color = color.copy(alpha = 0.15f))
    drawPath(path = hexPath, color = color, style = Stroke(width = 2.dp.toPx()))

    // 2. Draw Inner Triangle path
    val triPath = Path().apply {
        moveTo(innerPoints[0].x, innerPoints[0].y)
        lineTo(innerPoints[1].x, innerPoints[1].y)
        lineTo(innerPoints[2].x, innerPoints[2].y)
        close()
    }
    drawPath(path = triPath, color = color, style = Stroke(width = 1.5.dp.toPx()))

    // 3. Draw intersecting facets from outer corners to inner corners
    for (i in 0 until 3) {
        val outerIndex = i * 2
        drawLine(color, innerPoints[i], outerPoints[outerIndex], strokeWidth = 1.2.dp.toPx())
        
        val nextOuter = (outerIndex + 1) % 6
        drawLine(color, innerPoints[i], outerPoints[nextOuter], strokeWidth = 1.2.dp.toPx())

        val prevOuter = if (outerIndex == 0) 5 else outerIndex - 1
        drawLine(color, innerPoints[i], outerPoints[prevOuter], strokeWidth = 1.2.dp.toPx())
    }
}

@Composable
fun DiceShape(sides: Int, value: Int, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(40.dp)) {
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2
        val radius = width * 0.45f

        val path = Path()
        when (sides) {
            4 -> { // Triangle
                path.moveTo(centerX, centerY - radius)
                path.lineTo(centerX + radius * 0.86f, centerY + radius * 0.5f)
                path.lineTo(centerX - radius * 0.86f, centerY + radius * 0.5f)
                path.close()
            }
            6 -> { // Square
                path.moveTo(centerX - radius * 0.8f, centerY - radius * 0.8f)
                path.lineTo(centerX + radius * 0.8f, centerY - radius * 0.8f)
                path.lineTo(centerX + radius * 0.8f, centerY + radius * 0.8f)
                path.lineTo(centerX - radius * 0.8f, centerY + radius * 0.8f)
                path.close()
            }
            8 -> { // Diamond / Octahedron
                path.moveTo(centerX, centerY - radius)
                path.lineTo(centerX + radius * 0.8f, centerY)
                path.lineTo(centerX, centerY + radius)
                path.lineTo(centerX - radius * 0.8f, centerY)
                path.close()
            }
            10, 100 -> { // Kite
                path.moveTo(centerX, centerY - radius)
                path.lineTo(centerX + radius * 0.8f, centerY - radius * 0.1f)
                path.lineTo(centerX, centerY + radius)
                path.lineTo(centerX - radius * 0.8f, centerY - radius * 0.1f)
                path.close()
            }
            12 -> { // Pentagon
                for (i in 0 until 5) {
                    val angle = Math.toRadians((i * 72 - 90).toDouble())
                    val x = centerX + radius * Math.cos(angle).toFloat()
                    val y = centerY + radius * Math.sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
            }
            20 -> { // Hexagon / Icosahedron
                for (i in 0 until 6) {
                    val angle = Math.toRadians((i * 60 - 90).toDouble())
                    val x = centerX + radius * Math.cos(angle).toFloat()
                    val y = centerY + radius * Math.sin(angle).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
            }
        }

        // Draw background filled shape
        drawPath(path = path, color = color.copy(alpha = 0.15f))
        
        // Draw outline border
        drawPath(path = path, color = color, style = Stroke(width = 1.5.dp.toPx()))

        // Inner lines
        when (sides) {
            4 -> {
                drawLine(color, Offset(centerX, centerY + radius * 0.5f), Offset(centerX, centerY - radius), strokeWidth = 0.8.dp.toPx())
                drawLine(color, Offset(centerX, centerY + radius * 0.5f), Offset(centerX + radius * 0.86f, centerY + radius * 0.5f), strokeWidth = 0.8.dp.toPx())
                drawLine(color, Offset(centerX, centerY + radius * 0.5f), Offset(centerX - radius * 0.86f, centerY + radius * 0.5f), strokeWidth = 0.8.dp.toPx())
            }
            8 -> {
                drawLine(color, Offset(centerX - radius * 0.8f, centerY), Offset(centerX + radius * 0.8f, centerY), strokeWidth = 0.8.dp.toPx())
                drawLine(color, Offset(centerX, centerY - radius), Offset(centerX, centerY + radius), strokeWidth = 0.8.dp.toPx())
            }
            20 -> {
                val innerPath = Path()
                val innerRadius = radius * 0.5f
                for (i in 0 until 3) {
                    val angle = Math.toRadians((i * 120 - 90).toDouble())
                    val x = centerX + innerRadius * Math.cos(angle).toFloat()
                    val y = centerY + innerRadius * Math.sin(angle).toFloat()
                    if (i == 0) innerPath.moveTo(x, y) else innerPath.lineTo(x, y)
                }
                innerPath.close()
                drawPath(path = innerPath, color = color, style = Stroke(width = 0.8.dp.toPx()))
                for (i in 0 until 3) {
                    val angleInner = Math.toRadians((i * 120 - 90).toDouble())
                    val xInner = centerX + innerRadius * Math.cos(angleInner).toFloat()
                    val yInner = centerY + innerRadius * Math.sin(angleInner).toFloat()
                    
                    val angleOuter = Math.toRadians((i * 120 - 90).toDouble())
                    val xOuter = centerX + radius * Math.cos(angleOuter).toFloat()
                    val yOuter = centerY + radius * Math.sin(angleOuter).toFloat()
                    drawLine(color, Offset(xInner, yInner), Offset(xOuter, yOuter), strokeWidth = 0.8.dp.toPx())

                    val angleOuterNext = Math.toRadians(((i * 120 + 60) - 90).toDouble())
                    val xOuterNext = centerX + radius * Math.cos(angleOuterNext).toFloat()
                    val yOuterNext = centerY + radius * Math.sin(angleOuterNext).toFloat()
                    drawLine(color, Offset(xInner, yInner), Offset(xOuterNext, yOuterNext), strokeWidth = 0.8.dp.toPx())

                    val angleOuterPrev = Math.toRadians(((i * 120 - 60) - 90).toDouble())
                    val xOuterPrev = centerX + radius * Math.cos(angleOuterPrev).toFloat()
                    val yOuterPrev = centerY + radius * Math.sin(angleOuterPrev).toFloat()
                    drawLine(color, Offset(xInner, yInner), Offset(xOuterPrev, yOuterPrev), strokeWidth = 0.8.dp.toPx())
                }
            }
            12 -> {
                val innerPath = Path()
                val innerRadius = radius * 0.45f
                for (i in 0 until 5) {
                    val angle = Math.toRadians((i * 72 - 90 + 36).toDouble())
                    val x = centerX + innerRadius * Math.cos(angle).toFloat()
                    val y = centerY + innerRadius * Math.sin(angle).toFloat()
                    if (i == 0) innerPath.moveTo(x, y) else innerPath.lineTo(x, y)
                }
                innerPath.close()
                drawPath(path = innerPath, color = color, style = Stroke(width = 0.8.dp.toPx()))
            }
        }
    }
}
