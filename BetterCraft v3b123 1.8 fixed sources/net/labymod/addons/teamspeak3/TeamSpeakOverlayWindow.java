// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import net.labymod.utils.ModColor;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import java.util.ArrayList;
import net.labymod.utils.DrawUtils;

public class TeamSpeakOverlayWindow
{
    private DrawUtils draw;
    private ArrayList<InfoMessage> info;
    String infoTitle;
    boolean infoDrag;
    int infoPosX;
    int infoPosY;
    int infoLengthX;
    int infoLengthY;
    int infoClickX;
    int infoClickY;
    String inputTitle;
    boolean inputDrag;
    int inputPosX;
    int inputPosY;
    int inputLengthX;
    int inputLengthY;
    int inputClickX;
    int inputClickY;
    int inputTarget;
    String input;
    PopUpCallback inputCallBack;
    GuiTextField inputField;
    boolean closedInfo;
    boolean closedInput;
    
    public TeamSpeakOverlayWindow() {
        this.draw = new DrawUtils();
        this.info = new ArrayList<InfoMessage>();
        this.closedInfo = false;
        this.closedInput = false;
    }
    
    public void openInfo(final int clientId, final String title, String message) {
        if (message.length() > 70) {
            message = String.valueOf(message.substring(0, 70)) + "..";
        }
        this.info.add(new InfoMessage(clientId, message));
        this.infoTitle = title;
        if (this.info.size() > 15) {
            this.info.remove(0);
        }
        this.calcInfo();
    }
    
