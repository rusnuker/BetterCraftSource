// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;

public class ItemHangingEntity extends Item
{
    private final Class<? extends EntityHanging> hangingEntityClass;
    
    public ItemHangingEntity(final Class<? extends EntityHanging> entityClass) {
        this.hangingEntityClass = entityClass;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer stack, final World playerIn, final BlockPos worldIn, final EnumHand pos, final EnumFacing hand, final float facing, final float hitX, final float hitY) {
        final ItemStack itemstack = stack.getHeldItem(pos);
        final BlockPos blockpos = worldIn.offset(hand);
        if (hand != EnumFacing.DOWN && hand != EnumFacing.UP && stack.canPlayerEdit(blockpos, hand, itemstack)) {
            final EntityHanging entityhanging = this.createEntity(playerIn, blockpos, hand);
            if (entityhanging != null && entityhanging.onValidSurface()) {
                if (!playerIn.isRemote) {
                    entityhanging.playPlaceSound();
                    playerIn.spawnEntityInWorld(entityhanging);
                }
                itemstack.func_190918_g(1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
    
    @Nullable
    private EntityHanging createEntity(final World worldIn, final BlockPos pos, final EnumFacing clickedSide) {
        if (this.hangingEntityClass == EntityPainting.class) {
            return new EntityPainting(worldIn, pos, clickedSide);
        }
        return (this.hangingEntityClass == EntityItemFrame.class) ? new EntityItemFrame(worldIn, pos, clickedSide) : null;
    }
}
