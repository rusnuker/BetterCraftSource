// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.labymod.main.ModSettings;
import net.labymod.utils.Consumer;
import net.labymod.gui.elements.DropDownMenu;

public class DropDownElement<T> extends ControlElement
{
    private DropDownMenu dropDownMenu;
    private Consumer<T> changeListener;
    private Consumer<T> changeCallback;
    
    public DropDownElement(final String diplayName, final String configEntryName, final DropDownMenu dropDownMenu, final IconData iconData, final DrowpDownLoadValue<T> loadValue) {
        super(diplayName, configEntryName, iconData);
        this.dropDownMenu = dropDownMenu;
        if (!configEntryName.isEmpty()) {
            try {
                this.dropDownMenu.setSelected(loadValue.load(String.valueOf(ModSettings.class.getDeclaredField(configEntryName).get(LabyMod.getSettings()))));
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        this.changeListener = new Consumer<T>() {
            @Override
            public void accept(final T accepted) {
                try {
                    ModSettings.class.getDeclaredField(configEntryName).set(LabyMod.getSettings(), String.valueOf(accepted));
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                if (DropDownElement.this.changeCallback != null) {
                    DropDownElement.this.changeCallback.accept(accepted);
                }
            }
        };
    }
    
    public DropDownElement(final String diplayName, final DropDownMenu dropDownMenu) {
        super(diplayName, null, (IconData)null);
        this.dropDownMenu = dropDownMenu;
    }
    
    public DropDownElement(final String configEntryName, final DropDownMenu dropDownMenu, final IconData iconData, final DrowpDownLoadValue<T> loadValue) {
        this(configEntryName, configEntryName, dropDownMenu, iconData, loadValue);
    }
    
    @Override
    public void init() {
        if (this.dropDownMenu != null) {
            this.dropDownMenu.setOpen(false);
        }
    }
    
    @Override
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        this.mouseOver = this.dropDownMenu.isMouseOver(mouseX, mouseY);
        if (this.iconData == null) {
            this.dropDownMenu.setX(x);
            this.dropDownMenu.setY(y + 15);
            this.dropDownMenu.setWidth(maxX - x - 2);
            this.dropDownMenu.setHeight(maxY - y - 15 - 3);
            this.dropDownMenu.draw(mouseX, mouseY);
        }
        else {
            super.draw(x, y, maxX, maxY, mouseX, mouseY);
            LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
            final int width = 100;
            this.dropDownMenu.setX(maxX - 100 - 5);
            this.dropDownMenu.setY(y + 3);
            this.dropDownMenu.setWidth(100);
            this.dropDownMenu.setHeight(maxY - y - 6);
            this.dropDownMenu.draw(mouseX, mouseY);
        }
    }
    
    @Override
    public int getEntryHeight() {
        return (this.iconData == null) ? 35 : 23;
    }
    
    public boolean onClickDropDown(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.dropDownMenu.onClick(mouseX, mouseY, mouseButton)) {
            if (this.changeListener != null) {
                this.changeListener.accept(this.dropDownMenu.getSelected());
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void mouseRelease(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseRelease(mouseX, mouseY, mouseButton);
        this.dropDownMenu.onRelease(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void mouseClickMove(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClickMove(mouseX, mouseY, mouseButton);
        this.dropDownMenu.onDrag(mouseX, mouseY, mouseButton);
    }
    
    public void onScrollDropDown() {
        this.dropDownMenu.onScroll();
    }
    
    public DropDownElement setCallback(final Consumer<T> consumer) {
        this.changeCallback = consumer;
        return this;
    }
    
    public DropDownMenu getDropDownMenu() {
        return this.dropDownMenu;
    }
    
    public void setChangeListener(final Consumer<T> changeListener) {
        this.changeListener = changeListener;
    }
    
    public interface DrowpDownLoadValue<T>
    {
        T load(final String p0);
    }
}
