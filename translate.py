import re
import json

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

# We need to find all English strings to translate
strings_to_translate = [
    "Dice Sets", "Quick Tray", "Roll Log", "🎨 Presets", "🔧 Colorizer", "⚙️ Stylizer", "🌐 Language",
    "THEME COLOR", "DICE CAST", "ROLL TRAY", "ACTIVE ROLL", "Rolling Dice...",
    "Choose a dice set or quick roll below to cast!", "No Custom Dice Sets", "Create custom roll collections using the '+' button.",
    "The Dice Tray is Empty", "Tap dice to load up your quick roll collection.", "Clear", "Roll",
    "NORMAL", "ADVANTAGE", "DISADVANTAGE", "LOGGED HISTORY", "No Rolls Recorded", "Roll any dice set to record the logs.",
    "Clear Logs", "Clear Log History?", "This will permanently delete all historic roll logs from the database.", "Cancel", "Clear All",
    "EDIT DICE SET", "CREATE CUSTOM SET", "Set Name (e.g., Fireball Lvl 3)", "Short Description", "SELECT DICE QUANTITIES",
    "STATIC MODIFIER", "ADD MODIFIER", "Save Set", "Delete Set",
    "TAP TO ACTIVATE PRESET", "Each theme customizes the layout, buttons, accent glow, and texts:",
    "GRANULAR ELEMENT COLORS", "Tap any row to open the interactive Color Picker, or use the quick swatches below:",
    "App Background", "Panel / Card Background", "Highlight / D20 Accent", "Button Fill / Container Accent", "Secondary highlights", "Rage / Warning Accent",
    "Primary Text", "Secondary Text", "Advantage Button Color", "Disadvantage Button Color", "Dice Roll Result Color", "Dice Roll Animation Color",
    "d4 Color", "d6 Color", "d8 Color", "d10 Color", "d12 Color", "d20 Color", "d100 Color", "Dice Tray Gradient Top", "Dice Tray Gradient Bottom",
    "Active Roll Background", "Active Roll Content Color",
    "UI VISUAL STYLE", "Transform the structural aesthetic of cards, containers, and borders:",
    "Classic Rounded", "Elegant Material curves & subtle shadows",
    "Glassmorphic", "Translucent glass panels with frosted borders",
    "Neon Glow", "Sharp glowing tech borders & neon sci-fi halo",
    "Flat Minimalist", "Stark, crisp flat 2D retro design",
    "Obsidian Shard", "Hard angled cuts resembling carved stone",
    "Chunky Arcade", "Thick bold borders with heavy drop shadows",
    "Soft Organic", "Fully rounded soft edges",
    "BUTTON STYLE AESTHETIC", "Choose the shape and curvature of all action buttons:",
    "Pill Shaped", "Highly pill-shaped friendly curves",
    "Rounded Corner", "Standard subtle rounded corners",
    "Sharp Rectangle", "Hard 90-degree corners",
    "Cut Corners", "Sci-fi chamfered edges",
    "USER PREFERENCES", "Toggle game mechanisms and haptics for custom rolling suspense:",
    "Dice Rolling Delay", "Play 600ms roll delay for suspense",
    "Rolling Sound Effects", "Play fantasy sound effects when rolling",
    "Haptic Vibration", "Vibrate device when dice stop rolling",
    "LANGUAGE", "Select your preferred app language:",
    "Legendary Colorizer", "v1.3.0 • Unleash Your Aesthetic", "Close Settings",
    "Save Custom Preset", "Enter a name for your custom preset style:", "My Custom Style", "Save", "Save Current Style as Custom Preset",
    "Hex Code:", "Hue", "Saturation", "Brightness", "Custom Roll", "Formula: ", "Rolls: "
]

