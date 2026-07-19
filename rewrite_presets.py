import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

# Replace items(presets)
target_start = r"items\(presets\) \{ preset ->\s*val isSelected = DarkSlateBg == preset.bg && GoldAccent == preset.accent"
replacement_start = r"""items(presets + customThemePresets) { preset ->
                                val isCustom = customThemePresets.contains(preset)
                                val isSelected = DarkSlateBg == preset.bg && GoldAccent == preset.accent"""
content = re.sub(target_start, replacement_start, content)

# Now find the Colors Row Preview to insert the delete button
target_colors_row = r"""                                        // Colors Row Preview
                                        Row\(
                                            horizontalArrangement = Arrangement\.spacedBy\(4\.dp\),
                                            verticalAlignment = Alignment\.CenterVertically
                                        \) \{
                                            Box\(modifier = Modifier\.size\(16\.dp\)\.background\(preset\.bg, CircleShape\)\.border\(0\.5\.dp, Color\.White, CircleShape\)\)
                                            Box\(modifier = Modifier\.size\(16\.dp\)\.background\(preset\.cardBg, CircleShape\)\.border\(0\.5\.dp, Color\.White, CircleShape\)\)
                                            Box\(modifier = Modifier\.size\(16\.dp\)\.background\(preset\.accent, CircleShape\)\.border\(0\.5\.dp, Color\.White, CircleShape\)\)
                                        \}
                                    \}
                                \}
                            \}"""
replacement_colors_row = r"""                                        // Colors Row Preview
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(modifier = Modifier.size(16.dp).background(preset.bg, CircleShape).border(0.5.dp, Color.White, CircleShape))
                                            Box(modifier = Modifier.size(16.dp).background(preset.cardBg, CircleShape).border(0.5.dp, Color.White, CircleShape))
                                            Box(modifier = Modifier.size(16.dp).background(preset.accent, CircleShape).border(0.5.dp, Color.White, CircleShape))
                                            if (isCustom) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                IconButton(onClick = { customThemePresets.remove(preset) }, modifier = Modifier.size(24.dp)) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = preset.errorAccent, modifier = Modifier.size(16.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = { showSavePresetDialog = true },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    shape = getButtonShape(),
                                    border = BorderStroke(1.dp, GoldAccent),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save Current Style as Custom Preset", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }"""

content = re.sub(target_colors_row, replacement_colors_row, content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.write(content)

