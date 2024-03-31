// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.events;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventRegistry
{
    private CopyOnWriteArrayList<Consumer<ReceivePacketEvent>> receivePacketEvent;
    private CopyOnWriteArrayList<Consumer<Object>> leaveServerEvent;
    
    public EventRegistry() {
        this.receivePacketEvent = new CopyOnWriteArrayList<Consumer<ReceivePacketEvent>>();
        this.leaveServerEvent = new CopyOnWriteArrayList<Consumer<Object>>();
    }
    
    public void callReceivePacketEvent(final ReceivePacketEvent event) {
        for (final Consumer<ReceivePacketEvent> consumer : this.receivePacketEvent) {
            consumer.accept(event);
        }
    }
    
    public void registerOnReceivePacketEvent(final Consumer<ReceivePacketEvent> eventListener) {
        this.receivePacketEvent.add(eventListener);
    }
    
    public void callLeaveServerEvent(final Object event) {
        for (final Consumer<Object> consumer : this.leaveServerEvent) {
            consumer.accept(event);
        }
    }
    
    public void registerLeaveServerEvent(final Consumer<Object> eventListener) {
        this.leaveServerEvent.add(eventListener);
    }
}
