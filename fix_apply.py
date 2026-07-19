import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

target = r"""                                            DiceTrayGradientStart = preset.cardBg
                                            DiceTrayGradientEnd = preset.bg
                                            AdvantageColor = preset.secondaryAccent
                                            DisadvantageColor = preset.errorAccent
                                            DiceRollResultColor = preset.diceColor
                                            DiceRollAnimationColor = preset.diceColor
                                            D4Color = preset.diceColor
                                            D6Color = preset.diceColor
                                            D8Color = preset.diceColor
                                            D10Color = preset.diceColor
                                            D12Color = preset.diceColor
                                            D20Color = preset.diceColor
                                            D100Color = preset.diceColor
                                            ActiveRollBackgroundColor = preset.diceColor
                                            ActiveRollContentColor = preset.bg"""

replacement = r"""                                            DiceTrayGradientStart = preset.diceTrayGradientStart
                                            DiceTrayGradientEnd = preset.diceTrayGradientEnd
                                            AdvantageColor = preset.advantageColor
                                            DisadvantageColor = preset.disadvantageColor
                                            DiceRollResultColor = preset.diceRollResultColor
                                            DiceRollAnimationColor = preset.diceRollAnimationColor
                                            D4Color = preset.d4Color
                                            D6Color = preset.d6Color
                                            D8Color = preset.d8Color
                                            D10Color = preset.d10Color
                                            D12Color = preset.d12Color
                                            D20Color = preset.d20Color
                                            D100Color = preset.d100Color
                                            ActiveRollBackgroundColor = preset.activeRollBackgroundColor
                                            ActiveRollContentColor = preset.activeRollContentColor"""

content = re.sub(target, replacement, content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)

