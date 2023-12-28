// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.util;

public class BlockingRunnable implements Runnable
{
    private final Object LOCK;
    
    public BlockingRunnable() {
        this.LOCK = new Object();
    }
    
    public void onStart() {
        final Object object = this.LOCK;
        synchronized (object) {
            try {
                this.LOCK.wait();
            }
            catch (final InterruptedException ex) {}
            monitorexit(object);
        }
    }
    
    @Override
    public void run() {
        final Object object = this.LOCK;
        synchronized (object) {
            this.LOCK.notifyAll();
            monitorexit(object);
        }
    }
}
