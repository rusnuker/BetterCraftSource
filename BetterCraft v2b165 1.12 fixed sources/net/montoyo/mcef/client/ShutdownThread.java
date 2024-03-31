// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.utilities.Log;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class ShutdownThread extends Thread
{
    private Field running;
    private Minecraft mc;
    
    public ShutdownThread() {
        super("MCEF-Shutdown");
        this.running = null;
        this.mc = Minecraft.getMinecraft();
        this.setDaemon(false);
        try {
            final Field[] fields = Minecraft.class.getDeclaredFields();
            Field[] array;
            for (int length = (array = fields).length, i = 0; i < length; ++i) {
                final Field f = array[i];
                if (f.getType().equals(Boolean.TYPE) && f.getModifiers() == 64) {
                    f.setAccessible(true);
                    this.running = f;
                    Log.info("volatile boolean Minecraft.running => %s", f.getName());
                    break;
                }
            }
        }
        catch (final Throwable t) {
            Log.warning("Can't detect Minecraft shutdown:", new Object[0]);
            t.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        if (this.running == null) {
            return;
        }
        Log.info("Minecraft shutdown detection thread started.", new Object[0]);
        while (true) {
            try {
                if (!this.running.getBoolean(this.mc)) {
                    break;
                }
            }
            catch (final Throwable t) {
                Log.warning("Can't detect Minecraft shutdown:", new Object[0]);
                t.printStackTrace();
                return;
            }
            try {
                Thread.sleep(100L);
            }
            catch (final Throwable t2) {}
        }
        MCEF.PROXY_CLIENT.onShutdown();
    }
}
