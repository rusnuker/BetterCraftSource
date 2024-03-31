// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import java.util.Iterator;
import java.util.ArrayList;
import net.labymod.gui.elements.CheckBox;
import net.labymod.gui.elements.ColorPicker;
import java.util.List;

public class ColorPickerCheckBoxBulkElement extends ControlElement
{
    private List<ColorPicker> colorPickers;
    private List<CheckBox> checkBoxes;
    private boolean checkBoxRightBound;
    
    public ColorPickerCheckBoxBulkElement(final String displayName) {
        super(displayName, "", (IconData)null);
        this.colorPickers = new ArrayList<ColorPicker>();
        this.checkBoxes = new ArrayList<CheckBox>();
        this.checkBoxRightBound = false;
    }
    
    public void addColorPicker(final ColorPicker colorPicker) {
        this.colorPickers.add(colorPicker);
    }
    
    public void addCheckbox(final CheckBox checkBox) {
        this.checkBoxes.add(checkBox);
    }
    
    @Override
    public void init() {
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        int bulkList = 0;
        for (final ColorPicker colorPicker : this.colorPickers) {
            colorPicker.setX(x + bulkList);
            colorPicker.setY(y + 7);
            colorPicker.setWidth(20);
            colorPicker.setHeight(20);
            colorPicker.drawColorPicker(mouseX, mouseY);
            bulkList += 25;
        }
        if (this.checkBoxRightBound) {
            bulkList = maxX;
            for (final CheckBox checkBox : this.checkBoxes) {
                checkBox.setX(bulkList - 20);
                checkBox.setY(y + 7);
                checkBox.setWidth(20);
                checkBox.setHeight(20);
                checkBox.drawCheckbox(mouseX, mouseY);
                bulkList -= 25;
            }
        }
        else {
            for (final CheckBox checkBox : this.checkBoxes) {
                checkBox.setX(x + bulkList);
                checkBox.setY(y + 7);
                checkBox.setWidth(20);
                checkBox.setHeight(20);
                checkBox.drawCheckbox(mouseX, mouseY);
                bulkList += 25;
            }
        }
    }
    
    @Override
    public int getEntryHeight() {
        return 30;
    }
    
    @Override
    public void drawDescription(final int x, final int y, final int screenWidth) {
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    public boolean onClickBulkEntry(final int mouseX, final int mouseY, final int mouseButton) {
        for (final ColorPicker colorPicker : this.colorPickers) {
            if (!colorPicker.isMouseOver(mouseX, mouseY) && !colorPicker.isHoverAdvancedButton() && !colorPicker.isHoverSlider()) {
                colorPicker.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        for (final ColorPicker colorPicker : this.colorPickers) {
            if (colorPicker.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        for (final CheckBox checkBox : this.checkBoxes) {
            if (checkBox.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return false;
    }
    
    public void setCheckBoxRightBound(final boolean checkBoxRightBound) {
        this.checkBoxRightBound = checkBoxRightBound;
    }
}
