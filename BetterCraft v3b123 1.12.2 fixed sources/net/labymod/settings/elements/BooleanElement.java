// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import net.minecraft.client.Minecraft;
import net.labymod.utils.ModColor;
import net.labymod.core.LabyModCore;
import net.labymod.servermanager.Server;
import net.labymod.main.LabyMod;
import net.labymod.main.ModSettings;
import net.minecraft.client.gui.GuiButton;
import net.labymod.utils.Consumer;

public class BooleanElement extends ControlElement
{
    private boolean currentValue;
    private Consumer<Boolean> toggleListener;
    private GuiButton buttonToggle;
    private String stringEnabled;
    private String stringDisabled;
    private Consumer<Boolean> callback;
    
    public BooleanElement(final String displayName, final String configEntryName, final IconData iconData) {
        super(displayName, configEntryName, iconData);
        this.stringEnabled = "ON";
        this.stringDisabled = "OFF";
        if (configEntryName != null) {
            if (!configEntryName.isEmpty()) {
                try {
                    this.currentValue = ModSettings.class.getDeclaredField(configEntryName).getBoolean(LabyMod.getSettings());
                }
                catch (final IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (final NoSuchFieldException e2) {
                    e2.printStackTrace();
                }
            }
            this.toggleListener = new Consumer<Boolean>() {
                @Override
                public void accept(final Boolean accepted) {
                    try {
                        ModSettings.class.getDeclaredField(configEntryName).set(LabyMod.getSettings(), accepted);
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                    BooleanElement.this.setSettingEnabled(accepted);
                }
            };
        }
        this.createButton();
    }
    
    public BooleanElement(final String displayName, final Server server, final IconData iconData, final String attributeName) {
        super(displayName, null, iconData);
        this.stringEnabled = "ON";
        this.stringDisabled = "OFF";
        this.currentValue = server.getConfig().get(attributeName).getAsBoolean();
        this.toggleListener = new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted) {
                server.getConfig().addProperty(attributeName, accepted);
                server.saveConfig();
                server.loadConfig();
                BooleanElement.this.setSettingEnabled(accepted);
            }
        };
        this.createButton();
    }
    
    public BooleanElement(final String configEntryName, final IconData iconData) {
        this(configEntryName, configEntryName, iconData);
    }
    
    public void createButton() {
        this.buttonToggle = new GuiButton(-2, 0, 0, 0, 20, "");
        this.setSettingEnabled(this.currentValue);
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        if (this.buttonToggle == null) {
            return;
        }
        this.buttonToggle.enabled = false;
        LabyModCore.getMinecraft().setButtonYPosition(this.buttonToggle, y + 1);
        LabyModCore.getMinecraft().drawButton(this.buttonToggle, mouseX, mouseY);
        this.buttonToggle.enabled = true;
        final int buttonWidth = this.buttonToggle.getButtonWidth();
        final int valueXPos = this.currentValue ? ((buttonWidth - 4) / 2) : ((buttonWidth - 4) / 2 + 6);
        final String displayString = (this.buttonToggle.hovered ? ModColor.YELLOW : (this.currentValue ? ModColor.WHITE : ModColor.GRAY)) + (this.currentValue ? this.stringEnabled : this.stringDisabled);
        LabyMod.getInstance().getDrawUtils().drawCenteredString(displayString, LabyModCore.getMinecraft().getXPosition(this.buttonToggle) + valueXPos, LabyModCore.getMinecraft().getYPosition(this.buttonToggle) + 6);
        LabyMod.getInstance().getDrawUtils().drawString(this.currentValue ? ModColor.GREEN.toString() : ModColor.RED.toString(), 0.0, 0.0);
        this.mc.getTextureManager().bindTexture(BooleanElement.buttonTextures);
        LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, this.currentValue ? ModColor.toRGB(20, 120, 20, 120) : ModColor.toRGB(120, 20, 20, 120));
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.buttonToggle.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
            this.currentValue = !this.currentValue;
            if (this.toggleListener != null) {
                this.toggleListener.accept(this.currentValue);
            }
            if (this.callback != null) {
                this.callback.accept(this.currentValue);
            }
            this.buttonToggle.playPressSound(this.mc.getSoundHandler());
        }
    }
    
    public BooleanElement custom(final String... args) {
        if (args.length >= 1) {
            this.stringEnabled = args[0];
        }
        if (args.length >= 2) {
            this.stringDisabled = args[1];
        }
        return this;
    }
    
    public BooleanElement addCallback(final Consumer<Boolean> callback) {
        this.callback = callback;
        return this;
    }
    
    public boolean getCurrentValue() {
        return this.currentValue;
    }
}
