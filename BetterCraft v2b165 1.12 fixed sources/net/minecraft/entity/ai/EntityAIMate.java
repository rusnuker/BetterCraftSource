// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityAgeable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIMate extends EntityAIBase
{
    private final EntityAnimal theAnimal;
    private final Class<? extends EntityAnimal> field_190857_e;
    World theWorld;
    private EntityAnimal targetMate;
    int spawnBabyDelay;
    double moveSpeed;
    
    public EntityAIMate(final EntityAnimal animal, final double speedIn) {
        this(animal, speedIn, animal.getClass());
    }
    
    public EntityAIMate(final EntityAnimal p_i47306_1_, final double p_i47306_2_, final Class<? extends EntityAnimal> p_i47306_4_) {
        this.theAnimal = p_i47306_1_;
        this.theWorld = p_i47306_1_.world;
        this.field_190857_e = p_i47306_4_;
        this.moveSpeed = p_i47306_2_;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theAnimal.isInLove()) {
            return false;
        }
        this.targetMate = this.getNearbyMate();
        return this.targetMate != null;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }
    
    @Override
    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }
    
    @Override
    public void updateTask() {
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0f, (float)this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0) {
            this.spawnBaby();
        }
    }
    
    private EntityAnimal getNearbyMate() {
        final List<EntityAnimal> list = this.theWorld.getEntitiesWithinAABB(this.field_190857_e, this.theAnimal.getEntityBoundingBox().expandXyz(8.0));
        double d0 = Double.MAX_VALUE;
        EntityAnimal entityanimal = null;
        for (final EntityAnimal entityanimal2 : list) {
            if (this.theAnimal.canMateWith(entityanimal2) && this.theAnimal.getDistanceSqToEntity(entityanimal2) < d0) {
                entityanimal = entityanimal2;
                d0 = this.theAnimal.getDistanceSqToEntity(entityanimal2);
            }
        }
        return entityanimal;
    }
    
    private void spawnBaby() {
        final EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);
        if (entityageable != null) {
            EntityPlayerMP entityplayermp = this.theAnimal.func_191993_do();
            if (entityplayermp == null && this.targetMate.func_191993_do() != null) {
                entityplayermp = this.targetMate.func_191993_do();
            }
            if (entityplayermp != null) {
                entityplayermp.addStat(StatList.ANIMALS_BRED);
                CriteriaTriggers.field_192134_n.func_192168_a(entityplayermp, this.theAnimal, this.targetMate, entityageable);
            }
            this.theAnimal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0f, 0.0f);
            this.theWorld.spawnEntityInWorld(entityageable);
            final Random random = this.theAnimal.getRNG();
            for (int i = 0; i < 7; ++i) {
                final double d0 = random.nextGaussian() * 0.02;
                final double d2 = random.nextGaussian() * 0.02;
                final double d3 = random.nextGaussian() * 0.02;
                final double d4 = random.nextDouble() * this.theAnimal.width * 2.0 - this.theAnimal.width;
                final double d5 = 0.5 + random.nextDouble() * this.theAnimal.height;
                final double d6 = random.nextDouble() * this.theAnimal.width * 2.0 - this.theAnimal.width;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d4, this.theAnimal.posY + d5, this.theAnimal.posZ + d6, d0, d2, d3, new int[0]);
            }
            if (this.theWorld.getGameRules().getBoolean("doMobLoot")) {
                this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
            }
        }
    }
}
