// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import com.google.common.base.Predicates;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;

public final class EntitySelectors
{
    public static final Predicate<Entity> IS_ALIVE;
    public static final Predicate<Entity> IS_STANDALONE;
    public static final Predicate<Entity> HAS_INVENTORY;
    public static final Predicate<Entity> CAN_AI_TARGET;
    public static final Predicate<Entity> NOT_SPECTATING;
    
    static {
        IS_ALIVE = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive();
            }
        };
        IS_STANDALONE = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive() && !p_apply_1_.isBeingRidden() && !p_apply_1_.isRiding();
            }
        };
        HAS_INVENTORY = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_ instanceof IInventory && p_apply_1_.isEntityAlive();
            }
        };
        CAN_AI_TARGET = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return !(p_apply_1_ instanceof EntityPlayer) || (!((EntityPlayer)p_apply_1_).isSpectator() && !((EntityPlayer)p_apply_1_).isCreative());
            }
        };
        NOT_SPECTATING = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return !(p_apply_1_ instanceof EntityPlayer) || !((EntityPlayer)p_apply_1_).isSpectator();
            }
        };
    }
    
    public static <T extends Entity> Predicate<T> withinRange(final double x, final double y, final double z, final double range) {
        final double d0 = range * range;
        return new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_.getDistanceSq(x, y, z) <= d0;
            }
        };
    }
    
    public static <T extends Entity> Predicate<T> getTeamCollisionPredicate(final Entity entityIn) {
        final Team team = entityIn.getTeam();
        final Team.CollisionRule team$collisionrule = (team == null) ? Team.CollisionRule.ALWAYS : team.getCollisionRule();
        final Predicate<?> ret = (team$collisionrule == Team.CollisionRule.NEVER) ? Predicates.alwaysFalse() : Predicates.and((Predicate<?>)EntitySelectors.NOT_SPECTATING, (Predicate<?>)new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                if (!p_apply_1_.canBePushed()) {
                    return false;
                }
                if (entityIn.world.isRemote && (!(p_apply_1_ instanceof EntityPlayer) || !((EntityPlayer)p_apply_1_).isUser())) {
                    return false;
                }
                final Team team1 = p_apply_1_.getTeam();
                final Team.CollisionRule team$collisionrule1 = (team1 == null) ? Team.CollisionRule.ALWAYS : team1.getCollisionRule();
                if (team$collisionrule1 == Team.CollisionRule.NEVER) {
                    return false;
                }
                final boolean flag = team != null && team.isSameTeam(team1);
                return ((team$collisionrule != Team.CollisionRule.HIDE_FOR_OWN_TEAM && team$collisionrule1 != Team.CollisionRule.HIDE_FOR_OWN_TEAM) || !flag) && ((team$collisionrule != Team.CollisionRule.HIDE_FOR_OTHER_TEAMS && team$collisionrule1 != Team.CollisionRule.HIDE_FOR_OTHER_TEAMS) || flag);
            }
        });
        return (Predicate<T>)ret;
    }
    
    public static Predicate<Entity> func_191324_b(final Entity p_191324_0_) {
        return new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity p_apply_1_) {
                while (p_apply_1_.isRiding()) {
                    p_apply_1_ = p_apply_1_.getRidingEntity();
                    if (p_apply_1_ != p_191324_0_) {
                        continue;
                    }
                    return false;
                }
                return true;
            }
        };
    }
    
    public static class ArmoredMob implements Predicate<Entity>
    {
        private final ItemStack armor;
        
        public ArmoredMob(final ItemStack armor) {
            this.armor = armor;
        }
        
        @Override
        public boolean apply(@Nullable final Entity p_apply_1_) {
            if (!p_apply_1_.isEntityAlive()) {
                return false;
            }
            if (!(p_apply_1_ instanceof EntityLivingBase)) {
                return false;
            }
            final EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
            if (!entitylivingbase.getItemStackFromSlot(EntityLiving.getSlotForItemStack(this.armor)).func_190926_b()) {
                return false;
            }
            if (entitylivingbase instanceof EntityLiving) {
                return ((EntityLiving)entitylivingbase).canPickUpLoot();
            }
            return entitylivingbase instanceof EntityArmorStand || entitylivingbase instanceof EntityPlayer;
        }
    }
}
