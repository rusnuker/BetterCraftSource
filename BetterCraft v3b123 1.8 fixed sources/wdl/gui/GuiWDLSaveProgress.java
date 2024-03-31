// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import java.io.IOException;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiScreen;
import wdl.WorldBackup;

public class GuiWDLSaveProgress extends GuiTurningCameraBase implements WorldBackup.IBackupProgressMonitor
{
    private final String title;
    private volatile String majorTaskMessage;
    private volatile String minorTaskMessage;
    private volatile int majorTaskNumber;
    private final int majorTaskCount;
    private volatile int minorTaskProgress;
    private volatile int minorTaskMaximum;
    private volatile boolean doneWorking;
    
    public GuiWDLSaveProgress(final String title, final int taskCount) {
        this.majorTaskMessage = "";
        this.minorTaskMessage = "";
        this.doneWorking = false;
        this.title = title;
        this.majorTaskCount = taskCount;
        this.majorTaskNumber = 0;
    }
    
    public void startMajorTask(final String message, final int minorTaskMaximum) {
        this.majorTaskMessage = message;
        ++this.majorTaskNumber;
        this.minorTaskMessage = "";
        this.minorTaskProgress = 0;
        this.minorTaskMaximum = minorTaskMaximum;
    }
    
    public void setMinorTaskProgress(final String message, final int progress) {
        this.minorTaskMessage = message;
        this.minorTaskProgress = progress;
    }
    
    public void setMinorTaskProgress(final int progress) {
        this.minorTaskProgress = progress;
    }
    
    public void setDoneWorking() {
        this.doneWorking = true;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.doneWorking) {
            this.mc.displayGuiScreen(null);
        }
        else {
            Utils.drawBorder(32, 32, 0, 0, GuiWDLSaveProgress.height, GuiWDLSaveProgress.width);
            String majorTaskInfo = this.majorTaskMessage;
            if (this.majorTaskCount > 1) {
                majorTaskInfo = I18n.format("wdl.gui.saveProgress.progressInfo", this.majorTaskMessage, this.majorTaskNumber, this.majorTaskCount);
            }
            final String minorTaskInfo = this.minorTaskMessage;
            if (this.minorTaskMaximum > 1) {
                majorTaskInfo = I18n.format("wdl.gui.saveProgress.progressInfo", this.minorTaskMessage, this.minorTaskProgress, this.minorTaskMaximum);
            }
            Gui.drawCenteredString(this.fontRendererObj, this.title, GuiWDLSaveProgress.width / 2, 8, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, majorTaskInfo, GuiWDLSaveProgress.width / 2, 100, 16777215);
            if (this.minorTaskMaximum > 0) {
                this.drawProgressBar(110, 84, 89, this.majorTaskNumber * this.minorTaskMaximum + this.minorTaskProgress, (this.majorTaskCount + 1) * this.minorTaskMaximum);
            }
            else {
                this.drawProgressBar(110, 84, 89, this.majorTaskNumber, this.majorTaskCount);
            }
            Gui.drawCenteredString(this.fontRendererObj, minorTaskInfo, GuiWDLSaveProgress.width / 2, 130, 16777215);
            this.drawProgressBar(140, 64, 69, this.minorTaskProgress, this.minorTaskMaximum);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
    
    private void drawProgressBar(final int y, final int emptyV, final int filledV, final int progress, final int maximum) {
        if (maximum == 0) {
            return;
        }
        this.mc.getTextureManager().bindTexture(Gui.icons);
        final int fullWidth = 182;
        final int currentWidth = progress * 182 / maximum;
        final int height = 5;
        final int x = GuiWDLSaveProgress.width / 2 - 91;
        final int u = 0;
        this.drawTexturedModalRect(x, y, 0, emptyV, 182, 5);
        this.drawTexturedModalRect(x, y, 0, filledV, currentWidth, 5);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void setNumberOfFiles(final int num) {
        this.minorTaskMaximum = num;
    }
    
    @Override
    public void onNextFile(final String name) {
        ++this.minorTaskProgress;
        this.minorTaskMessage = I18n.format("wdl.saveProgress.backingUp.file", name);
    }
}
