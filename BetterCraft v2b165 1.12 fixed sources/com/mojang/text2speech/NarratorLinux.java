// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.text2speech;

import com.google.common.collect.Queues;
import java.util.Queue;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NarratorLinux implements Narrator
{
    private static boolean libraryFound;
    private static final Logger LOGGER;
    private final NarratorThread narratorThread;
    
    public NarratorLinux() {
        this.narratorThread = new NarratorThread();
        final Thread thread = new Thread(this.narratorThread);
        thread.setDaemon(true);
        thread.setName("Narrator");
        thread.start();
    }
    
    @Override
    public void say(final String msg) {
        this.narratorThread.add(msg);
    }
    
    @Override
    public void clear() {
        this.narratorThread.clear();
    }
    
    @Override
    public boolean active() {
        return NarratorLinux.libraryFound;
    }
    
    static {
        NarratorLinux.libraryFound = false;
        LOGGER = LogManager.getLogger();
        try {
            Native.register(FliteLibrary.class, NativeLibrary.getInstance("fliteWrapper"));
            FliteLibrary.init();
            NarratorLinux.libraryFound = true;
            NarratorLinux.LOGGER.info("Narrator library successfully loaded");
        }
        catch (final UnsatisfiedLinkError e) {
            NarratorLinux.LOGGER.warn("ERROR : Couldn't load Narrator library : " + e.getMessage());
        }
        catch (final Throwable e2) {
            NarratorLinux.LOGGER.warn("ERROR : Generic error while loading narrator : " + e2.getMessage());
        }
    }
    
    private static class FliteLibrary
    {
        public static native int init();
        
        public static native float say(final String p0);
    }
    
    private static class NarratorThread implements Runnable
    {
        protected final Queue<String> msgs;
        
        private NarratorThread() {
            this.msgs = (Queue<String>)Queues.newConcurrentLinkedQueue();
        }
        
        @Override
        public void run() {
            while (true) {
                if (this.msgs.peek() != null) {
                    this.say(this.msgs.poll());
                }
                try {
                    Thread.sleep(100L);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        public void add(final String msg) {
            this.msgs.add(msg);
        }
        
        public void clear() {
            this.msgs.clear();
        }
        
        private void say(final String text) {
            if (NarratorLinux.libraryFound) {
                FliteLibrary.say(text.replaceAll("[<>]", ""));
            }
        }
    }
}
