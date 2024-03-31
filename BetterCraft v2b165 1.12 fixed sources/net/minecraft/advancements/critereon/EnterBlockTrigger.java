// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.block.state.IBlockState;
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

public class EnterBlockTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192196_a;
    private final Map<PlayerAdvancements, Listeners> field_192197_b;
    
    static {
        field_192196_a = new ResourceLocation("enter_block");
    }
    
    public EnterBlockTrigger() {
        this.field_192197_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return EnterBlockTrigger.field_192196_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners enterblocktrigger$listeners = this.field_192197_b.get(p_192165_1_);
        if (enterblocktrigger$listeners == null) {
            enterblocktrigger$listeners = new Listeners(p_192165_1_);
            this.field_192197_b.put(p_192165_1_, enterblocktrigger$listeners);
        }
        enterblocktrigger$listeners.func_192472_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners enterblocktrigger$listeners = this.field_192197_b.get(p_192164_1_);
        if (enterblocktrigger$listeners != null) {
            enterblocktrigger$listeners.func_192469_b(p_192164_2_);
            if (enterblocktrigger$listeners.func_192470_a()) {
                this.field_192197_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192197_b.remove(p_192167_1_);
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
        return new Instance(block, map);
    }
    
    public void func_192193_a(final EntityPlayerMP p_192193_1_, final IBlockState p_192193_2_) {
        final Listeners enterblocktrigger$listeners = this.field_192197_b.get(p_192193_1_.func_192039_O());
        if (enterblocktrigger$listeners != null) {
            enterblocktrigger$listeners.func_192471_a(p_192193_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final Block field_192261_a;
        private final Map<IProperty<?>, Object> field_192262_b;
        
        public Instance(@Nullable final Block p_i47451_1_, @Nullable final Map<IProperty<?>, Object> p_i47451_2_) {
            super(EnterBlockTrigger.field_192196_a);
            this.field_192261_a = p_i47451_1_;
            this.field_192262_b = p_i47451_2_;
        }
        
        public boolean func_192260_a(final IBlockState p_192260_1_) {
            if (this.field_192261_a != null && p_192260_1_.getBlock() != this.field_192261_a) {
                return false;
            }
            if (this.field_192262_b != null) {
                for (final Map.Entry<IProperty<?>, Object> entry : this.field_192262_b.entrySet()) {
                    if (p_192260_1_.getValue(entry.getKey()) != entry.getValue()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192473_a;
        private final Set<Listener<Instance>> field_192474_b;
        
        public Listeners(final PlayerAdvancements p_i47452_1_) {
            this.field_192474_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192473_a = p_i47452_1_;
        }
        
        public boolean func_192470_a() {
            return this.field_192474_b.isEmpty();
        }
        
        public void func_192472_a(final Listener<Instance> p_192472_1_) {
            this.field_192474_b.add(p_192472_1_);
        }
        
        public void func_192469_b(final Listener<Instance> p_192469_1_) {
            this.field_192474_b.remove(p_192469_1_);
        }
        
        public void func_192471_a(final IBlockState p_192471_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192474_b) {
                if (listener.func_192158_a().func_192260_a(p_192471_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192473_a);
                }
            }
        }
    }
}
