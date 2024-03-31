// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.resources.I18n;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.Minecraft;

public class GuiKeyBindingList extends GuiListExtended
{
    private final GuiControls controlsScreen;
    private final Minecraft mc;
    private final IGuiListEntry[] listEntries;
    private int maxListLabelWidth;
    
    public GuiKeyBindingList(final GuiControls controls, final Minecraft mcIn) {
        super(mcIn, GuiControls.width + 45, GuiControls.height, 63, GuiControls.height - 32, 20);
        this.controlsScreen = controls;
        this.mc = mcIn;
        final KeyBinding[] akeybinding = ArrayUtils.clone(mcIn.gameSettings.keyBindings);
        this.listEntries = new IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
        Arrays.sort(akeybinding);
        int i = 0;
        String s = null;
        KeyBinding[] array;
        for (int length = (array = akeybinding).length, k = 0; k < length; ++k) {
            final KeyBinding keybinding = array[k];
            final String s2 = keybinding.getKeyCategory();
            if (!s2.equals(s)) {
                s = s2;
                this.listEntries[i++] = new CategoryEntry(s2);
            }
            final int j = mcIn.fontRendererObj.getStringWidth(I18n.format(keybinding.getKeyDescription(), new Object[0]));
            if (j > this.maxListLabelWidth) {
                this.maxListLabelWidth = j;
            }
            this.listEntries[i++] = new KeyEntry(keybinding, (KeyEntry)null);
        }
    }
    
    @Override
    protected int getSize() {
        return this.listEntries.length;
    }
    
    @Override
    public IGuiListEntry getListEntry(final int index) {
        return this.listEntries[index];
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }
    
    @Override
    public int getListWidth() {
        return super.getListWidth() + 32;
    }
    
    public class CategoryEntry implements IGuiListEntry
    {
        private final String labelText;
        private final int labelWidth;
        
        public CategoryEntry(final String name) {
            this.labelText = I18n.format(name, new Object[0]);
            this.labelWidth = GuiKeyBindingList.this.mc.fontRendererObj.getStringWidth(this.labelText);
        }
        
        @Override
        public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
            final FontRenderer fontRendererObj = GuiKeyBindingList.this.mc.fontRendererObj;
            final String labelText = this.labelText;
            final GuiScreen currentScreen = Minecraft.currentScreen;
            fontRendererObj.drawString(labelText, GuiScreen.width / 2 - this.labelWidth / 2, p_192634_3_ + p_192634_5_ - GuiKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT - 1, 16777215);
        }
        
        @Override
        public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
            return false;
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
        }
        
        @Override
        public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
        }
    }
    
    public class KeyEntry implements IGuiListEntry
    {
        private final KeyBinding keybinding;
        private final String keyDesc;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;
        
        private KeyEntry(final KeyBinding name) {
            this.keybinding = name;
            this.keyDesc = I18n.format(name.getKeyDescription(), new Object[0]);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format(name.getKeyDescription(), new Object[0]));
            this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset", new Object[0]));
        }
        
        @Override
        public void func_192634_a(final int p_192634_1_, final int p_192634_2_, final int p_192634_3_, final int p_192634_4_, final int p_192634_5_, final int p_192634_6_, final int p_192634_7_, final boolean p_192634_8_, final float p_192634_9_) {
            final boolean flag = GuiKeyBindingList.this.controlsScreen.buttonId == this.keybinding;
            GuiKeyBindingList.this.mc.fontRendererObj.drawString(this.keyDesc, p_192634_2_ + 90 - GuiKeyBindingList.this.maxListLabelWidth, p_192634_3_ + p_192634_5_ / 2 - GuiKeyBindingList.this.mc.fontRendererObj.FONT_HEIGHT / 2, 16777215);
            this.btnReset.xPosition = p_192634_2_ + 190;
            this.btnReset.yPosition = p_192634_3_;
            this.btnReset.enabled = (this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault());
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, p_192634_6_, p_192634_7_, p_192634_9_);
            this.btnChangeKeyBinding.xPosition = p_192634_2_ + 105;
            this.btnChangeKeyBinding.yPosition = p_192634_3_;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());
            boolean flag2 = false;
            if (this.keybinding.getKeyCode() != 0) {
                KeyBinding[] keyBindings;
                for (int length = (keyBindings = GuiKeyBindingList.this.mc.gameSettings.keyBindings).length, i = 0; i < length; ++i) {
                    final KeyBinding keybinding = keyBindings[i];
                    if (keybinding != this.keybinding && keybinding.getKeyCode() == this.keybinding.getKeyCode()) {
                        flag2 = true;
                        break;
                    }
                }
            }
            if (flag) {
                this.btnChangeKeyBinding.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + this.btnChangeKeyBinding.displayString + TextFormatting.WHITE + " <";
            }
            else if (flag2) {
                this.btnChangeKeyBinding.displayString = TextFormatting.RED + this.btnChangeKeyBinding.displayString;
            }
            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, p_192634_6_, p_192634_7_, p_192634_9_);
        }
        
        @Override
        public boolean mousePressed(final int slotIndex, final int mouseX, final int mouseY, final int mouseEvent, final int relativeX, final int relativeY) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, mouseX, mouseY)) {
                GuiKeyBindingList.this.controlsScreen.buttonId = this.keybinding;
                return true;
            }
            if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, mouseX, mouseY)) {
                GuiKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
                KeyBinding.resetKeyBindingArrayAndHash();
                return true;
            }
            return false;
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
            this.btnReset.mouseReleased(x, y);
        }
        
        @Override
        public void func_192633_a(final int p_192633_1_, final int p_192633_2_, final int p_192633_3_, final float p_192633_4_) {
        }
    }
}
