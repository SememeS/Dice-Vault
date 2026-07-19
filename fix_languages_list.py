import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

content = content.replace('val languages = listOf("English".t())', 'val languages = listOf("English", "Français")')
content = content.replace('val languages = listOf("English")', 'val languages = listOf("English", "Français")')

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
