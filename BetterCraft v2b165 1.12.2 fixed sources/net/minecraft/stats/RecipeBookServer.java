// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.stats;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.advancements.CriteriaTriggers;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecipeBookServer extends RecipeBook
{
    private static final Logger field_192828_d;
    
    static {
        field_192828_d = LogManager.getLogger();
    }
    
    public void func_193835_a(final List<IRecipe> p_193835_1_, final EntityPlayerMP p_193835_2_) {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (final IRecipe irecipe : p_193835_1_) {
            if (!this.field_194077_a.get(RecipeBook.func_194075_d(irecipe)) && !irecipe.func_192399_d()) {
                this.func_194073_a(irecipe);
                this.func_193825_e(irecipe);
                list.add(irecipe);
                CriteriaTriggers.field_192126_f.func_192225_a(p_193835_2_, irecipe);
            }
        }
        this.func_194081_a(SPacketRecipeBook.State.ADD, p_193835_2_, list);
    }
    
    public void func_193834_b(final List<IRecipe> p_193834_1_, final EntityPlayerMP p_193834_2_) {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (final IRecipe irecipe : p_193834_1_) {
            if (this.field_194077_a.get(RecipeBook.func_194075_d(irecipe))) {
                this.func_193831_b(irecipe);
                list.add(irecipe);
            }
        }
        this.func_194081_a(SPacketRecipeBook.State.REMOVE, p_193834_2_, list);
    }
    
    private void func_194081_a(final SPacketRecipeBook.State p_194081_1_, final EntityPlayerMP p_194081_2_, final List<IRecipe> p_194081_3_) {
        p_194081_2_.connection.sendPacket(new SPacketRecipeBook(p_194081_1_, p_194081_3_, Collections.emptyList(), this.field_192818_b, this.field_192819_c));
    }
    
    public NBTTagCompound func_192824_e() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setBoolean("isGuiOpen", this.field_192818_b);
        nbttagcompound.setBoolean("isFilteringCraftable", this.field_192819_c);
        final NBTTagList nbttaglist = new NBTTagList();
        for (final IRecipe irecipe : this.func_194079_d()) {
            nbttaglist.appendTag(new NBTTagString(CraftingManager.field_193380_a.getNameForObject(irecipe).toString()));
        }
        nbttagcompound.setTag("recipes", nbttaglist);
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (final IRecipe irecipe2 : this.func_194080_e()) {
            nbttaglist2.appendTag(new NBTTagString(CraftingManager.field_193380_a.getNameForObject(irecipe2).toString()));
        }
        nbttagcompound.setTag("toBeDisplayed", nbttaglist2);
        return nbttagcompound;
    }
    
    public void func_192825_a(final NBTTagCompound p_192825_1_) {
        this.field_192818_b = p_192825_1_.getBoolean("isGuiOpen");
        this.field_192819_c = p_192825_1_.getBoolean("isFilteringCraftable");
        final NBTTagList nbttaglist = p_192825_1_.getTagList("recipes", 8);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final ResourceLocation resourcelocation = new ResourceLocation(nbttaglist.getStringTagAt(i));
            final IRecipe irecipe = CraftingManager.func_193373_a(resourcelocation);
            if (irecipe == null) {
                RecipeBookServer.field_192828_d.info("Tried to load unrecognized recipe: {} removed now.", resourcelocation);
            }
            else {
                this.func_194073_a(irecipe);
            }
        }
        final NBTTagList nbttaglist2 = p_192825_1_.getTagList("toBeDisplayed", 8);
        for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
            final ResourceLocation resourcelocation2 = new ResourceLocation(nbttaglist2.getStringTagAt(j));
            final IRecipe irecipe2 = CraftingManager.func_193373_a(resourcelocation2);
            if (irecipe2 == null) {
                RecipeBookServer.field_192828_d.info("Tried to load unrecognized recipe: {} removed now.", resourcelocation2);
            }
            else {
                this.func_193825_e(irecipe2);
            }
        }
    }
    
    private List<IRecipe> func_194079_d() {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (int i = this.field_194077_a.nextSetBit(0); i >= 0; i = this.field_194077_a.nextSetBit(i + 1)) {
            list.add(CraftingManager.field_193380_a.getObjectById(i));
        }
        return list;
    }
    
    private List<IRecipe> func_194080_e() {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (int i = this.field_194078_b.nextSetBit(0); i >= 0; i = this.field_194078_b.nextSetBit(i + 1)) {
            list.add(CraftingManager.field_193380_a.getObjectById(i));
        }
        return list;
    }
    
    public void func_192826_c(final EntityPlayerMP p_192826_1_) {
        p_192826_1_.connection.sendPacket(new SPacketRecipeBook(SPacketRecipeBook.State.INIT, this.func_194079_d(), this.func_194080_e(), this.field_192818_b, this.field_192819_c));
    }
}
