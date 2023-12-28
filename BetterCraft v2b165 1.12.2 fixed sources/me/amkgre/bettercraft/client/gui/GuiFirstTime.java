// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.input.Keyboard;
import me.amkgre.bettercraft.client.utils.ClientSettingsUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiFirstTime extends GuiScreen
{
    public GuiScreen before;
    public static boolean firstTime;
    private GuiButton button;
    
    static {
        GuiFirstTime.firstTime = ClientSettingsUtils.firstTime;
    }
    
    public GuiFirstTime(final GuiScreen screen) {
        this.before = screen;
    }
    
    @Override
    public void updateScreen() {
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiFirstTime.width - (GuiFirstTime.width / (GuiFirstTime.width / 2) + GuiFirstTime.width / 6) - 6, GuiFirstTime.height - 26, GuiFirstTime.width / (GuiFirstTime.width / 2) + GuiFirstTime.width / 6, 20, "Quit"));
        this.buttonList.add(new GuiButton(1, 6, GuiFirstTime.height - 26, GuiFirstTime.width / (GuiFirstTime.width / 2) + GuiFirstTime.width / 6, 20, "Ok"));
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final int id = button.id;
        if (id == 0) {
            this.mc.shutdownMinecraftApplet();
        }
        if (id == 1) {
            GuiFirstTime.firstTime = (ClientSettingsUtils.firstTime = true);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, "Welcome!", GuiFirstTime.width / 2, 20 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Thank you for downloading and installing our client!", GuiFirstTime.width / 2, 40 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Here is some information you might need if you are using BetterCraft for the first time", GuiFirstTime.width / 2, 50 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "UI editing", GuiFirstTime.width / 2, 70 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Press RSHIFT to open up the UI options", GuiFirstTime.width / 2, 80 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "NBTEdit", GuiFirstTime.width / 2, 100 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Press N with NBTData Item to open up the NBTEdit", GuiFirstTime.width / 2, 110 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Important Commands", GuiFirstTime.width / 2, 130 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "+help 1", GuiFirstTime.width / 2, 140 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Need help? Feel free to contact us!", GuiFirstTime.width / 2, 160 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Discord https://dsc.gg/nzxterdc", GuiFirstTime.width / 2, 170 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
