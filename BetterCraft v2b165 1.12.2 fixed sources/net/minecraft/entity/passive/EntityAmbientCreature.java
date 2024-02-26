// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public abstract class EntityAmbientCreature extends EntityLiving implements IAnimals
{
    public EntityAmbientCreature(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public boolean canBeLeashedTo(final EntityPlayer player) {
        return false;
    }
}
