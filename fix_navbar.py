import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

content = content.replace('label = { Text("Dice Sets".t(), fontSize = 11.sp) }', 'label = { Text("Dice Sets".t(), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }')
content = content.replace('label = { Text("Quick Tray".t(), fontSize = 11.sp) }', 'label = { Text("Quick Tray".t(), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }')
content = content.replace('label = { Text("Roll Log".t(), fontSize = 11.sp) }', 'label = { Text("Roll Log".t(), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis) }')

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
