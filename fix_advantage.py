import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

# Fix ActiveRollingTray checking rollType
content = content.replace('rollType != "NORMAL".t()', 'rollType != "NORMAL"')
content = content.replace('rollType == "ADVANTAGE".t()', 'rollType == "ADVANTAGE"')
content = content.replace('text = rollType,', 'text = rollType.t(),')

# Fix onRoll
content = content.replace('onRoll(set, "ADVANTAGE".t())', 'onRoll(set, "ADVANTAGE")')
content = content.replace('onRoll(set, "NORMAL".t())', 'onRoll(set, "NORMAL")')
content = content.replace('onRoll(set, "DISADVANTAGE".t())', 'onRoll(set, "DISADVANTAGE")')

# Fix QuickRollerTab state
content = content.replace('var selectedRollType by remember { mutableStateOf("NORMAL".t()) }', 'var selectedRollType by remember { mutableStateOf("NORMAL") }')
content = content.replace('val options = listOf("NORMAL".t(), "ADVANTAGE".t(), "DISADVANTAGE".t())', 'val options = listOf("NORMAL", "ADVANTAGE", "DISADVANTAGE")')

# Fix color selector for selectedRollType
content = content.replace('"ADVANTAGE".t() -> AdvantageColor', '"ADVANTAGE" -> AdvantageColor')
content = content.replace('"DISADVANTAGE".t() -> DisadvantageColor', '"DISADVANTAGE" -> DisadvantageColor')

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
