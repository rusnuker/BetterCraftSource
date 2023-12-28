// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.channel.unix.FileDescriptor;
import io.netty.util.internal.PlatformDependent;

public final class Epoll
{
    private static final Throwable UNAVAILABILITY_CAUSE;
    
    public static boolean isAvailable() {
        return Epoll.UNAVAILABILITY_CAUSE == null;
    }
    
    public static void ensureAvailability() {
        if (Epoll.UNAVAILABILITY_CAUSE != null) {
            throw (Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(Epoll.UNAVAILABILITY_CAUSE);
        }
    }
    
    public static Throwable unavailabilityCause() {
        return Epoll.UNAVAILABILITY_CAUSE;
    }
    
    private Epoll() {
    }
    
    static {
        Throwable cause = null;
        FileDescriptor epollFd = null;
        FileDescriptor eventFd = null;
        try {
            epollFd = Native.newEpollCreate();
            eventFd = Native.newEventFd();
        }
        catch (final Throwable t) {
            cause = t;
        }
        finally {
            if (epollFd != null) {
                try {
                    epollFd.close();
                }
                catch (final Exception ex) {}
            }
            if (eventFd != null) {
                try {
                    eventFd.close();
                }
                catch (final Exception ex2) {}
            }
        }
        if (cause != null) {
            UNAVAILABILITY_CAUSE = cause;
        }
        else {
            UNAVAILABILITY_CAUSE = (PlatformDependent.hasUnsafe() ? null : new IllegalStateException("sun.misc.Unsafe not available"));
        }
    }
}
