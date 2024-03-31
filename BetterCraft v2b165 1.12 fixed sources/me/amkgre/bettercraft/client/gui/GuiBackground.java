// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui;

import java.io.IOException;
import me.amkgre.bettercraft.client.mods.shader.old.GuiShaderOld;
import me.amkgre.bettercraft.client.mods.shader.browser.GuiShaderBrowser;
import me.amkgre.bettercraft.client.utils.FileChooserUtils;
import net.minecraft.client.gui.GuiButton;
import me.amkgre.bettercraft.client.utils.ClientSettingsUtils;
import net.minecraft.client.gui.GuiScreen;

public class GuiBackground extends GuiScreen
{
    public static boolean animbutton;
    public static boolean oldbutton;
    public static boolean transbutton;
    public static boolean mcbutton;
    private GuiScreen parentScreen;
    
    static {
        GuiBackground.animbutton = ClientSettingsUtils.animbutton;
        GuiBackground.oldbutton = ClientSettingsUtils.oldbutton;
        GuiBackground.transbutton = ClientSettingsUtils.transbutton;
        GuiBackground.mcbutton = ClientSettingsUtils.mcbutton;
    }
    
    public GuiBackground(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, GuiBackground.width - (GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6) - 6, GuiBackground.height - 26, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, "Back"));
        this.buttonList.add(new GuiButton(3, 6, GuiBackground.height - 26, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, "Shader Browser"));
        this.buttonList.add(new GuiButton(4, 6, GuiBackground.height - 50, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, "Old Shaders"));
        this.buttonList.add(new GuiButton(12345, 5, 5, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, "Change BG"));
        this.buttonList.add(new GuiButton(123456, 5, 30, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, "Custom BG"));
        this.buttonList.add(new GuiButton(12, GuiBackground.width - (GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6) - 5, 5, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, GuiBackground.animbutton ? "§aAnimButton" : "§cAnimButton"));
        this.buttonList.add(new GuiButton(13, GuiBackground.width - (GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6) - 5, 30, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, GuiBackground.transbutton ? "§aTransButton" : "§cTransButton"));
        this.buttonList.add(new GuiButton(14, GuiBackground.width - (GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6) - 5, 55, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, GuiBackground.oldbutton ? "§aOldButton" : "§cOldButton"));
        this.buttonList.add(new GuiButton(15, GuiBackground.width - (GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6) - 5, 80, GuiBackground.width / (GuiBackground.width / 2) + GuiBackground.width / 6, 20, GuiBackground.mcbutton ? "§aMCButton" : "§cMCButton"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 123456: {
                final FileChooserUtils fileChooser = new FileChooserUtils();
                break;
            }
            case 12345: {
                try {
                    ClientSettingsUtils.strToField(ClientSettingsUtils.class.getDeclaredField("isCurrentBackgroundImageCustom"), "false");
                    if (Integer.parseInt(ClientSettingsUtils.fieldToStr(ClientSettingsUtils.class.getDeclaredField("currentBackgroundImage")).split("=")[1]) < 3) {
                        ClientSettingsUtils.strToField(ClientSettingsUtils.class.getDeclaredField("currentBackgroundImage"), String.valueOf(Integer.parseInt(ClientSettingsUtils.fieldToStr(ClientSettingsUtils.class.getDeclaredField("currentBackgroundImage")).split("=")[1]) + 1));
                    }
                    else {
                        ClientSettingsUtils.strToField(ClientSettingsUtils.class.getDeclaredField("currentBackgroundImage"), "0");
                    }
                }
                catch (final NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 0: {
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiShaderBrowser(this));
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiShaderOld(this));
                break;
            }
            case 12: {
                if (!GuiBackground.animbutton) {
                    button.displayString = "§aAnimButton";
                    GuiBackground.animbutton = (ClientSettingsUtils.animbutton = true);
                    break;
                }
                button.displayString = "§cAnimButton";
                GuiBackground.animbutton = (ClientSettingsUtils.animbutton = false);
                break;
            }
            case 13: {
                if (!GuiBackground.transbutton) {
                    button.displayString = "§aTransButton";
                    GuiBackground.transbutton = (ClientSettingsUtils.transbutton = true);
                    break;
                }
                button.displayString = "§cTransButton";
                GuiBackground.transbutton = (ClientSettingsUtils.transbutton = false);
                break;
            }
            case 14: {
                if (!GuiBackground.oldbutton) {
                    button.displayString = "§aOldButton";
                    GuiBackground.oldbutton = (ClientSettingsUtils.oldbutton = true);
                    break;
                }
                button.displayString = "§cOldButton";
                GuiBackground.oldbutton = (ClientSettingsUtils.oldbutton = false);
                break;
            }
            case 15: {
                if (!GuiBackground.mcbutton) {
                    button.displayString = "§aMCButton";
                    GuiBackground.mcbutton = (ClientSettingsUtils.mcbutton = true);
                    break;
                }
                button.displayString = "§cMCButton";
                GuiBackground.mcbutton = (ClientSettingsUtils.mcbutton = false);
                break;
            }
        }
    }
    
    private void refresh() {
        this.buttonList.clear();
        this.initGui();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
