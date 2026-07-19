with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    lines = f.readlines()

# Extract the block
lines.pop(3154) # remove the `                        }` at 3155 (0-indexed 3154)
lines.insert(3092, "                        }\n")

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'w') as f:
    f.writelines(lines)
