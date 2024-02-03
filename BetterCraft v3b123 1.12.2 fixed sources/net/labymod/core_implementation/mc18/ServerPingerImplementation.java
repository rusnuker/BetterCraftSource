// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18;

import net.labymod.core.ServerPingerData;
import net.labymod.utils.Consumer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.labymod.core_implementation.mc18.serverpinger.ServerPinger;
import java.util.concurrent.ThreadPoolExecutor;
import net.labymod.core.ServerPingerAdapter;

public class ServerPingerImplementation implements ServerPingerAdapter
{
    private static final ThreadPoolExecutor threadPool;
    private ServerPinger serverPinger;
    
    static {
        threadPool = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("LabyMod Server Pinger #%d").setDaemon(true).build());
    }
    
    public ServerPingerImplementation() {
        this.serverPinger = new ServerPinger();
    }
    
    @Override
    public void pingServer(ExecutorService threadPool, final long timePinged, final String ipAddress, final Consumer<ServerPingerData> callback) {
        if (threadPool == null) {
            threadPool = ServerPingerImplementation.threadPool;
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerPingerImplementation.this.serverPinger.ping(callback, new ServerPingerData(ipAddress, timePinged));
                }
                catch (final Throwable throwable) {
                    callback.accept(null);
                }
            }
        });
    }
    
    @Override
    public void pingServer(final ServerPingerData serverData, final Consumer<ServerPingerData> serverDataCallback) throws Throwable {
        this.serverPinger.ping(serverDataCallback, serverData);
    }
    
    @Override
    public void tick() {
        this.serverPinger.pingPendingNetworks();
    }
    
    @Override
    public void closePendingConnections() {
        this.serverPinger.clearPendingNetworks();
    }
}
