import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

target = r"""                                diceColor = DiceRollResultColor,
                                uiStyle = currentUiStyle,
                                buttonStyle = currentButtonStyle
                            \)"""

replacement = r"""                                diceColor = DiceRollResultColor,
                                uiStyle = currentUiStyle,
                                buttonStyle = currentButtonStyle,
                                advantageColor = AdvantageColor,
                                disadvantageColor = DisadvantageColor,
                                diceRollResultColor = DiceRollResultColor,
                                diceRollAnimationColor = DiceRollAnimationColor,
                                d4Color = D4Color,
                                d6Color = D6Color,
                                d8Color = D8Color,
                                d10Color = D10Color,
                                d12Color = D12Color,
                                d20Color = D20Color,
                                d100Color = D100Color,
                                diceTrayGradientStart = DiceTrayGradientStart,
                                diceTrayGradientEnd = DiceTrayGradientEnd,
                                activeRollBackgroundColor = ActiveRollBackgroundColor,
                                activeRollContentColor = ActiveRollContentColor
                            )"""

content = re.sub(target, replacement, content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)

