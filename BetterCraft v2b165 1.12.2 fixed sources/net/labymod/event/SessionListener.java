// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.event;

import net.labymod.labyconnect.packets.Packet;

public interface SessionListener
{
    void onConnected();
    
    void onDisconnected(final boolean p0, final String p1);
    
    void onPacketIn(final PacketEvent p0);
    
    void onPacketOut(final PacketEvent p0);
    
    public static class PacketEvent
    {
        private Packet packet;
        private boolean cancelled;
        
        public PacketEvent(final Packet packet) {
            this.packet = packet;
        }
        
        public Packet getPacket() {
            return this.packet;
        }
        
        public void setPacket(final Packet packet) {
            this.packet = packet;
        }
        
        public boolean isCancelled() {
            return this.cancelled;
        }
        
        public void setCancelled(final boolean cancelled) {
            this.cancelled = cancelled;
        }
    }
}
