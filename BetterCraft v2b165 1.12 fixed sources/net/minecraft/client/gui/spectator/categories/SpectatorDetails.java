// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.spectator.categories;

import com.google.common.base.MoreObjects;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import java.util.List;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;

public class SpectatorDetails
{
    private final ISpectatorMenuView category;
    private final List<ISpectatorMenuObject> items;
    private final int selectedSlot;
    
    public SpectatorDetails(final ISpectatorMenuView p_i45494_1_, final List<ISpectatorMenuObject> p_i45494_2_, final int p_i45494_3_) {
        this.category = p_i45494_1_;
        this.items = p_i45494_2_;
        this.selectedSlot = p_i45494_3_;
    }
    
    public ISpectatorMenuObject getObject(final int p_178680_1_) {
        return (p_178680_1_ >= 0 && p_178680_1_ < this.items.size()) ? MoreObjects.firstNonNull(this.items.get(p_178680_1_), SpectatorMenu.EMPTY_SLOT) : SpectatorMenu.EMPTY_SLOT;
    }
    
    public int getSelectedSlot() {
        return this.selectedSlot;
    }
}
