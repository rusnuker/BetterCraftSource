// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.elements;

import java.util.Iterator;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.core.LabyModCore;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import java.util.ArrayList;
import net.labymod.utils.Consumer;
import java.util.List;
import net.minecraft.client.gui.Gui;

public class CheckBox extends Gui
{
    private String title;
    private int x;
    private int y;
    private int width;
    private int height;
    private EnumCheckBoxValue currentValue;
    private DefaultCheckBoxValueCallback defaultValue;
    private boolean hasDefault;
    private List<CheckBox> childCheckBoxes;
    private CheckBox parentCheckBox;
    private Consumer<EnumCheckBoxValue> updateListener;
    private boolean visible;
    private String description;
    
    public CheckBox(final String title, final EnumCheckBoxValue currentValue, final DefaultCheckBoxValueCallback defaultValue, final int x, final int y, final int width, final int height) {
        this.hasDefault = false;
        this.childCheckBoxes = new ArrayList<CheckBox>();
        this.visible = true;
        this.description = null;
        this.title = title;
        this.currentValue = currentValue;
        this.defaultValue = defaultValue;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void drawCheckbox(final int mouseX, final int mouseY) {
        if (!this.visible) {
            return;
        }
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawCenteredString(this.title, this.x + this.width / 2, this.y - 5, 0.5);
        final boolean hover = this.isMouseOver(mouseX, mouseY);
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, ModColor.toRGB(0, 0, 0, 255));
        Gui.drawRect(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, ModColor.toRGB(170, 170, 170, 255));
        Gui.drawRect(this.x + 2, this.y + 2, this.x + this.width - 2, this.y + this.height - 2, ModColor.toRGB(hover ? 100 : 120, hover ? 100 : 120, hover ? 100 : 120, 255));
        final EnumCheckBoxValue value = this.currentValue;
        if (value == EnumCheckBoxValue.ENABLED) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_CHECKBOX);
            draw.drawTexture(this.x + 1, this.y - 1, 256.0, 256.0, this.width, this.height);
        }
        else if (value == EnumCheckBoxValue.INDETERMINATE) {
            Gui.drawRect(this.x + this.width / 3, this.y + this.height / 3, this.x + this.width - this.width / 3 + 1, this.y + this.height - this.height / 3 + 1, ModColor.toRGB(0, 100, 0, 255));
            Gui.drawRect(this.x + this.width / 3 - 1, this.y + this.height / 3 - 1, this.x + this.width - this.width / 3, this.y + this.height - this.height / 3, ModColor.toRGB(0, 150, 0, 255));
        }
        if (this.hasDefault && this.currentValue == EnumCheckBoxValue.DEFAULT) {
            if (this.getValue() == EnumCheckBoxValue.ENABLED) {
                Gui.drawRect(this.x + 2, this.y + 2, this.x + this.width - 2, this.y + this.height - 2, ModColor.toRGB(0, 150, 0, 155));
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_HOVER_DEFAULT);
            LabyMod.getInstance().getDrawUtils().drawTexture(this.x + 2, this.y + 2, 256.0, 256.0, this.width - 4, this.height - 4);
            if (this.isMouseOver(mouseX, mouseY)) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 200L, "Default");
            }
        }
        if (this.description != null && this.isMouseOver(mouseX, mouseY)) {
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 200L, this.description);
        }
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.visible) {
            return false;
        }
        if (this.isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            LabyModCore.getMinecraft().playSound(SettingsElement.BUTTON_PRESS_SOUND, this.currentValue.getPitch());
            if (this.currentValue == EnumCheckBoxValue.DISABLED) {
                this.updateValue(EnumCheckBoxValue.ENABLED);
                for (final CheckBox subCheckBox : this.childCheckBoxes) {
                    subCheckBox.updateValue(EnumCheckBoxValue.ENABLED);
                }
            }
            else if (this.currentValue == EnumCheckBoxValue.ENABLED) {
                final EnumCheckBoxValue value = this.hasDefault ? EnumCheckBoxValue.DEFAULT : EnumCheckBoxValue.DISABLED;
                this.updateValue(value);
                for (final CheckBox subCheckBox2 : this.childCheckBoxes) {
                    subCheckBox2.updateValue(value);
                }
            }
            else if (this.currentValue == EnumCheckBoxValue.INDETERMINATE) {
                this.updateValue(EnumCheckBoxValue.DISABLED);
                for (final CheckBox subCheckBox : this.childCheckBoxes) {
                    subCheckBox.updateValue(EnumCheckBoxValue.DISABLED);
                }
            }
            else if (this.currentValue == EnumCheckBoxValue.DEFAULT) {
                this.updateValue(EnumCheckBoxValue.DISABLED);
                for (final CheckBox subCheckBox : this.childCheckBoxes) {
                    subCheckBox.updateValue(EnumCheckBoxValue.DISABLED);
                }
            }
            return true;
        }
        return false;
    }
    
    public void updateValue(final EnumCheckBoxValue value) {
        this.currentValue = value;
        if (this.updateListener != null) {
            this.updateListener.accept(value);
        }
        if (this.parentCheckBox != null) {
            this.parentCheckBox.notfiyChildChange(this, value);
        }
    }
    
    private void notfiyChildChange(final CheckBox childCheckBox, final EnumCheckBoxValue value) {
        boolean allEnabled = true;
        boolean allDisable = true;
        boolean allInderterminate = true;
        for (final CheckBox subCheckBox : this.childCheckBoxes) {
            if (subCheckBox.getValue() != EnumCheckBoxValue.ENABLED) {
                allEnabled = false;
            }
            if (subCheckBox.getValue() != EnumCheckBoxValue.DISABLED) {
                allDisable = false;
            }
            if (subCheckBox.getValue() != EnumCheckBoxValue.INDETERMINATE) {
                allInderterminate = false;
            }
        }
        if (allEnabled && !allInderterminate) {
            this.updateValue(EnumCheckBoxValue.ENABLED);
        }
        else if (allDisable && !allInderterminate) {
            this.updateValue(EnumCheckBoxValue.DISABLED);
        }
        else {
            this.updateValue(EnumCheckBoxValue.INDETERMINATE);
        }
    }
    
    public EnumCheckBoxValue getValue() {
        return (this.hasDefault && this.currentValue == EnumCheckBoxValue.DEFAULT) ? this.defaultValue.getDefaultValue() : this.currentValue;
    }
    
    public boolean isMouseOver(final int mouseX, final int mouseY) {
        return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setCurrentValue(final EnumCheckBoxValue currentValue) {
        this.currentValue = currentValue;
    }
    
    public void setDefaultValue(final DefaultCheckBoxValueCallback defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public boolean isHasDefault() {
        return this.hasDefault;
    }
    
    public void setHasDefault(final boolean hasDefault) {
        this.hasDefault = hasDefault;
    }
    
    public List<CheckBox> getChildCheckBoxes() {
        return this.childCheckBoxes;
    }
    
    public CheckBox getParentCheckBox() {
        return this.parentCheckBox;
    }
    
    public void setParentCheckBox(final CheckBox parentCheckBox) {
        this.parentCheckBox = parentCheckBox;
    }
    
    public void setUpdateListener(final Consumer<EnumCheckBoxValue> updateListener) {
        this.updateListener = updateListener;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public enum EnumCheckBoxValue
    {
        DEFAULT("DEFAULT", 0, 1.0f), 
        ENABLED("ENABLED", 1, 1.4f), 
        DISABLED("DISABLED", 2, 1.5f), 
        INDETERMINATE("INDETERMINATE", 3, 1.3f);
        
        private float pitch;
        
        private EnumCheckBoxValue(final String s, final int n, final float pitch) {
            this.pitch = pitch;
        }
        
        public float getPitch() {
            return this.pitch;
        }
    }
    
    public interface DefaultCheckBoxValueCallback
    {
        EnumCheckBoxValue getDefaultValue();
    }
}
