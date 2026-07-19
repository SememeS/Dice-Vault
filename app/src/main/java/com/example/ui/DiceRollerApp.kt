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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DiceSet
import com.example.data.model.RollLog
import java.text.SimpleDateFormat
import java.util.*

// Styling Color Tokens
var DarkSlateBg by mutableStateOf(Color(0xFF1C1B1F)) // Sophisticated Dark Bg
var DarkCardBg by mutableStateOf(Color(0xFF2B2930))  // Sophisticated Dark Surface/Card
var GoldAccent by mutableStateOf(Color(0xFF7E57C2))  // Sophisticated Dark Pastel Violet Accent
var DarkGoldAccent by mutableStateOf(Color(0xFF311B92)) // Sophisticated Dark Container Purple
var AmberGlow by mutableStateOf(Color(0xFF4527A0))   // Sophisticated Dark Secondary Accent Lavender
var CrimsonAccent by mutableStateOf(Color(0xFFD81B60)) // Sophisticated Dark Coral Red
var TextPrimary by mutableStateOf(Color(0xFFE6E1E5))   // Sophisticated Dark Primary Text
var TextSecondary by mutableStateOf(Color(0xFFCAC4D0)) // Sophisticated Dark Secondary Text
var AdvantageColor by mutableStateOf(Color(0xFF4527A0)) // Advantage Roll Button
var DisadvantageColor by mutableStateOf(Color(0xFFD81B60)) // Disadvantage Roll Button
var DiceRollResultColor by mutableStateOf(Color(0xFF7E57C2)) // Dice Roll Result Color
var DiceRollAnimationColor by mutableStateOf(Color(0xFF7E57C2)) // Dice Roll Animation Color
var D4Color by mutableStateOf(Color(0xFFFFFFFF)) // d4 color
var D6Color by mutableStateOf(Color(0xFFFFFFFF)) // d6 color
var D8Color by mutableStateOf(Color(0xFFFFFFFF)) // d8 color
var D10Color by mutableStateOf(Color(0xFFFFFFFF)) // d10 color
var D12Color by mutableStateOf(Color(0xFFFFFFFF)) // d12 color
var D20Color by mutableStateOf(Color(0xFFFFFFFF)) // d20 color
var D100Color by mutableStateOf(Color(0xFFFFFFFF)) // d100 color
var DiceTrayGradientStart by mutableStateOf(Color(0xFF2B2930)) // Dice Tray Gradient Start
var DiceTrayGradientEnd by mutableStateOf(Color(0xFF1C1B1F)) // Dice Tray Gradient End
var ActiveRollBackgroundColor by mutableStateOf(Color(0xFF7E57C2))
var ActiveRollContentColor by mutableStateOf(Color(0xFF1C1B1F)) // Active Roll Gradient Start

enum class UiStyleTheme {
    CLASSIC_ROUNDED,
    GLASSMORPHIC,
    NEON_GLOW,
    FLAT_MINIMAL,
    OBSIDIAN_SHARD,
    CHUNKY_ARCADE,
    SOFT_ORGANIC
}

var currentUiStyle by mutableStateOf(UiStyleTheme.GLASSMORPHIC)

enum class ButtonStyleTheme {
    PILL,
    ROUNDED,
    SHARP,
    CUT_CORNER
}

var currentButtonStyle by mutableStateOf(ButtonStyleTheme.PILL)
var currentLanguage by mutableStateOf("English")

