// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.passive;

import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMule extends AbstractChestHorse
{
    public EntityMule(final World p_i47296_1_) {
        super(p_i47296_1_);
    }
    
    public static void func_190700_b(final DataFixer p_190700_0_) {
        AbstractChestHorse.func_190694_b(p_190700_0_, EntityMule.class);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.field_191191_I;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_MULE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_MULE_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource p_184601_1_) {
        super.getHurtSound(p_184601_1_);
        return SoundEvents.ENTITY_MULE_HURT;
    }
    
    @Override
    protected void func_190697_dk() {
        this.playSound(SoundEvents.field_191259_dX, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
    }
}
