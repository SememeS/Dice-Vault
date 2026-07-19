import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

target = r"""private data class ThemePreset\(
    val name: String,
    val description: String,
    val bg: Color,
    val cardBg: Color,
    val accent: Color,
    val darkAccent: Color,
    val secondaryAccent: Color,
    val errorAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val diceColor: Color = accent,
    val uiStyle: UiStyleTheme = UiStyleTheme\.CLASSIC_ROUNDED,
    val buttonStyle: ButtonStyleTheme = ButtonStyleTheme\.PILL
\)"""

replacement = r"""private data class ThemePreset(
    val name: String,
    val description: String,
    val bg: Color,
    val cardBg: Color,
    val accent: Color,
    val darkAccent: Color,
    val secondaryAccent: Color,
    val errorAccent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val diceColor: Color = accent,
    val uiStyle: UiStyleTheme = UiStyleTheme.CLASSIC_ROUNDED,
    val buttonStyle: ButtonStyleTheme = ButtonStyleTheme.PILL,
    val advantageColor: Color = secondaryAccent,
    val disadvantageColor: Color = errorAccent,
    val diceRollResultColor: Color = diceColor,
    val diceRollAnimationColor: Color = diceColor,
    val d4Color: Color = diceColor,
    val d6Color: Color = diceColor,
    val d8Color: Color = diceColor,
    val d10Color: Color = diceColor,
    val d12Color: Color = diceColor,
    val d20Color: Color = diceColor,
    val d100Color: Color = diceColor,
    val diceTrayGradientStart: Color = cardBg,
    val diceTrayGradientEnd: Color = bg,
    val activeRollBackgroundColor: Color = diceColor,
    val activeRollContentColor: Color = bg
)"""

content = re.sub(target, replacement, content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)

