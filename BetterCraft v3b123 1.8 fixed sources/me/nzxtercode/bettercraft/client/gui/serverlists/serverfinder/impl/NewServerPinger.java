// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui.serverlists.serverfinder.impl;

import java.net.UnknownHostException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import net.labymod.core_implementation.mc18.serverpinger.ServerPinger;
import net.minecraft.client.multiplayer.ServerAddress;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class NewServerPinger
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
    
    public NewServerPinger() {
        this.done = false;
        this.failed = false;
    }
    
    public void ping(final String ip) {
        this.pinger(ip, 25565);
    }
    
    public void pinger(final String ip, final int port) {
        this.serverData = new ServerData("", String.valueOf(String.valueOf(String.valueOf(ip))) + ":" + port, false);
        final ServerAddress serveradress = ServerAddress.resolveAddress(this.serverData.serverIP);
        new Thread("Server Connector #" + NewServerPinger.threadNumber.incrementAndGet()) {
            @Override
            public void run() {
                final ServerPinger pinger = new ServerPinger();
                try {
                    NewServerPinger.logger.info("Pinging " + ip + ":" + port + "...");
                    final Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(serveradress.getIP(), serveradress.getPort()), 500);
                    socket.close();
                    NewServerPinger.logger.info("Ping successful: " + ip + ":" + port);
                }
                catch (final UnknownHostException e2) {
                    NewServerPinger.logger.info("Unknown host: " + ip + ":" + port);
                    NewServerPinger.failed(NewServerPinger.this, true);
                }
                catch (final Exception e3) {
                    NewServerPinger.logger.info("Ping failed: " + ip + ":" + port);
                    NewServerPinger.failed(NewServerPinger.this, true);
                }
                pinger.clearPendingNetworks();
                NewServerPinger.done(NewServerPinger.this, true);
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
        return this.serverData.version != 47;
    }
    
    static void failed(final NewServerPinger wurstServerPinger, final boolean failed) {
        wurstServerPinger.failed = failed;
    }
    
    static void done(final NewServerPinger wurstServerPinger, final boolean done) {
        wurstServerPinger.done = done;
    }
}
