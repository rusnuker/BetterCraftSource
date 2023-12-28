// 
// Decompiled by Procyon v0.6.0
// 

package viaforge.gui;

import net.minecraft.util.text.TextFormatting;
import viaforge.ViaForge;
import viaforge.protocols.ProtocolCollection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiProtocolSelector extends GuiScreen
{
    public SlotList list;
    private GuiScreen parent;
    
    public GuiProtocolSelector(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, GuiProtocolSelector.width / 2 - 100, GuiProtocolSelector.height - 27, 200, 20, "Back"));
        this.list = new SlotList(this.mc, GuiProtocolSelector.width, GuiProtocolSelector.height, 32, GuiProtocolSelector.height - 32, 10);
    }
    
    @Override
    protected void actionPerformed(final GuiButton p_actionPerformed_1_) throws IOException {
        this.list.actionPerformed(p_actionPerformed_1_);
        if (p_actionPerformed_1_.id == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.list.handleMouseInput();
        super.handleMouseInput();
    }
    
    @Override
    public void drawScreen(final int p_drawScreen_1_, final int p_drawScreen_2_, final float p_drawScreen_3_) {
        this.drawDefaultBackground();
        this.list.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        GL11.glPushMatrix();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        Gui.drawCenteredString(this.fontRendererObj, "§dViaVersion", GuiProtocolSelector.width / 4, 6, 16777215);
        GL11.glPopMatrix();
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }
    
    class SlotList extends GuiSlot
    {
        public SlotList(final Minecraft p_i1052_1_, final int p_i1052_2_, final int p_i1052_3_, final int p_i1052_4_, final int p_i1052_5_, final int p_i1052_6_) {
            super(p_i1052_1_, p_i1052_2_, p_i1052_3_, p_i1052_4_, p_i1052_5_, p_i1052_6_);
        }
        
        @Override
        protected int getSize() {
            return ProtocolCollection.values().length;
        }
        
        @Override
        protected void elementClicked(final int i, final boolean b, final int i1, final int i2) {
            ViaForge.getInstance().setVersion(ProtocolCollection.values()[i].getVersion().getVersion());
        }
        
        @Override
        protected boolean isSelected(final int i) {
            return false;
        }
        
        @Override
        protected void drawBackground() {
            GuiProtocolSelector.this.drawDefaultBackground();
        }
        
        @Override
        protected void func_192637_a(final int p_192637_1_, final int p_192637_2_, final int p_192637_3_, final int p_192637_4_, final int p_192637_5_, final int p_192637_6_, final float p_192637_7_) {
            Gui.drawCenteredString(this.mc.fontRendererObj, String.valueOf((ViaForge.getInstance().getVersion() == ProtocolCollection.values()[p_192637_1_].getVersion().getVersion()) ? new StringBuilder(String.valueOf(TextFormatting.GREEN.toString())).append(TextFormatting.BOLD).toString() : TextFormatting.GRAY.toString()) + ProtocolCollection.getProtocolById(ProtocolCollection.values()[p_192637_1_].getVersion().getVersion()).getName() + " §d* §8(§5" + ProtocolCollection.values()[p_192637_1_].getVersion().getVersion() + "§8)", this.width / 2, p_192637_3_ + 2, -1);
        }
    }
}
