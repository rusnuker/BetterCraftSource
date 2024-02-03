// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.skin;

import net.labymod.utils.DrawUtils;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.labymod.gui.elements.CheckBox;

public abstract class SkinCustomizationSettingElement
{
    protected String displayString;
    protected CheckBox checkBox;
    protected ResourceLocation iconResource;
    private List<SkinLayerSettingElement> subSettingElements;
    
    public SkinCustomizationSettingElement(final String displayString, final String iconName) {
        this.subSettingElements = new ArrayList<SkinLayerSettingElement>();
        this.displayString = displayString;
        this.iconResource = new ResourceLocation("labymod/textures/settings/skin/" + iconName + ".png");
    }
    
    protected void initCheckBox() {
        this.checkBox = new CheckBox("", this.loadValue(), null, 0, 0, 15, 15);
    }
    
    protected abstract CheckBox.EnumCheckBoxValue loadValue();
    
    public void draw(final boolean subElement, final double x, final double y, final double elementWidth, final double elementHeight, final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final double textureSize = 292.0;
        final double textureScale = 36.0;
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_LARGE_DISABLED);
        draw.drawTexture(x, y, 1.46 * elementWidth / 2.0, 1.46 * elementHeight / 2.0, elementWidth / 2.0, elementHeight / 2.0);
        draw.drawTexture(x, y + elementHeight / 2.0, 0.0, 256.0 - 1.46 * elementHeight / 2.0, 1.46 * elementWidth / 2.0, 1.46 * elementHeight / 2.0, elementWidth / 2.0, elementHeight / 2.0);
        draw.drawTexture(x + elementWidth / 2.0, y, 256.0 - 1.46 * elementWidth / 2.0, 0.0, 1.46 * elementWidth / 2.0, 1.46 * elementHeight / 2.0, elementWidth / 2.0, elementHeight / 2.0);
        draw.drawTexture(x + elementWidth / 2.0, y + elementHeight / 2.0, 256.0 - 1.46 * elementWidth / 2.0, 256.0 - 1.46 * elementHeight / 2.0, 1.46 * elementWidth / 2.0, 1.46 * elementHeight / 2.0, elementWidth / 2.0, elementHeight / 2.0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.iconResource);
        draw.drawTexture(x + elementHeight / 4.0, y + elementHeight / 4.0, 256.0, 256.0, elementHeight / 2.0, elementHeight / 2.0);
        draw.drawString(draw.trimStringToWidth(this.displayString, (int)(elementWidth - this.checkBox.getWidth() - 10.0 - elementHeight)), x + elementHeight, y + elementHeight / 2.0 - 4.0);
        this.checkBox.setX((int)(x + elementWidth - this.checkBox.getWidth() - 5.0));
        this.checkBox.setY((int)(y + elementHeight / 2.0 - this.checkBox.getHeight() / 2));
        this.checkBox.drawCheckbox(mouseX, mouseY);
    }
    
    public void addSubSetting(final SkinLayerSettingElement subSetting) {
        this.subSettingElements.add(subSetting);
        subSetting.getCheckBox().setParentCheckBox(this.checkBox);
        this.checkBox.getChildCheckBoxes().add(subSetting.getCheckBox());
    }
    
    public CheckBox getCheckBox() {
        return this.checkBox;
    }
    
    public List<SkinLayerSettingElement> getSubSettingElements() {
        return this.subSettingElements;
    }
}
