// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.util;

public class BlockingCallback<T> extends Callback<T>
{
    private final Object LOCK;
    
    public BlockingCallback() {
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
    public void onDone(final T response) {
        final Object object = this.LOCK;
        synchronized (object) {
            this.LOCK.notifyAll();
            monitorexit(object);
        }
    }
}
