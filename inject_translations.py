import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

# Insert the dictionary and extensions
dictionary_code = """
val frTranslations = mapOf(
    "Dice Sets" to "Sets de Dés",
    "Quick Tray" to "Lancer Rapide",
    "Roll Log" to "Historique",
    "🎨 Presets" to "🎨 Préréglages",
    "🔧 Colorizer" to "🔧 Couleurs",
    "⚙️ Stylizer" to "⚙️ Styles",
    "🌐 Language" to "🌐 Langue",
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
    "English" to "Anglais",
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

"""

if "val frTranslations = mapOf(" not in content:
    content = content.replace("var currentLanguage by mutableStateOf(\"English\")", "var currentLanguage by mutableStateOf(\"English\")\n" + dictionary_code)

strings_to_replace = [
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
    "Hex Code:", "Hue", "Saturation", "Brightness", "Custom Roll", "Formula: ", "Rolls: ",
    "English", "Français", "Toggle color picker", "Menu", "Delete", "Selected", "Active", "Settings",
    "Pen and Paper", "High contrast ink & white", "Obsidian Violet", "Muted cosmic dark", "Metal", "Metal greyscale"
]

for s in strings_to_replace:
    # Safely replace only when the string is quoted and we use .t() on it
    # We shouldn't replace it if it's already .t() or if it's something we shouldn't replace.
    # Text(text = "...") -> Text(text = "...".t())
    # Text("...") -> Text("...".t())
    # value = "..." -> value = "...".t() (maybe unsafe? we'll stick to targeted replacements)
    
    # We will search for exactly "s" and replace with "s".t()
    # BUT only if it is preceded by a ( or whitespace and followed by a ) or , or whitespace
    # Let's just use simple text replacement but careful about duplicates.
    
    pattern = r'"{}"(?!\.t\(\))'.format(re.escape(s))
    
    # We should exclude the mapOf keys in our frTranslations.
    # We can do this by splitting the content in two parts: the translations and the rest of the code.
    
parts = content.split("val frTranslations = mapOf(")
if len(parts) > 1:
    before = parts[0]
    middle_and_after = parts[1].split(")@Composable")
    if len(middle_and_after) > 1:
        middle = middle_and_after[0]
        after = ")@Composable" + middle_and_after[1]
        
        for s in strings_to_replace:
            pattern = r'"{}"(?!\.t\(\))'.format(re.escape(s))
            after = re.sub(pattern, f'"{s}".t()', after)
            
        content = before + "val frTranslations = mapOf(" + middle + after

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
