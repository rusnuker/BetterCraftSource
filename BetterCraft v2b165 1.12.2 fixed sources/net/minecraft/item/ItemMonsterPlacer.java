// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.NonNullList;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.stats.StatList;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ActionResult;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.creativetab.CreativeTabs;

public class ItemMonsterPlacer extends Item
{
    public ItemMonsterPlacer() {
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        String s = new StringBuilder().append(I18n.translateToLocal(String.valueOf(this.getUnlocalizedName()) + ".name")).toString().trim();
        final String s2 = EntityList.func_191302_a(func_190908_h(stack));
        if (s2 != null) {
            s = String.valueOf(s) + " " + I18n.translateToLocal("entity." + s2 + ".name");
        }
        return s;
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final ItemStack itemstack = stack.getHeldItem(pos);
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (!stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate = playerIn.getBlockState(worldIn);
        final Block block = iblockstate.getBlock();
        if (block == Blocks.MOB_SPAWNER) {
            final TileEntity tileentity = playerIn.getTileEntity(worldIn);
            if (tileentity instanceof TileEntityMobSpawner) {
                final MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
                mobspawnerbaselogic.func_190894_a(func_190908_h(itemstack));
                tileentity.markDirty();
                playerIn.notifyBlockUpdate(worldIn, iblockstate, iblockstate, 3);
                if (!stack.capabilities.isCreativeMode) {
                    itemstack.func_190918_g(1);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        final BlockPos blockpos = worldIn.offset(hand);
        final double d0 = this.func_190909_a(playerIn, blockpos);
        final Entity entity = spawnCreature(playerIn, func_190908_h(itemstack), blockpos.getX() + 0.5, blockpos.getY() + d0, blockpos.getZ() + 0.5);
        if (entity != null) {
            if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
                entity.setCustomNameTag(itemstack.getDisplayName());
            }
            applyItemEntityDataToEntity(playerIn, stack, itemstack, entity);
            if (!stack.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }
    
    protected double func_190909_a(final World p_190909_1_, final BlockPos p_190909_2_) {
        final AxisAlignedBB axisalignedbb = new AxisAlignedBB(p_190909_2_).addCoord(0.0, -1.0, 0.0);
        final List<AxisAlignedBB> list = p_190909_1_.getCollisionBoxes(null, axisalignedbb);
        if (list.isEmpty()) {
            return 0.0;
        }
        double d0 = axisalignedbb.minY;
        for (final AxisAlignedBB axisalignedbb2 : list) {
            d0 = Math.max(axisalignedbb2.maxY, d0);
        }
        return d0 - p_190909_2_.getY();
    }
    
    public static void applyItemEntityDataToEntity(final World entityWorld, @Nullable final EntityPlayer player, final ItemStack stack, @Nullable final Entity targetEntity) {
        final MinecraftServer minecraftserver = entityWorld.getMinecraftServer();
        if (minecraftserver != null && targetEntity != null) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10)) {
                if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile()))) {
                    return;
                }
                final NBTTagCompound nbttagcompound2 = targetEntity.writeToNBT(new NBTTagCompound());
                final UUID uuid = targetEntity.getUniqueID();
                nbttagcompound2.merge(nbttagcompound.getCompoundTag("EntityTag"));
                targetEntity.setUniqueId(uuid);
                targetEntity.readFromNBT(nbttagcompound2);
            }
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World itemStackIn, final EntityPlayer worldIn, final EnumHand playerIn) {
        final ItemStack itemstack = worldIn.getHeldItem(playerIn);
        if (itemStackIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        final RayTraceResult raytraceresult = this.rayTrace(itemStackIn, worldIn, true);
        if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        final BlockPos blockpos = raytraceresult.getBlockPos();
        if (!(itemStackIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid)) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (!itemStackIn.isBlockModifiable(worldIn, blockpos) || !worldIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack)) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        final Entity entity = spawnCreature(itemStackIn, func_190908_h(itemstack), blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
        if (entity == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
            entity.setCustomNameTag(itemstack.getDisplayName());
        }
        applyItemEntityDataToEntity(itemStackIn, worldIn, itemstack, entity);
        if (!worldIn.capabilities.isCreativeMode) {
            itemstack.func_190918_g(1);
        }
        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    
    @Nullable
    public static Entity spawnCreature(final World worldIn, @Nullable final ResourceLocation entityID, final double x, final double y, final double z) {
        if (entityID != null && EntityList.ENTITY_EGGS.containsKey(entityID)) {
            Entity entity = null;
            for (int i = 0; i < 1; ++i) {
                entity = EntityList.createEntityByIDFromName(entityID, worldIn);
                if (entity instanceof EntityLiving) {
                    final EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0f), 0.0f);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
                    worldIn.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }
            return entity;
        }
        return null;
    }
    
    @Override
    public void getSubItems(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            for (final EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.ENTITY_EGGS.values()) {
                final ItemStack itemstack = new ItemStack(this, 1);
                applyEntityIdToItemStack(itemstack, entitylist$entityegginfo.spawnedID);
                tab.add(itemstack);
            }
        }
    }
    
    public static void applyEntityIdToItemStack(final ItemStack stack, final ResourceLocation entityId) {
        final NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
        nbttagcompound2.setString("id", entityId.toString());
        nbttagcompound.setTag("EntityTag", nbttagcompound2);
        stack.setTagCompound(nbttagcompound);
    }
    
    @Nullable
    public static ResourceLocation func_190908_h(final ItemStack p_190908_0_) {
        final NBTTagCompound nbttagcompound = p_190908_0_.getTagCompound();
        if (nbttagcompound == null) {
            return null;
        }
        if (!nbttagcompound.hasKey("EntityTag", 10)) {
            return null;
        }
        final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("EntityTag");
        if (!nbttagcompound2.hasKey("id", 8)) {
            return null;
        }
        final String s = nbttagcompound2.getString("id");
        final ResourceLocation resourcelocation = new ResourceLocation(s);
        if (!s.contains(":")) {
            nbttagcompound2.setString("id", resourcelocation.toString());
        }
        return resourcelocation;
    }
}
