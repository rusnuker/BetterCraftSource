// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;

public class ListContainerElement extends ControlElement
{
    public ListContainerElement(final String displayName, final IconData iconData) {
        super(displayName, null, iconData);
        final String key = "container_" + displayName;
        final String translation = LanguageManager.translate(key);
        if (!key.equals(translation)) {
            this.setDisplayName(translation);
        }
        this.setSettingEnabled(true);
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
    }
}
