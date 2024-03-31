// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.elements.basement;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.minecraft.util.ResourceLocation;

public abstract class PackElement extends DraggableElement
{
    public static final ResourceLocation DEFAULT_ICON;
    
    static {
        DEFAULT_ICON = new ResourceLocation("resourcepacks24/textures/pack.png");
    }
    
    public PackElement(final boolean deletable) {
        super(deletable);
    }
    
    public abstract ResourceLocation getIcon();
    
    public abstract String getDisplayName();
    
    public abstract String getDescription();
    
    @Override
    protected String getElementName() {
        return this.getDisplayName();
    }
    
    @Override
    public boolean draw(final double x, final double y, final double width, final double height, final int mouseX, final int mouseY) {
        final boolean mouseOver = super.draw(x, y, width, height, mouseX, mouseY);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        if (mouseOver) {
            DrawUtils.drawRect(x - 1.0, y - 1.0, x + width + 1.0, y + height + 1.0, ModColor.toRGB(100, 100, 100, 35));
        }
        final ResourceLocation resourceLocation = this.getIcon();
        Minecraft.getMinecraft().renderEngine.bindTexture((resourceLocation == null) ? PackElement.DEFAULT_ICON : resourceLocation);
        draw.drawTexture(x, y, 255.0, 255.0, height, height);
        if (mouseOver) {
            DrawUtils.drawRect(x, y, x + height, y + height, ModColor.toRGB(255, 255, 255, 60));
        }
        final String title = draw.trimStringToWidth(this.getDisplayName(), (int)(width - height - 4.0));
        draw.drawString(title, x + height + 2.0, y + 1.0);
        if (height > 14.0) {
            final List<String> list = draw.listFormattedStringToWidth(this.getDescription(), (int)((width - height - 4.0) / 0.8), (int)(height / 8.0 - 1.0));
            int listY = 0;
            for (final String line : list) {
                draw.drawString(String.valueOf(ModColor.cl('7')) + line, x + height + 2.0, y + 10.0 + listY, 0.7);
                listY += 7;
            }
        }
        return mouseOver;
    }
}
