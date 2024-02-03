// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.gui;

import net.minecraft.entity.Entity;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.BlockPos;
import com.mcf.davidee.nbtedit.nbt.NBTTree;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.gui.GuiScreen;

public class GuiEditNBTTree extends GuiScreen
{
    public final int entityOrX;
    public final int y;
    public final int z;
    private boolean entity;
    protected String screenTitle;
    private GuiNBTTree guiTree;
    
    public GuiEditNBTTree(final int entity, final NBTTagCompound tag) {
        this.entity = true;
        this.entityOrX = entity;
        this.y = 0;
        this.z = 0;
        this.screenTitle = "NBTEdit -- EntityId #" + this.entityOrX;
        this.guiTree = new GuiNBTTree(new NBTTree(tag));
    }
    
    public GuiEditNBTTree(final BlockPos pos, final NBTTagCompound tag) {
        this.entity = false;
        this.entityOrX = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.screenTitle = "NBTEdit -- TileEntity at " + pos.getX() + "," + pos.getY() + "," + pos.getZ();
        this.guiTree = new GuiNBTTree(new NBTTree(tag));
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.guiTree.initGUI(GuiEditNBTTree.width, GuiEditNBTTree.height, GuiEditNBTTree.height - 35);
        this.buttonList.add(new GuiButton(1, GuiEditNBTTree.width / 4 - 100, GuiEditNBTTree.height - 27, 200, 20, "Save"));
        this.buttonList.add(new GuiButton(0, GuiEditNBTTree.width * 3 / 4 - 100, GuiEditNBTTree.height - 27, 200, 20, "Quit"));
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char par1, final int key) {
        final GuiEditNBT window = this.guiTree.getWindow();
        if (window != null) {
            window.keyTyped(par1, key);
        }
        else if (key == 1) {
            if (this.guiTree.isEditingSlot()) {
                this.guiTree.stopEditingSlot();
            }
            else {
                this.quitWithoutSaving();
            }
        }
        else if (key == 211) {
            this.guiTree.deleteSelected();
        }
        else if (key == 28) {
            this.guiTree.editSelected();
        }
        else if (key == 200) {
            this.guiTree.arrowKeyPressed(true);
        }
        else if (key == 208) {
            this.guiTree.arrowKeyPressed(false);
        }
        else {
            this.guiTree.keyTyped(par1, key);
        }
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int t) throws IOException {
        if (this.guiTree.getWindow() == null) {
            super.mouseClicked(x, y, t);
        }
        if (t == 0) {
            this.guiTree.mouseClicked(x, y);
        }
        if (t == 1) {
            this.guiTree.rightClick(x, y);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        final int ofs = Mouse.getEventDWheel();
        if (ofs != 0) {
            this.guiTree.shift((ofs >= 1) ? 6 : -6);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton b) {
        if (b.enabled) {
            switch (b.id) {
                case 1: {
                    this.quitWithSave();
                    break;
                }
                default: {
                    this.quitWithoutSaving();
                    break;
                }
            }
        }
    }
    
    @Override
    public void updateScreen() {
        if (!this.mc.thePlayer.isEntityAlive()) {
            this.quitWithoutSaving();
        }
        else {
            this.guiTree.updateScreen();
        }
    }
    
    private void quitWithSave() {
        Minecraft.getMinecraft().thePlayer.getHeldItem().setTagCompound(this.guiTree.getNBTTree().toNBTTagCompound());
        new Thread(() -> {
            try {
                this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
                Thread.sleep(20L);
                this.mc.displayGuiScreen(null);
            }
            catch (final Exception ex2) {}
            return;
        }).start();
        try {
            this.mc.setIngameFocus();
        }
        catch (final NullPointerException ex) {}
    }
    
    private void quitWithoutSaving() {
        this.quitWithSave();
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float par3) {
        this.drawDefaultBackground();
        this.guiTree.draw(x, y);
        Gui.drawCenteredString(this.mc.fontRendererObj, this.screenTitle, GuiEditNBTTree.width / 2, 5, 16777215);
        if (this.guiTree.getWindow() == null) {
            super.drawScreen(x, y, par3);
        }
        else {
            super.drawScreen(-1, -1, par3);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
    
    public Entity getEntity() {
        return this.entity ? this.mc.theWorld.getEntityByID(this.entityOrX) : null;
    }
    
    public boolean isTileEntity() {
        return !this.entity;
    }
    
    public int getBlockX() {
        return this.entity ? 0 : this.entityOrX;
    }
}
