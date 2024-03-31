// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

public interface PopUpCallback
{
    void cancel();
    
    void ok();
    
    void ok(final int p0, final String p1);
    
    boolean tick(final int p0);
}
