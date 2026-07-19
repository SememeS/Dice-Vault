import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

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

match = re.search(r'\nfun String\.t\(\): String \{.*?\n\}', content, re.DOTALL)
if match:
    end_idx = match.end()
    before = content[:end_idx]
    after = content[end_idx:]
    
    for s in strings_to_replace:
        pattern = r'"{}"(?!\.t\(\))'.format(re.escape(s))
        after = re.sub(pattern, f'"{s}".t()', after)
        
    content = before + after

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
