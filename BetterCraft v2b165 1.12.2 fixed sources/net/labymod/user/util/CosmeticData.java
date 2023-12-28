// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.util;

import net.labymod.user.User;

public abstract class CosmeticData
{
    public abstract boolean isEnabled();
    
    public abstract void loadData(final String[] p0) throws Exception;
    
    public void init(final User user) {
    }
    
    public void completed(final User user) {
    }
}
