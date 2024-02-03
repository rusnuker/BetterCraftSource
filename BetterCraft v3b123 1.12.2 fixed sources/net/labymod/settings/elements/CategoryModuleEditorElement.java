// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import java.util.Iterator;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;

public class CategoryModuleEditorElement extends SettingsElement
{
    private ControlElement.IconData iconData;
    
    public CategoryModuleEditorElement(final String displayName, final ControlElement.IconData iconData) {
        super(displayName, null);
        this.iconData = iconData;
    }
    
    @Override
    public void init() {
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        final int absoluteY = y + 7;
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(200, 200, 200, this.mouseOver ? 50 : 30));
        final int imageSize = maxY - y;
        if (this.iconData.hastextureIcon()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.iconData.gettextureIcon());
            LabyMod.getInstance().getDrawUtils().drawTexture(x + 2, y + 2, 256.0, 256.0, 18.0, 18.0);
        }
        else if (this.iconData.hasMaterialIcon()) {
            LabyMod.getInstance().getDrawUtils().drawItem(this.iconData.getMaterialIcon().createItemStack(), x + 3, y + 3, null);
        }
        draw.drawString(this.getDisplayName(), x + imageSize + 5, absoluteY);
        final int totalSubCount = 0;
        final int enabledCount = 0;
        for (SettingsElement element : this.getSubSettings().getElements()) {}
        draw.drawRightString(String.valueOf(enabledCount) + ModColor.cl("7") + "/" + ModColor.cl("f") + totalSubCount, maxX - 5, absoluteY);
    }
    
    public ControlElement.IconData getIconData() {
        return this.iconData;
    }
    
    @Override
    public int getEntryHeight() {
        return 22;
    }
    
    @Override
    public void drawDescription(final int x, final int y, final int screenWidth) {
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void mouseRelease(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void mouseClickMove(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void unfocus(final int mouseX, final int mouseY, final int mouseButton) {
    }
}
