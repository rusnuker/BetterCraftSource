// 
// Decompiled by Procyon v0.6.0
// 

package viamcp.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import viamcp.protocolinfo.ProtocolInfo;
import viamcp.vialoadingbase.ViaLoadingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiProtocolSelector extends GuiScreen
{
    private final GuiScreen parent;
    public SlotList list;
    
    public GuiProtocolSelector(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiButton(1, GuiProtocolSelector.width / 2 - 100, GuiProtocolSelector.height - 25, 200, 20, "Back"));
        this.list = new SlotList(this.mc, GuiProtocolSelector.width, GuiProtocolSelector.height, 32, GuiProtocolSelector.height - 32);
    }
    
    @Override
    protected void actionPerformed(final GuiButton guiButton) throws IOException {
        this.list.actionPerformed(guiButton);
        if (guiButton.id == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.list.handleMouseInput();
        super.handleMouseInput();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0, 2.0, 2.0);
        final String title = EnumChatFormatting.BOLD + "ViaVersion";
        this.drawString(this.fontRendererObj, title, (GuiProtocolSelector.width - this.fontRendererObj.getStringWidth(title) * 2) / 4, 5, -1);
        GlStateManager.popMatrix();
        final ProtocolInfo protocolInfo = ProtocolInfo.fromProtocolVersion(ViaLoadingBase.getInstance().getTargetVersion());
        final String versionTitle = "Version: " + ViaLoadingBase.getInstance().getTargetVersion().getName() + " - " + protocolInfo.getName();
        final String versionReleased = "Released: " + protocolInfo.getReleaseDate();
        final int fixedHeight = (5 + this.fontRendererObj.FONT_HEIGHT) * 2 + 2;
        this.drawString(this.fontRendererObj, new StringBuilder().append(EnumChatFormatting.GRAY).append(EnumChatFormatting.BOLD).append("Information").toString(), (GuiProtocolSelector.width - this.fontRendererObj.getStringWidth("Version Information")) / 2, fixedHeight, -1);
        this.drawString(this.fontRendererObj, versionTitle, (GuiProtocolSelector.width - this.fontRendererObj.getStringWidth(versionTitle)) / 2, fixedHeight + this.fontRendererObj.FONT_HEIGHT, -1);
        this.drawString(this.fontRendererObj, versionReleased, (GuiProtocolSelector.width - this.fontRendererObj.getStringWidth(versionReleased)) / 2, fixedHeight + this.fontRendererObj.FONT_HEIGHT * 2, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    class SlotList extends GuiSlot
    {
        public SlotList(final Minecraft mc, final int width, final int height, final int top, final int bottom) {
            super(mc, width, height, top + 30, bottom, 18);
        }
        
        @Override
        protected int getSize() {
            return ViaLoadingBase.getProtocols().size();
        }
        
        @Override
        protected void elementClicked(final int i, final boolean b, final int i1, final int i2) {
            final ProtocolVersion protocolVersion = ViaLoadingBase.getProtocols().get(i);
            ViaLoadingBase.getInstance().reload(protocolVersion);
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
        protected void drawSlot(final int i, final int i1, final int i2, final int i3, final int i4, final int i5) {
            Gui.drawCenteredString(this.mc.fontRendererObj, String.valueOf((ViaLoadingBase.getInstance().getTargetVersion().getIndex() == i) ? new StringBuilder(String.valueOf(EnumChatFormatting.GREEN.toString())).append(EnumChatFormatting.BOLD).toString() : EnumChatFormatting.GRAY.toString()) + ViaLoadingBase.getProtocols().get(i).getName(), this.width / 2, i2 + 2, -1);
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            Gui.drawCenteredString(this.mc.fontRendererObj, "PVN: " + ViaLoadingBase.getProtocols().get(i).getVersion(), this.width, (i2 + 2) * 2 + 20, -1);
            GlStateManager.popMatrix();
        }
    }
}
