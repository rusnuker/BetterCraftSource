// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.utils;

import java.util.Iterator;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.Packet;
import java.util.ArrayList;

public final class TimeHelperUtils
{
    private static TimeHelperUtils tpsTimer;
    private static ArrayList<Long> times;
    public static double lastTps;
    private static long currentCalcLag;
    private long time;
    private long tick;
    public static Packet event1;
    
    static {
        TimeHelperUtils.tpsTimer = new TimeHelperUtils();
        TimeHelperUtils.times = new ArrayList<Long>();
        TimeHelperUtils.lastTps = 20.0;
    }
    
    public static long bytesToMb(final long l) {
        return l / 1024L / 1024L;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L - this.time;
    }
    
    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }
    
    public static long getLag() {
        return System.currentTimeMillis() - TimeHelperUtils.currentCalcLag;
    }
    
    public static long getFormattedLag() {
        final long currentLag = getLag();
        if (currentLag >= 2500L && currentLag < 5000000L) {
            return currentLag;
        }
        return 0L;
    }
    
    public static void onPacketRecieved(final Packet event) {
        if (event instanceof S03PacketTimeUpdate) {
            TimeHelperUtils.currentCalcLag = System.currentTimeMillis();
            TimeHelperUtils.times.add(Math.max(1000L, TimeHelperUtils.tpsTimer.getTime()));
            long timesAdded = 0L;
            if (TimeHelperUtils.times.size() > 5) {
                TimeHelperUtils.times.remove(0);
            }
            for (final long l : TimeHelperUtils.times) {
                timesAdded += l;
            }
            final long roundedTps = timesAdded / TimeHelperUtils.times.size();
            TimeHelperUtils.lastTps = 20.0 / roundedTps * 1000.0;
            TimeHelperUtils.tpsTimer.reset();
        }
    }
    
    public static char getTPSColorCode(final double tps2) {
        if (tps2 >= 17.0) {
            return 'a';
        }
        if (tps2 >= 13.0) {
            return 'e';
        }
        if (tps2 > 9.0) {
            return 'c';
        }
        return '4';
    }
}
