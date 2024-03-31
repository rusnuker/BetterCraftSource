// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.elements;

import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;

public class SmallDropDownMenu
{
    protected static final ResourceLocation buttonTextures;
    private int color;
    private int x;
    private int y;
    private int widthIn;
    private int heightIn;
    private String selectedOption;
    private boolean open;
    private int dropDownX;
    private int dropDownY;
    private int maxX;
    private int maxY;
    private boolean minecraftStyle;
    private boolean changeable;
    private String renderCustomSelected;
    private ArrayList<String> dropDownEntrys;
    
    static {
        buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    }
    
    public SmallDropDownMenu(final int x, final int y) {
        this(x, y, x + 200, y + 20);
    }
    
    public SmallDropDownMenu(final int x, final int y, final int widthIn, final int heightIn) {
        this.selectedOption = null;
        this.open = false;
        this.dropDownX = 0;
        this.dropDownY = 0;
        this.maxX = 0;
        this.maxY = 0;
        this.minecraftStyle = true;
        this.changeable = true;
        this.renderCustomSelected = null;
        this.dropDownEntrys = new ArrayList<String>();
        this.x = x;
        this.y = y;
        this.widthIn = widthIn;
        this.heightIn = heightIn;
        this.color = ModColor.toRGB(100, 100, 100, 100);
    }
    
    public void setColor(final int r, final int g, final int b, final int a) {
        this.color = ModColor.toRGB(r, g, b, a);
    }
    
    private int getPositionRight() {
        return this.x + this.widthIn;
    }
    
    private int getPositionBottom() {
        return this.y + this.heightIn;
    }
    
    public void setPosRight(final int right) {
        this.widthIn = right - this.x;
    }
    
    public void setPosBottom(final int bottom) {
        this.heightIn = bottom - this.y;
    }
    
    public void setPosition(final int left, final int top, final int right, final int bottom) {
        this.setX(left);
        this.setY(top);
        this.setPosRight(right);
        this.setPosBottom(bottom);
    }
    
    private void updateMax() {
        this.maxX = 0;
        for (final String entry : this.dropDownEntrys) {
            final int x = LabyMod.getInstance().getDrawUtils().getStringWidth(entry);
            if (x > this.maxX) {
                this.maxX = x;
            }
        }
        this.maxY = this.dropDownEntrys.size() * 10;
    }
    
    public void addDropDownEntry(final String name) {
        this.dropDownEntrys.add(name);
        this.updateMax();
        if (this.selectedOption == null) {
            this.selectedOption = this.dropDownEntrys.get(0);
        }
    }
    
    public void clearDropDownEntry() {
        this.dropDownEntrys.clear();
    }
    
    public String getSelectedOption() {
        return this.selectedOption;
    }
    
    public void setSelectedOption(final String selectedOption) {
        this.selectedOption = selectedOption;
    }
    
    public void setSelectedOptionIndex(final int index) {
        int i = 0;
        for (final String entry : this.dropDownEntrys) {
            if (i == index) {
                this.selectedOption = entry;
                break;
            }
            ++i;
        }
    }
    
    public String getRenderCustomSelected() {
        return this.renderCustomSelected;
    }
    
    public void setRenderCustomSelected(final String renderCustomSelected) {
        this.renderCustomSelected = renderCustomSelected;
    }
    
