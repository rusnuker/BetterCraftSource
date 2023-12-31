// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity;

import net.minecraft.util.math.MathHelper;

public class EntityBodyHelper
{
    private final EntityLivingBase theLiving;
    private int rotationTickCounter;
    private float prevRenderYawHead;
    
    public EntityBodyHelper(final EntityLivingBase livingIn) {
        this.theLiving = livingIn;
    }
    
    public void updateRenderAngles() {
        final double d0 = this.theLiving.posX - this.theLiving.prevPosX;
        final double d2 = this.theLiving.posZ - this.theLiving.prevPosZ;
        if (d0 * d0 + d2 * d2 > 2.500000277905201E-7) {
            this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
            this.theLiving.rotationYawHead = this.computeAngleWithBound(this.theLiving.renderYawOffset, this.theLiving.rotationYawHead, 75.0f);
            this.prevRenderYawHead = this.theLiving.rotationYawHead;
            this.rotationTickCounter = 0;
        }
        else if (this.theLiving.getPassengers().isEmpty() || !(this.theLiving.getPassengers().get(0) instanceof EntityLiving)) {
            float f = 75.0f;
            if (Math.abs(this.theLiving.rotationYawHead - this.prevRenderYawHead) > 15.0f) {
                this.rotationTickCounter = 0;
                this.prevRenderYawHead = this.theLiving.rotationYawHead;
            }
            else {
                ++this.rotationTickCounter;
                final int i = 10;
                if (this.rotationTickCounter > 10) {
                    f = Math.max(1.0f - (this.rotationTickCounter - 10) / 10.0f, 0.0f) * 75.0f;
                }
            }
            this.theLiving.renderYawOffset = this.computeAngleWithBound(this.theLiving.rotationYawHead, this.theLiving.renderYawOffset, f);
        }
    }
    
    private float computeAngleWithBound(final float p_75665_1_, final float p_75665_2_, final float p_75665_3_) {
        float f = MathHelper.wrapDegrees(p_75665_1_ - p_75665_2_);
        if (f < -p_75665_3_) {
            f = -p_75665_3_;
        }
        if (f >= p_75665_3_) {
            f = p_75665_3_;
        }
        return p_75665_1_ - f;
    }
}
