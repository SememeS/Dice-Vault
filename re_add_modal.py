import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    lines = f.readlines()
    
# Find the last closing brace in the file
last_brace_idx = -1
for i in range(len(lines) - 1, -1, -1):
    if lines[i].strip() == '}':
        last_brace_idx = i
        break

modal_code = """
    if (showSavePresetDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showSavePresetDialog = false },
            title = { Text("Save Custom Preset", color = TextPrimary) },
            text = {
                Column {
                    Text("Enter a name for your custom preset style:", color = TextSecondary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newPresetName,
                        onValueChange = { newPresetName = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = Color(0xFF49454F).copy(alpha = 0.5f)
                        ),
                        placeholder = { Text("My Custom Style", color = TextSecondary.copy(alpha = 0.5f)) }
                    )
                }
            },
            containerColor = DarkCardBg,
            dismissButton = {
                TextButton(onClick = { showSavePresetDialog = false }, shape = getButtonShape()) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            confirmButton = {
                Button(
                    shape = getButtonShape(),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = Color.White),
                    enabled = newPresetName.isNotBlank(),
                    onClick = {
                        customThemePresets.add(
                            ThemePreset(
                                name = newPresetName,
                                description = "Custom User Style",
                                bg = DarkSlateBg,
                                cardBg = DarkCardBg,
                                accent = GoldAccent,
                                darkAccent = DarkGoldAccent,
                                secondaryAccent = AmberGlow,
                                errorAccent = CrimsonAccent,
                                textPrimary = TextPrimary,
                                textSecondary = TextSecondary,
                                diceColor = DiceRollResultColor,
                                uiStyle = currentUiStyle,
                                buttonStyle = currentButtonStyle
                            )
                        )
                        newPresetName = ""
                        showSavePresetDialog = false
                    }
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
"""

lines.insert(last_brace_idx, modal_code)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.writelines(lines)

