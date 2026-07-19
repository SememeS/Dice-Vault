import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

# Update tabs list
target_tabs = r"""                val tabs = listOf\(
                    Triple\("🎨 Presets", 0, null as androidx\.compose\.ui\.graphics\.vector\.ImageVector\?\),
                    Triple\("🔧 Colorizer", 1, null as androidx\.compose\.ui\.graphics\.vector\.ImageVector\?\),
                    Triple\("⚙️ Stylizer", 2, null as androidx\.compose\.ui\.graphics\.vector\.ImageVector\?\)
                \)"""

replacement_tabs = r"""                val tabs = listOf(
                    Triple("🎨 Presets", 0, null as androidx.compose.ui.graphics.vector.ImageVector?),
                    Triple("🔧 Colorizer", 1, null as androidx.compose.ui.graphics.vector.ImageVector?),
                    Triple("⚙️ Stylizer", 2, null as androidx.compose.ui.graphics.vector.ImageVector?),
                    Triple("🌐 Language", 3, null as androidx.compose.ui.graphics.vector.ImageVector?)
                )"""

content = re.sub(target_tabs, replacement_tabs, content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)

