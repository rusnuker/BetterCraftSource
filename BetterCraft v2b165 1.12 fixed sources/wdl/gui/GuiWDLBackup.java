// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import wdl.WDL;
import net.minecraft.client.resources.I18n;
import wdl.WorldBackup;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLBackup extends GuiScreen
{
    private GuiScreen parent;
    private String description;
    private WorldBackup.WorldBackupType backupType;
    
    public GuiWDLBackup(final GuiScreen parent) {
        this.parent = parent;
        this.description = String.valueOf(I18n.format("wdl.gui.backup.description1", new Object[0])) + "\n\n" + I18n.format("wdl.gui.backup.description2", new Object[0]) + "\n\n" + I18n.format("wdl.gui.backup.description3", new Object[0]);
    }
    
    @Override
    public void initGui() {
        this.backupType = WorldBackup.WorldBackupType.match(WDL.baseProps.getProperty("Backup", "ZIP"));
        this.buttonList.add(new GuiButton(0, GuiWDLBackup.width / 2 - 100, 32, this.getBackupButtonText()));
        this.buttonList.add(new GuiButton(100, GuiWDLBackup.width / 2 - 100, GuiWDLBackup.height - 29, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (!button.enabled) {
            return;
        }
        if (button.id == 0) {
            switch (this.backupType) {
                case NONE: {
                    this.backupType = WorldBackup.WorldBackupType.FOLDER;
                    break;
                }
                case FOLDER: {
                    this.backupType = WorldBackup.WorldBackupType.ZIP;
                    break;
                }
                case ZIP: {
                    this.backupType = WorldBackup.WorldBackupType.NONE;
                    break;
                }
            }
            button.displayString = this.getBackupButtonText();
        }
        else if (button.id == 100) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    private String getBackupButtonText() {
        return I18n.format("wdl.gui.backup.backupMode", this.backupType.getDescription());
    }
    
    @Override
    public void onGuiClosed() {
        WDL.baseProps.setProperty("Backup", this.backupType.name());
        WDL.saveProps();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Utils.drawListBackground(23, 32, 0, 0, GuiWDLBackup.height, GuiWDLBackup.width);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.backup.title", new Object[0]), GuiWDLBackup.width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        Utils.drawGuiInfoBox(this.description, GuiWDLBackup.width - 50, 3 * GuiWDLBackup.height / 5, GuiWDLBackup.width, GuiWDLBackup.height, 48);
    }
}
