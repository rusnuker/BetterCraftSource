// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate
{
    private String title;
    private String stage;
    private int progress;
    private boolean doneWorking;
    
    public GuiScreenWorking() {
        this.title = "";
        this.stage = "";
    }
    
    @Override
    public void displaySavingString(final String message) {
        this.resetProgressAndMessage(message);
    }
    
    @Override
    public void resetProgressAndMessage(final String message) {
        this.title = message;
        this.displayLoadingString("Working...");
    }
    
    @Override
    public void displayLoadingString(final String message) {
        this.stage = message;
        this.setLoadingProgress(0);
    }
    
    @Override
    public void setLoadingProgress(final int progress) {
        this.progress = progress;
    }
    
    @Override
    public void setDoneWorking() {
        this.doneWorking = true;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.doneWorking) {
            if (!this.mc.isConnectedToRealms()) {
                this.mc.displayGuiScreen(null);
            }
        }
        else {
            this.drawDefaultBackground();
            Gui.drawCenteredString(this.fontRendererObj, this.title, GuiScreenWorking.width / 2, 70, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, String.valueOf(this.stage) + " " + this.progress + "%", GuiScreenWorking.width / 2, 90, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
