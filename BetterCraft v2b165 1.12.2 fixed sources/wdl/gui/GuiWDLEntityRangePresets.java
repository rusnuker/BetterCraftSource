// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import java.util.Iterator;
import java.util.Set;
import wdl.WDL;
import wdl.EntityUtils;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.gui.GuiYesNo;
import wdl.WDLPluginChannels;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLEntityRangePresets extends GuiScreen implements GuiYesNoCallback
{
    private final GuiScreen parent;
    private GuiButton vanillaButton;
    private GuiButton spigotButton;
    private GuiButton serverButton;
    private GuiButton cancelButton;
    
    public GuiWDLEntityRangePresets(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        int y = GuiWDLEntityRangePresets.height / 4;
        this.vanillaButton = new GuiButton(0, GuiWDLEntityRangePresets.width / 2 - 100, y, I18n.format("wdl.gui.rangePresets.vanilla", new Object[0]));
        y += 22;
        this.spigotButton = new GuiButton(1, GuiWDLEntityRangePresets.width / 2 - 100, y, I18n.format("wdl.gui.rangePresets.spigot", new Object[0]));
        y += 22;
        this.serverButton = new GuiButton(2, GuiWDLEntityRangePresets.width / 2 - 100, y, I18n.format("wdl.gui.rangePresets.server", new Object[0]));
        this.serverButton.enabled = WDLPluginChannels.hasServerEntityRange();
        this.buttonList.add(this.vanillaButton);
        this.buttonList.add(this.spigotButton);
        this.buttonList.add(this.serverButton);
        y += 28;
        this.cancelButton = new GuiButton(100, GuiWDLEntityRangePresets.width / 2 - 100, GuiWDLEntityRangePresets.height - 29, I18n.format("gui.cancel", new Object[0]));
        this.buttonList.add(this.cancelButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (!button.enabled) {
            return;
        }
        if (button.id < 3) {
            final String upper = I18n.format("wdl.gui.rangePresets.upperWarning", new Object[0]);
            String lower;
            if (button.id == 0) {
                lower = I18n.format("wdl.gui.rangePresets.vanilla.warning", new Object[0]);
            }
            else if (button.id == 1) {
                lower = I18n.format("wdl.gui.rangePresets.spigot.warning", new Object[0]);
            }
            else {
                if (button.id != 2) {
                    throw new Error("Button.id should never be negative.");
                }
                lower = I18n.format("wdl.gui.rangePresets.server.warning", new Object[0]);
            }
            this.mc.displayGuiScreen(new GuiYesNo(this, upper, lower, button.id));
        }
        if (button.id == 100) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Utils.drawListBackground(23, 32, 0, 0, GuiWDLEntityRangePresets.height, GuiWDLEntityRangePresets.width);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.rangePresets.title", new Object[0]), GuiWDLEntityRangePresets.width / 2, 8, 16777215);
        String infoText = null;
        if (this.vanillaButton.isMouseOver()) {
            infoText = I18n.format("wdl.gui.rangePresets.vanilla.description", new Object[0]);
        }
        else if (this.spigotButton.isMouseOver()) {
            infoText = I18n.format("wdl.gui.rangePresets.spigot.description", new Object[0]);
        }
        else if (this.serverButton.isMouseOver()) {
            infoText = String.valueOf(I18n.format("wdl.gui.rangePresets.server.description", new Object[0])) + "\n\n";
            if (this.serverButton.enabled) {
                infoText = String.valueOf(infoText) + I18n.format("wdl.gui.rangePresets.server.installed", new Object[0]);
            }
            else {
                infoText = String.valueOf(infoText) + I18n.format("wdl.gui.rangePresets.server.notInstalled", new Object[0]);
            }
        }
        else if (this.cancelButton.isMouseOver()) {
            infoText = I18n.format("wdl.gui.rangePresets.cancel.description", new Object[0]);
        }
        if (infoText != null) {
            Utils.drawGuiInfoBox(infoText, GuiWDLEntityRangePresets.width, GuiWDLEntityRangePresets.height, 48);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result) {
            final Set<String> entities = EntityUtils.getEntityTypes();
            if (id == 0) {
                for (final String entity : entities) {
                    WDL.worldProps.setProperty("Entity." + entity + ".TrackDistance", Integer.toString(EntityUtils.getDefaultEntityRange(entity)));
                }
            }
            else if (id == 1) {
                for (final String entity : entities) {
                    final Class<?> c = null;
                    if (c == null) {
                        continue;
                    }
                    WDL.worldProps.setProperty("Entity." + entity + ".TrackDistance", Integer.toString(EntityUtils.getDefaultSpigotEntityRange(c)));
                }
            }
            else if (id == 2) {
                for (final String entity : entities) {
                    WDL.worldProps.setProperty("Entity." + entity + ".TrackDistance", Integer.toString(WDLPluginChannels.getEntityRange(entity)));
                }
            }
        }
        this.mc.displayGuiScreen(this.parent);
    }
    
    @Override
    public void onGuiClosed() {
        WDL.saveProps();
    }
}
