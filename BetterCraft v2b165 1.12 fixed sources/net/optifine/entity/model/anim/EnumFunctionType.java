// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import optifine.MathUtils;
import net.minecraft.util.math.MathHelper;
import optifine.Config;

public enum EnumFunctionType
{
    PLUS("PLUS", 0, "+", 2, 0), 
    MINUS("MINUS", 1, "-", 2, 0), 
    MUL("MUL", 2, "*", 2, 1), 
    DIV("DIV", 3, "/", 2, 1), 
    MOD("MOD", 4, "%", 2, 1), 
    NEG("NEG", 5, "neg", 1), 
    PI("PI", 6, "pi", 0), 
    SIN("SIN", 7, "sin", 1), 
    COS("COS", 8, "cos", 1), 
    TAN("TAN", 9, "tan", 1), 
    ATAN("ATAN", 10, "atan", 1), 
    ATAN2("ATAN2", 11, "atan2", 2), 
    TORAD("TORAD", 12, "torad", 1), 
    TODEG("TODEG", 13, "todeg", 1), 
    MIN("MIN", 14, "min", 2), 
    MAX("MAX", 15, "max", 2), 
    CLAMP("CLAMP", 16, "clamp", 3), 
    ABS("ABS", 17, "abs", 1), 
    FLOOR("FLOOR", 18, "floor", 1), 
    CEIL("CEIL", 19, "ceil", 1), 
    FRAC("FRAC", 20, "frac", 1), 
    ROUND("ROUND", 21, "round", 1), 
    SQRT("SQRT", 22, "sqrt", 1), 
    FMOD("FMOD", 23, "fmod", 2), 
    TIME("TIME", 24, "time", 0);
    
    private String name;
    private int countArguments;
    private int precedence;
    public static EnumFunctionType[] VALUES;
    
    static {
        EnumFunctionType.VALUES = values();
    }
    
    private EnumFunctionType(final String s, final int n, final String name, final int countArguments) {
        this.name = name;
        this.countArguments = countArguments;
    }
    
    private EnumFunctionType(final String s, final int n, final String name, final int countArguments, final int precedence) {
        this.name = name;
        this.countArguments = countArguments;
        this.precedence = precedence;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getCountArguments() {
        return this.countArguments;
    }
    
    public int getPrecedence() {
        return this.precedence;
    }
    
    public float eval(final IExpression[] arguments) {
        if (arguments.length != this.countArguments) {
            Config.warn("Invalid number of arguments, function: " + this + ", arguments: " + arguments.length + ", should be: " + this.countArguments);
            return 0.0f;
        }
        switch (this) {
            case PLUS: {
                return arguments[0].eval() + arguments[1].eval();
            }
            case MINUS: {
                return arguments[0].eval() - arguments[1].eval();
            }
            case MUL: {
                return arguments[0].eval() * arguments[1].eval();
            }
            case DIV: {
                return arguments[0].eval() / arguments[1].eval();
            }
            case MOD: {
                final float f = arguments[0].eval();
                final float f2 = arguments[1].eval();
                return f - f2 * (int)(f / f2);
            }
            case NEG: {
                return -arguments[0].eval();
            }
            case PI: {
                return 3.1415927f;
            }
            case SIN: {
                return MathHelper.sin(arguments[0].eval());
            }
            case COS: {
                return MathHelper.cos(arguments[0].eval());
            }
            case TAN: {
                return (float)Math.tan(arguments[0].eval());
            }
            case ATAN: {
                return (float)Math.atan(arguments[0].eval());
            }
            case ATAN2: {
                return (float)MathHelper.atan2(arguments[0].eval(), arguments[1].eval());
            }
            case TORAD: {
                return MathUtils.toRad(arguments[0].eval());
            }
            case TODEG: {
                return MathUtils.toDeg(arguments[0].eval());
            }
            case MIN: {
                return Math.min(arguments[0].eval(), arguments[1].eval());
            }
            case MAX: {
                return Math.max(arguments[0].eval(), arguments[1].eval());
            }
            case CLAMP: {
                return MathHelper.clamp(arguments[0].eval(), arguments[1].eval(), arguments[2].eval());
            }
            case ABS: {
                return MathHelper.abs(arguments[0].eval());
            }
            case FLOOR: {
                return (float)MathHelper.floor(arguments[0].eval());
            }
            case CEIL: {
                return (float)MathHelper.ceil(arguments[0].eval());
            }
            case FRAC: {
                return (float)MathHelper.frac(arguments[0].eval());
            }
            case ROUND: {
                return (float)Math.round(arguments[0].eval());
            }
            case SQRT: {
                return MathHelper.sqrt(arguments[0].eval());
            }
            case FMOD: {
                final float f3 = arguments[0].eval();
                final float f4 = arguments[1].eval();
                return f3 - f4 * MathHelper.floor(f3 / f4);
            }
            case TIME: {
                final Minecraft minecraft = Minecraft.getMinecraft();
                final World world = minecraft.world;
                if (world == null) {
                    return 0.0f;
                }
                return world.getTotalWorldTime() % 24000L + minecraft.getRenderPartialTicks();
            }
            default: {
                Config.warn("Unknown function type: " + this);
                return 0.0f;
            }
        }
    }
    
    public static EnumFunctionType parse(final String str) {
        for (int i = 0; i < EnumFunctionType.VALUES.length; ++i) {
            final EnumFunctionType enumfunctiontype = EnumFunctionType.VALUES[i];
            if (enumfunctiontype.getName().equals(str)) {
                return enumfunctiontype;
            }
        }
        return null;
    }
}
