// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLMultiworld extends GuiScreen
{
    private final MultiworldCallback callback;
    private GuiButton multiworldEnabledBtn;
    private boolean enableMultiworld;
    private int infoBoxWidth;
    private int infoBoxHeight;
    private int infoBoxX;
    private int infoBoxY;
    private List<String> infoBoxLines;
    
    public GuiWDLMultiworld(final MultiworldCallback callback) {
        this.enableMultiworld = false;
        this.callback = callback;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        final String multiworldMessage = String.valueOf(I18n.format("wdl.gui.multiworld.descirption.requiredWhen", new Object[0])) + "\n\n" + I18n.format("wdl.gui.multiworld.descirption.whatIs", new Object[0]);
        this.infoBoxWidth = 320;
        this.infoBoxLines = Utils.wordWrap(multiworldMessage, this.infoBoxWidth - 20);
        this.infoBoxHeight = this.fontRendererObj.FONT_HEIGHT * (this.infoBoxLines.size() + 1) + 40;
        this.infoBoxX = GuiWDLMultiworld.width / 2 - this.infoBoxWidth / 2;
        this.infoBoxY = GuiWDLMultiworld.height / 2 - this.infoBoxHeight / 2;
        this.multiworldEnabledBtn = new GuiButton(1, GuiWDLMultiworld.width / 2 - 100, this.infoBoxY + this.infoBoxHeight - 30, this.getMultiworldEnabledText());
        this.buttonList.add(this.multiworldEnabledBtn);
        this.buttonList.add(new GuiButton(100, GuiWDLMultiworld.width / 2 - 155, GuiWDLMultiworld.height - 29, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(101, GuiWDLMultiworld.width / 2 + 5, GuiWDLMultiworld.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 1) {
            this.toggleMultiworldEnabled();
        }
        else if (button.id == 100) {
            this.callback.onCancel();
        }
        else if (button.id == 101) {
            this.callback.onSelect(this.enableMultiworld);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Utils.drawBorder(32, 32, 0, 0, GuiWDLMultiworld.height, GuiWDLMultiworld.width);
        Gui.drawCenteredString(this.fontRendererObj, I18n.format("wdl.gui.multiworld.title", new Object[0]), GuiWDLMultiworld.width / 2, 8, 16777215);
        Gui.drawRect(this.infoBoxX, this.infoBoxY, this.infoBoxX + this.infoBoxWidth, this.infoBoxY + this.infoBoxHeight, -1342177280);
        final int x = this.infoBoxX + 10;
        int y = this.infoBoxY + 10;
        for (final String s : this.infoBoxLines) {
            Gui.drawString(this.fontRendererObj, s, x, y, 16777215);
            y += this.fontRendererObj.FONT_HEIGHT;
        }
        Gui.drawRect(this.multiworldEnabledBtn.xPosition - 2, this.multiworldEnabledBtn.yPosition - 2, this.multiworldEnabledBtn.xPosition + this.multiworldEnabledBtn.getButtonWidth() + 2, this.multiworldEnabledBtn.yPosition + 20 + 2, -65536);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void toggleMultiworldEnabled() {
        if (this.enableMultiworld) {
            this.enableMultiworld = false;
        }
        else {
            this.enableMultiworld = true;
        }
        this.multiworldEnabledBtn.displayString = this.getMultiworldEnabledText();
    }
    
    private String getMultiworldEnabledText() {
        return I18n.format("wdl.gui.multiworld." + this.enableMultiworld, new Object[0]);
    }
    
    public interface MultiworldCallback
    {
        void onCancel();
        
        void onSelect(final boolean p0);
    }
}
