// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import me.amkgre.bettercraft.client.utils.ClientSettingsUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiMods extends GuiScreen
{
    public GuiScreen before;
    public static boolean esp;
    public static boolean nametags;
    public static boolean chunkanimator;
    public static boolean fbp;
    private GuiButton button;
    
    static {
        GuiMods.esp = ClientSettingsUtils.esp;
        GuiMods.nametags = ClientSettingsUtils.nametags;
        GuiMods.chunkanimator = ClientSettingsUtils.chunkanimator;
        GuiMods.fbp = ClientSettingsUtils.fbp;
    }
    
    public GuiMods(final GuiScreen screen) {
        this.before = screen;
    }
    
    @Override
    public void updateScreen() {
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiMods.width / 2 - 100, GuiMods.height / 3 + 100, 200, 20, "Back"));
        this.buttonList.add(this.button = new GuiButton(7, GuiMods.width / 2 - 100, GuiMods.height / 3 + 10, 98, 20, GuiMods.esp ? "§aESP" : "§cESP"));
        this.buttonList.add(this.button = new GuiButton(8, GuiMods.width / 2 + 2, GuiMods.height / 3 + 10, 98, 20, GuiMods.nametags ? "§aNametags" : "§cNametags"));
        this.buttonList.add(this.button = new GuiButton(11, GuiMods.width / 2 - 100, GuiMods.height / 3 + 35, 98, 20, GuiMods.chunkanimator ? "§aChunkanim" : "§cChunkanim"));
        this.buttonList.add(this.button = new GuiButton(12, GuiMods.width / 2 + 2, GuiMods.height / 3 + 35, 98, 20, GuiMods.fbp ? "§aFBP" : "§cFBP"));
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final int id = button.id;
        if (id == 0) {
            this.mc.displayGuiScreen(this.before);
        }
        if (id == 7) {
            if (!GuiMods.esp) {
                button.displayString = "§aESP";
                GuiMods.esp = (ClientSettingsUtils.esp = true);
            }
            else {
                button.displayString = "§cESP";
                GuiMods.esp = (ClientSettingsUtils.esp = false);
            }
        }
        if (id == 8) {
            if (!GuiMods.nametags) {
                button.displayString = "§aNametags";
                GuiMods.nametags = (ClientSettingsUtils.nametags = true);
            }
            else {
                button.displayString = "§cNametags";
                GuiMods.nametags = (ClientSettingsUtils.nametags = false);
            }
        }
        if (id == 11) {
            if (!GuiMods.chunkanimator) {
                button.displayString = "§aChunkanim";
                GuiMods.chunkanimator = (ClientSettingsUtils.chunkanimator = true);
            }
            else {
                button.displayString = "§cChunkanim";
                GuiMods.chunkanimator = (ClientSettingsUtils.chunkanimator = false);
            }
        }
        if (id == 12) {
            if (!GuiMods.fbp) {
                button.displayString = "§aFBP";
                GuiMods.fbp = (ClientSettingsUtils.fbp = true);
            }
            else {
                button.displayString = "§cFBP";
                GuiMods.fbp = (ClientSettingsUtils.fbp = false);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
