// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

public class ColorizerGrass
{
    private static int[] grassBuffer;
    
    static {
        ColorizerGrass.grassBuffer = new int[65536];
    }
    
    public static void setGrassBiomeColorizer(final int[] grassBufferIn) {
        ColorizerGrass.grassBuffer = grassBufferIn;
    }
    
    public static int getGrassColor(final double temperature, double humidity) {
        humidity *= temperature;
        final int i = (int)((1.0 - temperature) * 255.0);
        final int j = (int)((1.0 - humidity) * 255.0);
        final int k = j << 8 | i;
        return (k > ColorizerGrass.grassBuffer.length) ? -65281 : ColorizerGrass.grassBuffer[k];
    }
}
