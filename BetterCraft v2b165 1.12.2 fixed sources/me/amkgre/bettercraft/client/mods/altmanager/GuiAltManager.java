// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.altmanager;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import java.io.File;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import me.amkgre.bettercraft.client.mods.fourtwenty.GuiFourTwenty;
import me.amkgre.bettercraft.client.mods.thealtening.GuiAlterning;
import me.amkgre.bettercraft.client.mods.mcleaks.GuiMcLeaksLogin;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen
{
    private GuiScreen before;
    private GuiTextField searchField;
    private GuiTextField emailField;
    private GuiTextField passField;
    private boolean displayingLogin;
    private final JFileChooser fileChooser;
    private int selected;
    private JFrame frame;
    private int scroll;
    private boolean failed;
    private String failMessage;
    public String clipString;
    
    public GuiAltManager(final GuiScreen before) {
        this.before = null;
        this.displayingLogin = false;
        this.selected = 0;
        this.frame = new JFrame("FileChooser");
        this.scroll = 0;
        this.failed = false;
        this.failMessage = null;
        this.before = before;
        this.fileChooser = new JFileChooser();
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, 60, GuiAltManager.height - 44 - 5 + 26, 88, 20, "Login Selected"));
        this.buttonList.add(new GuiButton(1, 150, GuiAltManager.height - 44 - 5 + 26, 88, 20, "Remove Selected"));
        this.buttonList.add(new GuiButton(2, 3, 3, 88, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(3, GuiAltManager.width - 70, GuiAltManager.height - 44 - 5 + 26, 60, 20, "Back"));
        this.buttonList.add(new GuiButton(4, 10, GuiAltManager.height - 44 - 5 + 26, 48, 20, "Add Alt"));
        this.buttonList.add(new GuiButton(5, GuiAltManager.width - 135, 5, 133, 20, "Import from Accountlist"));
        this.buttonList.add(new GuiButton(6, GuiAltManager.width - 135, 28, 133, 20, "Export to Accountlist"));
        this.buttonList.add(new GuiButton(10, GuiAltManager.width / 2 + 1 + 100, GuiAltManager.height / 2, 48, 15, "Add"));
        this.buttonList.add(new GuiButton(11, GuiAltManager.width / 2 - 50, GuiAltManager.height - 80, 100, 20, "Okay"));
        this.buttonList.add(new GuiButton(12, 3, 26, 88, 20, "McLeaks"));
        this.buttonList.add(new GuiButton(20, 3, 49, 88, 20, "Alterning"));
        this.buttonList.add(new GuiButton(21, 3, 72, 88, 20, "FourTwenty"));
        this.buttonList.add(new GuiButton(15, 240, GuiAltManager.height - 44 - 5 + 26, 48, 20, "Clipboard"));
        this.buttonList.get(7).visible = false;
        this.buttonList.get(8).visible = false;
        (this.searchField = new GuiTextField(7, this.fontRendererObj, GuiAltManager.width / 2 - 50, 50, 98, 15)).setMaxStringLength(666);
        this.searchField.setFocused(true);
        (this.emailField = new GuiTextField(8, this.fontRendererObj, GuiAltManager.width / 2 - 99 - 50, GuiAltManager.height / 2, 145, 15)).setMaxStringLength(666);
        (this.passField = new GuiTextField(9, this.fontRendererObj, GuiAltManager.width / 2 + 1, GuiAltManager.height / 2, 95, 15)).setMaxStringLength(666);
        if (this.displayingLogin) {
            this.displayLogin();
        }
        else {
            this.stopDisplayingLogin();
        }
        if (this.failed) {
            this.startDisplayingFailed();
        }
        else {
            this.stopDisplayingFailed();
        }
    }
    
    public void displayLogin() {
        this.searchField.setFocused(false);
        this.displayingLogin = true;
        this.buttonList.get(7).visible = true;
        for (final GuiButton btn : this.buttonList) {
            btn.enabled = (btn.displayString.equals("Add") || btn.displayString.equals("Back"));
        }
    }
    
    public void stopDisplayingLogin() {
        this.searchField.setFocused(true);
        this.displayingLogin = false;
        this.buttonList.get(7).visible = false;
        for (final GuiButton btn : this.buttonList) {
            btn.enabled = true;
        }
    }
    
    public void startDisplayingFailed() {
        this.searchField.setFocused(false);
        this.failed = true;
        this.buttonList.get(8).visible = true;
        for (final GuiButton btn : this.buttonList) {
            btn.enabled = btn.displayString.equals("Okay");
        }
    }
    
    public void stopDisplayingFailed() {
        this.searchField.setFocused(true);
        this.failed = false;
        this.buttonList.get(8).visible = false;
        for (final GuiButton btn : this.buttonList) {
            btn.enabled = true;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiAltLogin(this.before));
        }
        if (button.id == 3) {
            if (this.displayingLogin) {
                this.stopDisplayingLogin();
            }
            else {
                this.mc.displayGuiScreen(this.before);
            }
        }
        if (button.id == 4) {
            this.displayLogin();
        }
        if (button.id == 12) {
            this.mc.displayGuiScreen(new GuiMcLeaksLogin(this.before));
        }
        if (button.id == 20) {
            this.mc.displayGuiScreen(new GuiAlterning(this.before));
        }
        if (button.id == 21) {
            this.mc.displayGuiScreen(new GuiFourTwenty(this.before));
        }
        if (button.id == 10) {
            final Alt alt = new Alt(this.emailField.getText(), this.passField.getText());
            AltManager.addAlt(alt);
            this.stopDisplayingLogin();
        }
        if (button.id == 11) {
            this.stopDisplayingFailed();
        }
        if (button.id == 1) {
            AltManager.getAlts().remove(AltManager.getAlts().get(this.selected));
        }
        if (button.id == 15) {
            try {
                final String cliptext = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                this.clipString = Login.loginclip(cliptext.split(":")[0], cliptext.split(":")[1]);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (button.id == 0) {
            final Alt alt = AltManager.getAlts().get(this.selected);
            if (alt.cracked) {
                Login.changeName(alt.name);
                AltManager.loggedInName = AltManager.getAlts().get(this.selected).name;
            }
            else {
                try {
                    final boolean changeIndex = Login.login(alt.email, alt.password);
                    if (changeIndex) {
                        AltManager.loggedInName = AltManager.getAlts().get(this.selected).name;
                    }
                }
                catch (final Exception e2) {
                    e2.printStackTrace();
                    System.out.println("Login failed!");
                    this.failMessage = e2.getMessage();
                    this.startDisplayingFailed();
                }
            }
        }
        if (button.id == 5) {
            this.frame.setVisible(true);
            this.frame.setAlwaysOnTop(true);
            final int result = this.fileChooser.showOpenDialog(this.frame);
            this.frame.setVisible(false);
            if (result == 0) {
                final File file = this.fileChooser.getSelectedFile();
                if (file.getName().endsWith(".txt")) {
                    try {
                        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        String line;
                        while ((line = br.readLine()) != null) {
                            final String email = line.split(":")[0];
                            String pass = null;
                            if (line.split(":").length > 1) {
                                pass = line.split(":")[1];
                            }
                            if (pass != null && pass.isEmpty()) {
                                final Alt alt2 = new Alt(email, null);
                                AltManager.addAlt(alt2);
                            }
                            else {
                                final Alt alt2 = new Alt(email, pass);
                                AltManager.addAlt(alt2);
                            }
                        }
                        br.close();
                    }
                    catch (final Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        if (button.id == 6) {
            this.frame.setVisible(true);
            this.frame.setAlwaysOnTop(true);
            final int result = this.fileChooser.showSaveDialog(this.frame);
            this.frame.setVisible(false);
            if (result == 0) {
                final File file = this.fileChooser.getSelectedFile();
                if (file.getName().endsWith(".txt")) {
                    file.delete();
                    try {
                        final PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)), true);
                        for (final Alt alt3 : AltManager.getAlts()) {
                            if (alt3.cracked) {
                                pw.println(alt3.name);
                            }
                            else {
                                pw.println(String.valueOf(alt3.email) + ":" + alt3.password);
                            }
                        }
                        pw.flush();
                        pw.close();
                    }
                    catch (final Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        super.actionPerformed(button);
    }
    
    @Override
    public void updateScreen() {
        this.searchField.updateCursorCounter();
        this.emailField.updateCursorCounter();
        this.passField.updateCursorCounter();
        if (this.selected >= AltManager.getAlts().size()) {
            this.selected = AltManager.getAlts().size() - 1;
        }
        if (!this.displayingLogin) {
            this.emailField.setText("");
            this.passField.setText("");
        }
        this.buttonList.get(0).enabled = !AltManager.getAlts().isEmpty();
        this.buttonList.get(1).enabled = !AltManager.getAlts().isEmpty();
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.scale(2.0f, 2.0f, 1.0f);
        Gui.drawCenteredString(this.fontRendererObj, "", GuiAltManager.width / 2 / 2, 2, -1);
        Gui.drawCenteredString(this.fontRendererObj, "§7Search", GuiAltManager.width / 2 / 2, 12, -1);
        GlStateManager.scale(0.5, 0.5, 1.0);
        this.searchField.drawTextBox();
        final int offsetY = 67;
        int alts = 0;
        for (final Alt alt : AltManager.getAlts()) {
            if (alt.name.toLowerCase().indexOf(this.searchField.getText().toLowerCase()) == -1) {
                continue;
            }
            final int left = GuiAltManager.width / 2 - 150;
            final int right = GuiAltManager.width / 2 + 150;
            final int top = offsetY + alts * 21 + this.scroll;
            final int bottom = offsetY + (alts * 21 + 20) + this.scroll;
            if (top < 51) {
                ++alts;
            }
            else {
                final boolean hover = mouseX > left && mouseX < right && mouseY > top && mouseY < bottom;
                Gui.drawRect(left, top, right, bottom, hover ? -1439686624 : -1440735200);
                this.drawHorizontalLine(left, right - 1, top, -1);
                this.drawHorizontalLine(left, right - 1, bottom - 1, -1);
                this.drawVerticalLine(left, top, bottom, -1);
                this.drawVerticalLine(right - 1, top, bottom, -1);
                String obfPass = "";
                if (!alt.cracked) {
                    for (int i = 0; i < alt.password.length(); ++i) {
                        obfPass = String.valueOf(obfPass) + "*";
                    }
                }
                if (AltManager.loggedInName != null && AltManager.loggedInName.equals(alt.name)) {
                    Gui.drawString(this.fontRendererObj, "§2Logged in", right - this.fontRendererObj.getStringWidth("§2Logged in") - 2, bottom - 18, -1);
                }
                Gui.drawString(this.fontRendererObj, String.valueOf(alts + 1) + ".", left - this.fontRendererObj.getStringWidth(String.valueOf(alts + 1) + ".") - 2, top + 6, -1);
                Gui.drawString(this.fontRendererObj, alt.cracked ? ("§c" + alt.name) : ("§6" + alt.name + "§r:" + "§6" + obfPass), left + 6, top + 6, -1);
                if (alt.cracked) {
                    Gui.drawString(this.fontRendererObj, "§cCracked", right - this.fontRendererObj.getStringWidth("§cCracked") - 2, bottom - 9, -1);
                }
                else {
                    Gui.drawString(this.fontRendererObj, "§6Premium", right - this.fontRendererObj.getStringWidth("§6Premium") - 2, bottom - 9, -1);
                }
                if (this.selected == alts) {
                    Gui.drawRect(left, top, right, bottom, 452984831);
                }
                ++alts;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.failed) {
            Gui.drawRect(0, 80, GuiAltManager.width, GuiAltManager.height - 40, -1392508928);
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            Gui.drawCenteredString(this.fontRendererObj, "§cFailed to login!", GuiAltManager.width / 2 / 4, 22, -1);
            GlStateManager.scale(0.25, 0.25, 1.0);
            Gui.drawCenteredString(this.fontRendererObj, "§7" + this.failMessage, GuiAltManager.width / 2, 130, -1);
            this.buttonList.get(8).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        if (this.displayingLogin) {
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.scale(0.25, 0.25, 1.0);
            Gui.drawString(this.fontRendererObj, "Email/Username", GuiAltManager.width / 2 - 99 - 50, GuiAltManager.height / 2 - 11, -1);
            Gui.drawString(this.fontRendererObj, "Password", GuiAltManager.width / 2 + 1, GuiAltManager.height / 2 - 11, -1);
            this.emailField.drawTextBox();
            this.passField.drawTextBox();
            this.buttonList.get(7).drawButton(this.mc, mouseX, mouseY, partialTicks);
            this.buttonList.get(3).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.displayingLogin) {
            this.emailField.mouseClicked(mouseX, mouseY, mouseButton);
            this.passField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (this.displayingLogin) {
            return;
        }
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        final int offsetY = 67;
        int alts = 0;
        for (final Alt alt : AltManager.getAlts()) {
            if (alt.name.toLowerCase().indexOf(this.searchField.getText().toLowerCase()) == -1) {
                continue;
            }
            final int left = GuiAltManager.width / 2 - 150;
            final int right = GuiAltManager.width / 2 + 150;
            final int top = offsetY + alts * 21 + this.scroll;
            final int bottom = offsetY + (alts * 21 + 20) + this.scroll;
            final boolean hover = mouseX > left && mouseX < right && mouseY > top && mouseY < bottom;
            if (hover) {
                if (this.selected == alts) {
                    final Alt altt = AltManager.getAlts().get(this.selected);
                    if (altt.cracked) {
                        Login.changeName(altt.name);
                        AltManager.loggedInName = AltManager.getAlts().get(this.selected).name;
                    }
                    else {
                        try {
                            final boolean changeIndex = Login.login(altt.email, altt.password);
                            if (changeIndex) {
                                AltManager.loggedInName = AltManager.getAlts().get(this.selected).name;
                            }
                        }
                        catch (final Exception e) {
                            e.printStackTrace();
                            this.failMessage = e.getMessage();
                            this.startDisplayingFailed();
                        }
                    }
                }
                this.selected = alts;
                break;
            }
            ++alts;
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.searchField.textboxKeyTyped(typedChar, keyCode);
        if (this.displayingLogin) {
            this.emailField.textboxKeyTyped(typedChar, keyCode);
            this.passField.textboxKeyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.displayingLogin) {
            return;
        }
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            if (i > 1) {
                i = 1;
            }
            if (i < -1) {
                i = -1;
            }
            if (isCtrlKeyDown()) {
                i *= 210;
            }
            else {
                i *= 21;
            }
            this.scroll += i;
            if (this.scroll > 0) {
                this.scroll = 0;
            }
            if (this.scroll < -AltManager.getAlts().size() * 21 + 105) {
                this.scroll = -AltManager.getAlts().size() * 21 + 105;
            }
        }
    }
}
