import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    lines = f.readlines()
    
# check within ranges of Button... {
in_button = False
shapes = []
for i, line in enumerate(lines):
    if re.search(r'(Button|OutlinedButton|TextButton)\(', line):
        in_button = True
        shapes = []
    
    if in_button:
        if 'shape =' in line or 'shape=' in line:
            shapes.append(i + 1)
        if ') {' in line or ')  {' in line:
            if len(shapes) > 1:
                print(f"Double shape near lines: {shapes}")
            in_button = False
