// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.spoofer;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.io.IOException;
import me.amkgre.bettercraft.client.utils.UUIDFetcherUtils;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiSpoofer extends GuiScreen
{
    public static String FakeIp;
    public static String FakeUUID;
    public static String renderText;
    private GuiTextField field_1;
    private GuiTextField field_2;
    private GuiTextField field_3;
    private GuiScreen before;
    
    static {
        GuiSpoofer.FakeIp = null;
        GuiSpoofer.FakeUUID = null;
        GuiSpoofer.renderText = "";
    }
    
    public GuiSpoofer(final GuiScreen before) {
        this.before = before;
    }
    
    @Override
    public void initGui() {
        GuiSpoofer.renderText = "";
        this.buttonList.add(new GuiButton(1, GuiSpoofer.width / 2 - 30, GuiSpoofer.height / 2 - 90, 60, 20, "§5Spoof"));
        this.buttonList.add(new GuiButton(0, GuiSpoofer.width / 2 + 5, GuiSpoofer.height / 2 + 70, 40, 20, "Back"));
        this.buttonList.add(new GuiButton(3, GuiSpoofer.width / 2 + 5, GuiSpoofer.height / 2 + 40, 70, 20, "RandomUUID"));
        this.buttonList.add(new GuiButton(4, GuiSpoofer.width / 2 - 75, GuiSpoofer.height / 2 + 40, 70, 20, "CurrentUUID"));
        this.buttonList.add(new GuiButton(5, GuiSpoofer.width / 2 - 45, GuiSpoofer.height / 2 + 70, 40, 20, "Clear"));
        (this.field_1 = new GuiTextField(100, this.mc.fontRendererObj, GuiSpoofer.width / 2 - 100, GuiSpoofer.height / 2 - 20, 200, 20)).setMaxStringLength(100);
        this.field_1.setText("FakeIp");
        this.field_1.setText((GuiSpoofer.FakeIp != null) ? GuiSpoofer.FakeIp : "");
        (this.field_2 = new GuiTextField(100, this.mc.fontRendererObj, GuiSpoofer.width / 2 - 100, GuiSpoofer.height / 2 - 20 + 35, 200, 20)).setMaxStringLength(100);
        this.field_2.setText("FakeUUID");
        this.field_2.setText((GuiSpoofer.FakeUUID != null) ? GuiSpoofer.FakeUUID : "");
        (this.field_3 = new GuiTextField(100, this.mc.fontRendererObj, GuiSpoofer.width / 2 - 100, GuiSpoofer.height / 2 - 20 - 35, 200, 20)).setMaxStringLength(100);
        this.field_3.setText("CrackedName");
        this.field_3.setText(Minecraft.getSession().username);
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.before);
                break;
            }
            case 1: {
                GuiSpoofer.FakeIp = (this.field_1.getText().trim().isEmpty() ? null : this.field_1.getText().trim());
                GuiSpoofer.FakeUUID = (this.field_2.getText().trim().isEmpty() ? null : this.field_2.getText().trim());
                if (!this.field_3.getText().trim().isEmpty()) {
                    Minecraft.getMinecraft().login(this.field_3.getText().trim());
                }
                GuiSpoofer.renderText = "§aSuccessful";
                break;
            }
            case 3: {
                this.field_2.setText(new StringBuilder().append(UUID.randomUUID()).toString());
                break;
            }
            case 4: {
                if (this.field_3.getText().trim().isEmpty()) {
                    this.field_2.setText((UUIDFetcherUtils.getUUID(Minecraft.getSession().username) != null) ? UUIDFetcherUtils.getUUID(Minecraft.getSession().username).toString() : "§4Error");
                    break;
                }
                this.field_2.setText((UUIDFetcherUtils.getUUID(this.field_3.getText().trim()) != null) ? UUIDFetcherUtils.getUUID(this.field_3.getText().trim()).toString() : "§4Error");
                break;
            }
            case 5: {
                this.field_1.setText("");
                this.field_2.setText("");
                this.field_3.setText("");
                GuiSpoofer.FakeIp = null;
                GuiSpoofer.FakeUUID = null;
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.field_3.drawTextBox();
        this.field_1.drawTextBox();
        this.field_2.drawTextBox();
        Gui.drawCenteredString(this.mc.fontRendererObj, "§7Fake IP", GuiSpoofer.width / 2, GuiSpoofer.height / 2 - 20 - 10, Color.WHITE.hashCode());
        Gui.drawCenteredString(this.mc.fontRendererObj, "§7Cracked Username", GuiSpoofer.width / 2, GuiSpoofer.height / 2 - 20 - 45, Color.WHITE.hashCode());
        Gui.drawCenteredString(this.mc.fontRendererObj, "§7Fake UUID", GuiSpoofer.width / 2, GuiSpoofer.height / 2 - 20 + 25, Color.WHITE.hashCode());
        GL11.glPushMatrix();
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GL11.glScaled(4.0, 4.0, 4.0);
        Gui.drawCenteredString(this.mc.fontRendererObj, GuiSpoofer.renderText, GuiSpoofer.width / 8, GuiSpoofer.height / 4 - this.mc.fontRendererObj.FONT_HEIGHT, 0);
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.field_1.mouseClicked(mouseX, mouseY, mouseButton);
        this.field_2.mouseClicked(mouseX, mouseY, mouseButton);
        this.field_3.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 15) {
            if (this.field_1.isFocused()) {
                this.field_1.setFocused(false);
                this.field_3.setFocused(true);
                return;
            }
            if (this.field_2.isFocused()) {
                this.field_2.setFocused(false);
                this.field_1.setFocused(true);
                return;
            }
            if (this.field_3.isFocused()) {
                this.field_3.setFocused(false);
                this.field_2.setFocused(true);
                return;
            }
            this.field_1.setFocused(true);
        }
        this.field_1.textboxKeyTyped(typedChar, keyCode);
        this.field_2.textboxKeyTyped(typedChar, keyCode);
        this.field_3.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }
}
