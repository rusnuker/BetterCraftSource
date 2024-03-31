// 
// Decompiled by Procyon v0.6.0
// 

package wdl.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

class GuiNumericTextField extends GuiTextField
{
    private String lastSafeText;
    
    public GuiNumericTextField(final int id, final FontRenderer fontRenderer, final int x, final int y, final int width, final int height) {
        super(id, fontRenderer, x, y, width, height);
        this.setText(this.lastSafeText = "0");
    }
    
    @Override
    public void drawTextBox() {
        try {
            Integer.parseInt("0" + this.getText());
            this.lastSafeText = this.getText();
        }
        catch (final NumberFormatException e) {
            this.setText(this.lastSafeText);
        }
        super.drawTextBox();
    }
    
    public int getValue() {
        try {
            return Integer.parseInt("0" + this.getText());
        }
        catch (final NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public void setValue(final int value) {
        final String text = String.valueOf(value);
        this.setText(this.lastSafeText = text);
    }
    
    @Override
    public String getText() {
        final String text = super.getText();
        try {
            final int value = Integer.parseInt("0" + text);
            return String.valueOf(value);
        }
        catch (final NumberFormatException e) {
            this.setText(this.lastSafeText);
            return this.lastSafeText;
        }
    }
    
    @Override
    public void setText(final String text) {
        String value;
        try {
            value = String.valueOf(Integer.parseInt("0" + text));
        }
        catch (final NumberFormatException e) {
            value = this.lastSafeText;
        }
        super.setText(value);
        this.lastSafeText = value;
    }
}
