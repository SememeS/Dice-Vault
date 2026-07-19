import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    # Remove the `shape = getButtonShape(),` if it's followed by `shape = ...`
    content = re.sub(r'shape\s*=\s*getButtonShape\(\),\s*shape\s*=\s*RoundedCornerShape\([^)]+\)', 'shape = getButtonShape()', content)
    
    # Check if there are still any 'shape = getButtonShape(),\s*shape ='
    content = re.sub(r'shape\s*=\s*getButtonShape\(\),\s*shape\s*=\s*getButtonShape\(\)', 'shape = getButtonShape()', content)
    
    # For lines 1159 etc, let's see what happened
    
    with open(filepath, 'w') as f:
        f.write(content)

process_file('app/src/main/java/com/example/ui/DiceRollerApp.kt')
