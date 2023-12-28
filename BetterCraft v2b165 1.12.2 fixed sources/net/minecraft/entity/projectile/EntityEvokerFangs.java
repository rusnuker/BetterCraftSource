// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.projectile;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityEvokerFangs extends Entity
{
    private int field_190553_a;
    private boolean field_190554_b;
    private int field_190555_c;
    private boolean field_190556_d;
    private EntityLivingBase field_190557_e;
    private UUID field_190558_f;
    
    public EntityEvokerFangs(final World p_i47275_1_) {
        super(p_i47275_1_);
        this.field_190555_c = 22;
        this.setSize(0.5f, 0.8f);
    }
    
    public EntityEvokerFangs(final World p_i47276_1_, final double p_i47276_2_, final double p_i47276_4_, final double p_i47276_6_, final float p_i47276_8_, final int p_i47276_9_, final EntityLivingBase p_i47276_10_) {
        this(p_i47276_1_);
        this.field_190553_a = p_i47276_9_;
        this.func_190549_a(p_i47276_10_);
        this.rotationYaw = p_i47276_8_ * 57.295776f;
        this.setPosition(p_i47276_2_, p_i47276_4_, p_i47276_6_);
    }
    
    @Override
    protected void entityInit() {
    }
    
    public void func_190549_a(@Nullable final EntityLivingBase p_190549_1_) {
        this.field_190557_e = p_190549_1_;
        this.field_190558_f = ((p_190549_1_ == null) ? null : p_190549_1_.getUniqueID());
    }
    
    @Nullable
    public EntityLivingBase func_190552_j() {
        if (this.field_190557_e == null && this.field_190558_f != null && this.world instanceof WorldServer) {
            final Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.field_190558_f);
            if (entity instanceof EntityLivingBase) {
                this.field_190557_e = (EntityLivingBase)entity;
            }
        }
        return this.field_190557_e;
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        this.field_190553_a = compound.getInteger("Warmup");
        this.field_190558_f = compound.getUniqueId("OwnerUUID");
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setInteger("Warmup", this.field_190553_a);
        if (this.field_190558_f != null) {
            compound.setUniqueId("OwnerUUID", this.field_190558_f);
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            if (this.field_190556_d) {
                --this.field_190555_c;
                if (this.field_190555_c == 14) {
                    for (int i = 0; i < 12; ++i) {
                        final double d0 = this.posX + (this.rand.nextDouble() * 2.0 - 1.0) * this.width * 0.5;
                        final double d2 = this.posY + 0.05 + this.rand.nextDouble() * 1.0;
                        final double d3 = this.posZ + (this.rand.nextDouble() * 2.0 - 1.0) * this.width * 0.5;
                        final double d4 = (this.rand.nextDouble() * 2.0 - 1.0) * 0.3;
                        final double d5 = 0.3 + this.rand.nextDouble() * 0.3;
                        final double d6 = (this.rand.nextDouble() * 2.0 - 1.0) * 0.3;
                        this.world.spawnParticle(EnumParticleTypes.CRIT, d0, d2 + 1.0, d3, d4, d5, d6, new int[0]);
                    }
                }
            }
        }
        else if (--this.field_190553_a < 0) {
            if (this.field_190553_a == -8) {
                for (final EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, this.getEntityBoundingBox().expand(0.2, 0.0, 0.2))) {
                    this.func_190551_c(entitylivingbase);
                }
            }
            if (!this.field_190554_b) {
                this.world.setEntityState(this, (byte)4);
                this.field_190554_b = true;
            }
            if (--this.field_190555_c < 0) {
                this.setDead();
            }
        }
    }
    
    private void func_190551_c(final EntityLivingBase p_190551_1_) {
        final EntityLivingBase entitylivingbase = this.func_190552_j();
        if (p_190551_1_.isEntityAlive() && !p_190551_1_.func_190530_aW() && p_190551_1_ != entitylivingbase) {
            if (entitylivingbase == null) {
                p_190551_1_.attackEntityFrom(DamageSource.magic, 6.0f);
            }
            else {
                if (entitylivingbase.isOnSameTeam(p_190551_1_)) {
                    return;
                }
                p_190551_1_.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, entitylivingbase), 6.0f);
            }
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        super.handleStatusUpdate(id);
        if (id == 4) {
            this.field_190556_d = true;
            if (!this.isSilent()) {
                this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.field_191242_bl, this.getSoundCategory(), 1.0f, this.rand.nextFloat() * 0.2f + 0.85f, false);
            }
        }
    }
    
    public float func_190550_a(final float p_190550_1_) {
        if (!this.field_190556_d) {
            return 0.0f;
        }
        final int i = this.field_190555_c - 2;
        return (i <= 0) ? 1.0f : (1.0f - (i - p_190550_1_) / 20.0f);
    }
}
