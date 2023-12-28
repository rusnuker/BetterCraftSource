// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.portscanner;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiPortScanner extends GuiScreen
{
    private GuiTextField hostField;
    private GuiTextField minPortField;
    private GuiTextField maxPortField;
    private GuiTextField threadsField;
    private GuiButton buttonToggle;
    private boolean running;
    private String status;
    private String host;
    private int currentPort;
    private int maxPort;
    private int minPort;
    private int checkedPort;
    private final List<Integer> ports;
    private final GuiScreen before;
    
    public GuiPortScanner(final GuiScreen before) {
        this.status = "§5Waiting...";
        this.ports = new ArrayList<Integer>();
        this.before = before;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        (this.hostField = new GuiTextField(0, this.mc.fontRendererObj, GuiPortScanner.width / 2 - 100, 40, 200, 20)).setFocused(true);
        this.hostField.setMaxStringLength(Integer.MAX_VALUE);
        this.hostField.setText("127.0.0.1");
        (this.minPortField = new GuiTextField(1, this.mc.fontRendererObj, GuiPortScanner.width / 2 - 100, 80, 90, 20)).setMaxStringLength(5);
        this.minPortField.setText(String.valueOf(25500));
        (this.maxPortField = new GuiTextField(2, this.mc.fontRendererObj, GuiPortScanner.width / 2 + 10, 80, 90, 20)).setMaxStringLength(5);
        this.maxPortField.setText(String.valueOf(25600));
        (this.threadsField = new GuiTextField(3, this.mc.fontRendererObj, GuiPortScanner.width / 2 - 100, 120, 200, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.threadsField.setText(String.valueOf(500));
        this.buttonToggle = new GuiButton(1, GuiPortScanner.width / 2 - 100, GuiPortScanner.height / 4 + 115, this.running ? "Stop" : "Start");
        this.buttonList.add(this.buttonToggle);
        this.buttonList.add(new GuiButton(0, GuiPortScanner.width / 2 - 100, GuiPortScanner.height / 4 + 140, "Back"));
        super.initGui();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.mc.fontRendererObj, "Server IP", GuiPortScanner.width / 2, 30, 10526880);
        Gui.drawCenteredString(this.mc.fontRendererObj, "Port von", GuiPortScanner.width / 2 - 55, 70, 10526880);
        Gui.drawCenteredString(this.mc.fontRendererObj, "Port bis", GuiPortScanner.width / 2 + 55, 70, 10526880);
        Gui.drawCenteredString(this.mc.fontRendererObj, "Threads", GuiPortScanner.width / 2, 110, 10526880);
        Gui.drawCenteredString(this.mc.fontRendererObj, this.running ? ("§5" + this.checkedPort + " §8/ §5" + this.maxPort) : ((this.status == null) ? "" : this.status), GuiPortScanner.width / 2, GuiPortScanner.height / 4 + 95, 16777215);
        this.buttonToggle.displayString = (this.running ? "§cStop" : "§aStart");
        this.hostField.drawTextBox();
        this.minPortField.drawTextBox();
        this.maxPortField.drawTextBox();
        this.threadsField.drawTextBox();
        Gui.drawString(this.mc.fontRendererObj, "§5Open Ports:", 2, 2, Color.WHITE.hashCode());
        final List<Integer> list = this.ports;
        synchronized (list) {
            int i = 12;
            for (final Integer integer : this.ports) {
                Gui.drawString(this.mc.fontRendererObj, String.valueOf(integer), 2, i, Color.WHITE.hashCode());
                i += this.mc.fontRendererObj.FONT_HEIGHT;
            }
            monitorexit(list);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.before);
                break;
            }
            case 1: {
                if (this.running) {
                    this.running = false;
                }
                else {
                    this.host = this.hostField.getText();
                    if (this.host.isEmpty()) {
                        this.status = "§cInvalid host";
                        return;
                    }
                    try {
                        this.minPort = Integer.parseInt(this.minPortField.getText());
                    }
                    catch (final NumberFormatException e) {
                        this.status = "§cInvalid min port";
                        return;
                    }
                    try {
                        this.maxPort = Integer.parseInt(this.maxPortField.getText());
                    }
                    catch (final NumberFormatException e) {
                        this.status = "§cInvalid max port";
                        return;
                    }
                    int threads;
                    try {
                        threads = Integer.parseInt(this.threadsField.getText());
                    }
                    catch (final NumberFormatException e) {
                        this.status = "§cInvalid threads";
                        return;
                    }
                    this.ports.clear();
                    this.currentPort = this.minPort - 1;
                    this.checkedPort = this.minPort;
                    for (int i = 0; i < threads; ++i) {
                        new Thread(() -> {
                            try {
                                while (this.running && this.currentPort < this.maxPort) {
                                    final int port = ++this.currentPort;
                                    try {
                                        final Socket socket = new Socket();
                                        socket.connect(new InetSocketAddress(this.host, port), 500);
                                        socket.close();
                                        final List<Integer> list = this.ports;
                                        synchronized (list) {
                                            if (!this.ports.contains(port)) {
                                                this.ports.add(port);
                                            }
                                            monitorexit(list);
                                        }
                                    }
                                    catch (final Exception ex) {}
                                    if (this.checkedPort >= port) {
                                        continue;
                                    }
                                    else {
                                        this.checkedPort = port;
                                    }
                                }
                                this.running = false;
                                this.buttonToggle.displayString = "Start";
                            }
                            catch (final Exception e2) {
                                this.status = "§a§l" + e2.getClass().getSimpleName() + ": §c" + e2.getMessage();
                            }
                            return;
                        }).start();
                    }
                    this.running = true;
                }
                this.buttonToggle.displayString = (this.running ? "Stop" : "Start");
                break;
            }
        }
        super.actionPerformed(button);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (1 == keyCode) {
            this.mc.displayGuiScreen(this.before);
            return;
        }
        if (this.running) {
            return;
        }
        if (this.hostField.isFocused()) {
            this.hostField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.minPortField.isFocused() && !Character.isLetter(typedChar)) {
            this.minPortField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.maxPortField.isFocused() && !Character.isLetter(typedChar)) {
            this.maxPortField.textboxKeyTyped(typedChar, keyCode);
        }
        if (this.threadsField.isFocused() && !Character.isLetter(typedChar)) {
            this.threadsField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.hostField.mouseClicked(mouseX, mouseY, mouseButton);
        this.minPortField.mouseClicked(mouseX, mouseY, mouseButton);
        this.maxPortField.mouseClicked(mouseX, mouseY, mouseButton);
        this.threadsField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void updateScreen() {
        this.hostField.updateCursorCounter();
        this.minPortField.updateCursorCounter();
        this.maxPortField.updateCursorCounter();
        this.threadsField.updateCursorCounter();
        super.updateScreen();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.running = false;
        super.onGuiClosed();
    }
}
