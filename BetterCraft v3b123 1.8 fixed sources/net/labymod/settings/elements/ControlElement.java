// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import net.minecraft.util.ResourceLocation;
import net.labymod.utils.Material;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.core.LabyModCore;
import net.labymod.utils.DrawUtils;
import java.util.Iterator;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.ModTextures;
import net.labymod.api.permissions.Permissions;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;

public class ControlElement extends SettingsElement
{
    protected IconData iconData;
    private GuiButton buttonAdvanced;
    private boolean selected;
    private boolean hoverable;
    private boolean hideSubList;
    private boolean settingEnabled;
    private int lastMaxX;
    private boolean blocked;
    
    public ControlElement(final String elementName, final String configEntryName, final IconData iconData) {
        super(elementName, configEntryName);
        this.blocked = false;
        this.iconData = iconData;
        if (this.iconData != null && this.iconData.isUseConfigName()) {
            this.iconData.apply((configEntryName == null) ? elementName : configEntryName);
        }
        this.createButton();
    }
    
    public ControlElement(final String displayName, final IconData iconData) {
        super(displayName, null);
        this.blocked = false;
        this.iconData = iconData;
        this.createButton();
    }
    
    private void createButton() {
        this.buttonAdvanced = new GuiButton(-2, 0, 0, 23, 20, "");
    }
    
