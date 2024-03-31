// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import com.mcf.davidee.nbtedit.NBTStringHelper;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;
import com.mcf.davidee.nbtedit.nbt.ParseHelper;
import net.minecraft.nbt.NBTTagByte;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTBase;
import com.mcf.davidee.nbtedit.nbt.NamedNBT;
import com.mcf.davidee.nbtedit.nbt.Node;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class GuiEditNBT extends Gui
{
    public static final ResourceLocation WINDOW_TEXTURE;
    public static final int WIDTH = 178;
    public static final int HEIGHT = 93;
    private Minecraft mc;
    private Node<NamedNBT> node;
    private NBTBase nbt;
    private boolean canEditText;
    private boolean canEditValue;
    private GuiNBTTree parent;
    private int x;
    private int y;
    private GuiTextField key;
    private GuiTextField value;
    private GuiButton save;
    private GuiButton cancel;
    private String kError;
    private String vError;
    private GuiCharacterButton newLine;
    private GuiCharacterButton section;
    
    static {
        WINDOW_TEXTURE = new ResourceLocation("nbtedit", "textures/gui/window.png");
    }
    
    public GuiEditNBT(final GuiNBTTree parent, final Node<NamedNBT> node, final boolean editText, final boolean editValue) {
        this.mc = Minecraft.getMinecraft();
        this.parent = parent;
        this.node = node;
        this.nbt = node.getObject().getNBT();
        this.canEditText = editText;
        this.canEditValue = editValue;
    }
    
    public void initGUI(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.section = new GuiCharacterButton((byte)0, x + 178 - 1, y + 34);
        this.newLine = new GuiCharacterButton((byte)1, x + 178 - 1, y + 50);
        final String sKey = (this.key == null) ? this.node.getObject().getName() : this.key.getText();
        final String sValue = (this.value == null) ? getValue(this.nbt) : this.value.getText();
        this.key = new GuiTextField(this.mc.fontRendererObj, x + 46, y + 18, 116, 15, false);
        this.value = new GuiTextField(this.mc.fontRendererObj, x + 46, y + 44, 116, 15, true);
        this.key.setText(sKey);
        this.key.setEnableBackgroundDrawing(false);
        this.key.func_82265_c(this.canEditText);
        this.value.setMaxStringLength(256);
        this.value.setText(sValue);
        this.value.setEnableBackgroundDrawing(false);
        this.value.func_82265_c(this.canEditValue);
        this.save = new GuiButton(1, x + 9, y + 62, 75, 20, "Save");
        if (!this.key.isFocused() && !this.value.isFocused()) {
            if (this.canEditText) {
                this.key.setFocused(true);
            }
            else if (this.canEditValue) {
                this.value.setFocused(true);
            }
        }
        this.section.setEnabled(this.value.isFocused());
        this.newLine.setEnabled(this.value.isFocused());
        this.cancel = new GuiButton(0, x + 93, y + 62, 75, 20, "Cancel");
    }
    
    public void click(final int mx, final int my) {
        if (this.newLine.inBounds(mx, my) && this.value.isFocused()) {
            this.value.writeText("\n");
            this.checkValidInput();
        }
        else if (this.section.inBounds(mx, my) && this.value.isFocused()) {
            this.value.writeText("§");
            this.checkValidInput();
        }
        else {
            this.key.mouseClicked(mx, my, 0);
            this.value.mouseClicked(mx, my, 0);
            if (this.save.mousePressed(this.mc, mx, my)) {
                this.saveAndQuit();
            }
            if (this.cancel.mousePressed(this.mc, mx, my)) {
                this.parent.closeWindow();
            }
            this.section.setEnabled(this.value.isFocused());
            this.newLine.setEnabled(this.value.isFocused());
        }
    }
    
    private void saveAndQuit() {
        this.mc.player.getHeldItemMainhand().setTagCompound(this.parent.getNBTTree().toNBTTagCompound());
        if (this.canEditText) {
            this.node.getObject().setName(this.key.getText());
        }
        setValidValue(this.node, this.value.getText());
        this.parent.nodeEdited(this.node);
        this.parent.closeWindow();
    }
    
    public void draw(final int mx, final int my) {
        this.mc.renderEngine.bindTexture(GuiEditNBT.WINDOW_TEXTURE);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(this.x, this.y, 0, 0, 178, 93);
        if (!this.canEditText) {
            Gui.drawRect(this.x + 42, this.y + 15, this.x + 169, this.y + 31, Integer.MIN_VALUE);
        }
        if (!this.canEditValue) {
            Gui.drawRect(this.x + 42, this.y + 41, this.x + 169, this.y + 57, Integer.MIN_VALUE);
        }
        this.key.drawTextBox();
        this.value.drawTextBox();
        this.save.drawButton(this.mc, mx, my, 0.0f);
        this.cancel.drawButton(this.mc, mx, my, 0.0f);
        if (this.kError != null) {
            this.drawCenteredStringg(this.mc.fontRendererObj, this.kError, this.x + 89, this.y + 4, 16711680);
        }
        if (this.vError != null) {
            this.drawCenteredStringg(this.mc.fontRendererObj, this.vError, this.x + 89, this.y + 32, 16711680);
        }
        this.newLine.draw(mx, my);
        this.section.draw(mx, my);
    }
    
    public void drawCenteredStringg(final FontRenderer par1FontRenderer, final String par2Str, final int par3, final int par4, final int par5) {
        par1FontRenderer.drawString(par2Str, par3 - par1FontRenderer.getStringWidth(par2Str) / 2, par4, par5);
    }
    
    public void update() {
        this.value.updateCursorCounter();
        this.key.updateCursorCounter();
    }
    
    public void keyTyped(final char c, final int i) {
        if (i == 1) {
            this.parent.closeWindow();
        }
        else if (i == 15) {
            if (this.key.isFocused() && this.canEditValue) {
                this.key.setFocused(false);
                this.value.setFocused(true);
            }
            else if (this.value.isFocused() && this.canEditText) {
                this.key.setFocused(true);
                this.value.setFocused(false);
            }
            this.section.setEnabled(this.value.isFocused());
            this.newLine.setEnabled(this.value.isFocused());
        }
        else if (i == 28) {
            this.checkValidInput();
            if (this.save.enabled) {
                this.saveAndQuit();
            }
        }
        else {
            this.key.textboxKeyTyped(c, i);
            this.value.textboxKeyTyped(c, i);
            this.checkValidInput();
        }
    }
    
    private void checkValidInput() {
        boolean valid = true;
        this.kError = null;
        this.vError = null;
        if (this.canEditText && !this.validName()) {
            valid = false;
            this.kError = "Duplicate Tag Name";
        }
        try {
            validValue(this.value.getText(), this.nbt.getId());
            valid &= true;
        }
        catch (final NumberFormatException e) {
            this.vError = e.getMessage();
            valid = false;
        }
        this.save.enabled = valid;
    }
    
    private boolean validName() {
        for (final Node<NamedNBT> node : this.node.getParent().getChildren()) {
            final NBTBase base = node.getObject().getNBT();
            if (base != this.nbt && node.getObject().getName().equals(this.key.getText())) {
                return false;
            }
        }
        return true;
    }
    
    private static void setValidValue(final Node<NamedNBT> node, final String value) {
        final NamedNBT named = node.getObject();
        final NBTBase base = named.getNBT();
        if (base instanceof NBTTagByte) {
            named.setNBT(new NBTTagByte(ParseHelper.parseByte(value)));
        }
        if (base instanceof NBTTagShort) {
            named.setNBT(new NBTTagShort(ParseHelper.parseShort(value)));
        }
        if (base instanceof NBTTagInt) {
            named.setNBT(new NBTTagInt(ParseHelper.parseInt(value)));
        }
        if (base instanceof NBTTagLong) {
            named.setNBT(new NBTTagLong(ParseHelper.parseLong(value)));
        }
        if (base instanceof NBTTagFloat) {
            named.setNBT(new NBTTagFloat(ParseHelper.parseFloat(value)));
        }
        if (base instanceof NBTTagDouble) {
            named.setNBT(new NBTTagDouble(ParseHelper.parseDouble(value)));
        }
        if (base instanceof NBTTagByteArray) {
            named.setNBT(new NBTTagByteArray(ParseHelper.parseByteArray(value)));
        }
        if (base instanceof NBTTagIntArray) {
            named.setNBT(new NBTTagIntArray(ParseHelper.parseIntArray(value)));
        }
        if (base instanceof NBTTagString) {
            named.setNBT(new NBTTagString(value));
        }
    }
    
    private static void validValue(final String value, final byte type) throws NumberFormatException {
        switch (type) {
            case 1: {
                ParseHelper.parseByte(value);
                break;
            }
            case 2: {
                ParseHelper.parseShort(value);
                break;
            }
            case 3: {
                ParseHelper.parseInt(value);
                break;
            }
            case 4: {
                ParseHelper.parseLong(value);
                break;
            }
            case 5: {
                ParseHelper.parseFloat(value);
                break;
            }
            case 6: {
                ParseHelper.parseDouble(value);
                break;
            }
            case 7: {
                ParseHelper.parseByteArray(value);
                break;
            }
            case 11: {
                ParseHelper.parseIntArray(value);
                break;
            }
        }
    }
    
    private static String getValue(final NBTBase base) {
        switch (base.getId()) {
            case 7: {
                String s = "";
                byte[] byteArray;
                for (int length = (byteArray = ((NBTTagByteArray)base).getByteArray()).length, j = 0; j < length; ++j) {
                    final byte b = byteArray[j];
                    s = String.valueOf(s) + b + " ";
                }
                return s;
            }
            case 9: {
                return "TagList";
            }
            case 10: {
                return "TagCompound";
            }
            case 11: {
                String i = "";
                int[] intArray;
                for (int length2 = (intArray = ((NBTTagIntArray)base).getIntArray()).length, k = 0; k < length2; ++k) {
                    final int a = intArray[k];
                    i = String.valueOf(i) + a + " ";
                }
                return i;
            }
            default: {
                return NBTStringHelper.toString(base);
            }
        }
    }
}
