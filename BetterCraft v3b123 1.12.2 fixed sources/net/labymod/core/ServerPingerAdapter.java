// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

import net.labymod.utils.Consumer;
import java.util.concurrent.ExecutorService;

public interface ServerPingerAdapter
{
    void pingServer(final ExecutorService p0, final long p1, final String p2, final Consumer<ServerPingerData> p3);
    
    void pingServer(final ServerPingerData p0, final Consumer<ServerPingerData> p1) throws Throwable;
    
    void tick();
    
    void closePendingConnections();
}
