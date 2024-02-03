// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.FontRenderer;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.gui.GuiMainMenu;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import me.nzxtercode.bettercraft.client.Config;
import net.minecraft.client.gui.GuiScreen;

public class GuiFirstTime extends GuiScreen
{
    public static boolean isEnabled;
    
    static {
        GuiFirstTime.isEnabled = Config.getInstance().getConfig("FirstTime").getAsJsonObject().get("enabled").getAsBoolean();
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
        Config.getInstance().editConfig("FirstTime", json -> {
            new JsonPrimitive(!GuiFirstTime.isEnabled);
            final JsonPrimitive value;
            final Object property;
            json.add((String)property, value);
        });
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.shutdownMinecraftApplet();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMainMenu());
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, "Welcome!", GuiFirstTime.width / 2, 20 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Thank you for downloading and installing our client!", GuiFirstTime.width / 2, 40 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        final FontRenderer fontRendererObj = this.fontRendererObj;
        final StringBuilder sb = new StringBuilder("Here is some information you might need if you are using §c§l§n");
        BetterCraft.getInstance();
        Gui.drawCenteredString(fontRendererObj, sb.append(BetterCraft.clientName).append("§r for the first time").toString(), GuiFirstTime.width / 2, 50 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "UI editing", GuiFirstTime.width / 2, 70 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Press M to open up the UI options", GuiFirstTime.width / 2, 80 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "NBTEdit", GuiFirstTime.width / 2, 100 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Press N with NBTData Item to open up the NBTEdit", GuiFirstTime.width / 2, 110 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Creative Own Items", GuiFirstTime.width / 2, 130 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Press I to save a Item to own items creative tab", GuiFirstTime.width / 2, 140 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Important Commands", GuiFirstTime.width / 2, 160 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, ".help", GuiFirstTime.width / 2, 170 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "Need help? Feel free to contact us!", GuiFirstTime.width / 2, 190 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        Gui.drawCenteredString(this.fontRendererObj, "https://nzxter.de.cool/", GuiFirstTime.width / 2, 200 + this.fontRendererObj.FONT_HEIGHT, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
