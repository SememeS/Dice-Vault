import re

with open('app/src/main/java/com/example/ui/DiceRollerApp.kt', 'r') as f:
    content = f.read()

parts = content.split("val frTranslations = mapOf(")
if len(parts) > 1:
    before = parts[0]
    middle_and_after = parts[1].split(")@Composable")
    print(f"len(middle_and_after)={len(middle_and_after)}")
    if len(middle_and_after) > 1:
        middle = middle_and_after[0]
        after = ")@Composable" + middle_and_after[1]
        
        s = "Rolls: "
        pattern = r'"{}"(?!\.t\(\))'.format(re.escape(s))
        after_new = re.sub(pattern, f'"{s}".t()', after)
        
        if after_new != after:
            print("Successfully replaced!")
        else:
            print("Failed to replace!")
