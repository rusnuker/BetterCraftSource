// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.math;

import java.util.UUID;
import java.util.Random;

public class MathHelper
{
    public static final float SQRT_2;
    private static final int SIN_BITS = 12;
    private static final int SIN_MASK = 4095;
    private static final int SIN_COUNT = 4096;
    public static final float PI = 3.1415927f;
    public static final float PI2 = 6.2831855f;
    public static final float PId2 = 1.5707964f;
    private static final float radFull = 6.2831855f;
    private static final float degFull = 360.0f;
    private static final float radToIndex = 651.8986f;
    private static final float degToIndex = 11.377778f;
    public static final float deg2Rad = 0.017453292f;
    private static final float[] SIN_TABLE_FAST;
    public static boolean fastMath;
    private static final float[] SIN_TABLE;
    private static final Random RANDOM;
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    private static final double FRAC_BIAS;
    private static final double[] ASINE_TAB;
    private static final double[] COS_TAB;
    
    static {
        SQRT_2 = sqrt(2.0f);
        SIN_TABLE_FAST = new float[4096];
        MathHelper.fastMath = false;
        SIN_TABLE = new float[65536];
        RANDOM = new Random();
        for (int i = 0; i < 65536; ++i) {
            MathHelper.SIN_TABLE[i] = (float)Math.sin(i * 3.141592653589793 * 2.0 / 65536.0);
        }
        for (int j = 0; j < 4096; ++j) {
            MathHelper.SIN_TABLE_FAST[j] = (float)Math.sin((j + 0.5f) / 4096.0f * 6.2831855f);
        }
        for (int k = 0; k < 360; k += 90) {
            MathHelper.SIN_TABLE_FAST[(int)(k * 11.377778f) & 0xFFF] = (float)Math.sin(k * 0.017453292f);
        }
        MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
        FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
        ASINE_TAB = new double[257];
        COS_TAB = new double[257];
        for (int l = 0; l < 257; ++l) {
            final double d0 = l / 256.0;
            final double d2 = Math.asin(d0);
            MathHelper.COS_TAB[l] = Math.cos(d2);
            MathHelper.ASINE_TAB[l] = d2;
        }
    }
    
    public static float sin(final float value) {
        return MathHelper.fastMath ? MathHelper.SIN_TABLE_FAST[(int)(value * 651.8986f) & 0xFFF] : MathHelper.SIN_TABLE[(int)(value * 10430.378f) & 0xFFFF];
    }
    
    public static float cos(final float value) {
        return MathHelper.fastMath ? MathHelper.SIN_TABLE_FAST[(int)((value + 1.5707964f) * 651.8986f) & 0xFFF] : MathHelper.SIN_TABLE[(int)(value * 10430.378f + 16384.0f) & 0xFFFF];
    }
    
    public static float sqrt(final float value) {
        return (float)Math.sqrt(value);
    }
    
    public static float sqrt(final double value) {
        return (float)Math.sqrt(value);
    }
    
