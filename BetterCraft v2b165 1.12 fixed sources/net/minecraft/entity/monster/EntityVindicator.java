// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.monster;

import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;

public class EntityVindicator extends AbstractIllager
{
    private boolean field_190643_b;
    private static final Predicate<Entity> field_190644_c;
    
    static {
        field_190644_c = new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).func_190631_cK();
            }
        };
    }
    
    public EntityVindicator(final World p_i47279_1_) {
        super(p_i47279_1_);
        this.setSize(0.6f, 1.95f);
    }
    
    public static void func_190641_b(final DataFixer p_190641_0_) {
        EntityLiving.registerFixesMob(p_190641_0_, EntityVindicator.class);
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0, false));
        this.tasks.addTask(8, new EntityAIWander(this, 0.6));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[] { EntityVindicator.class }));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityVillager.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityIronGolem.class, true));
        this.targetTasks.addTask(4, new AIJohnnyAttack(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3499999940395355);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
    }
    
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.field_191186_av;
    }
    
    public boolean func_190639_o() {
        return this.func_193078_a(1);
    }
    
    public void func_190636_a(final boolean p_190636_1_) {
        this.func_193079_a(1, p_190636_1_);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.field_190643_b) {
            compound.setBoolean("Johnny", true);
        }
    }
    
    @Override
    public IllagerArmPose func_193077_p() {
        return this.func_190639_o() ? IllagerArmPose.ATTACKING : IllagerArmPose.CROSSED;
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Johnny", 99)) {
            this.field_190643_b = compound.getBoolean("Johnny");
        }
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        final IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        return ientitylivingdata;
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.func_190636_a(this.getAttackTarget() != null);
    }
    
    @Override
    public boolean isOnSameTeam(final Entity entityIn) {
        return super.isOnSameTeam(entityIn) || (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER && (this.getTeam() == null && entityIn.getTeam() == null));
    }
    
    @Override
    public void setCustomNameTag(final String name) {
        super.setCustomNameTag(name);
        if (!this.field_190643_b && "Johnny".equals(name)) {
            this.field_190643_b = true;
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.field_191268_hm;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.field_191269_hn;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource p_184601_1_) {
        return SoundEvents.field_191270_ho;
    }
    
    static class AIJohnnyAttack extends EntityAINearestAttackableTarget<EntityLivingBase>
    {
        public AIJohnnyAttack(final EntityVindicator p_i47345_1_) {
            super(p_i47345_1_, EntityLivingBase.class, 0, true, true, EntityVindicator.field_190644_c);
        }
        
        @Override
        public boolean shouldExecute() {
            return ((EntityVindicator)this.taskOwner).field_190643_b && super.shouldExecute();
        }
    }
}
