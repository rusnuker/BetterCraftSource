// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity;

import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTTagCompound;

public class MultiPartEntityPart extends Entity
{
    public final IEntityMultiPart entityDragonObj;
    public final String partName;
    
    public MultiPartEntityPart(final IEntityMultiPart parent, final String partName, final float base, final float sizeHeight) {
        super(parent.getWorld());
        this.setSize(base, sizeHeight);
        this.entityDragonObj = parent;
        this.partName = partName;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return !this.isEntityInvulnerable(source) && this.entityDragonObj.attackEntityFromPart(this, source, amount);
    }
    
    @Override
    public boolean isEntityEqual(final Entity entityIn) {
        return this == entityIn || this.entityDragonObj == entityIn;
    }
}