    public static int floor(final float value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    public static int fastFloor(final double value) {
        return (int)(value + 1024.0) - 1024;
    }
    
    public static int floor(final double value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    public static long lFloor(final double value) {
        final long i = (long)value;
        return (value < i) ? (i - 1L) : i;
    }
    
    public static int absFloor(final double value) {
        return (int)((value >= 0.0) ? value : (-value + 1.0));
    }
    
    public static float abs(final float value) {
        return (value >= 0.0f) ? value : (-value);
    }
    
    public static int abs(final int value) {
        return (value >= 0) ? value : (-value);
    }
    
    public static int ceil(final float value) {
        final int i = (int)value;
        return (value > i) ? (i + 1) : i;
    }
    
    public static int ceil(final double value) {
        final int i = (int)value;
        return (value > i) ? (i + 1) : i;
    }
    
    public static int clamp(final int num, final int min, final int max) {
        if (num < min) {
            return min;
        }
        return (num > max) ? max : num;
    }
    
    public static float clamp(final float num, final float min, final float max) {
        if (num < min) {
            return min;
        }
        return (num > max) ? max : num;
    }
    
    public static double clamp(final double num, final double min, final double max) {
        if (num < min) {
            return min;
        }
        return (num > max) ? max : num;
    }
    
    public static double clampedLerp(final double lowerBnd, final double upperBnd, final double slide) {
        if (slide < 0.0) {
            return lowerBnd;
        }
        return (slide > 1.0) ? upperBnd : (lowerBnd + (upperBnd - lowerBnd) * slide);
    }
    
    public static double absMax(double p_76132_0_, double p_76132_2_) {
        if (p_76132_0_ < 0.0) {
            p_76132_0_ = -p_76132_0_;
        }
        if (p_76132_2_ < 0.0) {
            p_76132_2_ = -p_76132_2_;
        }
        return (p_76132_0_ > p_76132_2_) ? p_76132_0_ : p_76132_2_;
    }
    
    public static int intFloorDiv(final int p_76137_0_, final int p_76137_1_) {
        return (p_76137_0_ < 0) ? (-((-p_76137_0_ - 1) / p_76137_1_) - 1) : (p_76137_0_ / p_76137_1_);
    }
    
    public static int getInt(final Random random, final int minimum, final int maximum) {
        return (minimum >= maximum) ? minimum : (random.nextInt(maximum - minimum + 1) + minimum);
    }
    
    public static float nextFloat(final Random random, final float minimum, final float maximum) {
        return (minimum >= maximum) ? minimum : (random.nextFloat() * (maximum - minimum) + minimum);
    }
    
    public static double nextDouble(final Random random, final double minimum, final double maximum) {
        return (minimum >= maximum) ? minimum : (random.nextDouble() * (maximum - minimum) + minimum);
    }
    
    public static double average(final long[] values) {
        long i = 0L;
        for (final long j : values) {
            i += j;
        }
        return i / (double)values.length;
    }
    
    public static boolean epsilonEquals(final float p_180185_0_, final float p_180185_1_) {
        return abs(p_180185_1_ - p_180185_0_) < 1.0E-5f;
    }
    
    public static int normalizeAngle(final int p_180184_0_, final int p_180184_1_) {
        return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
    }
    
    public static float positiveModulo(final float numerator, final float denominator) {
        return (numerator % denominator + denominator) % denominator;
    }
    
    public static double func_191273_b(final double p_191273_0_, final double p_191273_2_) {
        return (p_191273_0_ % p_191273_2_ + p_191273_2_) % p_191273_2_;
    }
    
    public static float wrapDegrees(float value) {
        value %= 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }
    
    public static double wrapDegrees(double value) {
        value %= 360.0;
        if (value >= 180.0) {
            value -= 360.0;
        }
        if (value < -180.0) {
            value += 360.0;
        }
        return value;
    }
    
    public static int clampAngle(int angle) {
        angle %= 360;
        if (angle >= 180) {
            angle -= 360;
        }
        if (angle < -180) {
            angle += 360;
        }
        return angle;
    }
    
    public static int getInt(final String value, final int defaultValue) {
        try {
            return Integer.parseInt(value);
        }
        catch (final Throwable var3) {
            return defaultValue;
        }
    }
    
    public static int getInt(final String value, final int defaultValue, final int max) {
        return Math.max(max, getInt(value, defaultValue));
    }
    
    public static double getDouble(final String value, final double defaultValue) {
        try {
            return Double.parseDouble(value);
        }
        catch (final Throwable var4) {
            return defaultValue;
        }
    }
    
    public static double getDouble(final String value, final double defaultValue, final double max) {
        return Math.max(max, getDouble(value, defaultValue));
    }
    
    public static int smallestEncompassingPowerOfTwo(final int value) {
        int i = value - 1;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return i + 1;
    }
    
    private static boolean isPowerOfTwo(final int value) {
        return value != 0 && (value & value - 1) == 0x0;
    }
    
    public static int log2DeBruijn(int value) {
        value = (isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value));
        return MathHelper.MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(value * 125613361L >> 27) & 0x1F];
    }
    
    public static int log2(final int value) {
        return log2DeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
    }
    
    public static int roundUp(final int number, int interval) {
        if (interval == 0) {
            return 0;
        }
        if (number == 0) {
            return interval;
        }
        if (number < 0) {
            interval *= -1;
        }
        final int i = number % interval;
        return (i == 0) ? number : (number + interval - i);
    }
    
    public static int rgb(final float rIn, final float gIn, final float bIn) {
        return rgb(floor(rIn * 255.0f), floor(gIn * 255.0f), floor(bIn * 255.0f));
    }
    
    public static int rgb(final int rIn, final int gIn, final int bIn) {
        int i = (rIn << 8) + gIn;
        i = (i << 8) + bIn;
        return i;
    }
    
    public static int multiplyColor(final int p_180188_0_, final int p_180188_1_) {
        final int i = (p_180188_0_ & 0xFF0000) >> 16;
        final int j = (p_180188_1_ & 0xFF0000) >> 16;
        final int k = (p_180188_0_ & 0xFF00) >> 8;
        final int l = (p_180188_1_ & 0xFF00) >> 8;
        final int i2 = (p_180188_0_ & 0xFF) >> 0;
        final int j2 = (p_180188_1_ & 0xFF) >> 0;
        final int k2 = (int)(i * (float)j / 255.0f);
        final int l2 = (int)(k * (float)l / 255.0f);
        final int i3 = (int)(i2 * (float)j2 / 255.0f);
        return (p_180188_0_ & 0xFF000000) | k2 << 16 | l2 << 8 | i3;
    }
    
