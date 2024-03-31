// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import io.netty.util.internal.PlatformDependent;

final class EpollEventArray
{
    private static final int EPOLL_EVENT_SIZE;
    private static final int EPOLL_DATA_OFFSET;
    private long memoryAddress;
    private int length;
    
    EpollEventArray(final int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must be >= 1 but was " + length);
        }
        this.length = length;
        this.memoryAddress = allocate(length);
    }
    
    private static long allocate(final int length) {
        return PlatformDependent.allocateMemory(length * EpollEventArray.EPOLL_EVENT_SIZE);
    }
    
    long memoryAddress() {
        return this.memoryAddress;
    }
    
    int length() {
        return this.length;
    }
    
    void increase() {
        this.length <<= 1;
        this.free();
        this.memoryAddress = allocate(this.length);
    }
    
    void free() {
        PlatformDependent.freeMemory(this.memoryAddress);
    }
    
    int events(final int index) {
        return PlatformDependent.getInt(this.memoryAddress + index * EpollEventArray.EPOLL_EVENT_SIZE);
    }
    
    int fd(final int index) {
        return PlatformDependent.getInt(this.memoryAddress + index * EpollEventArray.EPOLL_EVENT_SIZE + EpollEventArray.EPOLL_DATA_OFFSET);
    }
    
    static {
        EPOLL_EVENT_SIZE = Native.sizeofEpollEvent();
        EPOLL_DATA_OFFSET = Native.offsetofEpollData();
    }
}
