# 06_fix_resources.py
import os, xml.etree.ElementTree as ET

STR = os.path.join("app","src","main","res","values","strings.xml")
os.makedirs(os.path.dirname(STR), exist_ok=True)

if not os.path.exists(STR):
    with open(STR,"w",encoding="utf-8") as f:
        f.write('<resources><string name="app_name">DrinkOrder</string></resources>\n')
else:
    tree = ET.parse(STR)
    root = tree.getroot()
    found = False
    for s in root.findall('string'):
        if s.get('name')=='app_name':
            s.text = 'DrinkOrder'; found=True
    if not found:
        ET.SubElement(root,'string',{'name':'app_name'}).text='DrinkOrder'
    tree.write(STR, encoding="utf-8", xml_declaration=False)
print("✔ strings.xml ensured with app_name=DrinkOrder")