    public GuiButton getButtonAdvanced() {
        return this.buttonAdvanced;
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        this.lastMaxX = maxX;
        if (this.displayName != null) {
            LabyMod.getInstance().getDrawUtils().drawRectangle(x, y, maxX, maxY, ModColor.toRGB(80, 80, 80, this.selected ? 130 : ((this.hoverable && this.mouseOver) ? 80 : 60)));
            final int iconWidth = (this.iconData != null) ? 25 : 2;
            if (this.iconData != null) {
                if (this.iconData.hastextureIcon()) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(this.iconData.gettextureIcon());
                    LabyMod.getInstance().getDrawUtils().drawTexture(x + 3, y + 3, 256.0, 256.0, 16.0, 16.0);
                }
                else if (this.iconData.hasMaterialIcon()) {
                    LabyMod.getInstance().getDrawUtils().drawItem(this.iconData.getMaterialIcon().createItemStack(), x + 3, y + 2, null);
                }
            }
            this.hideSubList = false;
            if (!this.permissions.isEmpty()) {
                boolean defaultDisabled = false;
                boolean isEnabledByServer = true;
                String allowedPermissions = "";
                for (final Permissions.Permission permission : this.permissions) {
                    if (!permission.isDefaultEnabled()) {
                        defaultDisabled = true;
                    }
                    if (!allowedPermissions.isEmpty()) {
                        allowedPermissions = String.valueOf(allowedPermissions) + ModColor.cl("7") + ", ";
                    }
                    allowedPermissions = String.valueOf(allowedPermissions) + ModColor.cl("e") + permission.getDisplayName();
                    if (!Permissions.isAllowed(permission)) {
                        isEnabledByServer = false;
                    }
                }
                if (defaultDisabled || !isEnabledByServer) {
                    final boolean hover = mouseX > x - 13 && mouseX < x - 13 + 7 && mouseY > y + 3 && mouseY < y + 3 + 16;
                    final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
                    if (isEnabledByServer) {
                        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_CHECKBOX);
                        draw.drawTexture(x - 13, y + 7, 0.0, 0.0, 255.0, 255.0, 10.0, 10.0);
                    }
                    else {
                        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_EXCLAMATION);
                        draw.drawTexture(x - 13, y + 3, hover ? 127.0 : 0.0, 0.0, 127.0, 255.0, 7.0, 16.0);
                    }
                    if (hover) {
                        final String text = String.valueOf(LanguageManager.translate(new StringBuilder("permission_information_").append(isEnabledByServer ? "enabled" : "disabled").toString())) + (isEnabledByServer ? "" : allowedPermissions);
                        TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, (String[])draw.listFormattedStringToWidth(text, draw.getWidth() / 3).toArray());
                    }
                }
            }
            if (this.blocked) {
                LabyMod.getInstance().getDrawUtils().drawRectangle(x, y, maxX, maxY, ModColor.toRGB(0, 0, 0, 200));
            }
        }
    }
    
    private void renderAdvancedButton(final int x, final int y, final int maxX, final int maxY, final boolean mouseOver, final int mouseX, final int mouseY) {
        if (!this.hasSubList()) {
            return;
        }
        if (this.buttonAdvanced == null) {
            return;
        }
        if (this.hideSubList) {
            return;
        }
        final boolean enabled = this.settingEnabled;
        LabyModCore.getMinecraft().setButtonXPosition(this.buttonAdvanced, maxX - this.getSubListButtonWidth() - 2);
        LabyModCore.getMinecraft().setButtonYPosition(this.buttonAdvanced, y + 1);
        this.buttonAdvanced.enabled = enabled;
        LabyModCore.getMinecraft().drawButton(this.buttonAdvanced, mouseX, mouseY);
        this.mc.getTextureManager().bindTexture(ModTextures.BUTTON_ADVANCED);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, enabled ? 1.0f : 0.2f);
        LabyMod.getInstance().getDrawUtils().drawTexture(LabyModCore.getMinecraft().getXPosition(this.buttonAdvanced) + 4, LabyModCore.getMinecraft().getYPosition(this.buttonAdvanced) + 3, 0.0, 0.0, 256.0, 256.0, 14.0, 14.0, 2.0f);
    }
    
    public int getSubListButtonWidth() {
        return this.hasSubList() ? 23 : 0;
    }
    
    @Override
    public void drawDescription(final int x, final int y, final int screenWidth) {
        final String description = this.getDescriptionText();
        if (description == null) {
            return;
        }
        if (this.buttonAdvanced != null && this.buttonAdvanced.hovered) {
            return;
        }
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final List<String> list = draw.listFormattedStringToWidth(description, screenWidth / 3);
        TooltipHelper.getHelper().pointTooltip(x, y, 500L, (String[])list.toArray());
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public int getEntryHeight() {
        return 23;
    }
    
    public boolean hasSubList() {
        return !this.subSettings.getElements().isEmpty() && !this.hideSubList;
    }
    
    public ControlElement setSelected(final boolean selected) {
        this.selected = selected;
        return this;
    }
    
    public ControlElement setHoverable(final boolean hoverable) {
        this.hoverable = hoverable;
        return this;
    }
    
    public ControlElement hideSubListButton() {
        this.hideSubList = true;
        return this;
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
    
    public IconData getIconData() {
        return this.iconData;
    }
    
    public void setSettingEnabled(final boolean settingEnabled) {
        this.settingEnabled = settingEnabled;
    }
    
    public boolean isBlocked() {
        return this.blocked;
    }
    
    public void setBlocked(final boolean blocked) {
        this.blocked = blocked;
    }
    
    public static class IconData
    {
        private boolean useConfigName;
        private Material materialIcon;
        private ResourceLocation textureIcon;
        
        public IconData(final Material materialIcon) {
            this.useConfigName = false;
            this.materialIcon = materialIcon;
        }
        
        public IconData(final ResourceLocation textureIcon) {
            this.useConfigName = false;
            this.textureIcon = textureIcon;
        }
        
        public IconData(final String resourceLocationPath) {
            this.useConfigName = false;
            this.textureIcon = new ResourceLocation(resourceLocationPath);
        }
        
        public IconData() {
            this.useConfigName = false;
            this.useConfigName = true;
        }
        
        public Material getMaterialIcon() {
            return this.materialIcon;
        }
        
        public ResourceLocation gettextureIcon() {
            return this.textureIcon;
        }
        
        public boolean hastextureIcon() {
            return this.textureIcon != null;
        }
        
        public boolean hasMaterialIcon() {
            return this.materialIcon != null;
        }
        
        public boolean isUseConfigName() {
            return this.useConfigName;
        }
        
        public void apply(final String configEntryName) {
            this.textureIcon = new ResourceLocation("labymod/textures/settings/settings/" + configEntryName.toLowerCase() + ".png");
        }
    }
}
