// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.gui.Gui;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.resources.I18n;
import java.util.Map;
import wdl.WDLPluginChannels;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiWDLPermissionRequest extends GuiScreen
{
    private static final int TOP_MARGIN = 61;
    private static final int BOTTOM_MARGIN = 32;
    private TextList list;
    private final GuiScreen parent;
    private GuiTextField requestField;
    private GuiButton submitButton;
    
    public GuiWDLPermissionRequest(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        (this.list = new TextList(this.mc, GuiWDLPermissionRequest.width, GuiWDLPermissionRequest.height, 61, 32)).addLine("§c§lThis is a work in progress.");
        this.list.addLine("You can request permissions in this GUI, although it currently requires manually specifying the names.");
        this.list.addBlankLine();
        this.list.addLine("Boolean fields: " + WDLPluginChannels.BOOLEAN_REQUEST_FIELDS);
        this.list.addLine("Integer fields: " + WDLPluginChannels.INTEGER_REQUEST_FIELDS);
        this.list.addBlankLine();
        for (final Map.Entry<String, String> request : WDLPluginChannels.getRequests().entrySet()) {
            this.list.addLine("Requesting '" + request.getKey() + "' to be '" + request.getValue() + "'.");
        }
        this.requestField = new GuiTextField(0, this.fontRendererObj, GuiWDLPermissionRequest.width / 2 - 155, 18, 150, 20);
        this.submitButton = new GuiButton(1, GuiWDLPermissionRequest.width / 2 + 5, 18, 150, 20, "Submit request");
        this.submitButton.enabled = !WDLPluginChannels.getRequests().isEmpty();
        this.buttonList.add(this.submitButton);
        this.buttonList.add(new GuiButton(100, GuiWDLPermissionRequest.width / 2 - 100, GuiWDLPermissionRequest.height - 29, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(200, GuiWDLPermissionRequest.width / 2 - 155, 39, 100, 20, I18n.format("wdl.gui.permissions.current", new Object[0])));
        this.buttonList.add(new GuiButton(201, GuiWDLPermissionRequest.width / 2 - 50, 39, 100, 20, I18n.format("wdl.gui.permissions.request", new Object[0])));
        this.buttonList.add(new GuiButton(202, GuiWDLPermissionRequest.width / 2 + 55, 39, 100, 20, I18n.format("wdl.gui.permissions.overrides", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 1) {
            WDLPluginChannels.sendRequests();
            button.displayString = "Submitted!";
        }
        if (button.id == 100) {
            this.mc.displayGuiScreen(this.parent);
        }
        if (button.id == 200) {
            this.mc.displayGuiScreen(new GuiWDLPermissions(this.parent));
        }
        final int id = button.id;
        if (button.id == 202) {
            this.mc.displayGuiScreen(new GuiWDLChunkOverrides(this.parent));
        }
    }
    
    @Override
    public void updateScreen() {
        this.requestField.updateCursorCounter();
        super.updateScreen();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.requestField.mouseClicked(mouseX, mouseY, mouseButton);
        this.list.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.requestField.textboxKeyTyped(typedChar, keyCode);
        if (this.requestField.isFocused()) {
            final String request = this.requestField.getText();
            boolean isValid = false;
            if (request.contains("=")) {
                final String[] requestData = request.split("=", 2);
                if (requestData.length == 2) {
                    final String key = requestData[0];
                    final String value = requestData[1];
                    isValid = WDLPluginChannels.isValidRequest(key, value);
                    if (isValid && keyCode == 28) {
                        this.requestField.setText("");
                        isValid = false;
                        WDLPluginChannels.addRequest(key, value);
                        this.list.addLine("Requesting '" + key + "' to be '" + value + "'.");
                        this.submitButton.enabled = true;
                    }
                }
            }
            this.requestField.setTextColor(isValid ? 4251712 : 14696512);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.list.mouseReleased(mouseX, mouseY, state)) {
            return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.list == null) {
            return;
        }
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.requestField.drawTextBox();
        Gui.drawCenteredString(this.fontRendererObj, "Permission request", GuiWDLPermissionRequest.width / 2, 8, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
