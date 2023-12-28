// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.net.UnknownHostException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.multiplayer.ServerAddress;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class NewServerPingerUtils
{
    private static final AtomicInteger threadNumber;
    public static final Logger logger;
    public ServerData serverData;
    private boolean done;
    private boolean failed;
    
    static {
        threadNumber = new AtomicInteger(0);
        logger = LogManager.getLogger();
    }
    
    public NewServerPingerUtils() {
        this.done = false;
        this.failed = false;
    }
    
    public void ping(final String ip) {
        this.pinger(ip, 25565);
    }
    
    public void pinger(final String ip, final int port) {
        this.serverData = new ServerData("", String.valueOf(String.valueOf(String.valueOf(ip))) + ":" + port, false);
        final ServerAddress serveradress = ServerAddress.resolveAddress(this.serverData.serverIP);
        new Thread("Server Connector #" + NewServerPingerUtils.threadNumber.incrementAndGet()) {
            @Override
            public void run() {
                final ServerPinger pinger = new ServerPinger();
                try {
                    NewServerPingerUtils.logger.info("Pinging " + ip + ":" + port + "...");
                    final Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(serveradress.getIP(), serveradress.getPort()), 500);
                    socket.close();
                    NewServerPingerUtils.logger.info("Ping successful: " + ip + ":" + port);
                }
                catch (final UnknownHostException e2) {
                    NewServerPingerUtils.logger.info("Unknown host: " + ip + ":" + port);
                    NewServerPingerUtils.failed(NewServerPingerUtils.this, true);
                }
                catch (final Exception e3) {
                    NewServerPingerUtils.logger.info("Ping failed: " + ip + ":" + port);
                    NewServerPingerUtils.failed(NewServerPingerUtils.this, true);
                }
                pinger.clearPendingNetworks();
                NewServerPingerUtils.done(NewServerPingerUtils.this, true);
            }
        }.start();
    }
    
    public boolean isStillPinging() {
        return !this.done;
    }
    
    public boolean isWorking() {
        return !this.failed;
    }
    
    public boolean isOtherVersion() {
        return this.serverData.version != 340;
    }
    
    static void failed(final NewServerPingerUtils wurstServerPinger, final boolean failed) {
        wurstServerPinger.failed = failed;
    }
    
    static void done(final NewServerPingerUtils wurstServerPinger, final boolean done) {
        wurstServerPinger.done = done;
    }
}
