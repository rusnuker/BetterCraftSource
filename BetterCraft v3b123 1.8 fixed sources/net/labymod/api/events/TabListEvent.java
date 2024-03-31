// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.events;

public interface TabListEvent
{
    void onUpdate(final Type p0, final String p1, final String p2);
    
    public enum Type
    {
        HEADER("HEADER", 0), 
        FOOTER("FOOTER", 1);
        
        private Type(final String s, final int n) {
        }
    }
}