val frTranslations = mapOf(
    "Dice Sets" to "Sets de Dés",
    "Quick Tray" to "Lancer Rapide",
    "Roll Log" to "Historique",
    "🎨 Presets" to "🎨 Prédéfini",
    "🔧 Colorizer" to "🔧 Coloriseur",
    "⚙️ Stylizer" to "⚙️ Styliseur",
    "🌐 Language" to "🌐 Langues",
    "THEME COLOR" to "COULEUR DU THÈME",
    "DICE CAST" to "LANCER DE DÉS",
    "ROLL TRAY" to "PLATEAU DE DÉS",
    "ACTIVE ROLL" to "LANCER ACTIF",
    "Rolling Dice..." to "Lancement des dés...",
    "Choose a dice set or quick roll below to cast!" to "Choisissez un set de dés ou un lancer rapide ci-dessous pour lancer !",
    "No Custom Dice Sets" to "Aucun set de dés personnalisé",
    "Create custom roll collections using the '+' button." to "Créez des collections de lancers avec le bouton '+'.",
    "The Dice Tray is Empty" to "Le plateau de dés est vide",
    "Tap dice to load up your quick roll collection." to "Appuyez sur les dés pour charger votre collection.",
    "Clear" to "Effacer",
    "Roll" to "Lancer",
    "NORMAL" to "NORMAL",
    "Quick Tray Roll" to "Lancer Rapide",
    "Quick Roll" to "Lancer Rapide",
    "ADVANTAGE" to "AVANTAGE",
    "DISADVANTAGE" to "DÉSAVANTAGE",
    "LOGGED HISTORY" to "HISTORIQUE",
    "No Rolls Recorded" to "Aucun Lancer Enregistré",
    "Roll any dice set to record the logs." to "Lancez n'importe quel dé pour l'enregistrer.",
    "Clear Logs" to "Effacer l'historique",
    "Clear Log History?" to "Effacer l'historique ?",
    "This will permanently delete all historic roll logs from the database." to "Cela supprimera définitivement tout l'historique de la base de données.",
    "Cancel" to "Annuler",
    "Clear All" to "Tout effacer",
    "EDIT DICE SET" to "ÉDITER SET DE DÉS",
    "CREATE CUSTOM SET" to "CRÉER UN SET",
    "Set Name (e.g., Fireball Lvl 3)" to "Nom du Set (ex: Boule de Feu Niv 3)",
    "Short Description" to "Courte Description",
    "SELECT DICE QUANTITIES" to "SÉLECTIONNER LES QUANTITÉS",
    "STATIC MODIFIER" to "MODIFICATEUR STATIQUE",
    "ADD MODIFIER" to "AJOUTER MODIFICATEUR",
    "Save Set" to "Sauvegarder",
    "Delete Set" to "Supprimer le set",
    "TAP TO ACTIVATE PRESET" to "APPUYEZ POUR ACTIVER LE THÈME",
    "Each theme customizes the layout, buttons, accent glow, and texts:" to "Chaque thème personnalise la disposition, les boutons et les couleurs :",
    "GRANULAR ELEMENT COLORS" to "COULEURS DES ÉLÉMENTS",
    "Tap any row to open the interactive Color Picker, or use the quick swatches below:" to "Appuyez pour ouvrir le sélecteur de couleurs, ou utilisez les palettes rapides :",
    "App Background" to "Fond de l'application",
    "Panel / Card Background" to "Fond des cartes",
    "Highlight / D20 Accent" to "Accentuation D20",
    "Button Fill / Container Accent" to "Couleur des boutons",
    "Secondary highlights" to "Accentuations secondaires",
    "Rage / Warning Accent" to "Accent d'alerte / Rage",
    "Primary Text" to "Texte principal",
    "Secondary Text" to "Texte secondaire",
    "Advantage Button Color" to "Bouton Avantage",
    "Disadvantage Button Color" to "Bouton Désavantage",
    "Dice Roll Result Color" to "Résultat des dés",
    "Dice Roll Animation Color" to "Animation des dés",
    "d4 Color" to "Couleur d4",
    "d6 Color" to "Couleur d6",
    "d8 Color" to "Couleur d8",
    "d10 Color" to "Couleur d10",
    "d12 Color" to "Couleur d12",
    "d20 Color" to "Couleur d20",
    "d100 Color" to "Couleur d100",
    "Dice Tray Gradient Top" to "Plateau - Dégradé haut",
    "Dice Tray Gradient Bottom" to "Plateau - Dégradé bas",
    "Active Roll Background" to "Fond du lancer actif",
    "Active Roll Content Color" to "Texte du lancer actif",
    "UI VISUAL STYLE" to "STYLE VISUEL",
    "Transform the structural aesthetic of cards, containers, and borders:" to "Transformez l'esthétique des cartes, des conteneurs et des bordures :",
    "Classic Rounded" to "Classique arrondi",
    "Elegant Material curves & subtle shadows" to "Courbes Material élégantes et ombres douces",
    "Glassmorphic" to "Effet Verre",
    "Translucent glass panels with frosted borders" to "Panneaux de verre translucide avec des bordures givrées",
    "Neon Glow" to "Lueur Néon",
    "Sharp glowing tech borders & neon sci-fi halo" to "Bordures technologiques lumineuses et halo de science-fiction",
    "Flat Minimalist" to "Plat minimaliste",
    "Stark, crisp flat 2D retro design" to "Design 2D plat et épuré rétro",
    "Obsidian Shard" to "Éclat d'obsidienne",
    "Hard angled cuts resembling carved stone" to "Coupes en biais dures ressemblant à la pierre sculptée",
    "Chunky Arcade" to "Arcade Massive",
    "Thick bold borders with heavy drop shadows" to "Bordures épaisses avec des ombres marquées",
    "Soft Organic" to "Doux organique",
    "Fully rounded soft edges" to "Bords doux entièrement arrondis",
    "BUTTON STYLE AESTHETIC" to "STYLE DES BOUTONS",
    "Choose the shape and curvature of all action buttons:" to "Choisissez la forme et la courbure de tous les boutons :",
    "Pill Shaped" to "Forme de pilule",
    "Highly pill-shaped friendly curves" to "Courbes douces très arrondies en pilule",
    "Rounded Corner" to "Coins arrondis",
    "Standard subtle rounded corners" to "Coins standard subtilement arrondis",
    "Sharp Rectangle" to "Rectangle net",
    "Hard 90-degree corners" to "Angles droits stricts de 90 degrés",
    "Cut Corners" to "Coins coupés",
    "Sci-fi chamfered edges" to "Bords chanfreinés style science-fiction",
    "USER PREFERENCES" to "PRÉFÉRENCES UTILISATEUR",
    "Toggle game mechanisms and haptics for custom rolling suspense:" to "Activez les mécanismes de jeu et le retour haptique pour le suspense :",
    "Dice Rolling Delay" to "Délai du lancer",
    "Play 600ms roll delay for suspense" to "600 ms de délai pour le suspense",
    "Rolling Sound Effects" to "Effets sonores",
    "Play fantasy sound effects when rolling" to "Jouer des sons fantasy lors du lancer",
    "Haptic Vibration" to "Vibration haptique",
    "Vibrate device when dice stop rolling" to "Vibrer quand les dés s'arrêtent",
    "LANGUAGE" to "LANGUE",
    "Select your preferred app language:" to "Sélectionnez la langue de l'application :",
    "Legendary Colorizer" to "Coloriseur Légendaire",
    "v1.3.0 • Unleash Your Aesthetic" to "v1.3.0 • Libérez votre esthétique",
    "Close Settings" to "Fermer",
    "Save Custom Preset" to "Sauvegarder le Préréglage",
    "Enter a name for your custom preset style:" to "Entrez un nom pour votre style personnalisé :",
    "My Custom Style" to "Mon Style",
    "Save" to "Sauvegarder",
    "Save Current Style as Custom Preset" to "Sauvegarder le style en tant que Préréglage",
    "Hex Code:" to "Code Hex :",
    "Hue" to "Teinte",
    "Saturation" to "Saturation",
    "Brightness" to "Luminosité",
    "Custom Roll" to "Lancer Personnalisé",
    "Formula: " to "Formule : ",
    "Rolls: " to "Lancers : ",
    "Settings" to "Paramètres",
    "English" to "English",
    "Français" to "Français",
    "Toggle color picker" to "Afficher couleurs",
    "Menu" to "Menu",
    "Delete" to "Suppr.",
    "Selected" to "Sélectionné",
    "Active" to "Actif",
    "Pen and Paper" to "Plume et Papier",
    "High contrast ink & white" to "Contraste élevé encre & blanc",
    "Obsidian Violet" to "Violet Obsidienne",
    "Muted cosmic dark" to "Sombre cosmique discret",
    "Metal" to "Métal",
    "Metal greyscale" to "Niveaux de gris métal"
)

@Composable
fun t(key: String): String {
    return if (currentLanguage == "Français") frTranslations[key] ?: key else key
}
fun String.t(): String {
    return if (currentLanguage == "Français") frTranslations[this] ?: this else this
}



@Composable
fun getButtonShape(defaultRadius: androidx.compose.ui.unit.Dp = 12.dp): androidx.compose.foundation.shape.CornerBasedShape {
    return when (currentButtonStyle) {
        ButtonStyleTheme.PILL -> androidx.compose.foundation.shape.RoundedCornerShape(50)
        ButtonStyleTheme.ROUNDED -> androidx.compose.foundation.shape.RoundedCornerShape(defaultRadius)
        ButtonStyleTheme.SHARP -> androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
        ButtonStyleTheme.CUT_CORNER -> androidx.compose.foundation.shape.CutCornerShape(defaultRadius)
    }
}


@Composable
fun getCardModifier(shape: androidx.compose.ui.graphics.Shape, elevation: androidx.compose.ui.unit.Dp = 2.dp): Modifier {
    return when (currentUiStyle) {
        UiStyleTheme.CLASSIC_ROUNDED -> Modifier.shadow(elevation, shape).border(0.5.dp, Color(0xFF49454F).copy(alpha = 0.3f), shape)
        UiStyleTheme.GLASSMORPHIC -> Modifier.border(1.dp, Color.White.copy(alpha = 0.15f), shape)
        UiStyleTheme.NEON_GLOW -> Modifier.border(1.5.dp, GoldAccent, shape).shadow(4.dp, shape, spotColor = GoldAccent, ambientColor = GoldAccent)
        UiStyleTheme.FLAT_MINIMAL -> Modifier.border(1.5.dp, TextPrimary, shape)
        UiStyleTheme.OBSIDIAN_SHARD -> Modifier.border(1.dp, Color.DarkGray, shape).shadow(6.dp, shape)
        UiStyleTheme.CHUNKY_ARCADE -> Modifier.border(3.dp, TextPrimary, shape).shadow(elevation = 8.dp, shape = shape, spotColor = Color.Black)
        UiStyleTheme.SOFT_ORGANIC -> Modifier.shadow(elevation + 4.dp, shape).border(0.5.dp, GoldAccent.copy(alpha=0.3f), shape)
    }
}

