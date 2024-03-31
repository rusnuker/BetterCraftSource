// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.creative.impl.otheritems;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import me.amkgre.bettercraft.client.mods.creative.InventoryItem;

public class MultiColorFirework1Item extends InventoryItem
{
    public MultiColorFirework1Item() {
        super(generate(), "§dMulti Color Firework\n\n§7firework with multicolor");
    }
    
    public static ItemStack generate() {
        final ItemStack itm = new ItemStack(Items.FIREWORKS);
        try {
            itm.setTagCompound(JsonToNBT.getTagFromJson("{Fireworks:{Flight:2b,Explosions:[{Type:1,Flicker:1b,Trail:1b,Colors:[I;1973019,11743532,3887386,5320730,2437522,8073150,2651799,11250603,4408131,14188952,4312372,14602026,6719955,12801229,15435844,15790320],FadeColors:[I;1973019,11743532,3887386,5320730,2437522,8073150,2651799,11250603,4408131,14188952,4312372,14602026,6719955,12801229,15435844,15790320]}]}}"));
        }
        catch (final Exception ex) {}
        return itm;
    }
}