    public void openInput(final int targetId, final String title, final String message, final PopUpCallback callBack) {
        this.inputTitle = title;
        this.input = message;
        this.inputTarget = targetId;
        this.inputCallBack = callBack;
        (this.inputField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, 0, 0, 159, 10)).setText("");
        this.inputField.setMaxStringLength(50);
        this.inputField.setFocused(true);
        this.calcInput();
    }
    
    private void resetInfo() {
        this.info.clear();
        this.infoTitle = null;
        this.infoDrag = false;
        this.infoPosX = 0;
        this.infoPosY = 0;
        this.infoLengthX = 0;
        this.infoLengthY = 0;
        this.infoClickX = 0;
        this.infoClickY = 0;
        this.closedInfo = true;
    }
    
    private void resetinput() {
        this.input = null;
        this.inputTitle = null;
        this.inputDrag = false;
        this.inputPosX = 0;
        this.inputPosY = 0;
        this.inputLengthX = 0;
        this.inputLengthY = 0;
        this.inputClickX = 0;
        this.inputClickY = 0;
        this.inputTarget = 0;
        this.closedInput = true;
        this.inputField = null;
    }
    
    public void drawWindow(final int mouseX, final int mouseY) {
        if (!this.info.isEmpty()) {
            this.drawInfo(mouseX, mouseY);
        }
        if (this.input != null) {
            this.drawInput(mouseX, mouseY);
        }
    }
    
    public boolean isInInfoScreen(final int mouseX, final int mouseY) {
        return this.closedInfo || (mouseX > this.infoPosX - 1 && mouseX < this.infoPosX + this.infoLengthX + 1 && mouseY > this.infoPosY - 1 && mouseY < this.infoPosY + this.infoLengthY + 1);
    }
    
    public boolean isInInfoFrame(final int mouseX, final int mouseY) {
        return mouseX > this.infoPosX - 1 && mouseX < this.infoPosX + this.infoLengthX + 1 && mouseY > this.infoPosY - 1 && mouseY < this.infoPosY + 15;
    }
    
    public boolean isInInputScreen(final int mouseX, final int mouseY) {
        return this.closedInput || (mouseX > this.inputPosX - 1 && mouseX < this.inputPosX + this.inputLengthX + 1 && mouseY > this.inputPosY - 1 && mouseY < this.inputPosY + this.inputLengthY + 1);
    }
    
    public boolean isInInputFrame(final int mouseX, final int mouseY) {
        return mouseX > this.inputPosX - 1 && mouseX < this.inputPosX + this.inputLengthX + 1 && mouseY > this.inputPosY - 1 && mouseY < this.inputPosY + 15;
    }
    
    public boolean isInScreen(final int mouseX, final int mouseY) {
        return this.isInInfoScreen(mouseX, mouseY) || this.isInInputScreen(mouseX, mouseY);
    }
    
    public boolean isInFrame(final int mouseX, final int mouseY) {
        return this.isInInputScreen(mouseX, mouseY) || this.isInInputScreen(mouseX, mouseY);
    }
    
    private void calcInfo() {
        int i = this.draw.getStringWidth(this.infoTitle);
        for (final InfoMessage teamspeakoverlaywindow$infomessage : this.info) {
            final int j = this.draw.getStringWidth(teamspeakoverlaywindow$infomessage.message);
            if (j > i) {
                i = j;
            }
        }
        this.infoLengthX = 20 + i;
        this.infoLengthY = 40 + this.info.size() * 10;
        if (this.info.size() == 1) {
            this.posInfo();
        }
    }
    
    private void calcInput() {
        int i = this.draw.getStringWidth(this.inputTitle);
        final int j = this.draw.getStringWidth(this.input);
        if (j > i) {
            i = j;
        }
        int k = 165;
        if (i > 165) {
            k = i + 10;
        }
        this.inputLengthX = k;
        this.inputLengthY = 62;
        this.inputPosX = this.draw.getWidth() / 2 - this.inputLengthX / 2;
        this.inputPosY = this.draw.getHeight() / 2 - this.inputLengthY / 2;
    }
    
    private void posInfo() {
        this.infoPosX = this.draw.getWidth() / 2 - this.infoLengthX / 2;
        this.infoPosY = this.draw.getHeight() / 2 - this.infoLengthY / 2;
    }
    
    private void drawInfo(final int mouseX, final int mouseY) {
        DrawUtils drawutils = this.draw;
        Gui.drawRect(this.infoPosX - 1, this.infoPosY - 1, this.infoPosX + this.infoLengthX + 1, this.infoPosY + this.infoLengthY + 1, Color.BLACK.getRGB());
        drawutils = this.draw;
        Gui.drawRect(this.infoPosX, this.infoPosY, this.infoPosX + this.infoLengthX, this.infoPosY + this.infoLengthY, Color.GRAY.getRGB());
        drawutils = this.draw;
        Gui.drawRect(this.infoPosX, this.infoPosY, this.infoPosX + this.infoLengthX, this.infoPosY + 15, Color.WHITE.getRGB());
        this.draw.drawString(String.valueOf(ModColor.cl("6")) + this.infoTitle, this.infoPosX + 3, this.infoPosY + 3);
        String s = ModColor.cl("c");
        if (mouseX > this.infoPosX + this.infoLengthX - 20 && mouseX < this.infoPosX + this.infoLengthX && mouseY > this.infoPosY + 3 && mouseY < this.infoPosY + 15) {
            s = ModColor.cl("4");
        }
        this.draw.drawString(String.valueOf(s) + "x", this.infoPosX + this.infoLengthX - 13, this.infoPosY + 3);
        int i = 0;
        for (final InfoMessage teamspeakoverlaywindow$infomessage : this.info) {
            final double d0 = this.infoPosX + 3;
            this.draw.drawString(teamspeakoverlaywindow$infomessage.message, d0, this.infoPosY + 20 + i);
            i += 10;
        }
        drawutils = this.draw;
        Gui.drawRect(this.infoPosX + this.infoLengthX / 2 - 25, this.infoPosY + this.infoLengthY - 15, this.infoPosX + this.infoLengthX / 2 + 25, this.infoPosY + this.infoLengthY - 2, Color.BLACK.getRGB());
        drawutils = this.draw;
        Gui.drawRect(this.infoPosX + this.infoLengthX / 2 - 24, this.infoPosY + this.infoLengthY - 14, this.infoPosX + this.infoLengthX / 2 + 24, this.infoPosY + this.infoLengthY - 3, Color.DARK_GRAY.getRGB());
        if (mouseX > this.infoPosX + this.infoLengthX / 2 - 24 && mouseX < this.infoPosX + this.infoLengthX / 2 + 24 && mouseY > this.infoPosY + this.infoLengthY - 14 && mouseY < this.infoPosY + this.infoLengthY - 3) {
            drawutils = this.draw;
            Gui.drawRect(this.infoPosX + this.infoLengthX / 2 - 24, this.infoPosY + this.infoLengthY - 14, this.infoPosX + this.infoLengthX / 2 + 24, this.infoPosY + this.infoLengthY - 3, Color.GRAY.getRGB() + 40);
        }
        final int j = this.infoPosX + this.infoLengthX / 2;
        final int k = this.infoPosY + this.infoLengthY;
        this.draw.drawCenteredString("OK", j, k - 12);
        while (this.infoPosX + this.infoLengthX > this.draw.getWidth()) {
            --this.infoPosX;
        }
    }
    
    private void drawInput(final int mouseX, final int mouseY) {
        if (this.inputCallBack.tick(this.inputTarget)) {
            this.resetinput();
        }
        else {
            DrawUtils drawutils = this.draw;
            Gui.drawRect(this.inputPosX - 1, this.inputPosY - 1, this.inputPosX + this.inputLengthX + 1, this.inputPosY + this.inputLengthY + 1, Color.BLACK.getRGB());
            drawutils = this.draw;
            Gui.drawRect(this.inputPosX, this.inputPosY, this.inputPosX + this.inputLengthX, this.inputPosY + this.inputLengthY, Color.GRAY.getRGB());
            drawutils = this.draw;
            Gui.drawRect(this.inputPosX, this.inputPosY, this.inputPosX + this.inputLengthX, this.inputPosY + 15, Color.WHITE.getRGB());
            this.draw.drawString(String.valueOf(ModColor.cl("6")) + this.inputTitle, this.inputPosX + 3, this.inputPosY + 3);
            String s = ModColor.cl("c");
            if (mouseX > this.inputPosX + this.inputLengthX - 20 && mouseX < this.inputPosX + this.inputLengthX && mouseY > this.inputPosY + 3 && mouseY < this.inputPosY + 15) {
                s = ModColor.cl("4");
            }
            this.draw.drawString(String.valueOf(s) + "x", this.inputPosX + this.inputLengthX - 13, this.inputPosY + 3);
            final double d0 = this.inputPosX + 3;
            this.draw.drawString(this.input, d0, this.inputPosY + 20);
            drawutils = this.draw;
            Gui.drawRect(this.inputPosX + this.inputLengthX / 2 - 60, this.inputPosY + this.inputLengthY - 15, this.inputPosX + this.inputLengthX / 2 - 10, this.inputPosY + this.inputLengthY - 2, Color.BLACK.getRGB());
            drawutils = this.draw;
            Gui.drawRect(this.inputPosX + this.inputLengthX / 2 - 60 + 1, this.inputPosY + this.inputLengthY - 14, this.inputPosX + this.inputLengthX / 2 - 11, this.inputPosY + this.inputLengthY - 3, Color.DARK_GRAY.getRGB());
            if (mouseX > this.inputPosX + this.inputLengthX / 2 - 60 + 1 && mouseX < this.inputPosX + this.inputLengthX / 2 + 11 && mouseY > this.inputPosY + this.inputLengthY - 14 && mouseY < this.inputPosY + this.inputLengthY - 3) {
                drawutils = this.draw;
                Gui.drawRect(this.inputPosX + this.inputLengthX / 2 - 60 + 1, this.inputPosY + this.inputLengthY - 14, this.inputPosX + this.inputLengthX / 2 - 11, this.inputPosY + this.inputLengthY - 3, Color.GRAY.getRGB() + 40);
            }
            int i = this.inputPosX + this.inputLengthX / 2 - 35;
            int j = this.inputPosY + this.inputLengthY;
            this.draw.drawCenteredString("OK", i, j - 12);
            drawutils = this.draw;
            Gui.drawRect(this.inputPosX + this.inputLengthX / 2 + 10, this.inputPosY + this.inputLengthY - 15, this.inputPosX + this.inputLengthX / 2 + 60, this.inputPosY + this.inputLengthY - 2, Color.BLACK.getRGB());
            drawutils = this.draw;
            Gui.drawRect(this.inputPosX + this.inputLengthX / 2 + 11, this.inputPosY + this.inputLengthY - 14, this.inputPosX + this.inputLengthX / 2 + 60 - 1, this.inputPosY + this.inputLengthY - 3, Color.DARK_GRAY.getRGB());
            if (mouseX > this.inputPosX + this.inputLengthX / 2 + 10 && mouseX < this.inputPosX + this.inputLengthX / 2 + 60 && mouseY > this.inputPosY + this.inputLengthY - 14 && mouseY < this.inputPosY + this.inputLengthY - 3) {
                drawutils = this.draw;
                Gui.drawRect(this.inputPosX + this.inputLengthX / 2 + 11, this.inputPosY + this.inputLengthY - 14, this.inputPosX + this.inputLengthX / 2 + 60 - 1, this.inputPosY + this.inputLengthY - 3, Color.GRAY.getRGB() + 40);
            }
            i = this.inputPosX + this.inputLengthX / 2 + 35;
            j = this.inputPosY + this.inputLengthY;
            this.draw.drawCenteredString("Cancel", i, j - 12);
            this.inputField.xPosition = this.inputPosX + 3;
            this.inputField.yPosition = this.inputPosY + 32;
            this.inputField.xPosition = this.inputPosX + this.inputLengthX / 2 - 80;
            this.inputField.drawTextBox();
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isOpen()) {
            if (this.isInInfoFrame(mouseX, mouseY)) {
                this.infoClickX = mouseX - this.infoPosX;
                this.infoClickY = mouseY - this.infoPosY;
                this.infoDrag = true;
            }
            if (this.isInInputFrame(mouseX, mouseY)) {
                this.inputClickX = mouseX - this.inputPosX;
                this.inputClickY = mouseY - this.inputPosY;
                this.inputDrag = true;
            }
            if (!this.info.isEmpty()) {
                if (mouseX > this.infoPosX + this.infoLengthX / 2 - 24 && mouseX < this.infoPosX + this.infoLengthX / 2 + 24 && mouseY > this.infoPosY + this.infoLengthY - 14 && mouseY < this.infoPosY + this.infoLengthY - 3) {
                    this.resetInfo();
                }
                if (mouseX > this.infoPosX + this.infoLengthX - 20 && mouseX < this.infoPosX + this.infoLengthX && mouseY > this.infoPosY + 3 && mouseY < this.infoPosY + 15) {
                    this.resetInfo();
                }
                int i = 0;
                for (final InfoMessage teamspeakoverlaywindow$infomessage : this.info) {
                    final double d0 = this.infoPosX + 3;
                    this.draw.drawString(teamspeakoverlaywindow$infomessage.message, d0, this.infoPosY + 20 + i);
                    int j = this.infoLengthX;
                    final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(teamspeakoverlaywindow$infomessage.clientId);
                    if (teamspeakuser != null) {
                        j = this.draw.getStringWidth(teamspeakuser.getNickName());
                    }
                    if (mouseX > this.infoPosX && mouseX < this.infoPosX + j && mouseY > this.infoPosY + 20 + i && mouseY < this.infoPosY + 20 + i + 10) {
                        TeamSpeak.callBack = true;
                        TeamSpeak.callBackClient = teamspeakoverlaywindow$infomessage.clientId;
                        break;
                    }
                    i += 10;
                }
            }
            if (this.input != null) {
                if (mouseX > this.inputPosX + this.inputLengthX / 2 - 60 + 1 && mouseX < this.inputPosX + this.inputLengthX / 2 + 11 && mouseY > this.inputPosY + this.inputLengthY - 14 && mouseY < this.inputPosY + this.inputLengthY - 3) {
                    this.inputCallBack.ok(this.inputTarget, this.inputField.getText());
                    this.resetinput();
                }
                if (mouseX > this.inputPosX + this.inputLengthX / 2 + 10 && mouseX < this.inputPosX + this.inputLengthX / 2 + 60 && mouseY > this.inputPosY + this.inputLengthY - 14 && mouseY < this.inputPosY + this.inputLengthY - 3) {
                    this.inputCallBack.cancel();
                    this.resetinput();
                }
                if (mouseX > this.inputPosX + this.inputLengthX - 20 && mouseX < this.inputPosX + this.inputLengthX && mouseY > this.inputPosY + 3 && mouseY < this.inputPosY + 15) {
                    this.inputCallBack.cancel();
                    this.resetinput();
                }
            }
            if (this.inputField != null) {
                this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    public void KeyTyped(final char typedChar, final int keyCode) {
        if (this.isOpen()) {
            if (keyCode == 28 && this.inputField != null && this.inputField.isFocused() && this.inputCallBack != null && this.inputField != null) {
                this.inputCallBack.ok(this.inputTarget, this.inputField.getText());
                this.resetinput();
            }
            if (this.inputField != null) {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }
    
    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        if (this.isOpen()) {
            if (this.infoDrag && !this.inputDrag) {
                if (mouseX - this.infoClickX > 0 && mouseX - this.infoClickX < this.draw.getWidth() - this.infoLengthX) {
                    this.infoPosX = mouseX - this.infoClickX;
                }
                if (mouseY - this.infoClickY > 0 && mouseY - this.infoClickY < this.draw.getHeight() - this.infoLengthY) {
                    this.infoPosY = mouseY - this.infoClickY;
                }
            }
            if (this.inputDrag) {
                if (mouseX - this.inputClickX > 0 && mouseX - this.inputClickX < this.draw.getWidth() - this.inputLengthX) {
                    this.inputPosX = mouseX - this.inputClickX;
                }
                if (mouseY - this.inputClickY > 0 && mouseY - this.inputClickY < this.draw.getHeight() - this.inputLengthY) {
                    this.inputPosY = mouseY - this.inputClickY;
                }
            }
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.infoDrag = false;
        this.inputDrag = false;
        this.closedInfo = false;
        this.closedInput = false;
    }
    
    public boolean allow() {
        return !this.closedInfo && !this.closedInput;
    }
    
    public boolean isOpen() {
        return !this.info.isEmpty() || this.input != null;
    }
    
    class InfoMessage
    {
        String message;
        int clientId;
        
        public InfoMessage(final int clientId, final String message) {
            this.clientId = clientId;
            this.message = message;
        }
    }
}