    public static double frac(final double number) {
        return number - Math.floor(number);
    }
    
    public static long getPositionRandom(final Vec3i pos) {
        return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static long getCoordinateRandom(final int x, final int y, final int z) {
        long i = (long)(x * 3129871) ^ z * 116129781L ^ (long)y;
        i = i * i * 42317861L + i * 11L;
        return i;
    }
    
    public static UUID getRandomUUID(final Random rand) {
        final long i = (rand.nextLong() & 0xFFFFFFFFFFFF0FFFL) | 0x4000L;
        final long j = (rand.nextLong() & 0x3FFFFFFFFFFFFFFFL) | Long.MIN_VALUE;
        return new UUID(i, j);
    }
    
    public static UUID getRandomUUID() {
        return getRandomUUID(MathHelper.RANDOM);
    }
    
    public static double pct(final double p_181160_0_, final double p_181160_2_, final double p_181160_4_) {
        return (p_181160_0_ - p_181160_2_) / (p_181160_4_ - p_181160_2_);
    }
    
    public static double atan2(double p_181159_0_, double p_181159_2_) {
        final double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;
        if (Double.isNaN(d0)) {
            return Double.NaN;
        }
        final boolean flag = p_181159_0_ < 0.0;
        if (flag) {
            p_181159_0_ = -p_181159_0_;
        }
        final boolean flag2 = p_181159_2_ < 0.0;
        if (flag2) {
            p_181159_2_ = -p_181159_2_;
        }
        final boolean flag3 = p_181159_0_ > p_181159_2_;
        if (flag3) {
            final double d2 = p_181159_2_;
            p_181159_2_ = p_181159_0_;
            p_181159_0_ = d2;
        }
        final double d3 = fastInvSqrt(d0);
        p_181159_2_ *= d3;
        p_181159_0_ *= d3;
        final double d4 = MathHelper.FRAC_BIAS + p_181159_0_;
        final int i = (int)Double.doubleToRawLongBits(d4);
        final double d5 = MathHelper.ASINE_TAB[i];
        final double d6 = MathHelper.COS_TAB[i];
        final double d7 = d4 - MathHelper.FRAC_BIAS;
        final double d8 = p_181159_0_ * d6 - p_181159_2_ * d7;
        final double d9 = (6.0 + d8 * d8) * d8 * 0.16666666666666666;
        double d10 = d5 + d9;
        if (flag3) {
            d10 = 1.5707963267948966 - d10;
        }
        if (flag2) {
            d10 = 3.141592653589793 - d10;
        }
        if (flag) {
            d10 = -d10;
        }
        return d10;
    }
    
    public static double fastInvSqrt(double p_181161_0_) {
        final double d0 = 0.5 * p_181161_0_;
        long i = Double.doubleToRawLongBits(p_181161_0_);
        i = 6910469410427058090L - (i >> 1);
        p_181161_0_ = Double.longBitsToDouble(i);
        p_181161_0_ *= 1.5 - d0 * p_181161_0_ * p_181161_0_;
        return p_181161_0_;
    }
    
    public static int hsvToRGB(final float hue, final float saturation, final float value) {
        final int i = (int)(hue * 6.0f) % 6;
        final float f = hue * 6.0f - i;
        final float f2 = value * (1.0f - saturation);
        final float f3 = value * (1.0f - f * saturation);
        final float f4 = value * (1.0f - (1.0f - f) * saturation);
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        switch (i) {
            case 0: {
                f5 = value;
                f6 = f4;
                f7 = f2;
                break;
            }
            case 1: {
                f5 = f3;
                f6 = value;
                f7 = f2;
                break;
            }
            case 2: {
                f5 = f2;
                f6 = value;
                f7 = f4;
                break;
            }
            case 3: {
                f5 = f2;
                f6 = f3;
                f7 = value;
                break;
            }
            case 4: {
                f5 = f4;
                f6 = f2;
                f7 = value;
                break;
            }
            case 5: {
                f5 = value;
                f6 = f2;
                f7 = f3;
                break;
            }
            default: {
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
            }
        }
        final int j = clamp((int)(f5 * 255.0f), 0, 255);
        final int k = clamp((int)(f6 * 255.0f), 0, 255);
        final int l = clamp((int)(f7 * 255.0f), 0, 255);
        return j << 16 | k << 8 | l;
    }
    
    public static int hash(int p_188208_0_) {
        p_188208_0_ ^= p_188208_0_ >>> 16;
        p_188208_0_ *= -2048144789;
        p_188208_0_ ^= p_188208_0_ >>> 13;
        p_188208_0_ *= -1028477387;
        p_188208_0_ ^= p_188208_0_ >>> 16;
        return p_188208_0_;
    }
}
