// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class GuiTextField extends Gui
{
    private final FontRenderer fontRenderer;
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    private boolean isFocused;
    private boolean isEnabled;
    private int field_73816_n;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor;
    private int disabledColor;
    private boolean visible;
    private boolean enableBackgroundDrawing;
    private boolean allowSection;
    
    public GuiTextField(final FontRenderer par1FontRenderer, final int x, final int y, final int w, final int h, final boolean allowSection) {
        this.text = "";
        this.maxStringLength = 32;
        this.isFocused = false;
        this.isEnabled = true;
        this.field_73816_n = 0;
        this.cursorPosition = 0;
        this.selectionEnd = 0;
        this.enabledColor = 14737632;
        this.disabledColor = 7368816;
        this.visible = true;
        this.enableBackgroundDrawing = true;
        this.fontRenderer = par1FontRenderer;
        this.xPos = x;
        this.yPos = y;
        this.width = w;
        this.height = h;
        this.allowSection = allowSection;
    }
    
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }
    
    public void setText(final String par1Str) {
        if (par1Str.length() > this.maxStringLength) {
            this.text = par1Str.substring(0, this.maxStringLength);
        }
        else {
            this.text = par1Str;
        }
        this.setCursorPositionEnd();
    }
    
    public String getText() {
        return this.text;
    }
    
    public String getSelectedtext() {
        final int var1 = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int var2 = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(var1, var2);
    }
    
    public void writeText(final String par1Str) {
        String var2 = "";
        final String var3 = CharacterFilter.filerAllowedCharacters(par1Str, this.allowSection);
        final int var4 = (this.cursorPosition < this.selectionEnd) ? this.cursorPosition : this.selectionEnd;
        final int var5 = (this.cursorPosition < this.selectionEnd) ? this.selectionEnd : this.cursorPosition;
        final int var6 = this.maxStringLength - this.text.length() - (var4 - this.selectionEnd);
        if (this.text.length() > 0) {
            var2 = String.valueOf(var2) + this.text.substring(0, var4);
        }
        int var7;
        if (var6 < var3.length()) {
            var2 = String.valueOf(var2) + var3.substring(0, var6);
            var7 = var6;
        }
        else {
            var2 = String.valueOf(var2) + var3;
            var7 = var3.length();
        }
        if (this.text.length() > 0 && var5 < this.text.length()) {
            var2 = String.valueOf(var2) + this.text.substring(var5);
        }
        this.text = var2;
        this.moveCursorBy(var4 - this.selectionEnd + var7);
    }
    
    public void deleteWords(final int par1) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                this.deleteFromCursor(this.getNthWordFromCursor(par1) - this.cursorPosition);
            }
        }
    }
    
    public void deleteFromCursor(final int par1) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            }
            else {
                final boolean var2 = par1 < 0;
                final int var3 = var2 ? (this.cursorPosition + par1) : this.cursorPosition;
                final int var4 = var2 ? this.cursorPosition : (this.cursorPosition + par1);
                String var5 = "";
                if (var3 >= 0) {
                    var5 = this.text.substring(0, var3);
                }
                if (var4 < this.text.length()) {
                    var5 = String.valueOf(var5) + this.text.substring(var4);
                }
                this.text = var5;
                if (var2) {
                    this.moveCursorBy(par1);
                }
            }
        }
    }
    
    public int getNthWordFromCursor(final int par1) {
        return this.getNthWordFromPos(par1, this.getCursorPosition());
    }
    
    public int getNthWordFromPos(final int par1, final int par2) {
        return this.func_73798_a(par1, this.getCursorPosition(), true);
    }
    
    public int func_73798_a(final int par1, final int par2, final boolean par3) {
        int var4 = par2;
        final boolean var5 = par1 < 0;
        for (int var6 = Math.abs(par1), var7 = 0; var7 < var6; ++var7) {
            if (var5) {
                while (par3 && var4 > 0) {
                    if (this.text.charAt(var4 - 1) != ' ') {
                        break;
                    }
                    --var4;
                }
                while (var4 > 0) {
                    if (this.text.charAt(var4 - 1) == ' ') {
                        break;
                    }
                    --var4;
                }
            }
            else {
                final int var8 = this.text.length();
                var4 = this.text.indexOf(32, var4);
                if (var4 == -1) {
                    var4 = var8;
                }
                else {
                    while (par3 && var4 < var8 && this.text.charAt(var4) == ' ') {
                        ++var4;
                    }
                }
            }
        }
        return var4;
    }
    
    public void moveCursorBy(final int par1) {
        this.setCursorPosition(this.selectionEnd + par1);
    }
    
    public void setCursorPosition(final int par1) {
        this.cursorPosition = par1;
        final int var2 = this.text.length();
        if (this.cursorPosition < 0) {
            this.cursorPosition = 0;
        }
        if (this.cursorPosition > var2) {
            this.cursorPosition = var2;
        }
        this.setSelectionPos(this.cursorPosition);
    }
    
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }
    
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }
    
    public boolean textboxKeyTyped(final char par1, final int par2) {
        if (!this.isEnabled || !this.isFocused) {
            return false;
        }
        switch (par1) {
            case '\u0001': {
                this.setCursorPositionEnd();
                this.setSelectionPos(0);
                return true;
            }
            case '\u0003': {
                GuiScreen.setClipboardString(this.getSelectedtext());
                return true;
            }
            case '\u0016': {
                this.writeText(GuiScreen.getClipboardString());
                return true;
            }
            case '\u0018': {
                GuiScreen.setClipboardString(this.getSelectedtext());
                this.writeText("");
                return true;
            }
            default: {
                switch (par2) {
                    case 14: {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.deleteWords(-1);
                        }
                        else {
                            this.deleteFromCursor(-1);
                        }
                        return true;
                    }
                    case 199: {
                        if (GuiScreen.isShiftKeyDown()) {
                            this.setSelectionPos(0);
                        }
                        else {
                            this.setCursorPositionZero();
                        }
                        return true;
                    }
                    case 203: {
                        if (GuiScreen.isShiftKeyDown()) {
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                            }
                            else {
                                this.setSelectionPos(this.getSelectionEnd() - 1);
                            }
                        }
                        else if (GuiScreen.isCtrlKeyDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(-1));
                        }
                        else {
                            this.moveCursorBy(-1);
                        }
                        return true;
                    }
                    case 205: {
                        if (GuiScreen.isShiftKeyDown()) {
                            if (GuiScreen.isCtrlKeyDown()) {
                                this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                            }
                            else {
                                this.setSelectionPos(this.getSelectionEnd() + 1);
                            }
                        }
                        else if (GuiScreen.isCtrlKeyDown()) {
                            this.setCursorPosition(this.getNthWordFromCursor(1));
                        }
                        else {
                            this.moveCursorBy(1);
                        }
                        return true;
                    }
                    case 207: {
                        if (GuiScreen.isShiftKeyDown()) {
                            this.setSelectionPos(this.text.length());
                        }
                        else {
                            this.setCursorPositionEnd();
                        }
                        return true;
                    }
                    case 211: {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.deleteWords(1);
                        }
                        else {
                            this.deleteFromCursor(1);
                        }
                        return true;
                    }
                    default: {
                        if (ChatAllowedCharacters.isAllowedCharacter(par1)) {
                            this.writeText(Character.toString(par1));
                            return true;
                        }
                        return false;
                    }
                }
                break;
            }
        }
    }
    
    public void mouseClicked(final int par1, final int par2, final int par3) {
        final String displayString = this.text.replace('§', '?');
        final boolean var4 = par1 >= this.xPos && par1 < this.xPos + this.width && par2 >= this.yPos && par2 < this.yPos + this.height;
        this.setFocused(this.isEnabled && var4);
        if (this.isFocused && par3 == 0) {
            int var5 = par1 - this.xPos;
            if (this.enableBackgroundDrawing) {
                var5 -= 4;
            }
            final String var6 = this.fontRenderer.trimStringToWidth(displayString.substring(this.field_73816_n), this.getWidth());
            this.setCursorPosition(this.fontRenderer.trimStringToWidth(var6, var5).length() + this.field_73816_n);
        }
    }
    
    public void drawTextBox() {
        final String textToDisplay = this.text.replace('§', '?');
        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                Gui.drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
                Gui.drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
            }
            final int var1 = this.isEnabled ? this.enabledColor : this.disabledColor;
            final int var2 = this.cursorPosition - this.field_73816_n;
            int var3 = this.selectionEnd - this.field_73816_n;
            final String var4 = this.fontRenderer.trimStringToWidth(textToDisplay.substring(this.field_73816_n), this.getWidth());
            final boolean var5 = var2 >= 0 && var2 <= var4.length();
            final boolean var6 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && var5;
            final int var7 = this.enableBackgroundDrawing ? (this.xPos + 4) : this.xPos;
            final int var8 = this.enableBackgroundDrawing ? (this.yPos + (this.height - 8) / 2) : this.yPos;
            int var9 = var7;
            if (var3 > var4.length()) {
                var3 = var4.length();
            }
            if (var4.length() > 0) {
                final String var10 = var5 ? var4.substring(0, var2) : var4;
                var9 = this.fontRenderer.drawStringWithShadow(var10, (float)var7, (float)var8, var1);
            }
            final boolean var11 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int var12 = var9;
            if (!var5) {
                var12 = ((var2 > 0) ? (var7 + this.width) : var7);
            }
            else if (var11) {
                var12 = var9 - 1;
                --var9;
            }
            if (var4.length() > 0 && var5 && var2 < var4.length()) {
                this.fontRenderer.drawStringWithShadow(var4.substring(var2), (float)var9, (float)var8, var1);
            }
            if (var6) {
                if (var11) {
                    Gui.drawRect(var12, var8 - 1, var12 + 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
                }
                else {
                    this.fontRenderer.drawStringWithShadow("_", (float)var12, (float)var8, var1);
                }
            }
            if (var3 != var2) {
                final int var13 = var7 + this.fontRenderer.getStringWidth(var4.substring(0, var3));
                this.drawCursorVertical(var12, var8 - 1, var13 - 1, var8 + 1 + this.fontRenderer.FONT_HEIGHT);
            }
        }
    }
    
    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        if (p_146188_1_ < p_146188_3_) {
            final int i = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i;
        }
        if (p_146188_2_ < p_146188_4_) {
            final int j = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = j;
        }
        if (p_146188_3_ > this.xPos + this.width) {
            p_146188_3_ = this.xPos + this.width;
        }
        if (p_146188_1_ > this.xPos + this.width) {
            p_146188_1_ = this.xPos + this.width;
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_146188_1_, p_146188_4_, 0.0).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_4_, 0.0).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_2_, 0.0).endVertex();
        worldrenderer.pos(p_146188_1_, p_146188_2_, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }
    
    public void setMaxStringLength(final int par1) {
        this.maxStringLength = par1;
        if (this.text.length() > par1) {
            this.text = this.text.substring(0, par1);
        }
    }
    
    public int getMaxStringLength() {
        return this.maxStringLength;
    }
    
    public int getCursorPosition() {
        return this.cursorPosition;
    }
    
    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }
    
    public void setEnableBackgroundDrawing(final boolean par1) {
        this.enableBackgroundDrawing = par1;
    }
    
    public void setTextColor(final int par1) {
        this.enabledColor = par1;
    }
    
    public void func_82266_h(final int par1) {
        this.disabledColor = par1;
    }
    
    public void setFocused(final boolean par1) {
        if (par1 && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = par1;
    }
    
    public boolean isFocused() {
        return this.isFocused;
    }
    
    public void func_82265_c(final boolean par1) {
        this.isEnabled = par1;
    }
    
    public int getSelectionEnd() {
        return this.selectionEnd;
    }
    
    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? (this.width - 8) : this.width;
    }
    
    public void setSelectionPos(int par1) {
        final String displayString = this.text.replace('§', '?');
        final int var2 = displayString.length();
        if (par1 > var2) {
            par1 = var2;
        }
        if (par1 < 0) {
            par1 = 0;
        }
        this.selectionEnd = par1;
        if (this.fontRenderer != null) {
            if (this.field_73816_n > var2) {
                this.field_73816_n = var2;
            }
            final int var3 = this.getWidth();
            final String var4 = this.fontRenderer.trimStringToWidth(displayString.substring(this.field_73816_n), var3);
            final int var5 = var4.length() + this.field_73816_n;
            if (par1 == this.field_73816_n) {
                this.field_73816_n -= this.fontRenderer.trimStringToWidth(displayString, var3, true).length();
            }
            if (par1 > var5) {
                this.field_73816_n += par1 - var5;
            }
            else if (par1 <= this.field_73816_n) {
                this.field_73816_n -= this.field_73816_n - par1;
            }
            if (this.field_73816_n < 0) {
                this.field_73816_n = 0;
            }
            if (this.field_73816_n > var2) {
                this.field_73816_n = var2;
            }
        }
    }
    
    public boolean getVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean par1) {
        this.visible = par1;
    }
}
