// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.monster;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.datasync.DataParameter;

public class EntityVex extends EntityMob
{
    protected static final DataParameter<Byte> field_190664_a;
    private EntityLiving field_190665_b;
    @Nullable
    private BlockPos field_190666_c;
    private boolean field_190667_bw;
    private int field_190668_bx;
    
    static {
        field_190664_a = EntityDataManager.createKey(EntityVex.class, DataSerializers.BYTE);
    }
    
    public EntityVex(final World p_i47280_1_) {
        super(p_i47280_1_);
        this.isImmuneToFire = true;
        this.moveHelper = new AIMoveControl(this);
        this.setSize(0.4f, 0.8f);
        this.experienceValue = 3;
    }
    
    @Override
    public void moveEntity(final MoverType x, final double p_70091_2_, final double p_70091_4_, final double p_70091_6_) {
        super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
        this.doBlockCollisions();
    }
    
    @Override
    public void onUpdate() {
        this.noClip = true;
        super.onUpdate();
        this.noClip = false;
        this.setNoGravity(true);
        if (this.field_190667_bw && --this.field_190668_bx <= 0) {
            this.field_190668_bx = 20;
            this.attackEntityFrom(DamageSource.starve, 1.0f);
        }
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new AIChargeAttack());
        this.tasks.addTask(8, new AIMoveRandom());
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[] { EntityVex.class }));
        this.targetTasks.addTask(2, new AICopyOwnerTarget(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityVex.field_190664_a, (Byte)0);
    }
    
    public static void func_190663_b(final DataFixer p_190663_0_) {
        EntityLiving.registerFixesMob(p_190663_0_, EntityVex.class);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("BoundX")) {
            this.field_190666_c = new BlockPos(compound.getInteger("BoundX"), compound.getInteger("BoundY"), compound.getInteger("BoundZ"));
        }
        if (compound.hasKey("LifeTicks")) {
            this.func_190653_a(compound.getInteger("LifeTicks"));
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.field_190666_c != null) {
            compound.setInteger("BoundX", this.field_190666_c.getX());
            compound.setInteger("BoundY", this.field_190666_c.getY());
            compound.setInteger("BoundZ", this.field_190666_c.getZ());
        }
        if (this.field_190667_bw) {
            compound.setInteger("LifeTicks", this.field_190668_bx);
        }
    }
    
    public EntityLiving func_190645_o() {
        return this.field_190665_b;
    }
    
    @Nullable
    public BlockPos func_190646_di() {
        return this.field_190666_c;
    }
    
    public void func_190651_g(@Nullable final BlockPos p_190651_1_) {
        this.field_190666_c = p_190651_1_;
    }
    
    private boolean func_190656_b(final int p_190656_1_) {
        final int i = this.dataManager.get(EntityVex.field_190664_a);
        return (i & p_190656_1_) != 0x0;
    }
    
    private void func_190660_a(final int p_190660_1_, final boolean p_190660_2_) {
        int i = this.dataManager.get(EntityVex.field_190664_a);
        if (p_190660_2_) {
            i |= p_190660_1_;
        }
        else {
            i &= ~p_190660_1_;
        }
        this.dataManager.set(EntityVex.field_190664_a, (byte)(i & 0xFF));
    }
    
    public boolean func_190647_dj() {
        return this.func_190656_b(1);
    }
    
    public void func_190648_a(final boolean p_190648_1_) {
        this.func_190660_a(1, p_190648_1_);
    }
    
    public void func_190658_a(final EntityLiving p_190658_1_) {
        this.field_190665_b = p_190658_1_;
    }
    
    public void func_190653_a(final int p_190653_1_) {
        this.field_190667_bw = true;
        this.field_190668_bx = p_190653_1_;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.field_191264_hc;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.field_191266_he;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource p_184601_1_) {
        return SoundEvents.field_191267_hf;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.field_191188_ax;
    }
    
    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }
    
    @Override
    public float getBrightness() {
        return 1.0f;
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0f);
    }
    
    class AIChargeAttack extends EntityAIBase
    {
        public AIChargeAttack() {
            this.setMutexBits(1);
        }
        
        @Override
        public boolean shouldExecute() {
            return EntityVex.this.getAttackTarget() != null && !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0 && EntityVex.this.getDistanceSqToEntity(EntityVex.this.getAttackTarget()) > 4.0;
        }
        
        @Override
        public boolean continueExecuting() {
            return EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.func_190647_dj() && EntityVex.this.getAttackTarget() != null && EntityVex.this.getAttackTarget().isEntityAlive();
        }
        
        @Override
        public void startExecuting() {
            final EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
            final Vec3d vec3d = entitylivingbase.getPositionEyes(1.0f);
            EntityVex.this.moveHelper.setMoveTo(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0);
            EntityVex.this.func_190648_a(true);
            EntityVex.this.playSound(SoundEvents.field_191265_hd, 1.0f, 1.0f);
        }
        
        @Override
        public void resetTask() {
            EntityVex.this.func_190648_a(false);
        }
        
        @Override
        public void updateTask() {
            final EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
            if (EntityVex.this.getEntityBoundingBox().intersectsWith(entitylivingbase.getEntityBoundingBox())) {
                EntityVex.this.attackEntityAsMob(entitylivingbase);
                EntityVex.this.func_190648_a(false);
            }
            else {
                final double d0 = EntityVex.this.getDistanceSqToEntity(entitylivingbase);
                if (d0 < 9.0) {
                    final Vec3d vec3d = entitylivingbase.getPositionEyes(1.0f);
                    EntityVex.this.moveHelper.setMoveTo(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 1.0);
                }
            }
        }
    }
    
    class AICopyOwnerTarget extends EntityAITarget
    {
        public AICopyOwnerTarget(final EntityCreature p_i47231_2_) {
            super(p_i47231_2_, false);
        }
        
        @Override
        public boolean shouldExecute() {
            return EntityVex.this.field_190665_b != null && EntityVex.this.field_190665_b.getAttackTarget() != null && this.isSuitableTarget(EntityVex.this.field_190665_b.getAttackTarget(), false);
        }
        
        @Override
        public void startExecuting() {
            EntityVex.this.setAttackTarget(EntityVex.this.field_190665_b.getAttackTarget());
            super.startExecuting();
        }
    }
    
    class AIMoveControl extends EntityMoveHelper
    {
        public AIMoveControl(final EntityVex p_i47230_2_) {
            super(p_i47230_2_);
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO) {
                final double d0 = this.posX - EntityVex.this.posX;
                final double d2 = this.posY - EntityVex.this.posY;
                final double d3 = this.posZ - EntityVex.this.posZ;
                double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                d4 = MathHelper.sqrt(d4);
                if (d4 < EntityVex.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = Action.WAIT;
                    final EntityVex this$0 = EntityVex.this;
                    this$0.motionX *= 0.5;
                    final EntityVex this$2 = EntityVex.this;
                    this$2.motionY *= 0.5;
                    final EntityVex this$3 = EntityVex.this;
                    this$3.motionZ *= 0.5;
                }
                else {
                    final EntityVex this$4 = EntityVex.this;
                    this$4.motionX += d0 / d4 * 0.05 * this.speed;
                    final EntityVex this$5 = EntityVex.this;
                    this$5.motionY += d2 / d4 * 0.05 * this.speed;
                    final EntityVex this$6 = EntityVex.this;
                    this$6.motionZ += d3 / d4 * 0.05 * this.speed;
                    if (EntityVex.this.getAttackTarget() == null) {
                        EntityVex.this.rotationYaw = -(float)MathHelper.atan2(EntityVex.this.motionX, EntityVex.this.motionZ) * 57.295776f;
                        EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
                    }
                    else {
                        final double d5 = EntityVex.this.getAttackTarget().posX - EntityVex.this.posX;
                        final double d6 = EntityVex.this.getAttackTarget().posZ - EntityVex.this.posZ;
                        EntityVex.this.rotationYaw = -(float)MathHelper.atan2(d5, d6) * 57.295776f;
                        EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
                    }
                }
            }
        }
    }
    
    class AIMoveRandom extends EntityAIBase
    {
        public AIMoveRandom() {
            this.setMutexBits(1);
        }
        
        @Override
        public boolean shouldExecute() {
            return !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0;
        }
        
        @Override
        public boolean continueExecuting() {
            return false;
        }
        
        @Override
        public void updateTask() {
            BlockPos blockpos = EntityVex.this.func_190646_di();
            if (blockpos == null) {
                blockpos = new BlockPos(EntityVex.this);
            }
            int i = 0;
            while (i < 3) {
                final BlockPos blockpos2 = blockpos.add(EntityVex.this.rand.nextInt(15) - 7, EntityVex.this.rand.nextInt(11) - 5, EntityVex.this.rand.nextInt(15) - 7);
                if (EntityVex.this.world.isAirBlock(blockpos2)) {
                    EntityVex.this.moveHelper.setMoveTo(blockpos2.getX() + 0.5, blockpos2.getY() + 0.5, blockpos2.getZ() + 0.5, 0.25);
                    if (EntityVex.this.getAttackTarget() == null) {
                        EntityVex.this.getLookHelper().setLookPosition(blockpos2.getX() + 0.5, blockpos2.getY() + 0.5, blockpos2.getZ() + 0.5, 180.0f, 20.0f);
                        break;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
        }
    }
}
