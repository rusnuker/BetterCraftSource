// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.village;

import net.minecraft.nbt.NBTTagList;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.ArrayList;

public class MerchantRecipeList extends ArrayList<MerchantRecipe>
{
    public MerchantRecipeList() {
    }
    
    public MerchantRecipeList(final NBTTagCompound compound) {
        this.readRecipiesFromTags(compound);
    }
    
    @Nullable
    public MerchantRecipe canRecipeBeUsed(final ItemStack p_77203_1_, final ItemStack p_77203_2_, final int p_77203_3_) {
        if (p_77203_3_ > 0 && p_77203_3_ < this.size()) {
            final MerchantRecipe merchantrecipe1 = this.get(p_77203_3_);
            return (!this.areItemStacksExactlyEqual(p_77203_1_, merchantrecipe1.getItemToBuy()) || ((!p_77203_2_.func_190926_b() || merchantrecipe1.hasSecondItemToBuy()) && (!merchantrecipe1.hasSecondItemToBuy() || !this.areItemStacksExactlyEqual(p_77203_2_, merchantrecipe1.getSecondItemToBuy()))) || p_77203_1_.func_190916_E() < merchantrecipe1.getItemToBuy().func_190916_E() || (merchantrecipe1.hasSecondItemToBuy() && p_77203_2_.func_190916_E() < merchantrecipe1.getSecondItemToBuy().func_190916_E())) ? null : merchantrecipe1;
        }
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe2 = this.get(i);
            if (this.areItemStacksExactlyEqual(p_77203_1_, merchantrecipe2.getItemToBuy()) && p_77203_1_.func_190916_E() >= merchantrecipe2.getItemToBuy().func_190916_E() && ((!merchantrecipe2.hasSecondItemToBuy() && p_77203_2_.func_190926_b()) || (merchantrecipe2.hasSecondItemToBuy() && this.areItemStacksExactlyEqual(p_77203_2_, merchantrecipe2.getSecondItemToBuy()) && p_77203_2_.func_190916_E() >= merchantrecipe2.getSecondItemToBuy().func_190916_E()))) {
                return merchantrecipe2;
            }
        }
        return null;
    }
    
    private boolean areItemStacksExactlyEqual(final ItemStack stack1, final ItemStack stack2) {
        return ItemStack.areItemsEqual(stack1, stack2) && (!stack2.hasTagCompound() || (stack1.hasTagCompound() && NBTUtil.areNBTEquals(stack2.getTagCompound(), stack1.getTagCompound(), false)));
    }
    
    public void writeToBuf(final PacketBuffer buffer) {
        buffer.writeByte((byte)(this.size() & 0xFF));
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe = this.get(i);
            buffer.writeItemStackToBuffer(merchantrecipe.getItemToBuy());
            buffer.writeItemStackToBuffer(merchantrecipe.getItemToSell());
            final ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
            buffer.writeBoolean(!itemstack.func_190926_b());
            if (!itemstack.func_190926_b()) {
                buffer.writeItemStackToBuffer(itemstack);
            }
            buffer.writeBoolean(merchantrecipe.isRecipeDisabled());
            buffer.writeInt(merchantrecipe.getToolUses());
            buffer.writeInt(merchantrecipe.getMaxTradeUses());
        }
    }
    
    public static MerchantRecipeList readFromBuf(final PacketBuffer buffer) throws IOException {
        final MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        for (int i = buffer.readByte() & 0xFF, j = 0; j < i; ++j) {
            final ItemStack itemstack = buffer.readItemStackFromBuffer();
            final ItemStack itemstack2 = buffer.readItemStackFromBuffer();
            ItemStack itemstack3 = ItemStack.field_190927_a;
            if (buffer.readBoolean()) {
                itemstack3 = buffer.readItemStackFromBuffer();
            }
            final boolean flag = buffer.readBoolean();
            final int k = buffer.readInt();
            final int l = buffer.readInt();
            final MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack3, itemstack2, k, l);
            if (flag) {
                merchantrecipe.compensateToolUses();
            }
            merchantrecipelist.add(merchantrecipe);
        }
        return merchantrecipelist;
    }
    
    public void readRecipiesFromTags(final NBTTagCompound compound) {
        final NBTTagList nbttaglist = compound.getTagList("Recipes", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            this.add(new MerchantRecipe(nbttagcompound));
        }
    }
    
    public NBTTagCompound getRecipiesAsTags() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.size(); ++i) {
            final MerchantRecipe merchantrecipe = this.get(i);
            nbttaglist.appendTag(merchantrecipe.writeToTags());
        }
        nbttagcompound.setTag("Recipes", nbttaglist);
        return nbttagcompound;
    }
}
