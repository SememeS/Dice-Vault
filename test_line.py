with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    lines = f.readlines()

for i, line in enumerate(lines[3080:3110]):
    print(f"{i + 3081}: {line.rstrip()}")
