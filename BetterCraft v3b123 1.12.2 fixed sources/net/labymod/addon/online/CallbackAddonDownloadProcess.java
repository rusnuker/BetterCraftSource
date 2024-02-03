// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addon.online;

import java.io.File;

public interface CallbackAddonDownloadProcess
{
    void progress(final double p0);
    
    void success(final File p0);
    
    void failed(final String p0);
}
