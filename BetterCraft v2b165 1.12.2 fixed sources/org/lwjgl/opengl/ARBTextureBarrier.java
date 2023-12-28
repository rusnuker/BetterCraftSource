// 
// Decompiled by Procyon v0.6.0
// 

package org.lwjgl.opengl;

public final class ARBTextureBarrier
{
    private ARBTextureBarrier() {
    }
    
    public static void glTextureBarrier() {
        GL45.glTextureBarrier();
    }
}