fr_translations = {
    "Dice Sets": "Sets de Dés",
    "Quick Tray": "Lancer Rapide",
    "Roll Log": "Historique",
    "🎨 Presets": "🎨 Préréglages",
    "🔧 Colorizer": "🔧 Couleurs",
    "⚙️ Stylizer": "⚙️ Styles",
    "🌐 Language": "🌐 Langue",
    "THEME COLOR": "COULEUR DU THÈME",
    "DICE CAST": "LANCER DE DÉS",
    "ROLL TRAY": "PLATEAU DE DÉS",
    "ACTIVE ROLL": "LANCER ACTIF",
    "Rolling Dice...": "Lancement des dés...",
    "Choose a dice set or quick roll below to cast!": "Choisissez un set de dés ou un lancer rapide ci-dessous pour lancer !",
    "No Custom Dice Sets": "Aucun set de dés personnalisé",
    "Create custom roll collections using the '+' button.": "Créez des collections de lancers avec le bouton '+'.",
    "The Dice Tray is Empty": "Le plateau de dés est vide",
    "Tap dice to load up your quick roll collection.": "Appuyez sur les dés pour charger votre collection.",
    "Clear": "Effacer",
    "Roll": "Lancer",
    "NORMAL": "NORMAL",
    "ADVANTAGE": "AVANTAGE",
    "DISADVANTAGE": "DÉSAVANTAGE",
    "LOGGED HISTORY": "HISTORIQUE",
    "No Rolls Recorded": "Aucun Lancer Enregistré",
    "Roll any dice set to record the logs.": "Lancez n'importe quel dé pour l'enregistrer.",
    "Clear Logs": "Effacer l'historique",
    "Clear Log History?": "Effacer l'historique ?",
    "This will permanently delete all historic roll logs from the database.": "Cela supprimera définitivement tout l'historique de la base de données.",
    "Cancel": "Annuler",
    "Clear All": "Tout effacer",
    "EDIT DICE SET": "ÉDITER SET DE DÉS",
    "CREATE CUSTOM SET": "CRÉER UN SET",
    "Set Name (e.g., Fireball Lvl 3)": "Nom du Set (ex: Boule de Feu Niv 3)",
    "Short Description": "Courte Description",
    "SELECT DICE QUANTITIES": "SÉLECTIONNER LES QUANTITÉS",
    "STATIC MODIFIER": "MODIFICATEUR STATIQUE",
    "ADD MODIFIER": "AJOUTER MODIFICATEUR",
    "Save Set": "Sauvegarder",
    "Delete Set": "Supprimer le set",
    "TAP TO ACTIVATE PRESET": "APPUYEZ POUR ACTIVER LE THÈME",
    "Each theme customizes the layout, buttons, accent glow, and texts:": "Chaque thème personnalise la disposition, les boutons et les couleurs :",
    "GRANULAR ELEMENT COLORS": "COULEURS DES ÉLÉMENTS",
    "Tap any row to open the interactive Color Picker, or use the quick swatches below:": "Appuyez pour ouvrir le sélecteur de couleurs, ou utilisez les palettes rapides :",
    "App Background": "Fond de l'application",
    "Panel / Card Background": "Fond des cartes",
    "Highlight / D20 Accent": "Accentuation D20",
    "Button Fill / Container Accent": "Couleur des boutons",
    "Secondary highlights": "Accentuations secondaires",
    "Rage / Warning Accent": "Accent d'alerte / Rage",
    "Primary Text": "Texte principal",
    "Secondary Text": "Texte secondaire",
    "Advantage Button Color": "Bouton Avantage",
    "Disadvantage Button Color": "Bouton Désavantage",
    "Dice Roll Result Color": "Résultat des dés",
    "Dice Roll Animation Color": "Animation des dés",
    "d4 Color": "Couleur d4",
    "d6 Color": "Couleur d6",
    "d8 Color": "Couleur d8",
    "d10 Color": "Couleur d10",
    "d12 Color": "Couleur d12",
    "d20 Color": "Couleur d20",
    "d100 Color": "Couleur d100",
    "Dice Tray Gradient Top": "Plateau - Dégradé haut",
    "Dice Tray Gradient Bottom": "Plateau - Dégradé bas",
    "Active Roll Background": "Fond du lancer actif",
    "Active Roll Content Color": "Texte du lancer actif",
    "UI VISUAL STYLE": "STYLE VISUEL",
    "Transform the structural aesthetic of cards, containers, and borders:": "Transformez l'esthétique des cartes, des conteneurs et des bordures :",
    "Classic Rounded": "Classique arrondi",
    "Elegant Material curves & subtle shadows": "Courbes Material élégantes et ombres douces",
    "Glassmorphic": "Effet Verre",
    "Translucent glass panels with frosted borders": "Panneaux de verre translucide avec des bordures givrées",
    "Neon Glow": "Lueur Néon",
    "Sharp glowing tech borders & neon sci-fi halo": "Bordures technologiques lumineuses et halo de science-fiction",
    "Flat Minimalist": "Plat minimaliste",
    "Stark, crisp flat 2D retro design": "Design 2D plat et épuré rétro",
    "Obsidian Shard": "Éclat d'obsidienne",
    "Hard angled cuts resembling carved stone": "Coupes en biais dures ressemblant à la pierre sculptée",
    "Chunky Arcade": "Arcade Massive",
    "Thick bold borders with heavy drop shadows": "Bordures épaisses avec des ombres marquées",
    "Soft Organic": "Doux organique",
    "Fully rounded soft edges": "Bords doux entièrement arrondis",
    "BUTTON STYLE AESTHETIC": "STYLE DES BOUTONS",
    "Choose the shape and curvature of all action buttons:": "Choisissez la forme et la courbure de tous les boutons :",
    "Pill Shaped": "Forme de pilule",
    "Highly pill-shaped friendly curves": "Courbes douces très arrondies en pilule",
    "Rounded Corner": "Coins arrondis",
    "Standard subtle rounded corners": "Coins standard subtilement arrondis",
    "Sharp Rectangle": "Rectangle net",
    "Hard 90-degree corners": "Angles droits stricts de 90 degrés",
    "Cut Corners": "Coins coupés",
    "Sci-fi chamfered edges": "Bords chanfreinés style science-fiction",
    "USER PREFERENCES": "PRÉFÉRENCES UTILISATEUR",
    "Toggle game mechanisms and haptics for custom rolling suspense:": "Activez les mécanismes de jeu et le retour haptique pour le suspense :",
    "Dice Rolling Delay": "Délai du lancer",
    "Play 600ms roll delay for suspense": "600 ms de délai pour le suspense",
    "Rolling Sound Effects": "Effets sonores",
    "Play fantasy sound effects when rolling": "Jouer des sons fantasy lors du lancer",
    "Haptic Vibration": "Vibration haptique",
    "Vibrate device when dice stop rolling": "Vibrer quand les dés s'arrêtent",
    "LANGUAGE": "LANGUE",
    "Select your preferred app language:": "Sélectionnez la langue de l'application :",
    "Legendary Colorizer": "Coloriseur Légendaire",
    "v1.3.0 • Unleash Your Aesthetic": "v1.3.0 • Libérez votre esthétique",
    "Close Settings": "Fermer",
    "Save Custom Preset": "Sauvegarder le Préréglage",
    "Enter a name for your custom preset style:": "Entrez un nom pour votre style personnalisé :",
    "My Custom Style": "Mon Style",
    "Save": "Sauvegarder",
    "Save Current Style as Custom Preset": "Sauvegarder le style en tant que Préréglage",
    "Hex Code:": "Code Hex :",
    "Hue": "Teinte",
    "Saturation": "Saturation",
    "Brightness": "Luminosité",
    "Custom Roll": "Lancer Personnalisé",
    "Formula: ": "Formule : ",
    "Rolls: ": "Lancers : ",
    "Settings": "Paramètres"
}

# Wait, there's more strings like "English", "Français", "Toggle color picker", "Menu", "Delete", "Selected", "Active"
fr_translations.update({
    "English": "Anglais",
    "Français": "Français",
    "Toggle color picker": "Afficher couleurs",
    "Menu": "Menu",
    "Delete": "Suppr.",
    "Selected": "Sélectionné",
    "Active": "Actif"
})

fr_translations_str = json.dumps(fr_translations, ensure_ascii=False)

kotlin_code = f"""
val frTranslations = mapOf(
"""
for k, v in fr_translations.items():
    kotlin_code += f'    "{k}" to "{v}",\n'
kotlin_code += """)

@Composable
fun t(key: String): String {
    return if (currentLanguage == "Français") frTranslations[key] ?: key else key
}
fun String.t(): String {
    return if (currentLanguage == "Français") frTranslations[this] ?: this else this
}
"""

with open('translations.kt', 'w') as f:
    f.write(kotlin_code)

