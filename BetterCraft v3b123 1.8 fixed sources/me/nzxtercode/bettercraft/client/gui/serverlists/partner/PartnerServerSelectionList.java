// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui.serverlists.partner;

import net.minecraft.client.gui.ServerListEntryLanScan;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.client.gui.GuiListExtended;

public class PartnerServerSelectionList extends GuiListExtended
{
    private final GuiPartnerMultiplayer owner;
    private final List<PartnerServerListEntryNormal> normalServers;
    private final IGuiListEntry lanScanEntry;
    private int selectedSlotIndex;
    
    public PartnerServerSelectionList(final GuiPartnerMultiplayer ownerIn, final Minecraft mcIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.normalServers = (List<PartnerServerListEntryNormal>)Lists.newArrayList();
        this.lanScanEntry = new ServerListEntryLanScan();
        this.selectedSlotIndex = -1;
        this.owner = ownerIn;
    }
    
    @Override
    public IGuiListEntry getListEntry(int index) {
        if (index < this.normalServers.size()) {
            return this.normalServers.get(index);
        }
        if ((index -= this.normalServers.size()) == 0) {
            return this.lanScanEntry;
        }
        --index;
        return null;
    }
    
    public int getSize() {
        return this.normalServers.size() + 1;
    }
    
    public void setSelectedSlotIndex(final int selectedSlotIndexIn) {
        this.selectedSlotIndex = selectedSlotIndexIn;
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return slotIndex == this.selectedSlotIndex;
    }
    
    public int func_148193_k() {
        return this.selectedSlotIndex;
    }
    
    public void func_148195_a(final PartnerServerList savedServerList) {
        this.normalServers.clear();
        for (int i = 0; i < savedServerList.countServers(); ++i) {
            this.normalServers.add(new PartnerServerListEntryNormal(this.owner, savedServerList.getServerData(i)));
        }
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 30;
    }
    
    @Override
    public int getListWidth() {
        return super.getListWidth() + 85;
    }
    
    @Override
    public void drawScreen(final int mouseXIn, final int mouseYIn, final float p_148128_3_) {
        super.drawScreen(mouseXIn, mouseYIn, p_148128_3_);
    }
    
    public void clear() {
        this.normalServers.clear();
    }
}
