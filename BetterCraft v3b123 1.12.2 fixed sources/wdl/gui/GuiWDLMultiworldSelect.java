// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import java.io.File;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import java.util.Properties;
import java.util.Collection;
import java.util.ArrayList;
import wdl.WDL;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;

public class GuiWDLMultiworldSelect extends GuiTurningCameraBase
{
    private final WorldSelectionCallback callback;
    private final String title;
    private GuiButton cancelBtn;
    private GuiButton acceptBtn;
    private GuiTextField newNameField;
    private GuiTextField searchField;
    private GuiButton newWorldButton;
    private boolean showNewWorldTextBox;
    private List<MultiworldInfo> linkedWorlds;
    private List<MultiworldInfo> linkedWorldsFiltered;
    private MultiworldInfo selectedMultiWorld;
    private int index;
    private GuiButton nextButton;
    private GuiButton prevButton;
    private int numWorldButtons;
    private String searchText;
    
    public GuiWDLMultiworldSelect(final String title, final WorldSelectionCallback callback) {
        this.index = 0;
        this.searchText = "";
        this.title = title;
        this.callback = callback;
        final String[] worldNames = WDL.baseProps.getProperty("LinkedWorlds").split("\\|");
        this.linkedWorlds = new ArrayList<MultiworldInfo>();
        String[] array;
        for (int length = (array = worldNames).length, i = 0; i < length; ++i) {
            final String worldName = array[i];
            if (worldName != null) {
                if (!worldName.isEmpty()) {
                    final Properties props = WDL.loadWorldProps(worldName);
                    if (props.containsKey("WorldName")) {
                        final String displayName = props.getProperty("WorldName", worldName);
                        this.linkedWorlds.add(new MultiworldInfo(worldName, displayName));
                    }
                }
            }
        }
        (this.linkedWorldsFiltered = new ArrayList<MultiworldInfo>()).addAll(this.linkedWorlds);
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.numWorldButtons = (GuiWDLMultiworldSelect.width - 50) / 155;
        if (this.numWorldButtons < 1) {
            this.numWorldButtons = 1;
        }
        final int offset = (this.numWorldButtons * 155 + 45) / 2;
        final int y = GuiWDLMultiworldSelect.height - 49;
        this.cancelBtn = new GuiButton(-1, GuiWDLMultiworldSelect.width / 2 - 155, GuiWDLMultiworldSelect.height - 25, 150, 20, I18n.format("gui.cancel", new Object[0]));
        this.buttonList.add(this.cancelBtn);
        this.acceptBtn = new GuiButton(-2, GuiWDLMultiworldSelect.width / 2 + 5, GuiWDLMultiworldSelect.height - 25, 150, 20, I18n.format("wdl.gui.multiworldSelect.done", new Object[0]));
        this.acceptBtn.enabled = (this.selectedMultiWorld != null);
        this.buttonList.add(this.acceptBtn);
        this.prevButton = new GuiButton(-4, GuiWDLMultiworldSelect.width / 2 - offset, y, 20, 20, "<");
        this.buttonList.add(this.prevButton);
        for (int i = 0; i < this.numWorldButtons; ++i) {
            this.buttonList.add(new WorldGuiButton(i, GuiWDLMultiworldSelect.width / 2 - offset + i * 155 + 25, y, 150, 20));
        }
        this.nextButton = new GuiButton(-5, GuiWDLMultiworldSelect.width / 2 - offset + 25 + this.numWorldButtons * 155, y, 20, 20, ">");
        this.buttonList.add(this.nextButton);
        this.newWorldButton = new GuiButton(-3, GuiWDLMultiworldSelect.width / 2 - 155, 29, 150, 20, I18n.format("wdl.gui.multiworldSelect.newName", new Object[0]));
        this.buttonList.add(this.newWorldButton);
        this.newNameField = new GuiTextField(40, this.fontRendererObj, GuiWDLMultiworldSelect.width / 2 - 155, 29, 150, 20);
        (this.searchField = new GuiTextField(41, this.fontRendererObj, GuiWDLMultiworldSelect.width / 2 + 5, 29, 150, 20)).setText(this.searchText);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.enabled) {
            if (button instanceof WorldGuiButton) {
                this.selectedMultiWorld = ((WorldGuiButton)button).getWorldInfo();
                if (this.selectedMultiWorld != null) {
                    this.acceptBtn.enabled = true;
                }
                else {
                    this.acceptBtn.enabled = false;
                }
            }
            else if (button.id == -1) {
                this.callback.onCancel();
            }
            else if (button.id == -2) {
                this.callback.onWorldSelected(this.selectedMultiWorld.folderName);
            }
            else if (button.id == -3) {
                this.showNewWorldTextBox = true;
            }
            else if (button.id == -4) {
                --this.index;
            }
            else if (button.id == -5) {
                ++this.index;
            }
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.showNewWorldTextBox) {
            this.newNameField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.callback.onCancel();
        }
        super.keyTyped(typedChar, keyCode);
        if (this.showNewWorldTextBox) {
            this.newNameField.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == 28) {
                final String newName = this.newNameField.getText();
                if (newName != null && !newName.isEmpty()) {
                    this.addMultiworld(newName);
                    this.newNameField.setText("");
                    this.showNewWorldTextBox = false;
                }
            }
        }
        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            this.searchText = this.searchField.getText();
            this.rebuildFilteredWorlds();
        }
    }
    
    @Override
    public void updateScreen() {
        this.newNameField.updateCursorCounter();
        this.searchField.updateCursorCounter();
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.index >= this.linkedWorlds.size() - this.numWorldButtons) {
            this.index = this.linkedWorlds.size() - this.numWorldButtons;
            this.nextButton.enabled = false;
        }
        else {
            this.nextButton.enabled = true;
        }
        if (this.index <= 0) {
            this.index = 0;
            this.prevButton.enabled = false;
        }
        else {
            this.prevButton.enabled = true;
        }
        Utils.drawBorder(53, 53, 0, 0, GuiWDLMultiworldSelect.height, GuiWDLMultiworldSelect.width);
        Gui.drawCenteredString(this.fontRendererObj, this.title, GuiWDLMultiworldSelect.width / 2, 8, 16777215);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.multiworldSelect.subtitle", new Object[0]), GuiWDLMultiworldSelect.width / 2, 18, 16711680);
        if (this.showNewWorldTextBox) {
            this.newNameField.drawTextBox();
        }
        this.searchField.drawTextBox();
        if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
            this.drawString(this.fontRendererObj, I18n.format("wdl.gui.multiworldSelect.search", new Object[0]), this.searchField.xPosition + 4, this.searchField.yPosition + 6, 9474192);
        }
        this.newWorldButton.visible = !this.showNewWorldTextBox;
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawMultiworldDescription();
    }
    
    private void addMultiworld(final String worldName) {
        String folderName = worldName;
        final char[] unsafeChars = "\\/:*?\"<>|.".toCharArray();
        char[] array;
        for (int length = (array = unsafeChars).length, i = 0; i < length; ++i) {
            final char unsafeChar = array[i];
            folderName = folderName.replace(unsafeChar, '_');
        }
        final Properties worldProps = new Properties(WDL.baseProps);
        worldProps.setProperty("WorldName", worldName);
        String linkedWorldsProp = WDL.baseProps.getProperty("LinkedWorlds");
        linkedWorldsProp = String.valueOf(linkedWorldsProp) + "|" + folderName;
        WDL.baseProps.setProperty("LinkedWorlds", linkedWorldsProp);
        WDL.saveProps(folderName, worldProps);
        this.linkedWorlds.add(new MultiworldInfo(folderName, worldName));
        this.rebuildFilteredWorlds();
    }
    
    private void rebuildFilteredWorlds() {
        final String searchFilter = this.searchText.toLowerCase();
        this.linkedWorldsFiltered.clear();
        for (final MultiworldInfo info : this.linkedWorlds) {
            if (info.displayName.toLowerCase().contains(searchFilter)) {
                this.linkedWorldsFiltered.add(info);
            }
        }
    }
    
    private void drawMultiworldDescription() {
        if (this.selectedMultiWorld == null) {
            return;
        }
        final String title = "Info about " + this.selectedMultiWorld.displayName;
        final List<String> description = this.selectedMultiWorld.getDescription();
        int maxWidth = this.fontRendererObj.getStringWidth(title);
        for (final String line : description) {
            final int width = this.fontRendererObj.getStringWidth(line);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        Gui.drawRect(2, 61, 5 + maxWidth + 3, GuiWDLMultiworldSelect.height - 61, Integer.MIN_VALUE);
        this.drawString(this.fontRendererObj, title, 5, 64, 16777215);
        int y = 64 + this.fontRendererObj.FONT_HEIGHT;
        for (final String s : description) {
            this.drawString(this.fontRendererObj, s, 5, y, 16777215);
            y += this.fontRendererObj.FONT_HEIGHT;
        }
    }
    
    private class WorldGuiButton extends GuiButton
    {
        public WorldGuiButton(final int offset, final int x, final int y, final int width, final int height) {
            super(offset, x, y, width, height, "");
        }
        
        public WorldGuiButton(final int offset, final int x, final int y, final String worldName, final String displayName) {
            super(offset, x, y, "");
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
            final MultiworldInfo info = this.getWorldInfo();
            if (info == null) {
                this.displayString = "";
                this.enabled = false;
            }
            else {
                this.displayString = info.displayName;
                this.enabled = true;
            }
            if (info != null && info == GuiWDLMultiworldSelect.this.selectedMultiWorld) {
                Gui.drawRect(this.xPosition - 2, this.yPosition - 2, this.xPosition + this.width + 2, this.yPosition + this.height + 2, -16744704);
            }
            super.drawButton(mc, mouseX, mouseY);
        }
        
        public MultiworldInfo getWorldInfo() {
            final int location = GuiWDLMultiworldSelect.this.index + this.id;
            if (location < 0) {
                return null;
            }
            if (location >= GuiWDLMultiworldSelect.this.linkedWorldsFiltered.size()) {
                return null;
            }
            return GuiWDLMultiworldSelect.this.linkedWorldsFiltered.get(location);
        }
    }
    
    private static class MultiworldInfo
    {
        public final String folderName;
        public final String displayName;
        private List<String> description;
        
        public MultiworldInfo(final String folderName, final String displayName) {
            this.folderName = folderName;
            this.displayName = displayName;
        }
        
        public List<String> getDescription() {
            if (this.description == null) {
                (this.description = new ArrayList<String>()).add("Defined dimensions:");
                final File savesFolder = new File(WDL.minecraft.mcDataDir, "saves");
                final File world = new File(savesFolder, WDL.getWorldFolderName(this.folderName));
                final File[] subfolders = world.listFiles();
                if (subfolders != null) {
                    File[] array;
                    for (int length = (array = subfolders).length, i = 0; i < length; ++i) {
                        final File subfolder = array[i];
                        if (subfolder.listFiles() != null) {
                            if (subfolder.listFiles().length != 0) {
                                if (subfolder.getName().equals("region")) {
                                    this.description.add(" * Overworld (#0)");
                                }
                                else if (subfolder.getName().startsWith("DIM")) {
                                    final String dimension = subfolder.getName().substring(3);
                                    if (dimension.equals("-1")) {
                                        this.description.add(" * Nether (#-1)");
                                    }
                                    else if (dimension.equals("1")) {
                                        this.description.add(" * The End (#1)");
                                    }
                                    else {
                                        this.description.add(" * #" + dimension);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return this.description;
        }
    }
    
    public interface WorldSelectionCallback
    {
        void onCancel();
        
        void onWorldSelected(final String p0);
    }
}
