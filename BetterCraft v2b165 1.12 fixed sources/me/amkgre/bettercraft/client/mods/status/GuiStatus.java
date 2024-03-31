// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.status;

import java.net.URLConnection;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraft.client.gui.Gui;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiStatus extends GuiScreen
{
    private GuiButton login;
    private GuiTextField name;
    private String err;
    private boolean error;
    private boolean mc2;
    private String minecraft;
    private String session;
    private String account;
    private String authserver;
    private String sessionserver;
    private String api;
    private String textures;
    private String mojang;
    private GuiScreen before;
    
    public GuiStatus(final GuiScreen before) {
        this.error = false;
        this.mc2 = false;
        this.before = before;
    }
    
    @Override
    public void initGui() {
        this.getStatus();
        this.buttonList.add(new GuiButton(1, GuiStatus.width / 2 - 75, GuiStatus.height / 2 + 80, 150, 20, "Back"));
        Keyboard.enableRepeatEvents(true);
        (this.name = new GuiTextField(2, this.fontRendererObj, GuiStatus.width / 2 - 75, GuiStatus.height / 2 - 10, 150, 20)).setMaxStringLength(Integer.MAX_VALUE);
        super.initGui();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.name.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.name.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void updateScreen() {
        this.name.updateCursorCounter();
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        RenderUtils.drawBorderedRect(GuiStatus.width / 2 - 75, GuiStatus.height / 2 + 60, GuiStatus.width / 2 + 75, GuiStatus.height / 2 - 100, ColorUtils.rainbowEffect(0L, 1.0f).getRGB(), Integer.MIN_VALUE);
        if (this.mc2) {
            Gui.drawCenteredString(this.fontRendererObj, "§7§m-----§7(§2§l Minecraft §7)§7§m-----", GuiStatus.width / 2 + 3, GuiStatus.height / 2 - 90, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.minecraft, GuiStatus.width / 2, GuiStatus.height / 2 - 70, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.session, GuiStatus.width / 2, GuiStatus.height / 2 - 60, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.textures, GuiStatus.width / 2, GuiStatus.height / 2 - 50, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, "§7§m-----§7(§2§l Mojang §7)§7§m-----", GuiStatus.width / 2 + 3, GuiStatus.height / 2 - 20, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.account, GuiStatus.width / 2, GuiStatus.height / 2, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.authserver, GuiStatus.width / 2, GuiStatus.height / 2 + 10, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.sessionserver, GuiStatus.width / 2, GuiStatus.height / 2 + 20, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.api, GuiStatus.width / 2, GuiStatus.height / 2 + 30, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.mojang, GuiStatus.width / 2, GuiStatus.height / 2 + 40, 16777215);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled && button.id == 1) {
            this.mc.displayGuiScreen(this.before);
        }
        super.actionPerformed(button);
    }
    
    public void getStatus() {
        try {
            final URL web = new URL("https://status.mojang.com/check");
            final URLConnection wc2 = web.openConnection();
            final BufferedReader in2 = new BufferedReader(new InputStreamReader(wc2.getInputStream()));
            final String[] split = in2.readLine().split(",");
            this.mc2 = true;
            if (split[0].equalsIgnoreCase("[{\"minecraft.net\":\"green\"}")) {
                this.minecraft = "§7§lMinecraft §7» §a§lOnline";
            }
            if (split[0].equalsIgnoreCase("[{\\\"minecraft.net\\\":\\\"red\\\"}")) {
                this.minecraft = "§7§lMinecraft §7» §c§lOffline";
            }
            if (split[0].equalsIgnoreCase("[{\\\"minecraft.net\\\":\\\"yellow\\\"}")) {
                this.minecraft = "§7§lMinecraft §7» §e§lLagging";
            }
            if (split[1].equalsIgnoreCase("{\"session.minecraft.net\":\"green\"}")) {
                this.session = "§7§lSession §7» §a§lOnline";
            }
            if (split[1].equalsIgnoreCase("{\"session.minecraft.net\":\"red\"}")) {
                this.session = "§7§lSession §7» §c§lOffline";
            }
            if (split[1].equalsIgnoreCase("{\"session.minecraft.net\":\"yellow\"}")) {
                this.session = "§7§lSession §7» §e§lLagging";
            }
            if (split[2].equalsIgnoreCase("{\"account.mojang.com\":\"green\"}")) {
                this.account = "§7§lAccount §7» §a§lOnline";
            }
            if (split[2].equalsIgnoreCase("{\"account.mojang.com\":\"red\"}")) {
                this.account = "§7§lAccount §7» §c§lOffline";
            }
            if (split[2].equalsIgnoreCase("{\"account.mojang.com\":\"yellow\"}")) {
                this.account = "§7§lAccount §7» §e§lLagging";
            }
            if (split[3].equalsIgnoreCase("{\"authserver.mojang.com\":\"green\"}")) {
                this.authserver = "§7§lAuthserver §7» §a§lOnline";
            }
            if (split[3].equalsIgnoreCase("{\"authserver.mojang.com\":\"red\"}")) {
                this.authserver = "§7§lAuthserver §7» §c§lOffline";
            }
            if (split[3].equalsIgnoreCase("{\"authserver.mojang.com\":\"yellow\"}")) {
                this.authserver = "§7§lAuthserver §7» §e§lLagging";
            }
            if (split[4].equalsIgnoreCase("{\"sessionserver.mojang.com\":\"green\"}")) {
                this.sessionserver = "§7§lSessionserver §7» §a§lOnline";
            }
            if (split[4].equalsIgnoreCase("{\"sessionserver.mojang.com\":\"red\"}")) {
                this.sessionserver = "§7§lSessionserver §7» §c§lOffline";
            }
            if (split[4].equalsIgnoreCase("{\"sessionserver.mojang.com\":\"yellow\"}")) {
                this.sessionserver = "§7§lSessionserver §7» §e§lLagging";
            }
            if (split[5].equalsIgnoreCase("{\"api.mojang.com\":\"green\"}")) {
                this.api = "§7§lAPI §7» §a§lOnline";
            }
            if (split[5].equalsIgnoreCase("{\"api.mojang.com\":\"red\"}")) {
                this.api = "§7§lAPI §7» §c§lOffline";
            }
            if (split[5].equalsIgnoreCase("{\"api.mojang.com\":\"yellow\"}")) {
                this.api = "§7§lAPI §7» §e§lLagging";
            }
            if (split[6].equalsIgnoreCase("{\"textures.minecraft.net\":\"green\"}")) {
                this.textures = "§7§lTextures §7» §a§lOnline";
            }
            if (split[6].equalsIgnoreCase("{\"textures.minecraft.net\":\"red\"}")) {
                this.textures = "§7§lTextures §7» §c§lOffline";
            }
            if (split[6].equalsIgnoreCase("{\"textures.minecraft.net\":\"yellow\"}")) {
                this.textures = "§7§lTextures §7» §e§lLagging";
            }
            if (split[7].equalsIgnoreCase("{\"mojang.com\":\"green\"}]")) {
                this.mojang = "§7§lMojang §7» §a§lOnline";
            }
            if (split[7].equalsIgnoreCase("{\"mojang.com\":\"red\"}]")) {
                this.mojang = "§7§lMojang §7» §c§lOffline";
            }
            if (split[7].equalsIgnoreCase("{\"mojang.com\":\"yellow\"}]")) {
                this.mojang = "§7§lMojang §7» §e§lLagging";
            }
        }
        catch (final IOException e2) {
            System.out.println(e2);
        }
    }
}
