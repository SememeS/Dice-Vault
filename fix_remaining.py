import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # We just want to replace all occurrences of `shape = RoundedCornerShape(...)` inside button definitions with `shape = getButtonShape()`.
    # Let's find specific ones
    content = re.sub(r'shape\s*=\s*RoundedCornerShape\([^)]+\)', 'shape = getButtonShape()', content)
    
    with open(filepath, 'w') as f:
        f.write(content)

process_file('app/src/main/java/com/example/ui/DiceRollerApp.kt')
