import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Find button calls: Button(, OutlinedButton(, TextButton(
    # We want to add or replace shape
    
    # It's tricky to parse nested brackets in regex. Let's just do targeted replacements.
    replacements = [
        (r'Button\(\s*onClick = \{ onRoll\(set, "ADVANTAGE"\) \},\s*colors = ButtonDefaults\.buttonColors\([^)]+\)',
         r'\g<0>,\n                                    shape = getButtonShape()'),
        (r'Button\(\s*onClick = \{ onRoll\(set, "NORMAL"\) \},\s*colors = ButtonDefaults\.buttonColors\([^)]+\)',
         r'\g<0>,\n                                    shape = getButtonShape()'),
        (r'Button\(\s*onClick = \{ onRoll\(set, "DISADVANTAGE"\) \},\s*colors = ButtonDefaults\.buttonColors\([^)]+\)',
         r'\g<0>,\n                                    shape = getButtonShape()'),
        (r'Button\(\s*onClick = \{ if \(modifierValue > -20\) modifierValue-- \},\s*colors = ButtonDefaults\.buttonColors[^,]+,\s*shape = RoundedCornerShape\([^)]+\)',
         r'Button(\n                            onClick = { if (modifierValue > -20) modifierValue-- },\n                            colors = ButtonDefaults.buttonColors(containerColor = DarkSlateBg),\n                            shape = getButtonShape()'),
        (r'Button\(\s*onClick = \{ if \(modifierValue < 20\) modifierValue\+\+ \},\s*colors = ButtonDefaults\.buttonColors[^,]+,\s*shape = RoundedCornerShape\([^)]+\)',
         r'Button(\n                            onClick = { if (modifierValue < 20) modifierValue++ },\n                            colors = ButtonDefaults.buttonColors(containerColor = DarkSlateBg),\n                            shape = getButtonShape()'),
        (r'Button\(\s*onClick = \{ selectedRollType = opt \},\s*colors = ButtonDefaults\.buttonColors\(\s*containerColor = [^,]+,\s*contentColor = [^\n]+\s*\)',
         r'\g<0>,\n                                    shape = getButtonShape()'),
        (r'OutlinedButton\(\s*onClick = \{\s*d4Count = 0\s*d6Count = 0\s*d8Count = 0\s*d10Count = 0\s*d12Count = 0\s*d20Count = 0\s*d100Count = 0\s*\},',
         r'OutlinedButton(\n                    onClick = {\n                        d4Count = 0\n                        d6Count = 0\n                        d8Count = 0\n                        d10Count = 0\n                        d12Count = 0\n                        d20Count = 0\n                        d100Count = 0\n                    },\n                    shape = getButtonShape(),'),
        (r'Button\(\s*onClick = \{\s*onRoll\(\s*"Quick Tray Roll",\s*d4Count,',
         r'Button(\n                    shape = getButtonShape(),\n                    onClick = {\n                        onRoll(\n                            "Quick Tray Roll",\n                            d4Count,'),
        (r'TextButton\(onClick = \{ showConfirmClear = true \}\) \{',
         r'TextButton(onClick = { showConfirmClear = true }, shape = getButtonShape()) {'),
        (r'TextButton\(onClick = \{ showConfirmClear = false \}\) \{',
         r'TextButton(onClick = { showConfirmClear = false }, shape = getButtonShape()) {'),
        (r'TextButton\(onClick = \{\s*onClearHistory\(\)\s*showConfirmClear = false\s*\}\) \{',
         r'TextButton(\n                    shape = getButtonShape(),\n                    onClick = {\n                    onClearHistory()\n                    showConfirmClear = false\n                }) {'),
        (r'Button\(\s*onClick = \{ if \(modifier > -20\) modifier-- \},\s*colors = ButtonDefaults\.buttonColors[^,]+,\s*shape = RoundedCornerShape\([^)]+\)',
         r'Button(\n                            onClick = { if (modifier > -20) modifier-- },\n                            colors = ButtonDefaults.buttonColors(containerColor = DarkCardBg),\n                            shape = getButtonShape()'),
        (r'Button\(\s*onClick = \{ if \(modifier < 20\) modifier\+\+ \},\s*colors = ButtonDefaults\.buttonColors[^,]+,\s*shape = RoundedCornerShape\([^)]+\)',
         r'Button(\n                            onClick = { if (modifier < 20) modifier++ },\n                            colors = ButtonDefaults.buttonColors(containerColor = DarkCardBg),\n                            shape = getButtonShape()'),
        (r'OutlinedButton\(\s*onClick = onDismiss,\s*border = BorderStroke\([^)]+\),\s*colors = ButtonDefaults\.outlinedButtonColors\([^)]+\),\s*shape = RoundedCornerShape\([^)]+\)',
         r'OutlinedButton(\n                            onClick = onDismiss,\n                            border = BorderStroke(1.dp, Color(0xFF3C3C46)),\n                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),\n                            shape = getButtonShape()'),
        (r'Button\(\s*onClick = \{\s*if \(name\.isNotEmpty\(\)\) \{\s*val saved = DiceSet\(',
         r'Button(\n                            shape = getButtonShape(),\n                            onClick = {\n                                if (name.isNotEmpty()) {\n                                    val saved = DiceSet('),
        (r'Button\(\s*onClick = onDismiss,\s*modifier = Modifier\s*\.fillMaxWidth\(\)\s*\.height\(48\.dp\),\s*colors = ButtonDefaults\.buttonColors\(',
         r'Button(\n                    onClick = onDismiss,\n                    shape = getButtonShape(),\n                    modifier = Modifier\n                        .fillMaxWidth()\n                        .height(48.dp),\n                    colors = ButtonDefaults.buttonColors(')
    ]
    
    for pat, rep in replacements:
        content = re.sub(pat, rep, content)
        
    with open(filepath, 'w') as f:
        f.write(content)

process_file('app/src/main/java/com/example/ui/DiceRollerApp.kt')
