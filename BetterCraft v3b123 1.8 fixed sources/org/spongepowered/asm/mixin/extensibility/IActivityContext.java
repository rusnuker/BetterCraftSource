// 
// Decompiled by Procyon v0.6.0
// 

package org.spongepowered.asm.mixin.extensibility;

public interface IActivityContext
{
    String toString(final String p0);
    
    IActivity begin(final String p0, final Object... p1);
    
    IActivity begin(final String p0);
    
    void clear();
    
    public interface IActivity
    {
        void next(final String p0, final Object... p1);
        
        void next(final String p0);
        
        void end();
        
        void append(final String p0, final Object... p1);
        
        void append(final String p0);
    }
}
