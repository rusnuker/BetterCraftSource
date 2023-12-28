// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import java.util.Random;
import net.minecraft.util.math.Vec3d;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityCreature;

public class EntityAIFleeSun extends EntityAIBase
{
    private final EntityCreature theCreature;
    private double shelterX;
    private double shelterY;
    private double shelterZ;
    private final double movementSpeed;
    private final World theWorld;
    
    public EntityAIFleeSun(final EntityCreature theCreatureIn, final double movementSpeedIn) {
        this.theCreature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.theWorld = theCreatureIn.world;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theWorld.isDaytime()) {
            return false;
        }
        if (!this.theCreature.isBurning()) {
            return false;
        }
        if (!this.theWorld.canSeeSky(new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ))) {
            return false;
        }
        if (!this.theCreature.getItemStackFromSlot(EntityEquipmentSlot.HEAD).func_190926_b()) {
            return false;
        }
        final Vec3d vec3d = this.findPossibleShelter();
        if (vec3d == null) {
            return false;
        }
        this.shelterX = vec3d.xCoord;
        this.shelterY = vec3d.yCoord;
        this.shelterZ = vec3d.zCoord;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.theCreature.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }
    
    @Nullable
    private Vec3d findPossibleShelter() {
        final Random random = this.theCreature.getRNG();
        final BlockPos blockpos = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);
        for (int i = 0; i < 10; ++i) {
            final BlockPos blockpos2 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (!this.theWorld.canSeeSky(blockpos2) && this.theCreature.getBlockPathWeight(blockpos2) < 0.0f) {
                return new Vec3d(blockpos2.getX(), blockpos2.getY(), blockpos2.getZ());
            }
        }
        return null;
    }
}
