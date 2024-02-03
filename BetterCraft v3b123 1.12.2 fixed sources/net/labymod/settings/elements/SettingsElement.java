// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import net.labymod.main.lang.LanguageManager;
import java.util.ArrayList;
import net.labymod.main.Source;
import net.labymod.api.permissions.Permissions;
import java.util.List;
import net.labymod.settings.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class SettingsElement
{
    public static final ResourceLocation buttonTextures;
    public static final ResourceLocation BUTTON_PRESS_SOUND;
    protected Minecraft mc;
    protected Settings subSettings;
    protected String displayName;
    private String descriptionText;
    protected boolean mouseOver;
    private int sortingId;
    private boolean visible;
    private String configEntryName;
    protected List<Permissions.Permission> permissions;
    
    static {
        buttonTextures = new ResourceLocation("textures/gui/widgets.png");
        BUTTON_PRESS_SOUND = new ResourceLocation(Source.ABOUT_MC_VERSION.startsWith("1.8") ? "gui.button.press" : "ui.button.click");
    }
    
    public SettingsElement(final String displayName, final String description, final String configEntryName) {
        this.mc = Minecraft.getMinecraft();
        this.subSettings = new Settings();
        this.sortingId = 0;
        this.visible = true;
        this.permissions = new ArrayList<Permissions.Permission>();
        this.displayName = displayName;
        this.descriptionText = description;
        this.configEntryName = configEntryName;
        this.preInit();
    }
    
    public SettingsElement(final String displayName, final String configEntryName) {
        this.mc = Minecraft.getMinecraft();
        this.subSettings = new Settings();
        this.sortingId = 0;
        this.visible = true;
        this.permissions = new ArrayList<Permissions.Permission>();
        this.displayName = displayName;
        this.initTranslation(this.configEntryName = configEntryName);
        this.preInit();
    }
    
    protected SettingsElement initTranslation(final String key) {
        if (key == null) {
            return this;
        }
        final String descriptionKey = "description_" + key.toLowerCase();
        this.descriptionText = LanguageManager.translate(descriptionKey);
        if (this.descriptionText.equals(descriptionKey)) {
            this.descriptionText = null;
        }
        final String titleKey = "setting_" + key.toLowerCase();
        final String displayName = LanguageManager.translate(titleKey);
        if (!displayName.equals(titleKey)) {
            this.displayName = displayName;
        }
        return this;
    }
    
    public SettingsElement bindPermission(final Permissions.Permission... permissions) {
        for (final Permissions.Permission permission : permissions) {
            this.permissions.add(permission);
        }
        return this;
    }
    
    public SettingsElement bindDescription(final String customDescription) {
        this.descriptionText = customDescription;
        return this;
    }
    
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        this.mouseOver = (mouseX > x && mouseX < maxX && mouseY > y && mouseY < maxY);
    }
    
    public abstract void drawDescription(final int p0, final int p1, final int p2);
    
    public abstract void mouseClicked(final int p0, final int p1, final int p2);
    
    public abstract void mouseRelease(final int p0, final int p1, final int p2);
    
    public abstract void mouseClickMove(final int p0, final int p1, final int p2);
    
    public abstract void keyTyped(final char p0, final int p1);
    
    public abstract void unfocus(final int p0, final int p1, final int p2);
    
    public abstract int getEntryHeight();
    
    public void preInit() {
    }
    
    public void init() {
    }
    
    public void updateScreen() {
    }
    
    public Minecraft getMc() {
        return this.mc;
    }
    
    public Settings getSubSettings() {
        return this.subSettings;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public String getDescriptionText() {
        return this.descriptionText;
    }
    
    public boolean isMouseOver() {
        return this.mouseOver;
    }
    
    public int getSortingId() {
        return this.sortingId;
    }
    
    public String getConfigEntryName() {
        return this.configEntryName;
    }
    
    public List<Permissions.Permission> getPermissions() {
        return this.permissions;
    }
    
    public void setMc(final Minecraft mc) {
        this.mc = mc;
    }
    
    public void setSubSettings(final Settings subSettings) {
        this.subSettings = subSettings;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public void setDescriptionText(final String descriptionText) {
        this.descriptionText = descriptionText;
    }
    
    public void setMouseOver(final boolean mouseOver) {
        this.mouseOver = mouseOver;
    }
    
    public void setSortingId(final int sortingId) {
        this.sortingId = sortingId;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public void setConfigEntryName(final String configEntryName) {
        this.configEntryName = configEntryName;
    }
    
    public void setPermissions(final List<Permissions.Permission> permissions) {
        this.permissions = permissions;
    }
}
