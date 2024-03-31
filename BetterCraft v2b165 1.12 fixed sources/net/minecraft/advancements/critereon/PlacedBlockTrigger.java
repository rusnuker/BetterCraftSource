// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.WorldServer;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Optional;
import java.util.Iterator;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.properties.IProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class PlacedBlockTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193174_a;
    private final Map<PlayerAdvancements, Listeners> field_193175_b;
    
    static {
        field_193174_a = new ResourceLocation("placed_block");
    }
    
    public PlacedBlockTrigger() {
        this.field_193175_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return PlacedBlockTrigger.field_193174_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners placedblocktrigger$listeners = this.field_193175_b.get(p_192165_1_);
        if (placedblocktrigger$listeners == null) {
            placedblocktrigger$listeners = new Listeners(p_192165_1_);
            this.field_193175_b.put(p_192165_1_, placedblocktrigger$listeners);
        }
        placedblocktrigger$listeners.func_193490_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners placedblocktrigger$listeners = this.field_193175_b.get(p_192164_1_);
        if (placedblocktrigger$listeners != null) {
            placedblocktrigger$listeners.func_193487_b(p_192164_2_);
            if (placedblocktrigger$listeners.func_193488_a()) {
                this.field_193175_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193175_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        Block block = null;
        if (p_192166_1_.has("block")) {
            final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(p_192166_1_, "block"));
            if (!Block.REGISTRY.containsKey(resourcelocation)) {
                throw new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
            }
            block = Block.REGISTRY.getObject(resourcelocation);
        }
        Map<IProperty<?>, Object> map = null;
        if (p_192166_1_.has("state")) {
            if (block == null) {
                throw new JsonSyntaxException("Can't define block state without a specific block type");
            }
            final BlockStateContainer blockstatecontainer = block.getBlockState();
            for (final Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(p_192166_1_, "state").entrySet()) {
                final IProperty<?> iproperty = blockstatecontainer.getProperty(entry.getKey());
                if (iproperty == null) {
                    throw new JsonSyntaxException("Unknown block state property '" + entry.getKey() + "' for block '" + Block.REGISTRY.getNameForObject(block) + "'");
                }
                final String s = JsonUtils.getString(entry.getValue(), entry.getKey());
                final Optional<?> optional = iproperty.parseValue(s);
                if (!optional.isPresent()) {
                    throw new JsonSyntaxException("Invalid block state value '" + s + "' for property '" + entry.getKey() + "' on block '" + Block.REGISTRY.getNameForObject(block) + "'");
                }
                if (map == null) {
                    map = (Map<IProperty<?>, Object>)Maps.newHashMap();
                }
                map.put(iproperty, optional.get());
            }
        }
        final LocationPredicate locationpredicate = LocationPredicate.func_193454_a(p_192166_1_.get("location"));
        final ItemPredicate itempredicate = ItemPredicate.func_192492_a(p_192166_1_.get("item"));
        return new Instance(block, map, locationpredicate, itempredicate);
    }
    
    public void func_193173_a(final EntityPlayerMP p_193173_1_, final BlockPos p_193173_2_, final ItemStack p_193173_3_) {
        final IBlockState iblockstate = p_193173_1_.world.getBlockState(p_193173_2_);
        final Listeners placedblocktrigger$listeners = this.field_193175_b.get(p_193173_1_.func_192039_O());
        if (placedblocktrigger$listeners != null) {
            placedblocktrigger$listeners.func_193489_a(iblockstate, p_193173_2_, p_193173_1_.getServerWorld(), p_193173_3_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final Block field_193211_a;
        private final Map<IProperty<?>, Object> field_193212_b;
        private final LocationPredicate field_193213_c;
        private final ItemPredicate field_193214_d;
        
        public Instance(@Nullable final Block p_i47566_1_, @Nullable final Map<IProperty<?>, Object> p_i47566_2_, final LocationPredicate p_i47566_3_, final ItemPredicate p_i47566_4_) {
            super(PlacedBlockTrigger.field_193174_a);
            this.field_193211_a = p_i47566_1_;
            this.field_193212_b = p_i47566_2_;
            this.field_193213_c = p_i47566_3_;
            this.field_193214_d = p_i47566_4_;
        }
        
        public boolean func_193210_a(final IBlockState p_193210_1_, final BlockPos p_193210_2_, final WorldServer p_193210_3_, final ItemStack p_193210_4_) {
            if (this.field_193211_a != null && p_193210_1_.getBlock() != this.field_193211_a) {
                return false;
            }
            if (this.field_193212_b != null) {
                for (final Map.Entry<IProperty<?>, Object> entry : this.field_193212_b.entrySet()) {
                    if (p_193210_1_.getValue(entry.getKey()) != entry.getValue()) {
                        return false;
                    }
                }
            }
            return this.field_193213_c.func_193453_a(p_193210_3_, (float)p_193210_2_.getX(), (float)p_193210_2_.getY(), (float)p_193210_2_.getZ()) && this.field_193214_d.func_192493_a(p_193210_4_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193491_a;
        private final Set<Listener<Instance>> field_193492_b;
        
        public Listeners(final PlayerAdvancements p_i47567_1_) {
            this.field_193492_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193491_a = p_i47567_1_;
        }
        
        public boolean func_193488_a() {
            return this.field_193492_b.isEmpty();
        }
        
        public void func_193490_a(final Listener<Instance> p_193490_1_) {
            this.field_193492_b.add(p_193490_1_);
        }
        
        public void func_193487_b(final Listener<Instance> p_193487_1_) {
            this.field_193492_b.remove(p_193487_1_);
        }
        
        public void func_193489_a(final IBlockState p_193489_1_, final BlockPos p_193489_2_, final WorldServer p_193489_3_, final ItemStack p_193489_4_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193492_b) {
                if (listener.func_192158_a().func_193210_a(p_193489_1_, p_193489_2_, p_193489_3_, p_193489_4_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193491_a);
                }
            }
        }
    }
}
