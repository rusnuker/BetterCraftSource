// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.multiplayer.PlayerControllerMP;

public class PlayerControllerOF extends PlayerControllerMP
{
    private boolean acting;
    private BlockPos lastClickBlockPos;
    private Entity lastClickEntity;
    
    public PlayerControllerOF(final Minecraft p_i72_1_, final NetHandlerPlayClient p_i72_2_) {
        super(p_i72_1_, p_i72_2_);
        this.acting = false;
        this.lastClickBlockPos = null;
        this.lastClickEntity = null;
    }
    
    @Override
    public boolean clickBlock(final BlockPos loc, final EnumFacing face) {
        this.acting = true;
        this.lastClickBlockPos = loc;
        final boolean flag = super.clickBlock(loc, face);
        this.acting = false;
        return flag;
    }
    
    @Override
    public boolean onPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing) {
        this.acting = true;
        this.lastClickBlockPos = posBlock;
        final boolean flag = super.onPlayerDamageBlock(posBlock, directionFacing);
        this.acting = false;
        return flag;
    }
    
    @Override
    public EnumActionResult processRightClick(final EntityPlayer player, final World worldIn, final EnumHand stack) {
        this.acting = true;
        final EnumActionResult enumactionresult = super.processRightClick(player, worldIn, stack);
        this.acting = false;
        return enumactionresult;
    }
    
    @Override
    public EnumActionResult processRightClickBlock(final EntityPlayerSP player, final WorldClient worldIn, final BlockPos stack, final EnumFacing pos, final Vec3d facing, final EnumHand vec) {
        this.acting = true;
        this.lastClickBlockPos = stack;
        final EnumActionResult enumactionresult = super.processRightClickBlock(player, worldIn, stack, pos, facing, vec);
        this.acting = false;
        return enumactionresult;
    }
    
    @Override
    public EnumActionResult interactWithEntity(final EntityPlayer player, final Entity target, final EnumHand heldItem) {
        this.lastClickEntity = target;
        return super.interactWithEntity(player, target, heldItem);
    }
    
    @Override
    public EnumActionResult interactWithEntity(final EntityPlayer player, final Entity target, final RayTraceResult raytrace, final EnumHand heldItem) {
        this.lastClickEntity = target;
        return super.interactWithEntity(player, target, raytrace, heldItem);
    }
    
    public boolean isActing() {
        return this.acting;
    }
    
    public BlockPos getLastClickBlockPos() {
        return this.lastClickBlockPos;
    }
    
    public Entity getLastClickEntity() {
        return this.lastClickEntity;
    }
}
