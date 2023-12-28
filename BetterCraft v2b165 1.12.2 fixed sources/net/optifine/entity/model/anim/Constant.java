// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

public class Constant implements IExpression
{
    private float value;
    
    public Constant(final float value) {
        this.value = value;
    }
    
    @Override
    public float eval() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(this.value).toString();
    }
}
