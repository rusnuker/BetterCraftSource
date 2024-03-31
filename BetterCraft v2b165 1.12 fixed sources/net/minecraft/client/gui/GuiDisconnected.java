// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.multiplayer.ServerData;
import java.util.List;
import net.minecraft.util.text.ITextComponent;

public class GuiDisconnected extends GuiScreen
{
    private final String reason;
    private final ITextComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int textHeight;
    private GuiButton refreshButton;
    public static ServerData lastlogin;
    public static boolean useTheAltening;
    int a;
    int d;
    int scrollspeed;
    
    public GuiDisconnected(final GuiScreen screen, final String reasonLocalizationKey, final ITextComponent chatComp) {
        this.a = 0;
        this.d = 1;
        this.scrollspeed = 1;
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth((this.message.getFormattedText().length() < 2500) ? this.message.getFormattedText() : "AntiNewlineCrash", GuiDisconnected.width - 50);
        this.textHeight = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, GuiDisconnected.width / 2 + 2, GuiDisconnected.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT, 98, 20, "Back"));
        this.buttonList.add(this.refreshButton = new GuiButton(1, GuiDisconnected.width / 2 - 100, GuiDisconnected.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT, 98, 20, "§cReconnect"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 1) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), this.mc, GuiDisconnected.lastlogin));
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, this.reason, GuiDisconnected.width / 2, GuiDisconnected.height / 2 - this.textHeight / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = GuiDisconnected.height / 2 - this.textHeight / 2;
        if (this.multilineMessage != null) {
            String rawText;
            int rows;
            if (this.multilineMessage.size() == 0) {
                rawText = "";
                rows = 0;
            }
            else if (this.multilineMessage.size() == 1) {
                rawText = this.multilineMessage.get(0);
                rows = 1;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                for (final String s : this.multilineMessage) {
                    sb.append('\n').append(s);
                }
                rawText = sb.toString().substring(1);
                rows = this.multilineMessage.size();
            }
            this.scrollspeed = rows / 2;
            if (this.scrollspeed <= 1) {
                this.scrollspeed = 1;
            }
            this.d += this.scrollspeed;
            int c = this.d;
            if (c > rawText.length()) {
                c = rawText.length();
            }
            if (c <= 0) {
                c = 0;
            }
            rawText = rawText.substring(0, ColorUtils.getRealStringLength(rawText, c));
            String[] split;
            for (int length = (split = rawText.split("\n")).length, j = 0; j < length; ++j) {
                final String f = split[j];
                Gui.drawCenteredString(this.fontRendererObj, f, GuiDisconnected.width / 2, i, -1);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