@Composable
fun getCardShape(defaultRadius: androidx.compose.ui.unit.Dp = 16.dp): androidx.compose.foundation.shape.CornerBasedShape {
    return when (currentUiStyle) {
        UiStyleTheme.CLASSIC_ROUNDED -> androidx.compose.foundation.shape.RoundedCornerShape(defaultRadius)
        UiStyleTheme.GLASSMORPHIC -> androidx.compose.foundation.shape.RoundedCornerShape(defaultRadius + 4.dp)
        UiStyleTheme.NEON_GLOW -> androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        UiStyleTheme.FLAT_MINIMAL -> androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
        UiStyleTheme.OBSIDIAN_SHARD -> androidx.compose.foundation.shape.CutCornerShape(defaultRadius - 2.dp)
        UiStyleTheme.CHUNKY_ARCADE -> androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
        UiStyleTheme.SOFT_ORGANIC -> androidx.compose.foundation.shape.RoundedCornerShape(defaultRadius + 16.dp)
    }
}

@Composable
fun getCardContainerColor(baseColor: Color = DarkCardBg): Color {
    return when (currentUiStyle) {
        UiStyleTheme.CLASSIC_ROUNDED -> baseColor
        UiStyleTheme.GLASSMORPHIC -> baseColor.copy(alpha = 0.65f)
        UiStyleTheme.NEON_GLOW -> baseColor
        UiStyleTheme.FLAT_MINIMAL -> baseColor
        UiStyleTheme.OBSIDIAN_SHARD -> baseColor.copy(alpha = 0.9f)
        UiStyleTheme.CHUNKY_ARCADE -> baseColor
        UiStyleTheme.SOFT_ORGANIC -> baseColor
    }
}

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

    var showSettingsDialog by remember { mutableStateOf(false) }
    var enableShakeAnimation by remember { mutableStateOf(true) }
    var enableHapticFeedback by remember { mutableStateOf(true) }
    var enableSoundEffects by remember { mutableStateOf(false) }
    var chosenAccentTheme by remember { mutableStateOf("violet") }

    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    LaunchedEffect(isRolling) {
        if (!isRolling && currentTotal != null && enableHapticFeedback) {
            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkSlateBg,
        contentColor = TextPrimary,
        bottomBar = {
            NavigationBar(
                containerColor = DarkCardBg,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .border(
                        width = 1.dp,
                        color = Color(0xFF49454F).copy(alpha = 0.4f),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                // Tab 1: Dice Sets
                NavigationBarItem(
                    selected = activeTab == "sets",
                    onClick = { activeTab = "sets" },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = "Dice Sets".t(),
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Dice Sets".t(), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkSlateBg,
                        selectedTextColor = GoldAccent,
                        indicatorColor = GoldAccent,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_sets")
                )

                // Tab 2: Quick Tray
                NavigationBarItem(
                    selected = activeTab == "quick",
                    onClick = { activeTab = "quick" },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Quick Tray".t(),
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Quick Tray".t(), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkSlateBg,
                        selectedTextColor = GoldAccent,
                        indicatorColor = GoldAccent,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_quick")
                )

                // Action: Custom Set (Direct Action, styled nicely as a central gold/violet button)
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        selectedDiceSetForEdit = null
                        showCreateDialog = true
                    },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(GoldAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Custom Set",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    label = { Text("Custom Set", fontSize = 11.sp, color = GoldAccent, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = GoldAccent,
                        unselectedTextColor = GoldAccent
                    ),
                    modifier = Modifier.testTag("nav_custom_set")
                )

                // Tab 3: Roll Log
                NavigationBarItem(
                    selected = activeTab == "history",
                    onClick = { activeTab = "history" },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Roll Log".t(),
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text("Roll Log".t(), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkSlateBg,
                        selectedTextColor = GoldAccent,
                        indicatorColor = GoldAccent,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_history")
                )

                // Action: Settings (Direct Action, styled nicely)
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        showSettingsDialog = true
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings".t(),
                            modifier = Modifier.size(22.dp),
                            tint = TextSecondary
                        )
                    },
                    label = { Text("Settings".t(), fontSize = 11.sp, color = TextSecondary) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_settings")
                )
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

    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
            enableShakeAnimation = enableShakeAnimation,
            onShakeAnimationToggled = { enableShakeAnimation = it },
            enableHapticFeedback = enableHapticFeedback,
            onHapticFeedbackToggled = { enableHapticFeedback = it },
            enableSoundEffects = enableSoundEffects,
            onSoundEffectsToggled = { enableSoundEffects = it },
            chosenAccentTheme = chosenAccentTheme,
            onAccentThemeChanged = { chosenAccentTheme = it }
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
            Triple("sets", "Dice Sets".t(), Icons.Default.Casino),
            Triple("quick", "Quick Tray".t(), Icons.Default.PlayArrow),
            Triple("history", "Roll Log".t(), Icons.Default.History)
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
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    color = contentColor,
                    fontSize = 11.sp,
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

    val trayShape = getCardShape(36.dp)
    val innerShape = getCardShape(22.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 260.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .then(getCardModifier(trayShape, 8.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DiceTrayGradientStart, DiceTrayGradientEnd)
                ),
                shape = trayShape
            )
            .padding(24.dp)
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
                    modifier = Modifier.padding(vertical = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .rotate(rotationAngle)
                            .background(
                                color = ActiveRollBackgroundColor,
                                shape = innerShape
                            )
                            .border(1.2.dp, ActiveRollContentColor, innerShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .size(60.dp)
                                .rotate(-rotationAngle)
                        ) {
                            drawD20Wireframe(ActiveRollContentColor, size.width)
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Rolling Dice...".t(),
                        color = DiceRollAnimationColor,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 17.sp
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
                            text = "ACTIVE ROLL".t(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiceRollResultColor.copy(alpha = 0.8f),
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = rollName.ifEmpty { "Custom Roll".t() },
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Formula: ".t() + formula,
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                            if (rollType != "NORMAL") {
                                Spacer(modifier = Modifier.width(8.dp))
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (rollType == "ADVANTAGE") AdvantageColor else DisadvantageColor
                                    ),
                                    shape = getButtonShape(),
                                    modifier = Modifier.padding(2.dp)
                                ) {
                                    Text(
                                        text = rollType.t(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Giant active die total result matching "Obsidian Shard".t() - made larger
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .rotate(45f)
                            .background(
                                color = ActiveRollBackgroundColor,
                                shape = innerShape
                            )
                            .border(1.2.dp, ActiveRollContentColor, innerShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = total.toString(),
                            color = ActiveRollContentColor,
                            fontSize = 42.sp,
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
                    text = "DICE CAST".t(),
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
                // Empty Tray State - made larger and more visually inviting
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = TextSecondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "The Dice Tray is Empty".t(),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Choose a dice set or quick roll below to cast!".t(),
                        color = TextSecondary,
                        fontSize = 13.sp,
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
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(results) { (sides, valRolled) ->
            Box(
                modifier = Modifier
                    .width(72.dp)
                    .background(DarkSlateBg, RoundedCornerShape(12.dp))
                    .border(0.5.dp, Color(0xFF3C3C46), RoundedCornerShape(12.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Custom drawn D&D die - made larger
                    DiceShape(sides = sides, value = valRolled, color = getDieColor(sides), modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = valRolled.toString(),
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
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
                Text(text = "No Custom Dice Sets".t(), color = TextPrimary, fontWeight = FontWeight.Bold)
                Text(text = "Create custom roll collections using the '+' button.".t(), color = TextSecondary, fontSize = 12.sp)
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

                val cardShape = getCardShape(16.dp)
                Card(
                    colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkCardBg)),
                    shape = cardShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(getCardModifier(cardShape, 2.dp))
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
                                        contentDescription = "Menu".t(),
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
                                            text = { Text("Delete Set".t(), color = CrimsonAccent) },
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
                                        containerColor = AdvantageColor,
                                        contentColor = Color.White
                                    ),
                                    shape = getButtonShape(),
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
                                    contentColor = Color.White
                                ),
                                    shape = getButtonShape(),
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
                                Text("Roll".t(), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            if (hasD20) {
                                Button(
                                    onClick = { onRoll(set, "DISADVANTAGE") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = DisadvantageColor,
                                        contentColor = Color.White
                                    ),
                                    shape = getButtonShape(),
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

    var selectedRollType by remember { mutableStateOf("NORMAL") } // "NORMAL".t(), "ADVANTAGE".t(), "DISADVANTAGE".t()

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
                text = "Tap dice to load up your quick roll collection.".t(),
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

            val diceSelectShape = getCardShape(16.dp)
            Card(
                colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkCardBg)),
                shape = diceSelectShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(getCardModifier(diceSelectShape, 2.dp))
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
                                                    color = Color.White,
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
            val modShape = getCardShape(16.dp)
            Card(
                colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkCardBg)),
                shape = modShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(getCardModifier(modShape, 2.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "STATIC MODIFIER".t(),
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
                            shape = getButtonShape()
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
                            shape = getButtonShape()
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
                val rulesShape = getCardShape(16.dp)
                Card(
                    colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkCardBg)),
                    shape = rulesShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(getCardModifier(rulesShape, 2.dp))
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
                                    "ADVANTAGE" -> AdvantageColor
                                    "DISADVANTAGE" -> DisadvantageColor
                                    else -> GoldAccent
                                }

                                Button(
                                    onClick = { selectedRollType = opt },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isChosen) chosenColor else DarkSlateBg,
                                        contentColor = if (isChosen) Color.White else TextSecondary
                                    ),
                                    shape = getButtonShape(),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = opt.t(),
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
                        selectedRollType = "NORMAL".t()
                    },
                    border = BorderStroke(1.dp, Color(0xFF32323C)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonAccent),
                    shape = getButtonShape(),
                    modifier = Modifier.height(48.dp),
                    enabled = hasSelectedDice || modifierValue != 0
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Clear".t())
                }

                // Roll Button
                Button(
                    shape = getButtonShape(),
                    onClick = {
                        onRoll(
                            "Quick Tray Roll".t(),
                            d4Count,
                            d6Count,
                            d8Count,
                            d10Count,
                            d12Count,
                            d20Count,
                            d100Count,
                            modifierValue,
                            if (d20Count > 0) selectedRollType else "NORMAL".t()
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("roll_tray_button"),
                    enabled = hasSelectedDice || modifierValue != 0
                ) {
                    Icon(imageVector = Icons.Default.Casino, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "ROLL TRAY".t(), fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                text = "LOGGED HISTORY".t(),
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp
            )
            if (rollLogs.isNotEmpty()) {
                TextButton(onClick = { showConfirmClear = true },
                                    shape = getButtonShape()
                                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = CrimsonAccent)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Clear Logs".t(), color = CrimsonAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                    Text(text = "No Rolls Recorded".t(), color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text(text = "Roll any dice set to record the logs.".t(), color = TextSecondary, fontSize = 12.sp)
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
                    val logShape = getCardShape(12.dp)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkCardBg)),
                        shape = logShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(getCardModifier(logShape, 2.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = log.name.ifEmpty { "Quick Roll".t() },
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
                                        text = "Rolls: ".t(),
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
            title = { Text("Clear Log History?".t(), color = TextPrimary) },
            text = { Text("This will permanently delete all historic roll logs from the database.".t(), color = TextSecondary) },
            containerColor = DarkCardBg,
            dismissButton = {
                TextButton(onClick = { showConfirmClear = false },
                                    shape = getButtonShape()
                                ) {
                    Text("Cancel".t(), color = TextSecondary)
                }
            },
            confirmButton = {
                TextButton(
                    shape = getButtonShape(),
                    onClick = {
                    onClearHistory()
                    showConfirmClear = false
                }) {
                    Text("Clear All".t(), color = CrimsonAccent, fontWeight = FontWeight.Bold)
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
        val dialogShape = getCardShape(20.dp)
        Card(
            colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkCardBg)),
            shape = dialogShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .then(getCardModifier(dialogShape, 12.dp))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = if (diceSet == null) "CREATE CUSTOM SET".t() else "EDIT DICE SET".t(),
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
                            label = { Text("Set Name (e.g., Fireball Lvl 3)".t()) },
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
                            label = { Text("Short Description".t()) },
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
                        text = "SELECT DICE QUANTITIES".t(),
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
                        text = "ADD MODIFIER".t(),
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
                            shape = getButtonShape(),
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
                            shape = getButtonShape(),
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
                        text = "THEME COLOR".t(),
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
                                        contentDescription = "Selected".t(),
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
                            shape = getButtonShape(),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel".t())
                        }

                        Button(
                            shape = getButtonShape(),
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
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = Color.White),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("save_set_button"),
                            enabled = name.isNotEmpty() && hasAnyDice
                        ) {
                            Text("Save Set".t(), fontWeight = FontWeight.Bold)
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
        4 -> D4Color
        6 -> D6Color
        8 -> D8Color
        10 -> D10Color
        12 -> D12Color
        20 -> D20Color
        100 -> D100Color
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
fun DiceShape(sides: Int, value: Int, color: Color, modifier: Modifier = Modifier.size(40.dp)) {
    Canvas(modifier = modifier) {
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
            10, 100 -> {
                val pCenter = Offset(centerX, centerY - radius * 0.2f)
                val p1 = Offset(centerX - radius * 0.4f, centerY + radius * 0.1f)
                val p2 = Offset(centerX + radius * 0.4f, centerY + radius * 0.1f)
                val top = Offset(centerX, centerY - radius)
                val bottom = Offset(centerX, centerY + radius)
                val left = Offset(centerX - radius * 0.8f, centerY - radius * 0.1f)
                val right = Offset(centerX + radius * 0.8f, centerY - radius * 0.1f)
                
                drawLine(color, left, p1, strokeWidth = 0.8.dp.toPx())
                drawLine(color, p1, pCenter, strokeWidth = 0.8.dp.toPx())
                drawLine(color, pCenter, p2, strokeWidth = 0.8.dp.toPx())
                drawLine(color, p2, right, strokeWidth = 0.8.dp.toPx())
                
                drawLine(color, top, pCenter, strokeWidth = 0.8.dp.toPx())
                drawLine(color, bottom, p1, strokeWidth = 0.8.dp.toPx())
                drawLine(color, bottom, p2, strokeWidth = 0.8.dp.toPx())
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
                
                for (i in 0 until 5) {
                    val angleInner = Math.toRadians((i * 72 - 90 + 36).toDouble())
                    val xInner = centerX + innerRadius * Math.cos(angleInner).toFloat()
                    val yInner = centerY + innerRadius * Math.sin(angleInner).toFloat()
                    
                    val angleOuter1 = Math.toRadians((i * 72 - 90).toDouble())
                    val xOuter1 = centerX + radius * Math.cos(angleOuter1).toFloat()
                    val yOuter1 = centerY + radius * Math.sin(angleOuter1).toFloat()
                    
                    val angleOuter2 = Math.toRadians((((i + 1) % 5) * 72 - 90).toDouble())
                    val xOuter2 = centerX + radius * Math.cos(angleOuter2).toFloat()
                    val yOuter2 = centerY + radius * Math.sin(angleOuter2).toFloat()
                    
                    drawLine(color, Offset(xInner, yInner), Offset(xOuter1, yOuter1), strokeWidth = 0.8.dp.toPx())
                    drawLine(color, Offset(xInner, yInner), Offset(xOuter2, yOuter2), strokeWidth = 0.8.dp.toPx())
                }
            }
        }
    }


}

private fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
    val h = hue / 60f
    val c = value * saturation
    val x = c * (1f - kotlin.math.abs((h % 2f) - 1f))
    val m = value - c

    val (r, g, b) = when {
        h < 1f -> Triple(c, x, 0f)
        h < 2f -> Triple(x, c, 0f)
        h < 3f -> Triple(0f, c, x)
        h < 4f -> Triple(0f, x, c)
        h < 5f -> Triple(x, 0f, c)
        h <= 6f -> Triple(c, 0f, x)
        else -> Triple(0f, 0f, 0f)
    }
    return Color(
        red = (r + m).coerceIn(0f, 1f),
        green = (g + m).coerceIn(0f, 1f),
        blue = (b + m).coerceIn(0f, 1f),
        alpha = 1f
    )
}

private fun colorToHsv(color: Color): Triple<Float, Float, Float> {
    val r = color.red
    val g = color.green
    val b = color.blue

    val max = maxOf(r, g, b)
    val min = minOf(r, g, b)
    val delta = max - min

    val h = when {
        delta == 0f -> 0f
        max == r -> ((g - b) / delta % 6f) * 60f
        max == g -> ((b - r) / delta + 2f) * 60f
        else -> ((r - g) / delta + 4f) * 60f
    }
    val hue = if (h < 0) h + 360f else h
    val s = if (max == 0f) 0f else delta / max
    val v = max

    return Triple(hue, s, v)
}

@Composable
private fun PremiumColorPicker(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val (initialHue, initialSat, initialVal) = remember(currentColor) { colorToHsv(currentColor) }
    
    var hue by remember(initialHue) { mutableStateOf(initialHue) }
    var saturation by remember(initialSat) { mutableStateOf(initialSat) }
    var value by remember(initialVal) { mutableStateOf(initialVal) }

    var hexInput by remember(currentColor) {
        val hex = "%02X%02X%02X".format(
            (currentColor.red * 255).toInt(),
            (currentColor.green * 255).toInt(),
            (currentColor.blue * 255).toInt()
        )
        mutableStateOf(hex)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Rainbow Hue Slider track background
        val hueTrackBrush = Brush.horizontalGradient(
            listOf(
                Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red
            )
        )
        
        // Saturation track background (from white to fully saturated color at current hue)
        val satTrackBrush = remember(hue, value) {
            Brush.horizontalGradient(
                listOf(Color.White, hsvToColor(hue, 1.0f, value))
            )
        }
        
        // Value track background (from black to fully bright color at current hue/sat)
        val valTrackBrush = remember(hue, saturation) {
            Brush.horizontalGradient(
                listOf(Color.Black, hsvToColor(hue, saturation, 1.0f))
            )
        }

        // --- HUE CONTROL ---
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Hue".t(), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("${hue.toInt()}°", color = GoldAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(hueTrackBrush)
            )
            Slider(
                value = hue,
                onValueChange = {
                    hue = it
                    onColorSelected(hsvToColor(hue, saturation, value))
                },
                valueRange = 0f..360f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .offset(y = (-7).dp)
            )
        }

        // --- SATURATION CONTROL ---
        Column(modifier = Modifier.offset(y = (-8).dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Saturation".t(), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("${(saturation * 100).toInt()}%", color = GoldAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(satTrackBrush)
            )
            Slider(
                value = saturation,
                onValueChange = {
                    saturation = it
                    onColorSelected(hsvToColor(hue, saturation, value))
                },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .offset(y = (-7).dp)
            )
        }

        // --- VALUE/BRIGHTNESS CONTROL ---
        Column(modifier = Modifier.offset(y = (-16).dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Brightness".t(), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("${(value * 100).toInt()}%", color = GoldAccent, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(valTrackBrush)
            )
            Slider(
                value = value,
                onValueChange = {
                    value = it
                    onColorSelected(hsvToColor(hue, saturation, value))
                },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .offset(y = (-7).dp)
            )
        }

        // --- HEX TEXT INPUT ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Hex Code:".t(), color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            BasicTextField(
                value = hexInput,
                onValueChange = { newValue ->
                    val clean = newValue.uppercase().filter { it in "0123456789ABCDEF" }.take(6)
                    hexInput = clean
                    if (clean.length == 6) {
                        try {
                            val r = clean.substring(0, 2).toInt(16) / 255f
                            val g = clean.substring(2, 4).toInt(16) / 255f
                            val b = clean.substring(4, 6).toInt(16) / 255f
                            onColorSelected(Color(r, g, b))
                        } catch (e: Exception) {
                            // ignore
                        }
                    }
                },
                textStyle = TextStyle(color = TextPrimary, fontSize = 12.sp, fontFamily = FontFamily.Monospace),
                cursorBrush = SolidColor(GoldAccent),
                modifier = Modifier
                    .background(DarkSlateBg, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(80.dp)
                    .border(0.5.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
                singleLine = true
            )
        }
    }
}

private data class ThemePreset(
    val name: String,
    val description: String,
    val bg: Color,
    val cardBg: Color,
    val accent: Color,
    val darkAccent: Color,
    val secondaryAccent: Color,
    val errorAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val diceColor: Color = accent,
    val uiStyle: UiStyleTheme = UiStyleTheme.CLASSIC_ROUNDED,
    val buttonStyle: ButtonStyleTheme = ButtonStyleTheme.PILL,
    val advantageColor: Color = secondaryAccent,
    val disadvantageColor: Color = errorAccent,
    val diceRollResultColor: Color = diceColor,
    val diceRollAnimationColor: Color = diceColor,
    val d4Color: Color = diceColor,
    val d6Color: Color = diceColor,
    val d8Color: Color = diceColor,
    val d10Color: Color = diceColor,
    val d12Color: Color = diceColor,
    val d20Color: Color = diceColor,
    val d100Color: Color = diceColor,
    val diceTrayGradientStart: Color = cardBg,
    val diceTrayGradientEnd: Color = bg,
    val activeRollBackgroundColor: Color = diceColor,
    val activeRollContentColor: Color = bg
)

private val customThemePresets = mutableStateListOf<ThemePreset>()

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    enableShakeAnimation: Boolean,
    onShakeAnimationToggled: (Boolean) -> Unit,
    enableHapticFeedback: Boolean,
    onHapticFeedbackToggled: (Boolean) -> Unit,
    enableSoundEffects: Boolean,
    onSoundEffectsToggled: (Boolean) -> Unit,
    chosenAccentTheme: String,
    onAccentThemeChanged: (String) -> Unit
) {
    val presets = listOf(
        ThemePreset(
            name = "Pen and Paper".t(),
            description = "High contrast ink & white".t(),
            bg = Color(0xFFFFFFFF),
            cardBg = Color(0xFFF3F4F6),
            accent = Color(0xFF111827),
            darkAccent = Color(0xFF374151),
            secondaryAccent = Color(0xFF4B5563),
            errorAccent = Color(0xFFDC2626),
            textPrimary = Color(0xFF111827),
            textSecondary = Color(0xFF4B5563),
            uiStyle = UiStyleTheme.FLAT_MINIMAL,
            buttonStyle = ButtonStyleTheme.SHARP
        ),
        ThemePreset(
            name = "Obsidian Violet".t(),
            description = "Muted cosmic dark".t(),
            bg = Color(0xFF1C1B1F),
            cardBg = Color(0xFF2B2930),
            accent = Color(0xFF7E57C2),
            darkAccent = Color(0xFF311B92),
            secondaryAccent = Color(0xFF4527A0),
            errorAccent = Color(0xFFD81B60),
            textPrimary = Color(0xFFE6E1E5),
            textSecondary = Color(0xFFCAC4D0),
            diceColor = Color(0xFFFFFFFF),
            diceRollResultColor = Color(0xFF7E57C2),
            diceRollAnimationColor = Color(0xFF7E57C2),
            activeRollBackgroundColor = Color(0xFF7E57C2),
            uiStyle = UiStyleTheme.GLASSMORPHIC,
            buttonStyle = ButtonStyleTheme.PILL
        ),
        ThemePreset(
            name = "Metal".t(),
            description = "Metal greyscale".t(),
            bg = Color(0xFF141414),
            cardBg = Color(0xFF262626),
            accent = Color(0xFFB0B0B0),
            darkAccent = Color(0xFF424242),
            secondaryAccent = Color(0xFF757575),
            errorAccent = Color(0xFFD32F2F),
            textPrimary = Color(0xFFF5F5F5),
            textSecondary = Color(0xFF9E9E9E),
            uiStyle = UiStyleTheme.OBSIDIAN_SHARD,
            buttonStyle = ButtonStyleTheme.CUT_CORNER
        )
    )

    val colorOptions = listOf(
        Color(0xFFFFFFFF) to "Pure White",
        Color(0xFF121212) to "Ink Black",
        Color(0xFF1C1B1F) to "Obsidian Black",
        Color(0xFF121214) to "Midnight Pitch",
        Color(0xFF0B140D) to "Deep Forest",
        Color(0xFF0A1118) to "Abyssal Blue",
        Color(0xFF1A0A0A) to "Blood Crimson",
        Color(0xFF2B2930) to "Slate Graphite",
        Color(0xFF1E1E22) to "Dungeon Grey",
        Color(0xFF7E57C2) to "Deep Violet",
        Color(0xFFFFB300) to "Gold Ore",
        Color(0xFF81C784) to "Elf Green",
        Color(0xFF4DD0E1) to "Mana Cyan",
        Color(0xFFFF8A80) to "Rage Coral",
        Color(0xFFF2B8B5) to "Soft Red",
        Color(0xFFE8DEF8) to "Lavender Mist",
        Color(0xFFCAC4D0) to "Silver Shield",
        Color(0xFFE6E1E5) to "Ivory Silk",
        Color(0xFF311B92) to "Royal Purple",
        Color(0xFFC59B27) to "Burnished Bronze",
        Color(0xFF2E7D32) to "Earth Green",
        Color(0xFF00838F) to "Deep Ocean",
        Color(0xFFC62828) to "Vampire Crimson",
        Color(0xFFE0F7FA) to "Ice Diamond",
        Color(0xFFFF5722) to "Deep Orange",
        Color(0xFF9C27B0) to "Classic Purple",
        Color(0xFF00BCD4) to "Teal Accent"
    )

    var expandedElementIndex by remember { mutableStateOf<Int?>(null) }
    var selectedTabIndex by remember { mutableStateOf(0) } // 0: Presets, 1: Custom colors, 2: Preferences
    var showSavePresetDialog by remember { mutableStateOf(false) }
    var newPresetName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        val settingsShape = getCardShape(24.dp)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(8.dp)
                .then(getCardModifier(settingsShape, 12.dp)),
            colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkCardBg)),
            shape = settingsShape
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header (non-scrollable)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Settings".t(),
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider(color = Color(0xFF49454F).copy(alpha = 0.3f), modifier = Modifier.padding(bottom = 12.dp))

                // Custom Material Segmented Tab Option Picker
                val tabs = listOf(
                    Triple("🎨 Presets".t(), 0, null as androidx.compose.ui.graphics.vector.ImageVector?),
                    Triple("🔧 Colorizer".t(), 1, null as androidx.compose.ui.graphics.vector.ImageVector?),
                    Triple("⚙️ Stylizer".t(), 2, null as androidx.compose.ui.graphics.vector.ImageVector?),
                    Triple("🌐 Language".t(), 3, null as androidx.compose.ui.graphics.vector.ImageVector?)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSlateBg.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    tabs.forEach { (title, index, icon) ->
                        val isSelected = selectedTabIndex == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GoldAccent else Color.Transparent)
                                .clickable { selectedTabIndex = index }
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (icon != null) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSelected) DarkSlateBg else TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = title.replace(Regex("^.*?\\s"), ""), // Skip emoji for cleaner UI
                                    color = if (isSelected) DarkSlateBg else TextPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable contents
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    when (selectedTabIndex) {
                        0 -> {
                            // PRESET THEMES SECTION
                            item {
                                Text(
                                    text = "TAP TO ACTIVATE PRESET".t(),
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Each theme customizes the layout, buttons, accent glow, and texts:".t(),
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            items(presets + customThemePresets) { preset ->
                                val isCustom = customThemePresets.contains(preset)
                                val isSelected = DarkSlateBg == preset.bg && GoldAccent == preset.accent
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            DarkSlateBg = preset.bg
                                            DarkCardBg = preset.cardBg
                                            GoldAccent = preset.accent
                                            DarkGoldAccent = preset.darkAccent
                                            AmberGlow = preset.secondaryAccent
                                            CrimsonAccent = preset.errorAccent
                                            TextPrimary = preset.textPrimary
                                            TextSecondary = preset.textSecondary
                                            DiceTrayGradientStart = preset.diceTrayGradientStart
                                            DiceTrayGradientEnd = preset.diceTrayGradientEnd
                                            AdvantageColor = preset.advantageColor
                                            DisadvantageColor = preset.disadvantageColor
                                            DiceRollResultColor = preset.diceRollResultColor
                                            DiceRollAnimationColor = preset.diceRollAnimationColor
                                            D4Color = preset.d4Color
                                            D6Color = preset.d6Color
                                            D8Color = preset.d8Color
                                            D10Color = preset.d10Color
                                            D12Color = preset.d12Color
                                            D20Color = preset.d20Color
                                            D100Color = preset.d100Color
                                            ActiveRollBackgroundColor = preset.activeRollBackgroundColor
                                            ActiveRollContentColor = preset.activeRollContentColor
                                            currentUiStyle = preset.uiStyle
                                            currentButtonStyle = preset.buttonStyle
                                        }
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) preset.accent else preset.accent.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = preset.cardBg
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = preset.name,
                                                    color = preset.textPrimary,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                if (isSelected) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .background(preset.accent.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = "Active".t(),
                                                            color = preset.accent,
                                                            fontSize = 9.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                            Text(
                                                text = preset.description,
                                                color = preset.textSecondary,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }

                                        // Colors Row Preview
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(modifier = Modifier.size(16.dp).background(preset.bg, CircleShape).border(0.5.dp, Color.White, CircleShape))
                                            Box(modifier = Modifier.size(16.dp).background(preset.cardBg, CircleShape).border(0.5.dp, Color.White, CircleShape))
                                            Box(modifier = Modifier.size(16.dp).background(preset.accent, CircleShape).border(0.5.dp, Color.White, CircleShape))
                                            if (isCustom) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                IconButton(onClick = { customThemePresets.remove(preset) }, modifier = Modifier.size(24.dp)) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Delete".t(), tint = preset.errorAccent, modifier = Modifier.size(16.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = { showSavePresetDialog = true },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    shape = getButtonShape(),
                                    border = BorderStroke(1.dp, GoldAccent),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save Current Style as Custom Preset".t(), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                        1 -> {
                            // GRANULAR UI ELEMENT CUSTOMIZER
                            item {
                                Text(
                                    text = "GRANULAR ELEMENT COLORS".t(),
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Tap any row to open the interactive Color Picker, or use the quick swatches below:".t(),
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            val elementsList = listOf(
                                Triple("App Background".t(), DarkSlateBg) { c: Color -> DarkSlateBg = c },
                                Triple("Panel / Card Background".t(), DarkCardBg) { c: Color -> DarkCardBg = c },
                                Triple("Highlight / D20 Accent".t(), GoldAccent) { c: Color -> GoldAccent = c },
                                Triple("Button Fill / Container Accent".t(), DarkGoldAccent) { c: Color -> DarkGoldAccent = c },
                                Triple("Secondary highlights".t(), AmberGlow) { c: Color -> AmberGlow = c },
                                Triple("Rage / Warning Accent".t(), CrimsonAccent) { c: Color -> CrimsonAccent = c },
                                Triple("Primary Text".t(), TextPrimary) { c: Color -> TextPrimary = c },
                                Triple("Secondary Text".t(), TextSecondary) { c: Color -> TextSecondary = c },
                                Triple("Advantage Button Color".t(), AdvantageColor) { c: Color -> AdvantageColor = c },
                                Triple("Disadvantage Button Color".t(), DisadvantageColor) { c: Color -> DisadvantageColor = c },
                                Triple("Dice Roll Result Color".t(), DiceRollResultColor) { c: Color -> DiceRollResultColor = c },
                                Triple("Dice Roll Animation Color".t(), DiceRollAnimationColor) { c: Color -> DiceRollAnimationColor = c },
                                Triple("d4 Color".t(), D4Color) { c: Color -> D4Color = c },
                                Triple("d6 Color".t(), D6Color) { c: Color -> D6Color = c },
                                Triple("d8 Color".t(), D8Color) { c: Color -> D8Color = c },
                                Triple("d10 Color".t(), D10Color) { c: Color -> D10Color = c },
                                Triple("d12 Color".t(), D12Color) { c: Color -> D12Color = c },
                                Triple("d20 Color".t(), D20Color) { c: Color -> D20Color = c },
                                Triple("d100 Color".t(), D100Color) { c: Color -> D100Color = c },
                                Triple("Dice Tray Gradient Top".t(), DiceTrayGradientStart) { c: Color -> DiceTrayGradientStart = c },
                                Triple("Dice Tray Gradient Bottom".t(), DiceTrayGradientEnd) { c: Color -> DiceTrayGradientEnd = c },
                                Triple("Active Roll Background".t(), ActiveRollBackgroundColor) { c: Color -> ActiveRollBackgroundColor = c },
                                Triple("Active Roll Content Color".t(), ActiveRollContentColor) { c: Color -> ActiveRollContentColor = c }
                            )

                            elementsList.forEachIndexed { index, (label, currentColor, updateFn) ->
                                item {
                                    val isExpanded = expandedElementIndex == index
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(DarkSlateBg.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                            .border(
                                                width = if (isExpanded) 1.dp else 0.5.dp,
                                                color = if (isExpanded) GoldAccent else Color(0xFF49454F).copy(alpha = 0.3f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                expandedElementIndex = if (isExpanded) null else index
                                            }
                                            .padding(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = label,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .background(currentColor, CircleShape)
                                                        .border(1.dp, TextSecondary, CircleShape)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                val hexString = "#%02X%02X%02X".format(
                                                    (currentColor.red * 255).toInt(),
                                                    (currentColor.green * 255).toInt(),
                                                    (currentColor.blue * 255).toInt()
                                                )
                                                Text(
                                                    text = hexString,
                                                    color = TextSecondary,
                                                    fontSize = 10.sp,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Icon(
                                                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                    contentDescription = "Toggle color picker".t(),
                                                    tint = TextSecondary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }

                                        if (isExpanded) {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            HorizontalDivider(color = Color(0xFF49454F).copy(alpha = 0.2f))
                                            Spacer(modifier = Modifier.height(12.dp))
                                            PremiumColorPicker(
                                                currentColor = currentColor,
                                                onColorSelected = updateFn
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            // Quick swatches inline when collapsed
                                            androidx.compose.foundation.lazy.LazyRow(
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                contentPadding = PaddingValues(horizontal = 2.dp)
                                            ) {
                                                items(colorOptions) { (col, colName) ->
                                                    val isSwatched = col == currentColor
                                                    Box(
                                                        modifier = Modifier
                                                            .size(22.dp)
                                                            .background(col, CircleShape)
                                                            .border(
                                                                width = if (isSwatched) 1.5.dp else 0.5.dp,
                                                                color = if (isSwatched) GoldAccent else Color.White.copy(alpha = 0.4f),
                                                                shape = CircleShape
                                                            )
                                                            .clickable { updateFn(col) }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        2 -> {
                            // UI STYLE THEME SECTION
                            item {
                                Text(
                                    text = "UI VISUAL STYLE".t(),
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Transform the structural aesthetic of cards, containers, and borders:".t(),
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            item {
                                val styles = listOf(
                                    Triple(UiStyleTheme.CLASSIC_ROUNDED, "Classic Rounded".t(), "Elegant Material curves & subtle shadows".t()),
                                    Triple(UiStyleTheme.GLASSMORPHIC, "Glassmorphic".t(), "Translucent glass panels with frosted borders".t()),
                                    Triple(UiStyleTheme.NEON_GLOW, "Cyber Glow", "Sharp glowing tech borders & neon sci-fi halo".t()),
                                    Triple(UiStyleTheme.FLAT_MINIMAL, "Flat Minimalist".t(), "Stark, crisp flat 2D retro design".t()),
                                    Triple(UiStyleTheme.OBSIDIAN_SHARD, "Obsidian Shard".t(), "Hard angled cuts resembling carved stone".t()),
                                    Triple(UiStyleTheme.CHUNKY_ARCADE, "Chunky Arcade".t(), "Thick bold borders with heavy drop shadows".t()),
                                    Triple(UiStyleTheme.SOFT_ORGANIC, "Soft Organic".t(), "Highly pill-shaped friendly curves".t())
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    styles.forEach { (style, name, desc) ->
                                        val isSelected = currentUiStyle == style
                                        val cardShape = RoundedCornerShape(12.dp)
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { currentUiStyle = style }
                                                .border(
                                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                                    color = if (isSelected) GoldAccent else Color(0xFF49454F).copy(alpha = 0.3f),
                                                    shape = cardShape
                                                ),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSelected) DarkSlateBg.copy(alpha = 0.6f) else DarkSlateBg.copy(alpha = 0.2f)
                                            ),
                                            shape = cardShape
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = { currentUiStyle = style },
                                                    colors = RadioButtonDefaults.colors(
                                                        selectedColor = GoldAccent,
                                                        unselectedColor = TextSecondary
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column {
                                                    Text(
                                                        text = name,
                                                        color = TextPrimary,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = desc,
                                                        color = TextSecondary,
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = Color(0xFF49454F).copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            item {
                                Text(
                                    text = "BUTTON STYLE AESTHETIC".t(),
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Choose the shape and curvature of all action buttons:".t(),
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            
                            item {
                                val buttonStyles = listOf(
                                    Triple(ButtonStyleTheme.PILL, "Pill Shaped".t(), "Fully rounded soft edges".t()),
                                    Triple(ButtonStyleTheme.ROUNDED, "Rounded Corner".t(), "Standard subtle rounded corners".t()),
                                    Triple(ButtonStyleTheme.SHARP, "Sharp Rectangle".t(), "Hard 90-degree corners".t()),
                                    Triple(ButtonStyleTheme.CUT_CORNER, "Cut Corners".t(), "Sci-fi chamfered edges".t())
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    buttonStyles.forEach { (style, name, desc) ->
                                        val isSelected = currentButtonStyle == style
                                        val cardShape = RoundedCornerShape(12.dp)
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { currentButtonStyle = style }
                                                .border(
                                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                                    color = if (isSelected) GoldAccent else Color(0xFF49454F).copy(alpha = 0.3f),
                                                    shape = cardShape
                                                ),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSelected) DarkSlateBg.copy(alpha = 0.6f) else DarkSlateBg.copy(alpha = 0.2f)
                                            ),
                                            shape = cardShape
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = { currentButtonStyle = style },
                                                    colors = RadioButtonDefaults.colors(
                                                        selectedColor = GoldAccent,
                                                        unselectedColor = TextSecondary
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Column {
                                                    Text(
                                                        text = name,
                                                        color = TextPrimary,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = desc,
                                                        color = TextSecondary,
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = Color(0xFF49454F).copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            // STANDARD FUNCTIONAL SETTINGS
                            item {
                                Text(
                                    text = "USER PREFERENCES".t(),
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Toggle game mechanisms and haptics for custom rolling suspense:".t(),
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            // Setting 1: Roll Delay & Shake Animation
                            item {
                                val cardShape = getCardShape(12.dp)
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .then(getCardModifier(cardShape, 1.dp)),
                                    colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkSlateBg.copy(alpha = 0.4f))),
                                    shape = cardShape
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = null,
                                                tint = GoldAccent,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = "Dice Rolling Delay".t(),
                                                    color = TextPrimary,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    text = "Play 600ms roll delay for suspense".t(),
                                                    color = TextSecondary,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        Switch(
                                            checked = enableShakeAnimation,
                                            onCheckedChange = onShakeAnimationToggled,
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = DarkSlateBg,
                                                checkedTrackColor = GoldAccent,
                                                uncheckedThumbColor = TextSecondary,
                                                uncheckedTrackColor = DarkSlateBg.copy(alpha = 0.8f)
                                            )
                                        )
                                    }
                                }
                            }

                            // Setting 2: Sound Effects
                            item {
                                val cardShape = getCardShape(12.dp)
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .then(getCardModifier(cardShape, 1.dp)),
                                    colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkSlateBg.copy(alpha = 0.4f))),
                                    shape = cardShape
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = null,
                                                tint = GoldAccent,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = "Rolling Sound Effects".t(),
                                                    color = TextPrimary,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    text = "Play fantasy sound effects when rolling".t(),
                                                    color = TextSecondary,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        Switch(
                                            checked = enableSoundEffects,
                                            onCheckedChange = onSoundEffectsToggled,
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = DarkSlateBg,
                                                checkedTrackColor = GoldAccent,
                                                uncheckedThumbColor = TextSecondary,
                                                uncheckedTrackColor = DarkSlateBg.copy(alpha = 0.8f)
                                            )
                                        )
                                    }
                                }
                            }

                            // Setting 3: Haptic Feedback
                            item {
                                val cardShape = getCardShape(12.dp)
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .then(getCardModifier(cardShape, 1.dp)),
                                    colors = CardDefaults.cardColors(containerColor = getCardContainerColor(DarkSlateBg.copy(alpha = 0.4f))),
                                    shape = cardShape
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = GoldAccent,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = "Haptic Vibration".t(),
                                                    color = TextPrimary,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    text = "Vibrate device when dice stop rolling".t(),
                                                    color = TextSecondary,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        Switch(
                                            checked = enableHapticFeedback,
                                            onCheckedChange = onHapticFeedbackToggled,
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = DarkSlateBg,
                                                checkedTrackColor = GoldAccent,
                                                uncheckedThumbColor = TextSecondary,
                                                uncheckedTrackColor = DarkSlateBg.copy(alpha = 0.8f)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        3 -> {
                            // LANGUAGE SECTION
                            item {
                                Text(
                                    text = "LANGUAGE".t(),
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Select your preferred app language:".t(),
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            
                            val languages = listOf("English", "Français")
                            items(languages) { lang ->
                                val isSelected = currentLanguage == lang
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { currentLanguage = lang }
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) GoldAccent else Color(0xFF49454F).copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) GoldAccent.copy(alpha = 0.1f) else DarkCardBg
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = lang.t(),
                                            color = if (isSelected) GoldAccent else TextPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected".t(),
                                                tint = GoldAccent,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    shape = getButtonShape(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Close Settings".t(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }



    if (showSavePresetDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSavePresetDialog = false },
            title = { Text("Save Custom Preset".t(), color = TextPrimary) },
            text = {
                Column {
                    Text("Enter a name for your custom preset style:".t(), color = TextSecondary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newPresetName,
                        onValueChange = { newPresetName = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = Color(0xFF49454F).copy(alpha = 0.5f)
                        ),
                        placeholder = { Text("My Custom Style".t(), color = TextSecondary.copy(alpha = 0.5f)) }
                    )
                }
            },
            containerColor = DarkCardBg,
            dismissButton = {
                TextButton(onClick = { showSavePresetDialog = false }, shape = getButtonShape()) {
                    Text("Cancel".t(), color = TextSecondary)
                }
            },
            confirmButton = {
                Button(
                    shape = getButtonShape(),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = Color.White),
                    enabled = newPresetName.isNotBlank(),
                    onClick = {
                        customThemePresets.add(
                            ThemePreset(
                                name = newPresetName,
                                description = "Custom User Style",
                                bg = DarkSlateBg,
                                cardBg = DarkCardBg,
                                accent = GoldAccent,
                                darkAccent = DarkGoldAccent,
                                secondaryAccent = AmberGlow,
                                errorAccent = CrimsonAccent,
                                textPrimary = TextPrimary,
                                textSecondary = TextSecondary,
                                diceColor = DiceRollResultColor,
                                uiStyle = currentUiStyle,
                                buttonStyle = currentButtonStyle,
                                advantageColor = AdvantageColor,
                                disadvantageColor = DisadvantageColor,
                                diceRollResultColor = DiceRollResultColor,
                                diceRollAnimationColor = DiceRollAnimationColor,
                                d4Color = D4Color,
                                d6Color = D6Color,
                                d8Color = D8Color,
                                d10Color = D10Color,
                                d12Color = D12Color,
                                d20Color = D20Color,
                                d100Color = D100Color,
                                diceTrayGradientStart = DiceTrayGradientStart,
                                diceTrayGradientEnd = DiceTrayGradientEnd,
                                activeRollBackgroundColor = ActiveRollBackgroundColor,
                                activeRollContentColor = ActiveRollContentColor
                            )
                        )
                        newPresetName = ""
                        showSavePresetDialog = false
                    }
                ) {
                    Text("Save".t(), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
