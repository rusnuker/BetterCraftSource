// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private final Entity indirectEntity;
    
    public EntityDamageSourceIndirect(final String damageTypeIn, final Entity source, @Nullable final Entity indirectEntityIn) {
        super(damageTypeIn, source);
        this.indirectEntity = indirectEntityIn;
    }
    
    @Nullable
    @Override
    public Entity getSourceOfDamage() {
        return this.damageSourceEntity;
    }
    
    @Nullable
    @Override
    public Entity getEntity() {
        return this.indirectEntity;
    }
    
    @Override
    public ITextComponent getDeathMessage(final EntityLivingBase entityLivingBaseIn) {
        final ITextComponent itextcomponent = (this.indirectEntity == null) ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
        final ItemStack itemstack = (this.indirectEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.indirectEntity).getHeldItemMainhand() : ItemStack.field_190927_a;
        final String s = "death.attack." + this.damageType;
        final String s2 = String.valueOf(s) + ".item";
        return (!itemstack.func_190926_b() && itemstack.hasDisplayName() && I18n.canTranslate(s2)) ? new TextComponentTranslation(s2, new Object[] { entityLivingBaseIn.getDisplayName(), itextcomponent, itemstack.getTextComponent() }) : new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName(), itextcomponent });
    }
}
