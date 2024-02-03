// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import java.util.concurrent.ExecutorService;
import net.labymod.core.ServerPingerData;
import net.labymod.utils.Consumer;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.utils.manager.ServerInfoRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.GuiScreen;

public class ModGuiScreenServerList extends GuiScreen
{
    private final GuiScreen guiScreen;
    private final ServerData serverData;
    private GuiTextField textField;
    private long lastUpdate;
    private long updateCooldown;
    private ServerInfoRenderer serverInfoRenderer;
    
    public ModGuiScreenServerList(final GuiScreen guiScreen, final ServerData serverData) {
        this.lastUpdate = 0L;
        this.updateCooldown = 2000L;
        this.guiScreen = guiScreen;
        this.serverData = serverData;
    }
    
    @Override
    public void updateScreen() {
        this.textField.updateCursorCounter();
        if (LabyMod.getSettings().directConnectInfo && !this.textField.getText().replace(" ", "").isEmpty()) {
            if (this.lastUpdate + this.updateCooldown < System.currentTimeMillis()) {
                this.lastUpdate = System.currentTimeMillis();
                LabyModCore.getServerPinger().pingServer(null, this.lastUpdate, this.textField.getText(), new Consumer<ServerPingerData>() {
                    @Override
                    public void accept(final ServerPingerData accepted) {
                        if (accepted != null && accepted.getTimePinged() != ModGuiScreenServerList.this.lastUpdate) {
                            return;
                        }
                        ModGuiScreenServerList.access$2(ModGuiScreenServerList.this, new ServerInfoRenderer(ModGuiScreenServerList.this.textField.getText(), accepted));
                    }
                });
            }
        }
        else {
            this.serverInfoRenderer = new ServerInfoRenderer(this.textField.getText(), null);
            this.lastUpdate = -1L;
        }
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, ModGuiScreenServerList.width / 2 - 100, ModGuiScreenServerList.height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
        this.buttonList.add(new GuiButton(1, ModGuiScreenServerList.width / 2 - 100, ModGuiScreenServerList.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
        (this.textField = new GuiTextField(2, LabyModCore.getMinecraft().getFontRenderer(), ModGuiScreenServerList.width / 2 - 100, 116, 200, 20)).setMaxStringLength(128);
        this.textField.setFocused(true);
        this.textField.setText(this.mc.gameSettings.lastServer);
        this.buttonList.get(0).enabled = (this.textField.getText().length() > 0 && this.textField.getText().split(":").length > 0);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.gameSettings.lastServer = this.textField.getText();
        this.mc.gameSettings.saveOptions();
        LabyModCore.getServerPinger().closePendingConnections();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.guiScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0) {
                this.serverData.serverIP = this.textField.getText();
                this.guiScreen.confirmClicked(true, 5);
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            this.buttonList.get(0).enabled = (this.textField.getText().length() > 0 && this.textField.getText().split(":").length > 0);
        }
        else if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), I18n.format("selectServer.direct", new Object[0]), ModGuiScreenServerList.width / 2, 20, 16777215);
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), I18n.format("addServer.enterIp", new Object[0]), ModGuiScreenServerList.width / 2 - 100, 100, 10526880);
        this.textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.serverInfoRenderer == null || this.lastUpdate == -1L) {
            return;
        }
        final DrawUtils drawUtils = LabyMod.getInstance().getDrawUtils();
        final int leftBound = ModGuiScreenServerList.width / 2 - 150;
        final int rightBound = ModGuiScreenServerList.width / 2 + 150;
        final int posY = 44;
        final int height = 30;
        drawUtils.drawRectangle(leftBound, 40, rightBound, 80, Integer.MIN_VALUE);
        final int stateColorR = this.serverInfoRenderer.canReachServer() ? 105 : 240;
        final int stateColorG = this.serverInfoRenderer.canReachServer() ? 240 : 105;
        final int stateColorB = 105;
        final double total = rightBound - leftBound;
        double barPercent = total / this.updateCooldown * (System.currentTimeMillis() - this.lastUpdate);
        if (barPercent > total) {
            barPercent = total;
        }
        final int colorPercent = (int)Math.round(155.0 / this.updateCooldown * (System.currentTimeMillis() - this.lastUpdate - 100L));
        drawUtils.drawRectangle(leftBound, 38, rightBound, 39, Integer.MIN_VALUE);
        drawUtils.drawRectangle(leftBound, 38, rightBound, 39, ModColor.toRGB(stateColorR, stateColorG, 105, 155 - colorPercent));
        DrawUtils.drawRect(leftBound, 38.0, leftBound + barPercent, 39.0, ModColor.toRGB(stateColorR, stateColorG, 105, 150));
        drawUtils.drawGradientShadowTop(40.0, leftBound, rightBound);
        drawUtils.drawGradientShadowBottom(80.0, leftBound, rightBound);
    }
    
    static /* synthetic */ void access$2(final ModGuiScreenServerList list, final ServerInfoRenderer serverInfoRenderer) {
        list.serverInfoRenderer = serverInfoRenderer;
    }
}
