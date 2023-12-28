// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.text2speech;

import com.google.common.collect.Queues;
import java.util.Queue;
import com.sun.jna.WString;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NarratorWindows implements Narrator
{
    private static boolean libraryFound;
    private static final Logger LOGGER;
    private final NarratorThread narratorThread;
    
    public NarratorWindows() {
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
        return NarratorWindows.libraryFound;
    }
    
    static {
        NarratorWindows.libraryFound = false;
        LOGGER = LogManager.getLogger();
        String result = "";
        try {
            Native.register(SAPIWrapperSolutionDLL.class, NativeLibrary.getInstance("SAPIWrapper_x64"));
            NarratorWindows.libraryFound = true;
            NarratorWindows.LOGGER.info("Narrator library for x64 successfully loaded");
        }
        catch (final UnsatisfiedLinkError e) {
            result = result + "ERROR : Couldn't load Narrator library : " + e.getMessage() + "\n";
        }
        catch (final Throwable e2) {
            result = result + "ERROR : Generic error while loading narrator : " + e2.getMessage() + "\n";
        }
        if (!NarratorWindows.libraryFound) {
            try {
                Native.register(SAPIWrapperSolutionDLL.class, NativeLibrary.getInstance("SAPIWrapper_x86"));
                NarratorWindows.libraryFound = true;
                NarratorWindows.LOGGER.info("Narrator library for x86 successfully loaded");
            }
            catch (final UnsatisfiedLinkError e) {
                result = result + "ERROR : Couldn't load Narrator library : " + e.getMessage() + "\n";
            }
            catch (final Throwable e2) {
                result = result + "ERROR : Generic error while loading narrator : " + e2.getMessage() + "\n";
            }
        }
        if (!NarratorWindows.libraryFound) {
            NarratorWindows.LOGGER.warn(result);
        }
    }
    
    private static class SAPIWrapperSolutionDLL
    {
        public static native long say(final WString p0);
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
            SAPIWrapperSolutionDLL.say(new WString(text.replaceAll("[<>]", "")));
        }
    }
}