    public void renderButton(final int mouseX, final int mouseY) {
        if (this.minecraftStyle) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(SmallDropDownMenu.buttonTextures);
            final int hovered = (this.open || this.isHovered(mouseX, mouseY)) ? 20 : 0;
            LabyMod.getInstance().getDrawUtils().drawTexturedModalRect(this.x, this.y, 0, 66 + hovered, this.widthIn / 2, this.heightIn);
            LabyMod.getInstance().getDrawUtils().drawTexturedModalRect(this.x + this.widthIn / 2, this.y, 200 - this.widthIn / 2, 66 + hovered, this.widthIn / 2, this.heightIn);
        }
        else {
            LabyMod.getInstance().getDrawUtils().drawRectangle(this.x, this.y, this.getPositionRight(), this.getPositionBottom(), this.color);
            if (this.open || this.isHovered(mouseX, mouseY)) {
                LabyMod.getInstance().getDrawUtils().drawRectangle(this.x - 1, this.y - 1, this.getPositionRight() + 1, this.getPositionBottom() + 1, this.color * -1);
            }
        }
        String text = (this.renderCustomSelected == null) ? this.selectedOption : this.renderCustomSelected;
        if (text != null) {
            text = LabyMod.getInstance().getDrawUtils().trimStringToWidth(text, this.widthIn + 4);
        }
        LabyMod.getInstance().getDrawUtils().drawCenteredString(text, (this.getPositionRight() - this.x) / 2 + this.x, (this.getPositionBottom() - this.y) / 2 + this.y - 4 + 1, 0.75);
        if (this.open && this.dropDownEntrys.size() != 0) {
            int dropDownX = this.dropDownX;
            if (dropDownX + this.maxX > LabyMod.getInstance().getDrawUtils().getWidth() - 10) {
                dropDownX -= this.maxX;
            }
            LabyMod.getInstance().getDrawUtils().drawRectangle(dropDownX - 1, this.dropDownY - 1, dropDownX + this.maxX + 2 + 1, this.dropDownY + this.maxY + 2, ModColor.toRGB(210, 210, 210, 250));
            LabyMod.getInstance().getDrawUtils().drawRectangle(dropDownX, this.dropDownY, dropDownX + this.maxX + 2, this.dropDownY + this.maxY + 1, ModColor.toRGB(10, 10, 10, 250));
            int listY = 0;
            for (final String entry : this.dropDownEntrys) {
                if (mouseX > dropDownX - 1 && mouseX < dropDownX + this.maxX + 2 + 1 && mouseY > this.dropDownY + listY && mouseY < this.dropDownY + listY + 11) {
                    LabyMod.getInstance().getDrawUtils().drawRectangle(dropDownX, this.dropDownY + listY, dropDownX + this.maxX + 2, this.dropDownY + listY + 11, ModColor.toRGB(140, 140, 140, 85));
                }
                LabyMod.getInstance().getDrawUtils().drawString(entry, dropDownX + 4, this.dropDownY + 3 + listY, 0.7);
                listY += 10;
            }
        }
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return mouseX > this.x && mouseX < this.getPositionRight() && mouseY > this.y && mouseY < this.getPositionBottom();
    }
    
    public int onClick(final int mouseX, final int mouseY) {
        if (this.open) {
            int dropDownX = this.dropDownX;
            if (dropDownX + this.maxX > LabyMod.getInstance().getDrawUtils().getWidth() - 10) {
                dropDownX -= this.maxX;
            }
            int index = 0;
            int listY = 0;
            for (final String entry : this.dropDownEntrys) {
                if (mouseX > dropDownX - 1 && mouseX < dropDownX + this.maxX + 2 + 1 && mouseY > this.dropDownY + listY && mouseY < this.dropDownY + listY + 11) {
                    if (this.changeable) {
                        this.selectedOption = entry;
                    }
                    this.open = false;
                    return index;
                }
                listY += 10;
                ++index;
            }
            this.open = false;
            return -3;
        }
        if (this.isHovered(mouseX, mouseY)) {
            this.open = !this.open;
            this.dropDownX = mouseX;
            this.dropDownY = mouseY;
            return -2;
        }
        this.open = false;
        return -1;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidthIn() {
        return this.widthIn;
    }
    
    public int getHeightIn() {
        return this.heightIn;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public int getDropDownX() {
        return this.dropDownX;
    }
    
    public int getDropDownY() {
        return this.dropDownY;
    }
    
    public int getMaxX() {
        return this.maxX;
    }
    
    public int getMaxY() {
        return this.maxY;
    }
    
    public boolean isMinecraftStyle() {
        return this.minecraftStyle;
    }
    
    public boolean isChangeable() {
        return this.changeable;
    }
    
    public ArrayList<String> getDropDownEntrys() {
        return this.dropDownEntrys;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setWidthIn(final int widthIn) {
        this.widthIn = widthIn;
    }
    
    public void setHeightIn(final int heightIn) {
        this.heightIn = heightIn;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public void setDropDownX(final int dropDownX) {
        this.dropDownX = dropDownX;
    }
    
    public void setDropDownY(final int dropDownY) {
        this.dropDownY = dropDownY;
    }
    
    public void setMaxX(final int maxX) {
        this.maxX = maxX;
    }
    
    public void setMaxY(final int maxY) {
        this.maxY = maxY;
    }
    
    public void setMinecraftStyle(final boolean minecraftStyle) {
        this.minecraftStyle = minecraftStyle;
    }
    
    public void setChangeable(final boolean changeable) {
        this.changeable = changeable;
    }
    
    public void setDropDownEntrys(final ArrayList<String> dropDownEntrys) {
        this.dropDownEntrys = dropDownEntrys;
    }
}
