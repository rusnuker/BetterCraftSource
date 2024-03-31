// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.montoyo.mcef.example.BrowserScreen;
import me.amkgre.bettercraft.client.mods.discord.rpc.GuiDiscordRPC;
import me.amkgre.bettercraft.client.mods.rcon.GuiRconConnection;
import me.amkgre.bettercraft.client.mods.proxy.GuiProxy;
import me.amkgre.bettercraft.client.mods.music.GuiMusic;
import me.amkgre.bettercraft.client.mods.fritzbox.GuiFritzbox;
import me.amkgre.bettercraft.client.mods.spoofer.GuiSpoofer;
import me.amkgre.bettercraft.client.mods.portscanner.GuiPortScanner;
import me.amkgre.bettercraft.client.mods.checkhost.GuiCheckHost;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiTools extends GuiScreen
{
    private Minecraft mc;
    private GuiScreen before;
    
    public GuiTools(final GuiScreen before) {
        this.mc = Minecraft.getMinecraft();
        this.before = before;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, GuiTools.width / 2 - 100, GuiTools.height / 3 - 40, "Checkhost"));
        this.buttonList.add(new GuiButton(2, GuiTools.width / 2 - 100, GuiTools.height / 3 - 15, 98, 20, "Port Scanner"));
        this.buttonList.add(new GuiButton(3, GuiTools.width / 2 + 2, GuiTools.height / 3 - 15, 98, 20, "Spoofer"));
        this.buttonList.add(new GuiButton(4, GuiTools.width / 2 - 100, GuiTools.height / 3 + 10, 98, 20, "Fritzbox"));
        this.buttonList.add(new GuiButton(5, GuiTools.width / 2 + 2, GuiTools.height / 3 + 10, 98, 20, "Music"));
        this.buttonList.add(new GuiButton(6, GuiTools.width / 2 - 100, GuiTools.height / 3 + 35, 98, 20, "Proxy"));
        this.buttonList.add(new GuiButton(7, GuiTools.width / 2 + 2, GuiTools.height / 3 + 35, 98, 20, "Rcon"));
        this.buttonList.add(new GuiButton(8, GuiTools.width / 2 - 100, GuiTools.height / 3 + 60, "Discord"));
        this.buttonList.add(new GuiButton(9, 6, GuiTools.height - 26, GuiTools.width / (GuiTools.width / 2) + GuiTools.width / 6, 20, "Browser"));
        this.buttonList.add(new GuiButton(0, GuiTools.width - (GuiTools.width / (GuiTools.width / 2) + GuiTools.width / 6) - 6, GuiTools.height - 26, GuiTools.width / (GuiTools.width / 2) + GuiTools.width / 6, 20, "Back"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final int id = button.id;
        if (id == 1) {
            this.mc.displayGuiScreen(new GuiCheckHost(this));
        }
        if (id == 2) {
            this.mc.displayGuiScreen(new GuiPortScanner(this));
        }
        if (id == 3) {
            this.mc.displayGuiScreen(new GuiSpoofer(this));
        }
        if (id == 4) {
            this.mc.displayGuiScreen(new GuiFritzbox(this));
        }
        if (id == 5) {
            this.mc.displayGuiScreen(new GuiMusic(this));
        }
        if (id == 6) {
            this.mc.displayGuiScreen(new GuiProxy(this));
        }
        if (id == 7) {
            this.mc.displayGuiScreen(new GuiRconConnection(this));
        }
        if (id == 8) {
            this.mc.displayGuiScreen(new GuiDiscordRPC(this));
        }
        if (id == 9) {
            this.mc.displayGuiScreen(new BrowserScreen());
        }
        if (id == 0) {
            this.mc.displayGuiScreen(this.before);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.scale(4.0f, 4.0f, 1.0f);
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.scale(0.5, 0.5, 1.0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
