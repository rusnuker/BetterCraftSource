// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.GameSettings;

public class GuiControls extends GuiScreen
{
    private static final GameSettings.Options[] OPTIONS_ARR;
    private final GuiScreen parentScreen;
    protected String screenTitle;
    private final GameSettings options;
    public KeyBinding buttonId;
    public long time;
    private GuiKeyBindingList keyBindingList;
    private GuiButton buttonReset;
    
    static {
        OPTIONS_ARR = new GameSettings.Options[] { GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN, GameSettings.Options.AUTO_JUMP };
    }
    
    public GuiControls(final GuiScreen screen, final GameSettings settings) {
        this.screenTitle = "Controls";
        this.parentScreen = screen;
        this.options = settings;
    }
    
    @Override
    public void initGui() {
        this.keyBindingList = new GuiKeyBindingList(this, this.mc);
        this.buttonList.add(new GuiButton(200, GuiControls.width / 2 - 155 + 160, GuiControls.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonReset = this.addButton(new GuiButton(201, GuiControls.width / 2 - 155, GuiControls.height - 29, 150, 20, I18n.format("controls.resetAll", new Object[0])));
        this.screenTitle = I18n.format("controls.title", new Object[0]);
        int i = 0;
        GameSettings.Options[] options_ARR;
        for (int length = (options_ARR = GuiControls.OPTIONS_ARR).length, j = 0; j < length; ++j) {
            final GameSettings.Options gamesettings$options = options_ARR[j];
            if (gamesettings$options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), GuiControls.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options));
            }
            else {
                this.buttonList.add(new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), GuiControls.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.options.getKeyBinding(gamesettings$options)));
            }
            ++i;
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.keyBindingList.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 201) {
            KeyBinding[] keyBindings;
            for (int length = (keyBindings = this.mc.gameSettings.keyBindings).length, i = 0; i < length; ++i) {
                final KeyBinding keybinding = keyBindings[i];
                keybinding.setKeyCode(keybinding.getKeyCodeDefault());
            }
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (button.id < 100 && button instanceof GuiOptionButton) {
            this.options.setOptionValue(((GuiOptionButton)button).returnEnumOptions(), 1);
            button.displayString = this.options.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.buttonId != null) {
            this.options.setOptionKeyBinding(this.buttonId, -100 + mouseButton);
            this.buttonId = null;
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (state != 0 || !this.keyBindingList.mouseReleased(mouseX, mouseY, state)) {
            super.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.buttonId != null) {
            if (keyCode == 1) {
                this.options.setOptionKeyBinding(this.buttonId, 0);
            }
            else if (keyCode != 0) {
                this.options.setOptionKeyBinding(this.buttonId, keyCode);
            }
            else if (typedChar > '\0') {
                this.options.setOptionKeyBinding(this.buttonId, typedChar + '\u0100');
            }
            this.buttonId = null;
            this.time = Minecraft.getSystemTime();
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, this.screenTitle, GuiControls.width / 2, 8, 16777215);
        boolean flag = false;
        KeyBinding[] keyBindings;
        for (int length = (keyBindings = this.options.keyBindings).length, i = 0; i < length; ++i) {
            final KeyBinding keybinding = keyBindings[i];
            if (keybinding.getKeyCode() != keybinding.getKeyCodeDefault()) {
                flag = true;
                break;
            }
        }
        this.buttonReset.enabled = flag;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
