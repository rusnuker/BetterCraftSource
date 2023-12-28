// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import net.minecraft.nbt.NBTException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;

public class NBTPredicate
{
    public static final NBTPredicate field_193479_a;
    @Nullable
    private final NBTTagCompound field_193480_b;
    
    static {
        field_193479_a = new NBTPredicate(null);
    }
    
    public NBTPredicate(@Nullable final NBTTagCompound p_i47536_1_) {
        this.field_193480_b = p_i47536_1_;
    }
    
    public boolean func_193478_a(final ItemStack p_193478_1_) {
        return this == NBTPredicate.field_193479_a || this.func_193477_a(p_193478_1_.getTagCompound());
    }
    
    public boolean func_193475_a(final Entity p_193475_1_) {
        return this == NBTPredicate.field_193479_a || this.func_193477_a(CommandBase.entityToNBT(p_193475_1_));
    }
    
    public boolean func_193477_a(@Nullable final NBTBase p_193477_1_) {
        if (p_193477_1_ == null) {
            return this == NBTPredicate.field_193479_a;
        }
        return this.field_193480_b == null || NBTUtil.areNBTEquals(this.field_193480_b, p_193477_1_, true);
    }
    
    public static NBTPredicate func_193476_a(@Nullable final JsonElement p_193476_0_) {
        if (p_193476_0_ != null && !p_193476_0_.isJsonNull()) {
            NBTTagCompound nbttagcompound;
            try {
                nbttagcompound = JsonToNBT.getTagFromJson(JsonUtils.getString(p_193476_0_, "nbt"));
            }
            catch (final NBTException nbtexception) {
                throw new JsonSyntaxException("Invalid nbt tag: " + nbtexception.getMessage());
            }
            return new NBTPredicate(nbttagcompound);
        }
        return NBTPredicate.field_193479_a;
    }
}
