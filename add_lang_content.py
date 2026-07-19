import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    lines = f.readlines()

new_content = """                        3 -> {
                            // LANGUAGE SECTION
                            item {
                                Text(
                                    text = "LANGUAGE",
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Select your preferred app language:",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            
                            val languages = listOf("English", "Español", "Français", "Deutsch", "Italiano", "日本語")
                            items(languages) { lang ->
                                val isSelected = currentLanguage == lang
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { currentLanguage = lang }
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) GoldAccent else Color(0xFF49454F).copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) GoldAccent.copy(alpha = 0.1f) else DarkCardBg
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = lang,
                                            color = if (isSelected) GoldAccent else TextPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = GoldAccent,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
"""

lines.insert(3092, new_content)

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.writelines(lines)
