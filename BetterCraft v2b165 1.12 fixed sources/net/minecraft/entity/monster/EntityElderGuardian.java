// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.monster;

import java.util.Iterator;
import java.util.List;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.potion.PotionEffect;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityElderGuardian extends EntityGuardian
{
    public EntityElderGuardian(final World p_i47288_1_) {
        super(p_i47288_1_);
        this.setSize(this.width * 2.35f, this.height * 2.35f);
        this.enablePersistence();
        if (this.wander != null) {
            this.wander.setExecutionChance(400);
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0);
    }
    
    public static void func_190768_b(final DataFixer p_190768_0_) {
        EntityLiving.registerFixesMob(p_190768_0_, EntityElderGuardian.class);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ELDER_GUARDIAN;
    }
    
    @Override
    public int getAttackDuration() {
        return 60;
    }
    
    public void func_190767_di() {
        this.clientSideSpikesAnimation = 1.0f;
        this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT : SoundEvents.ENTITY_ELDERGUARDIAN_AMBIENTLAND;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource p_184601_1_) {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_HURT : SoundEvents.ENTITY_ELDER_GUARDIAN_HURT_LAND;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH : SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
    }
    
    @Override
    protected SoundEvent func_190765_dj() {
        return SoundEvents.field_191240_aK;
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        final int i = 1200;
        if ((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
            final Potion potion = MobEffects.MINING_FATIGUE;
            final List<EntityPlayerMP> list = this.world.getPlayers((Class<? extends EntityPlayerMP>)EntityPlayerMP.class, (Predicate<? super EntityPlayerMP>)new Predicate<EntityPlayerMP>() {
                @Override
                public boolean apply(@Nullable final EntityPlayerMP p_apply_1_) {
                    return EntityElderGuardian.this.getDistanceSqToEntity(p_apply_1_) < 2500.0 && p_apply_1_.interactionManager.survivalOrAdventure();
                }
            });
            final int j = 2;
            final int k = 6000;
            final int l = 1200;
            for (final EntityPlayerMP entityplayermp : list) {
                if (!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2 || entityplayermp.getActivePotionEffect(potion).getDuration() < 1200) {
                    entityplayermp.connection.sendPacket(new SPacketChangeGameState(10, 0.0f));
                    entityplayermp.addPotionEffect(new PotionEffect(potion, 6000, 2));
                }
            }
        }
        if (!this.hasHome()) {
            this.setHomePosAndDistance(new BlockPos(this), 16);
        }
    }
}
