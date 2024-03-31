// 
// Decompiled by Procyon v0.6.0
// 

package org.lwjgl.opengl;

public final class ARBES31Compatibility
{
    private ARBES31Compatibility() {
    }
    
    public static void glMemoryBarrierByRegion(final int barriers) {
        GL45.glMemoryBarrierByRegion(barriers);
    }
}
