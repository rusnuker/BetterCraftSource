// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

public class Function implements IExpression
{
    private EnumFunctionType enumFunction;
    private IExpression[] arguments;
    
    public Function(final EnumFunctionType enumFunction, final IExpression[] arguments) {
        this.enumFunction = enumFunction;
        this.arguments = arguments;
    }
    
    @Override
    public float eval() {
        return this.enumFunction.eval(this.arguments);
    }
    
    @Override
    public String toString() {
        return this.enumFunction + "()";
    }
}
