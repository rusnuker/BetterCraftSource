// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network;

import me.amkgre.bettercraft.client.utils.TimeHelperUtils;

public abstract class AbstractPacket<T extends INetHandler> implements Packet<T>
{
    protected boolean cancelled;
    public boolean crit;
    
    public AbstractPacket() {
        this.cancelled = false;
        this.crit = false;
    }
    
    @Override
    public void processPacket(final T handler) {
        TimeHelperUtils.onPacketRecieved(this);
    }
    
    public void cancel() {
        this.cancelled = true;
    }
    
    public AbstractPacket setCrit(final boolean crit) {
        this.crit = crit;
        return this;
    }
}
