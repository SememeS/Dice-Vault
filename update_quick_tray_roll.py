import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

content = content.replace('"Quick Tray Roll",', '"Quick Tray Roll".t(),')
content = content.replace('"Quick Roll"', '"Quick Roll".t()')
content = content.replace('"NORMAL" to "NORMAL",', '"NORMAL" to "NORMAL",\n    "Quick Tray Roll" to "Lancer Rapide",\n    "Quick Roll" to "Lancer Rapide",')

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
