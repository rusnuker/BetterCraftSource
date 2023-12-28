// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import me.amkgre.bettercraft.client.utils.ClientSettingsUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiClientUI extends GuiScreen
{
    public GuiScreen before;
    public static boolean chatBackground;
    public static boolean scoreboardBackground;
    public static boolean tabBackground;
    public static boolean hotbar;
    public static boolean keystrokes;
    public static boolean armorstatus;
    public static boolean itemsize;
    public static boolean skin;
    public static boolean networksettings;
    public static boolean uhr;
    public static boolean blockoverlay;
    public static boolean radar;
    public static boolean chunkanimator;
    public static boolean fbp;
    private GuiButton button;
    
    static {
        GuiClientUI.chatBackground = ClientSettingsUtils.chatBackground;
        GuiClientUI.scoreboardBackground = ClientSettingsUtils.scoreboardBackground;
        GuiClientUI.tabBackground = ClientSettingsUtils.tabBackground;
        GuiClientUI.hotbar = ClientSettingsUtils.hotbar;
        GuiClientUI.keystrokes = ClientSettingsUtils.keystrokes;
        GuiClientUI.armorstatus = ClientSettingsUtils.armorstatus;
        GuiClientUI.itemsize = ClientSettingsUtils.itemsize;
        GuiClientUI.skin = ClientSettingsUtils.skin;
        GuiClientUI.networksettings = ClientSettingsUtils.networksettings;
        GuiClientUI.uhr = ClientSettingsUtils.uhr;
        GuiClientUI.blockoverlay = ClientSettingsUtils.blockoverlay;
        GuiClientUI.radar = ClientSettingsUtils.radar;
        GuiClientUI.chunkanimator = ClientSettingsUtils.chunkanimator;
        GuiClientUI.fbp = ClientSettingsUtils.fbp;
    }
    
    public GuiClientUI(final GuiScreen screen) {
        this.before = screen;
    }
    
    @Override
    public void updateScreen() {
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiClientUI.width - 70, GuiClientUI.height - 30, 60, 20, "Back"));
        this.buttonList.add(this.button = new GuiButton(1, GuiClientUI.width - 144 + 10, 20, 60, 20, GuiClientUI.chatBackground ? "§aChatBG" : "§cChatBG"));
        this.buttonList.add(this.button = new GuiButton(2, GuiClientUI.width - 144 + 74, 20, 60, 20, GuiClientUI.scoreboardBackground ? "§aSBBG" : "§cSBBG"));
        this.buttonList.add(this.button = new GuiButton(10, GuiClientUI.width - 144 - 54, 20, 60, 20, GuiClientUI.tabBackground ? "§aTabBG" : "§cTabBG"));
        this.buttonList.add(this.button = new GuiButton(11, GuiClientUI.width - 144 - 118, 20, 60, 20, GuiClientUI.hotbar ? "§aHotbar" : "§cHotbar"));
        this.buttonList.add(this.button = new GuiButton(15, GuiClientUI.width - 144 - 54, 65, 60, 20, GuiClientUI.itemsize ? "§aItemSize" : "§cItemSize"));
        this.buttonList.add(this.button = new GuiButton(3, GuiClientUI.width - 144 + 10, 65, 60, 20, GuiClientUI.keystrokes ? "§aKeystroke" : "§cKeystroke"));
        this.buttonList.add(this.button = new GuiButton(4, GuiClientUI.width - 144 + 74, 65, 60, 20, GuiClientUI.armorstatus ? "§aArmorstats" : "§cArmorstats"));
        this.buttonList.add(this.button = new GuiButton(14, GuiClientUI.width - 144 - 118, 110, 60, 20, GuiClientUI.blockoverlay ? "§aBlocklayer" : "§cBlocklayer"));
        this.buttonList.add(this.button = new GuiButton(13, GuiClientUI.width - 144 - 54, 110, 60, 20, GuiClientUI.uhr ? "§aClock" : "§cClock"));
        this.buttonList.add(this.button = new GuiButton(5, GuiClientUI.width - 144 + 74, 110, 60, 20, GuiClientUI.skin ? "§aSkin" : "§cSkin"));
        this.buttonList.add(this.button = new GuiButton(6, GuiClientUI.width - 144 + 10, 110, 60, 20, GuiClientUI.networksettings ? "§aNetwork" : "§cNetwork"));
        this.buttonList.add(this.button = new GuiButton(9, GuiClientUI.width - 144 + 74, 155, 60, 20, GuiClientUI.radar ? "§aRadar" : "§cRadar"));
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
        if (id == 15) {
            if (!GuiClientUI.itemsize) {
                button.displayString = "§aItemSize";
                GuiClientUI.itemsize = (ClientSettingsUtils.itemsize = true);
            }
            else {
                button.displayString = "§cItemSize";
                GuiClientUI.itemsize = (ClientSettingsUtils.itemsize = false);
            }
        }
        if (id == 14) {
            if (!GuiClientUI.blockoverlay) {
                button.displayString = "§aBlocklayer";
                GuiClientUI.blockoverlay = (ClientSettingsUtils.blockoverlay = true);
            }
            else {
                button.displayString = "§cBlocklayer";
                GuiClientUI.blockoverlay = (ClientSettingsUtils.blockoverlay = false);
            }
        }
        if (id == 13) {
            if (!GuiClientUI.uhr) {
                button.displayString = "§aClock";
                GuiClientUI.uhr = (ClientSettingsUtils.uhr = true);
            }
            else {
                button.displayString = "§cClock";
                GuiClientUI.uhr = (ClientSettingsUtils.uhr = false);
            }
        }
        if (id == 11) {
            if (!GuiClientUI.hotbar) {
                button.displayString = "§aHotbar";
                GuiClientUI.hotbar = (ClientSettingsUtils.hotbar = true);
            }
            else {
                button.displayString = "§cHotbar";
                GuiClientUI.hotbar = (ClientSettingsUtils.hotbar = false);
            }
        }
        if (id == 10) {
            if (!GuiClientUI.tabBackground) {
                button.displayString = "§aTabBG";
                GuiClientUI.tabBackground = (ClientSettingsUtils.tabBackground = true);
            }
            else {
                button.displayString = "§cTabBG";
                GuiClientUI.tabBackground = (ClientSettingsUtils.tabBackground = false);
            }
        }
        if (id == 1) {
            if (!GuiClientUI.chatBackground) {
                button.displayString = "§aChatBG";
                GuiClientUI.chatBackground = (ClientSettingsUtils.chatBackground = true);
            }
            else {
                button.displayString = "§cChatBG";
                GuiClientUI.chatBackground = (ClientSettingsUtils.chatBackground = false);
            }
        }
        if (id == 2) {
            if (!GuiClientUI.scoreboardBackground) {
                button.displayString = "§aSBBG";
                GuiClientUI.scoreboardBackground = (ClientSettingsUtils.scoreboardBackground = true);
            }
            else {
                button.displayString = "§cSBBG";
                GuiClientUI.scoreboardBackground = (ClientSettingsUtils.scoreboardBackground = false);
            }
        }
        if (id == 3) {
            if (!GuiClientUI.keystrokes) {
                button.displayString = "§aKeystroke";
                GuiClientUI.keystrokes = (ClientSettingsUtils.keystrokes = true);
            }
            else {
                button.displayString = "§cKeystroke";
                GuiClientUI.keystrokes = (ClientSettingsUtils.keystrokes = false);
            }
        }
        if (id == 4) {
            if (!GuiClientUI.armorstatus) {
                button.displayString = "§aArmorstats";
                GuiClientUI.armorstatus = (ClientSettingsUtils.armorstatus = true);
            }
            else {
                button.displayString = "§cArmorstats";
                GuiClientUI.armorstatus = (ClientSettingsUtils.armorstatus = false);
            }
        }
        if (id == 5) {
            if (!GuiClientUI.skin) {
                button.displayString = "§aSkin";
                GuiClientUI.skin = (ClientSettingsUtils.skin = true);
            }
            else {
                button.displayString = "§cSkin";
                GuiClientUI.skin = (ClientSettingsUtils.skin = false);
            }
        }
        if (id == 6) {
            if (!GuiClientUI.networksettings) {
                button.displayString = "§aNetwork";
                GuiClientUI.networksettings = (ClientSettingsUtils.networksettings = true);
            }
            else {
                button.displayString = "§cNetwork";
                GuiClientUI.networksettings = (ClientSettingsUtils.networksettings = false);
            }
        }
        if (id == 9) {
            if (!GuiClientUI.radar) {
                button.displayString = "§aRadar";
                GuiClientUI.radar = (ClientSettingsUtils.radar = true);
            }
            else {
                button.displayString = "§cRadar";
                GuiClientUI.radar = (ClientSettingsUtils.radar = false);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.mc.fontRendererObj.drawString("§7UI", GuiClientUI.width - 77, 7, -1);
        this.mc.fontRendererObj.drawString("§7PvP", GuiClientUI.width - 80, 52, -1);
        this.mc.fontRendererObj.drawString("§7Misc", GuiClientUI.width - 82, 97, -1);
        this.mc.fontRendererObj.drawString("§7Tools", GuiClientUI.width - 83, 142, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
