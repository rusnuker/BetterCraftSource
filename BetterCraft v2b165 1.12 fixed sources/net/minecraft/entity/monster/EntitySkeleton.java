// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntitySkeleton extends AbstractSkeleton
{
    public EntitySkeleton(final World worldIn) {
        super(worldIn);
    }
    
    public static void registerFixesSkeleton(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySkeleton.class);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SKELETON;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }
    
    @Override
    SoundEvent func_190727_o() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (cause.getEntity() instanceof EntityCreeper) {
            final EntityCreeper entitycreeper = (EntityCreeper)cause.getEntity();
            if (entitycreeper.getPowered() && entitycreeper.isAIEnabled()) {
                entitycreeper.incrementDroppedSkulls();
                this.entityDropItem(new ItemStack(Items.SKULL, 1, 0), 0.0f);
            }
        }
    }
    
    @Override
    protected EntityArrow func_190726_a(final float p_190726_1_) {
        final ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        if (itemstack.getItem() == Items.SPECTRAL_ARROW) {
            final EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(this.world, this);
            entityspectralarrow.func_190547_a(this, p_190726_1_);
            return entityspectralarrow;
        }
        final EntityArrow entityarrow = super.func_190726_a(p_190726_1_);
        if (itemstack.getItem() == Items.TIPPED_ARROW && entityarrow instanceof EntityTippedArrow) {
            ((EntityTippedArrow)entityarrow).setPotionEffect(itemstack);
        }
        return entityarrow;
    }
}
