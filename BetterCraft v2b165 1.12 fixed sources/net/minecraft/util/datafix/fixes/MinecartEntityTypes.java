// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.datafix.IFixableData;

public class MinecartEntityTypes implements IFixableData
{
    private static final List<String> MINECART_TYPE_LIST;
    
    static {
        MINECART_TYPE_LIST = Lists.newArrayList("MinecartRideable", "MinecartChest", "MinecartFurnace", "MinecartTNT", "MinecartSpawner", "MinecartHopper", "MinecartCommandBlock");
    }
    
    @Override
    public int getFixVersion() {
        return 106;
    }
    
    @Override
    public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
        if ("Minecart".equals(compound.getString("id"))) {
            String s = "MinecartRideable";
            final int i = compound.getInteger("Type");
            if (i > 0 && i < MinecartEntityTypes.MINECART_TYPE_LIST.size()) {
                s = MinecartEntityTypes.MINECART_TYPE_LIST.get(i);
            }
            compound.setString("id", s);
            compound.removeTag("Type");
        }
        return compound;
    }
}
