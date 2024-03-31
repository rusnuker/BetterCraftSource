// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import org.apache.commons.lang3.StringUtils;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ModColor;
import net.labymod.core.LabyModCore;
import java.lang.reflect.Field;
import net.labymod.main.LabyMod;
import net.labymod.main.ModSettings;
import net.minecraft.client.gui.GuiTextField;
import net.labymod.utils.Consumer;
import net.minecraft.util.ResourceLocation;

public class NumberElement extends ControlElement
{
    private static final ResourceLocation SERVER_SELECTION_BUTTONS;
    private Integer currentValue;
    private Consumer<Integer> changeListener;
    private GuiTextField textField;
    private Consumer<Integer> callback;
    private int minValue;
    private int maxValue;
    private boolean hoverUp;
    private boolean hoverDown;
    private int steps;
    private long fastTickerCounterValue;
    
    static {
        SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
    }
    
    public NumberElement(final String displayName, final String configEntryName, final IconData iconData) {
        super(displayName, configEntryName, iconData);
        this.minValue = 0;
        this.maxValue = Integer.MAX_VALUE;
        this.steps = 1;
        this.fastTickerCounterValue = 0L;
        if (!configEntryName.isEmpty()) {
            try {
                this.currentValue = (Integer)ModSettings.class.getDeclaredField(configEntryName).get(LabyMod.getSettings());
            }
            catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (final NoSuchFieldException e2) {
                e2.printStackTrace();
            }
        }
        if (this.currentValue == null) {
            this.currentValue = this.minValue;
        }
        this.changeListener = new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                try {
                    final Field f = ModSettings.class.getDeclaredField(configEntryName);
                    if (f.getType().equals(Integer.TYPE)) {
                        f.set(LabyMod.getSettings(), accepted);
                    }
                    else {
                        f.set(LabyMod.getSettings(), String.valueOf(accepted));
                    }
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                if (NumberElement.this.callback != null) {
                    NumberElement.this.callback.accept(accepted);
                }
            }
        };
        this.createTextfield();
    }
    
    public NumberElement(final String displayName, final IconData iconData, final int currentValue) {
        super(displayName, null, iconData);
        this.minValue = 0;
        this.maxValue = Integer.MAX_VALUE;
        this.steps = 1;
        this.fastTickerCounterValue = 0L;
        this.currentValue = currentValue;
        this.changeListener = new Consumer<Integer>() {
            @Override
            public void accept(final Integer accepted) {
                if (NumberElement.this.callback != null) {
                    NumberElement.this.callback.accept(accepted);
                }
            }
        };
        this.createTextfield();
    }
    
    public NumberElement(final String configEntryName, final IconData iconData) {
        this(configEntryName, configEntryName, iconData);
    }
    
    public NumberElement setMinValue(final int minValue) {
        this.minValue = minValue;
        if (this.currentValue < this.minValue) {
            this.currentValue = this.minValue;
        }
        return this;
    }
    
    public NumberElement setMaxValue(final int maxValue) {
        this.maxValue = maxValue;
        if (this.currentValue > this.maxValue) {
            this.currentValue = this.maxValue;
        }
        return this;
    }
    
    public NumberElement setRange(final int min, final int max) {
        this.setMinValue(min);
        this.setMaxValue(max);
        return this;
    }
    
    public NumberElement setSteps(final int steps) {
        this.steps = steps;
        return this;
    }
    
    public void createTextfield() {
        this.textField = new GuiTextField(-2, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, this.getSubListButtonWidth(), 20);
        this.updateValue();
        this.textField.setFocused(false);
    }
    
    private void updateValue() {
        this.textField.setText(String.valueOf(this.currentValue));
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        if (this.textField == null) {
            return;
        }
        LabyModCore.getMinecraft().setTextFieldYPosition(this.textField, y + 1);
        this.textField.drawTextBox();
        LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        Minecraft.getMinecraft().getTextureManager().bindTexture(NumberElement.SERVER_SELECTION_BUTTONS);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        this.hoverUp = (mouseX > maxX - 15 && mouseX < maxX - 15 + 11 && mouseY > y + 2 && mouseY < y + 2 + 7);
        this.hoverDown = (mouseX > maxX - 15 && mouseX < maxX - 15 + 11 && mouseY > y + 12 && mouseY < y + 12 + 7);
        draw.drawTexture(maxX - 15, y + 2, 99.0, this.hoverUp ? 37.0 : 5.0, 11.0, 7.0, 11.0, 7.0);
        draw.drawTexture(maxX - 15, y + 12, 67.0, this.hoverDown ? 52.0 : 20.0, 11.0, 7.0, 11.0, 7.0);
        if (this.isMouseOver() && this.fastTickerCounterValue != 0L) {
            if (this.fastTickerCounterValue > 0L && this.fastTickerCounterValue + 80L < System.currentTimeMillis()) {
                this.fastTickerCounterValue = System.currentTimeMillis();
                if (this.currentValue < this.maxValue) {
                    this.currentValue += this.steps;
                    this.updateValue();
                }
            }
            if (this.fastTickerCounterValue < 0L && this.fastTickerCounterValue - 80L > System.currentTimeMillis() * -1L) {
                this.fastTickerCounterValue = System.currentTimeMillis() * -1L;
                if (this.currentValue > this.minValue) {
                    this.currentValue -= this.steps;
                    this.updateValue();
                }
            }
        }
        else {
            this.mouseRelease(mouseX, mouseY, 0);
        }
    }
    
    @Override
    public void unfocus(final int mouseX, final int mouseY, final int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
        this.textField.setFocused(false);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoverUp && this.currentValue < this.maxValue) {
            this.currentValue += this.steps;
            this.updateValue();
            this.fastTickerCounterValue = System.currentTimeMillis() + 500L;
        }
        if (this.hoverDown && this.currentValue > this.minValue) {
            this.currentValue -= this.steps;
            this.updateValue();
            this.fastTickerCounterValue = System.currentTimeMillis() * -1L - 500L;
        }
        if (this.currentValue > this.maxValue) {
            this.currentValue = this.maxValue;
            this.updateValue();
        }
        if (this.currentValue < this.minValue) {
            this.currentValue = this.minValue;
            this.updateValue();
        }
        this.textField.mouseClicked(mouseX, mouseY, 0);
    }
    
    @Override
    public void mouseRelease(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseRelease(mouseX, mouseY, mouseButton);
        if (this.fastTickerCounterValue != 0L) {
            this.fastTickerCounterValue = 0L;
            this.changeListener.accept(this.currentValue);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        final int preNumber = this.textField.getText().isEmpty() ? this.minValue : Integer.valueOf(this.textField.getText());
        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            final String currentText;
            String numericCheck = currentText = this.textField.getText();
            if (numericCheck.startsWith("-")) {
                numericCheck = numericCheck.replaceFirst("-", "");
            }
            final boolean numeric = currentText.isEmpty() || StringUtils.isNumeric(numericCheck);
            int newNumber = 0;
            try {
                newNumber = ((currentText.isEmpty() || !numeric) ? this.minValue : Integer.valueOf(currentText.isEmpty() ? String.valueOf(this.minValue) : currentText));
                if (!numeric) {
                    newNumber = preNumber;
                }
                if (newNumber > this.maxValue) {
                    newNumber = this.maxValue;
                }
                if (newNumber < this.minValue) {
                    newNumber = this.minValue;
                }
            }
            catch (final NumberFormatException e) {
                newNumber = this.maxValue;
            }
            final String newText = String.valueOf(newNumber);
            if (!currentText.equals(newText)) {
                this.textField.setText(String.valueOf(newNumber));
            }
            this.changeListener.accept(newNumber);
            this.currentValue = newNumber;
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.textField.updateCursorCounter();
    }
    
    public GuiTextField getTextField() {
        return this.textField;
    }
    
    public NumberElement addCallback(final Consumer<Integer> callback) {
        this.callback = callback;
        return this;
    }
    
    public Integer getCurrentValue() {
        return this.currentValue;
    }
}
