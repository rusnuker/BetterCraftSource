// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import net.labymod.utils.ModColor;
import org.lwjgl.input.Keyboard;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.main.ModSettings;
import net.labymod.gui.elements.ModTextField;
import net.labymod.utils.Consumer;

public class KeyElement extends ControlElement
{
    private Integer currentKey;
    private Consumer<Integer> changeListener;
    private ModTextField textField;
    private Consumer<Integer> callback;
    
    public KeyElement(final String displayName, final String configEntryName, final IconData iconData) {
        super(displayName, configEntryName, iconData);
        if (!configEntryName.isEmpty()) {
            try {
                this.currentKey = (Integer)ModSettings.class.getDeclaredField(configEntryName).get(LabyMod.getSettings());
            }
            catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (final NoSuchFieldException e2) {
                e2.printStackTrace();
            }
        }
        if (this.currentKey == null) {
            this.currentKey = -1;
        }
        this.changeListener = new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                try {
                    ModSettings.class.getDeclaredField(configEntryName).set(LabyMod.getSettings(), accepted);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                if (KeyElement.this.callback != null) {
                    KeyElement.this.callback.accept(accepted);
                }
            }
        };
        this.createTextfield();
    }
    
    public KeyElement(final String displayName, final LabyModAddon labymodAddon, final IconData iconData, final String attributeName, final int currentKey) {
        super(displayName, iconData);
        this.currentKey = currentKey;
        this.changeListener = new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                labymodAddon.getConfig().addProperty(attributeName, accepted);
                labymodAddon.loadConfig();
                if (KeyElement.this.callback != null) {
                    KeyElement.this.callback.accept(accepted);
                }
            }
        };
        this.createTextfield();
    }
    
    public KeyElement(final String displayName, final IconData iconData, final int currentKey, final Consumer<Integer> changeListener) {
        super(displayName, iconData);
        this.currentKey = currentKey;
        this.changeListener = changeListener;
        this.createTextfield();
    }
    
    public KeyElement(final String configEntryName, final IconData iconData) {
        this(configEntryName, configEntryName, iconData);
    }
    
    public void createTextfield() {
        this.updateValue();
        this.textField.setCursorPositionEnd();
        this.textField.setFocused(false);
    }
    
    private void updateValue() {
        if (this.currentKey == -1) {
            this.textField.setText("NONE");
        }
        else {
            try {
                this.textField.setText(Keyboard.getKeyName(this.currentKey));
            }
            catch (final Exception error) {
                this.currentKey = -1;
                error.printStackTrace();
            }
        }
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        if (this.textField == null) {
            return;
        }
        this.textField.yPosition = y + 1;
        this.textField.drawTextBox();
        LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
    }
    
    @Override
    public void unfocus(final int mouseX, final int mouseY, final int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
        this.textField.setFocused(false);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, 0);
    }
    
    @Override
    public void keyTyped(final char typedChar, int keyCode) {
        if (keyCode == 1) {
            keyCode = -1;
        }
        if (this.textField.isFocused()) {
            this.textField.setFocused(false);
            this.currentKey = keyCode;
            this.changeListener.accept(keyCode);
            this.updateValue();
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.textField.updateCursorCounter();
    }
    
    public KeyElement maxLength(final int maxLength) {
        this.textField.setMaxStringLength(maxLength);
        return this;
    }
    
    public KeyElement addCallback(final Consumer<Integer> callback) {
        this.callback = callback;
        return this;
    }
    
    public ModTextField getTextField() {
        return this.textField;
    }
}
