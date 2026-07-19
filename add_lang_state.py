import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

target = r"var currentButtonStyle by mutableStateOf\(ButtonStyleTheme\.PILL\)"
replacement = r"var currentButtonStyle by mutableStateOf(ButtonStyleTheme.PILL)\nvar currentLanguage by mutableStateOf(\"English\")"

content = re.sub(target, replacement, content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)

