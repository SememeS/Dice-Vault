import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

content = content.replace('"🎨 Presets" to "🎨 Préréglages"', '"🎨 Presets" to "🎨 Prédéfini"')
content = content.replace('"🔧 Colorizer" to "🔧 Couleurs"', '"🔧 Colorizer" to "🔧 Coloriseur"')
content = content.replace('"⚙️ Stylizer" to "⚙️ Styles"', '"⚙️ Stylizer" to "⚙️ Styliseur"')
content = content.replace('"🌐 Language" to "🌐 Langue"', '"🌐 Language" to "🌐 Langues"')

# Let's fix the substring(2) issue so the text is actually clean.
# title.substring(2) leaves a space and sometimes cuts wrong.
# Let's replace title.substring(2) with title.drop(2).trim() or something similar, or just replace it with title.dropWhile { !it.isLetter() }
content = content.replace('text = title.substring(2), // Skip emoji for cleaner UI', 'text = title.replace(Regex("^.*?\\\\s"), ""), // Skip emoji for cleaner UI')

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
