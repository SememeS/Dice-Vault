import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

# Update top level variables
replacements = [
    (r"var DiceRollResultColor by mutableStateOf\(Color\(0xFFFFFFFF\)\)", r"var DiceRollResultColor by mutableStateOf(Color(0xFF7E57C2))"),
    (r"var DiceRollAnimationColor by mutableStateOf\(Color\(0xFFFFFFFF\)\)", r"var DiceRollAnimationColor by mutableStateOf(Color(0xFF7E57C2))"),
    (r"var ActiveRollBackgroundColor by mutableStateOf\(Color\(0xFFFFFFFF\)\)", r"var ActiveRollBackgroundColor by mutableStateOf(Color(0xFF7E57C2))"),
    (r"var currentUiStyle by mutableStateOf\(UiStyleTheme.CLASSIC_ROUNDED\)", r"var currentUiStyle by mutableStateOf(UiStyleTheme.GLASSMORPHIC)"),
    (r"var currentButtonStyle by mutableStateOf\(ButtonStyleTheme.PILL\)", r"var currentButtonStyle by mutableStateOf(ButtonStyleTheme.PILL)")
]

for target, replacement in replacements:
    content = re.sub(target, replacement, content)

# Update the preset definition
target_preset = r"""            diceRollResultColor = Color\(0xFF311B92\),
            diceRollAnimationColor = Color\(0xFF311B92\),"""

replacement_preset = r"""            diceRollResultColor = Color(0xFF7E57C2),
            diceRollAnimationColor = Color(0xFF7E57C2),"""

content = re.sub(target_preset, replacement_preset, content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)
