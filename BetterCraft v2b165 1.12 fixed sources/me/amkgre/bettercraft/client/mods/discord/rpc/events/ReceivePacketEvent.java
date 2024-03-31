// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.events;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

public class ReceivePacketEvent
{
    private Packet packet;
    private NetworkManager networkManager;
    
    public ReceivePacketEvent(final Packet packet, final NetworkManager networkManager) {
        this.packet = packet;
        this.networkManager = networkManager;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }
}
