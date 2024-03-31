// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage.loot;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;

public class LootContext
{
    private final float luck;
    private final WorldServer worldObj;
    private final LootTableManager lootTableManager;
    @Nullable
    private final Entity lootedEntity;
    @Nullable
    private final EntityPlayer player;
    @Nullable
    private final DamageSource damageSource;
    private final Set<LootTable> lootTables;
    
    public LootContext(final float luckIn, final WorldServer worldIn, final LootTableManager lootTableManagerIn, @Nullable final Entity lootedEntityIn, @Nullable final EntityPlayer playerIn, @Nullable final DamageSource damageSourceIn) {
        this.lootTables = (Set<LootTable>)Sets.newLinkedHashSet();
        this.luck = luckIn;
        this.worldObj = worldIn;
        this.lootTableManager = lootTableManagerIn;
        this.lootedEntity = lootedEntityIn;
        this.player = playerIn;
        this.damageSource = damageSourceIn;
    }
    
    @Nullable
    public Entity getLootedEntity() {
        return this.lootedEntity;
    }
    
    @Nullable
    public Entity getKillerPlayer() {
        return this.player;
    }
    
    @Nullable
    public Entity getKiller() {
        return (this.damageSource == null) ? null : this.damageSource.getEntity();
    }
    
    public boolean addLootTable(final LootTable lootTableIn) {
        return this.lootTables.add(lootTableIn);
    }
    
    public void removeLootTable(final LootTable lootTableIn) {
        this.lootTables.remove(lootTableIn);
    }
    
    public LootTableManager getLootTableManager() {
        return this.lootTableManager;
    }
    
    public float getLuck() {
        return this.luck;
    }
    
    @Nullable
    public Entity getEntity(final EntityTarget target) {
        switch (target) {
            case THIS: {
                return this.getLootedEntity();
            }
            case KILLER: {
                return this.getKiller();
            }
            case KILLER_PLAYER: {
                return this.getKillerPlayer();
            }
            default: {
                return null;
            }
        }
    }
    
    public enum EntityTarget
    {
        THIS("THIS", 0, "this"), 
        KILLER("KILLER", 1, "killer"), 
        KILLER_PLAYER("KILLER_PLAYER", 2, "killer_player");
        
        private final String targetType;
        
        private EntityTarget(final String s, final int n, final String type) {
            this.targetType = type;
        }
        
        public static EntityTarget fromString(final String type) {
            EntityTarget[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EntityTarget lootcontext$entitytarget = values[i];
                if (lootcontext$entitytarget.targetType.equals(type)) {
                    return lootcontext$entitytarget;
                }
            }
            throw new IllegalArgumentException("Invalid entity target " + type);
        }
        
        public static class Serializer extends TypeAdapter<EntityTarget>
        {
            @Override
            public void write(final JsonWriter p_write_1_, final EntityTarget p_write_2_) throws IOException {
                p_write_1_.value(p_write_2_.targetType);
            }
            
            @Override
            public EntityTarget read(final JsonReader p_read_1_) throws IOException {
                return EntityTarget.fromString(p_read_1_.nextString());
            }
        }
    }
    
    public static class Builder
    {
        private final WorldServer worldObj;
        private float luck;
        private Entity lootedEntity;
        private EntityPlayer player;
        private DamageSource damageSource;
        
        public Builder(final WorldServer worldIn) {
            this.worldObj = worldIn;
        }
        
        public Builder withLuck(final float luckIn) {
            this.luck = luckIn;
            return this;
        }
        
        public Builder withLootedEntity(final Entity entityIn) {
            this.lootedEntity = entityIn;
            return this;
        }
        
        public Builder withPlayer(final EntityPlayer playerIn) {
            this.player = playerIn;
            return this;
        }
        
        public Builder withDamageSource(final DamageSource dmgSource) {
            this.damageSource = dmgSource;
            return this;
        }
        
        public LootContext build() {
            return new LootContext(this.luck, this.worldObj, this.worldObj.getLootTableManager(), this.lootedEntity, this.player, this.damageSource);
        }
    }
}
