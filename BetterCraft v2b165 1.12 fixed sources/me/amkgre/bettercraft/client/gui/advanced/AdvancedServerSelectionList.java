// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.gui.advanced;

import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.ServerListEntryLanScan;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.client.gui.GuiListExtended;

public class AdvancedServerSelectionList extends GuiListExtended
{
    private final AdvancedGuiMainMenu owner;
    private final List<AdvancedServerListEntryNormal> normalServers;
    private final IGuiListEntry lanScanEntry;
    private int selectedSlotIndex;
    
    public AdvancedServerSelectionList(final AdvancedGuiMainMenu ownerIn, final Minecraft mcIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.normalServers = (List<AdvancedServerListEntryNormal>)Lists.newArrayList();
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
    
    @Override
    protected int getSize() {
        return this.normalServers.size() + 1;
    }
    
    public void setSelectedSlotIndex(final int selectedSlotIndexIn) {
        this.selectedSlotIndex = selectedSlotIndexIn;
    }
    
    @Override
    protected boolean isSelected(final int slotIndex) {
        return slotIndex == this.selectedSlotIndex;
    }
    
    public int getSelectedSlotIndex() {
        return this.selectedSlotIndex;
    }
    
    public void load(final ServerList p_148195_1_) {
        this.normalServers.clear();
        for (int i = 0; i < p_148195_1_.countServers(); ++i) {
            this.normalServers.add(new AdvancedServerListEntryNormal(this.owner, p_148195_1_.getServerData(i)));
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
