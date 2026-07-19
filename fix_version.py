import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

pattern = r'\s*// Version\s*item \{\s*Box\([^}]*contentAlignment = Alignment\.Center\s*\)\s*\{\s*Column\(horizontalAlignment = Alignment\.CenterHorizontally\)\s*\{\s*Text\([^}]*"Legendary Colorizer"\.t\(\)[^}]*\)\s*Text\([^}]*"v1\.3\.0 • Unleash Your Aesthetic"\.t\(\)[^}]*\)\s*\}\s*\}\s*\}'
content = re.sub(pattern, '', content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
