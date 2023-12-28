// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import wdl.api.IWDLMessageType;
import wdl.WDLMessages;
import wdl.WDLMessageTypes;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import wdl.WDL;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import wdl.WorldBackup;

public class GuiWDLOverwriteChanges extends GuiTurningCameraBase implements WorldBackup.IBackupProgressMonitor
{
    private volatile boolean backingUp;
    private volatile String backupData;
    private volatile int backupCount;
    private volatile int backupCurrent;
    private volatile String backupFile;
    private int infoBoxX;
    private int infoBoxY;
    private int infoBoxWidth;
    private int infoBoxHeight;
    private GuiButton backupAsZipButton;
    private GuiButton backupAsFolderButton;
    private GuiButton downloadNowButton;
    private GuiButton cancelButton;
    private final long lastSaved;
    private final long lastPlayed;
    private String title;
    private String footer;
    private String captionTitle;
    private String captionSubtitle;
    private String overwriteWarning1;
    private String overwriteWarning2;
    private String backingUpTitle;
    
    public GuiWDLOverwriteChanges(final long lastSaved, final long lastPlayed) {
        this.backingUp = false;
        this.backupData = "";
        this.backupFile = "";
        this.lastSaved = lastSaved;
        this.lastPlayed = lastPlayed;
    }
    
    @Override
    public void initGui() {
        this.backingUp = false;
        this.title = I18n.format("wdl.gui.overwriteChanges.title", new Object[0]);
        if (this.lastSaved != -1L) {
            this.footer = I18n.format("wdl.gui.overwriteChanges.footer", this.lastSaved, this.lastPlayed);
        }
        else {
            this.footer = I18n.format("wdl.gui.overwriteChanges.footerNeverSaved", this.lastPlayed);
        }
        this.captionTitle = I18n.format("wdl.gui.overwriteChanges.captionTitle", new Object[0]);
        this.captionSubtitle = I18n.format("wdl.gui.overwriteChanges.captionSubtitle", new Object[0]);
        this.overwriteWarning1 = I18n.format("wdl.gui.overwriteChanges.overwriteWarning1", new Object[0]);
        this.overwriteWarning2 = I18n.format("wdl.gui.overwriteChanges.overwriteWarning2", new Object[0]);
        this.backingUpTitle = I18n.format("wdl.gui.overwriteChanges.backingUp.title", new Object[0]);
        this.infoBoxWidth = this.fontRendererObj.getStringWidth(this.overwriteWarning1);
        this.infoBoxHeight = 132;
        if (this.infoBoxWidth < 200) {
            this.infoBoxWidth = 200;
        }
        this.infoBoxY = 48;
        this.infoBoxX = GuiWDLOverwriteChanges.width / 2 - this.infoBoxWidth / 2;
        final int x = GuiWDLOverwriteChanges.width / 2 - 100;
        int y = this.infoBoxY + 22;
        this.backupAsZipButton = new GuiButton(0, x, y, I18n.format("wdl.gui.overwriteChanges.asZip.name", new Object[0]));
        this.buttonList.add(this.backupAsZipButton);
        y += 22;
        this.backupAsFolderButton = new GuiButton(1, x, y, I18n.format("wdl.gui.overwriteChanges.asFolder.name", new Object[0]));
        this.buttonList.add(this.backupAsFolderButton);
        y += 22;
        this.downloadNowButton = new GuiButton(2, x, y, I18n.format("wdl.gui.overwriteChanges.startNow.name", new Object[0]));
        this.buttonList.add(this.downloadNowButton);
        y += 22;
        this.cancelButton = new GuiButton(3, x, y, I18n.format("wdl.gui.overwriteChanges.cancel.name", new Object[0]));
        this.buttonList.add(this.cancelButton);
        super.initGui();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (this.backingUp) {
            return;
        }
        if (button.id == 0) {
            this.backingUp = true;
            new BackupThread(true).start();
        }
        if (button.id == 1) {
            this.backingUp = true;
            new BackupThread(false).start();
        }
        if (button.id == 2) {
            WDL.overrideLastModifiedCheck = true;
            this.mc.displayGuiScreen(null);
            WDL.startDownload();
        }
        if (button.id == 3) {
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.backingUp) {
            this.drawBackground(0);
            Gui.drawCenteredString(this.fontRendererObj, this.backingUpTitle, GuiWDLOverwriteChanges.width / 2, GuiWDLOverwriteChanges.height / 4 - 40, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.backupData, GuiWDLOverwriteChanges.width / 2, GuiWDLOverwriteChanges.height / 4 - 10, 16777215);
            if (this.backupFile != null) {
                final String text = I18n.format("wdl.gui.overwriteChanges.backingUp.progress", this.backupCurrent, this.backupCount, this.backupFile);
                Gui.drawCenteredString(this.fontRendererObj, text, GuiWDLOverwriteChanges.width / 2, GuiWDLOverwriteChanges.height / 4 + 10, 16777215);
            }
        }
        else {
            this.drawDefaultBackground();
            Utils.drawBorder(32, 22, 0, 0, GuiWDLOverwriteChanges.height, GuiWDLOverwriteChanges.width);
            Gui.drawCenteredString(this.fontRendererObj, this.title, GuiWDLOverwriteChanges.width / 2, 8, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.footer, GuiWDLOverwriteChanges.width / 2, GuiWDLOverwriteChanges.height - 8 - this.fontRendererObj.FONT_HEIGHT, 16777215);
            Gui.drawRect(this.infoBoxX - 5, this.infoBoxY - 5, this.infoBoxX + this.infoBoxWidth + 5, this.infoBoxY + this.infoBoxHeight + 5, -1342177280);
            Gui.drawCenteredString(this.fontRendererObj, this.captionTitle, GuiWDLOverwriteChanges.width / 2, this.infoBoxY, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.captionSubtitle, GuiWDLOverwriteChanges.width / 2, this.infoBoxY + this.fontRendererObj.FONT_HEIGHT, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.overwriteWarning1, GuiWDLOverwriteChanges.width / 2, this.infoBoxY + 115, 16777215);
            Gui.drawCenteredString(this.fontRendererObj, this.overwriteWarning2, GuiWDLOverwriteChanges.width / 2, this.infoBoxY + 115 + this.fontRendererObj.FONT_HEIGHT, 16777215);
            super.drawScreen(mouseX, mouseY, partialTicks);
            String tooltip = null;
            if (this.backupAsZipButton.isMouseOver()) {
                tooltip = I18n.format("wdl.gui.overwriteChanges.asZip.description", new Object[0]);
            }
            else if (this.backupAsFolderButton.isMouseOver()) {
                tooltip = I18n.format("wdl.gui.overwriteChanges.asFolder.description", new Object[0]);
            }
            else if (this.downloadNowButton.isMouseOver()) {
                tooltip = I18n.format("wdl.gui.overwriteChanges.startNow.description", new Object[0]);
            }
            else if (this.cancelButton.isMouseOver()) {
                tooltip = I18n.format("wdl.gui.overwriteChanges.cancel.description", new Object[0]);
            }
            Utils.drawGuiInfoBox(tooltip, GuiWDLOverwriteChanges.width, GuiWDLOverwriteChanges.height, 48);
        }
    }
    
