// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import java.util.Iterator;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.AbstractPacket;
import java.time.Instant;
import java.util.ArrayList;

public final class TimeHelperUtils
{
    public long time;
    public long tick;
    public int time1;
    public boolean enabling;
    private long prevMS;
    private static long lastMS;
    public static long currentCalcLag;
    private static TimeHelperUtils tpsTimer;
    public static double lastTps;
    private static ArrayList<Long> times;
    
    static {
        TimeHelperUtils.lastMS = 0L;
        TimeHelperUtils.tpsTimer = new TimeHelperUtils();
        TimeHelperUtils.lastTps = 20.0;
        TimeHelperUtils.times = new ArrayList<Long>();
    }
    
    public void update() {
        if (this.enabling) {
            ++this.time;
        }
        else {
            --this.time;
        }
        if (this.time < 0L) {
            this.time = 0L;
        }
        if (this.time > this.getMaxTime()) {
            this.time = this.getMaxTime();
        }
    }
    
    public void reset1() {
        this.time = 0L;
    }
    
    public int getMaxTime() {
        return 10;
    }
    
    public int getTime1() {
        return this.time1;
    }
    
    public void on() {
        this.enabling = true;
    }
    
    public static double fromMillis(final long millis) {
        return millis / 1000.0;
    }
    
    public static long getCurrentMillis() {
        return Instant.now().toEpochMilli();
    }
    
    public static double getCurrentTime() {
        return fromMillis(getCurrentMillis());
    }
    
    public static double calcDuration(final double startTime) {
        return getCurrentTime() - startTime;
    }
    
    public static double calcDuration(final double startTime, final double endTime) {
        return endTime - startTime;
    }
    
    public static double calcDuration(final double startTime, final long endTimeMillis) {
        return calcDuration(startTime, fromMillis(endTimeMillis));
    }
    
    public boolean delay(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
    }
    
    public void resetss() {
        this.prevMS = this.getTime();
    }
    
    public long getTimes() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getDifference() {
        return this.getTime() - this.prevMS;
    }
    
    public void setDifference(final long difference) {
        this.prevMS = this.getTime() - difference;
    }
    
    public boolean isDelayComplete(final float f) {
        return System.currentTimeMillis() - TimeHelperUtils.lastMS >= f;
    }
    
    public static long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    public void setLastMS(final long lastMS) {
        TimeHelperUtils.lastMS = System.currentTimeMillis();
    }
    
    public int convertToMS(final int perSecound) {
        return 1000 / perSecound;
    }
    
    public static boolean hasReached(final long millisecounds) {
        return getCurrentMS() - TimeHelperUtils.lastMS >= millisecounds;
    }
    
    public static void resets() {
        TimeHelperUtils.lastMS = getCurrentMS();
    }
    
    public void updateTiming() {
        ++this.time1;
    }
    
    public void deleteTiming() {
        --this.time1;
    }
    
    public TimeHelperUtils() {
        this.prevMS = 0L;
        this.time = System.nanoTime() / 1000000L;
    }
    
    public void updateTick() {
        ++this.tick;
    }
    
    public void resetTick() {
        this.tick = 0L;
    }
    
    public boolean hasTimePassedTick(final long ticks) {
        return this.tick >= ticks;
    }
    
    public long getTick() {
        return this.tick;
    }
    
    public int getTiming() {
        return this.time1;
    }
    
    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (this.getTime() >= time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L - this.time;
    }
    
    public long getTimeing() {
        return System.nanoTime() / 1000L;
    }
    
    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }
    
    public static long bytesToMb(final long l) {
        return l / 1024L / 1024L;
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
    
    public static void onPacketRecieved(final AbstractPacket modPacket) {
        if (modPacket instanceof SPacketTimeUpdate) {
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
}
