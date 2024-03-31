// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.item;

import net.minecraft.util.EnumHand;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.init.Blocks;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.ItemMap;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.EntityHanging;

public class EntityItemFrame extends EntityHanging
{
    private static final DataParameter<ItemStack> ITEM;
    private static final DataParameter<Integer> ROTATION;
    private float itemDropChance;
    
    static {
        ITEM = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.OPTIONAL_ITEM_STACK);
        ROTATION = EntityDataManager.createKey(EntityItemFrame.class, DataSerializers.VARINT);
    }
    
    public EntityItemFrame(final World worldIn) {
        super(worldIn);
        this.itemDropChance = 1.0f;
    }
    
    public EntityItemFrame(final World worldIn, final BlockPos p_i45852_2_, final EnumFacing p_i45852_3_) {
        super(worldIn, p_i45852_2_);
        this.itemDropChance = 1.0f;
        this.updateFacingWithBoundingBox(p_i45852_3_);
    }
    
    @Override
    protected void entityInit() {
        this.getDataManager().register(EntityItemFrame.ITEM, ItemStack.field_190927_a);
        this.getDataManager().register(EntityItemFrame.ROTATION, 0);
    }
    
    @Override
    public float getCollisionBorderSize() {
        return 0.0f;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!source.isExplosion() && !this.getDisplayedItem().func_190926_b()) {
            if (!this.world.isRemote) {
                this.dropItemOrSelf(source.getEntity(), false);
                this.playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0f, 1.0f);
                this.setDisplayedItem(ItemStack.field_190927_a);
            }
            return true;
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public int getWidthPixels() {
        return 12;
    }
    
    @Override
    public int getHeightPixels() {
        return 12;
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = 16.0;
        d0 = d0 * 64.0 * getRenderDistanceWeight();
        return distance < d0 * d0;
    }
    
    @Override
    public void onBroken(@Nullable final Entity brokenEntity) {
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_BREAK, 1.0f, 1.0f);
        this.dropItemOrSelf(brokenEntity, true);
    }
    
    @Override
    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_ITEMFRAME_PLACE, 1.0f, 1.0f);
    }
    
    public void dropItemOrSelf(@Nullable final Entity entityIn, final boolean p_146065_2_) {
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = this.getDisplayedItem();
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)entityIn;
                if (entityplayer.capabilities.isCreativeMode) {
                    this.removeFrameFromMap(itemstack);
                    return;
                }
            }
            if (p_146065_2_) {
                this.entityDropItem(new ItemStack(Items.ITEM_FRAME), 0.0f);
            }
            if (!itemstack.func_190926_b() && this.rand.nextFloat() < this.itemDropChance) {
                itemstack = itemstack.copy();
                this.removeFrameFromMap(itemstack);
                this.entityDropItem(itemstack, 0.0f);
            }
        }
    }
    
    private void removeFrameFromMap(final ItemStack stack) {
        if (!stack.func_190926_b()) {
            if (stack.getItem() == Items.FILLED_MAP) {
                final MapData mapdata = ((ItemMap)stack.getItem()).getMapData(stack, this.world);
                mapdata.mapDecorations.remove("frame-" + this.getEntityId());
            }
            stack.setItemFrame(null);
        }
    }
    
    public ItemStack getDisplayedItem() {
        return this.getDataManager().get(EntityItemFrame.ITEM);
    }
    
    public void setDisplayedItem(final ItemStack stack) {
        this.setDisplayedItemWithUpdate(stack, true);
    }
    
    private void setDisplayedItemWithUpdate(ItemStack stack, final boolean p_174864_2_) {
        if (!stack.func_190926_b()) {
            stack = stack.copy();
            stack.func_190920_e(1);
            stack.setItemFrame(this);
        }
        this.getDataManager().set(EntityItemFrame.ITEM, stack);
        this.getDataManager().setDirty(EntityItemFrame.ITEM);
        if (!stack.func_190926_b()) {
            this.playSound(SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, 1.0f, 1.0f);
        }
        if (p_174864_2_ && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (key.equals(EntityItemFrame.ITEM)) {
            final ItemStack itemstack = this.getDisplayedItem();
            if (!itemstack.func_190926_b() && itemstack.getItemFrame() != this) {
                itemstack.setItemFrame(this);
            }
        }
    }
    
    public int getRotation() {
        return this.getDataManager().get(EntityItemFrame.ROTATION);
    }
    
    public void setItemRotation(final int rotationIn) {
        this.setRotation(rotationIn, true);
    }
    
    private void setRotation(final int rotationIn, final boolean p_174865_2_) {
        this.getDataManager().set(EntityItemFrame.ROTATION, rotationIn % 8);
        if (p_174865_2_ && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }
    }
    
    public static void registerFixesItemFrame(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityItemFrame.class, new String[] { "Item" }));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        if (!this.getDisplayedItem().func_190926_b()) {
            compound.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
            compound.setByte("ItemRotation", (byte)this.getRotation());
            compound.setFloat("ItemDropChance", this.itemDropChance);
        }
        super.writeEntityToNBT(compound);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        final NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
        if (nbttagcompound != null && !nbttagcompound.hasNoTags()) {
            this.setDisplayedItemWithUpdate(new ItemStack(nbttagcompound), false);
            this.setRotation(compound.getByte("ItemRotation"), false);
            if (compound.hasKey("ItemDropChance", 99)) {
                this.itemDropChance = compound.getFloat("ItemDropChance");
            }
        }
        super.readEntityFromNBT(compound);
    }
    
    @Override
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand stack) {
        final ItemStack itemstack = player.getHeldItem(stack);
        if (!this.world.isRemote) {
            if (this.getDisplayedItem().func_190926_b()) {
                if (!itemstack.func_190926_b()) {
                    this.setDisplayedItem(itemstack);
                    if (!player.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                }
            }
            else {
                this.playSound(SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM, 1.0f, 1.0f);
                this.setItemRotation(this.getRotation() + 1);
            }
        }
        return true;
    }
    
    public int getAnalogOutput() {
        return this.getDisplayedItem().func_190926_b() ? 0 : (this.getRotation() % 8 + 1);
    }
}