    @Override
    public void setNumberOfFiles(final int num) {
        this.backupCount = num;
        this.backupCurrent = 0;
    }
    
    @Override
    public void onNextFile(final String name) {
        ++this.backupCurrent;
        this.backupFile = name;
    }
    
    static /* synthetic */ void access$0(final GuiWDLOverwriteChanges guiWDLOverwriteChanges, final boolean backingUp) {
        guiWDLOverwriteChanges.backingUp = backingUp;
    }
    
    static /* synthetic */ void access$2(final GuiWDLOverwriteChanges guiWDLOverwriteChanges, final String backupData) {
        guiWDLOverwriteChanges.backupData = backupData;
    }
    
    private class BackupThread extends Thread
    {
        private final DateFormat folderDateFormat;
        private final boolean zip;
        
        public BackupThread(final boolean zip) {
            this.folderDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            this.zip = zip;
        }
        
        @Override
        public void run() {
            try {
                final String backupName = String.valueOf(WDL.getWorldFolderName(WDL.worldName)) + "_" + this.folderDateFormat.format(new Date()) + "_user" + (this.zip ? ".zip" : "");
                if (this.zip) {
                    GuiWDLOverwriteChanges.access$2(GuiWDLOverwriteChanges.this, I18n.format("wdl.gui.overwriteChanges.backingUp.zip", backupName));
                }
                else {
                    GuiWDLOverwriteChanges.access$2(GuiWDLOverwriteChanges.this, I18n.format("wdl.gui.overwriteChanges.backingUp.folder", backupName));
                }
                final File fromFolder = WDL.saveHandler.getWorldDirectory();
                final File backupFile = new File(fromFolder.getParentFile(), backupName);
                if (backupFile.exists()) {
                    throw new IOException("Backup target (" + backupFile + ") already exists!");
                }
                if (this.zip) {
                    WorldBackup.zipDirectory(fromFolder, backupFile, GuiWDLOverwriteChanges.this);
                }
                else {
                    WorldBackup.copyDirectory(fromFolder, backupFile, GuiWDLOverwriteChanges.this);
                }
            }
            catch (final Exception e) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.ERROR, "wdl.messages.generalError.failedToSetUpEntityUI", new Object[0]);
                return;
            }
            finally {
                GuiWDLOverwriteChanges.access$0(GuiWDLOverwriteChanges.this, false);
                WDL.overrideLastModifiedCheck = true;
                GuiWDLOverwriteChanges.this.mc.displayGuiScreen(null);
                WDL.startDownload();
            }
            GuiWDLOverwriteChanges.access$0(GuiWDLOverwriteChanges.this, false);
            WDL.overrideLastModifiedCheck = true;
            GuiWDLOverwriteChanges.this.mc.displayGuiScreen(null);
            WDL.startDownload();
        }
    }
}
