import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

content = content.replace("""                                Text(
                                    text = title.replace(Regex("^.*?\\\\s"), ""), // Skip emoji for cleaner UI
                                    color = if (isSelected) DarkSlateBg else TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )""", """                                Text(
                                    text = title.replace(Regex("^.*?\\\\s"), ""), // Skip emoji for cleaner UI
                                    color = if (isSelected) DarkSlateBg else TextPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )""")

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
