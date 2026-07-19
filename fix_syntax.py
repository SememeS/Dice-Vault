import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

content = content.replace('var currentLanguage by mutableStateOf(\\"English\\")', 'var currentLanguage by mutableStateOf("English")')

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)

