// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.elements.basement;

import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;

public abstract class Element
{
    public boolean draw(final double x, final double y, final double width, final double height, final int mouseX, final int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final GuiResourcepacks24 gui) {
    }
    
    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick, final GuiResourcepacks24 gui) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton, final GuiResourcepacks24 gui) {
    }
}
