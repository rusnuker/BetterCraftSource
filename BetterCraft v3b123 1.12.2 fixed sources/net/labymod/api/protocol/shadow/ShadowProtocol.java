// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.protocol.shadow;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import net.minecraft.network.PacketBuffer;
import net.labymod.api.EventManager;
import net.labymod.main.LabyMod;
import java.util.concurrent.atomic.AtomicInteger;
import net.labymod.api.events.PluginMessageEvent;
import net.labymod.utils.ServerData;
import net.labymod.utils.Consumer;

public class ShadowProtocol implements Consumer<ServerData>, PluginMessageEvent
{
    public static final String PM_CHANNEL = "SHADOW";
    public static final int SHADOW_VERSION = 1;
    private boolean shadowSupported;
    private AtomicInteger packetCounter;
    
    public ShadowProtocol() {
        this.shadowSupported = false;
        this.packetCounter = new AtomicInteger(0);
        final EventManager eventManager = LabyMod.getInstance().getEventManager();
        eventManager.registerOnQuit(this);
        eventManager.register(this);
    }
    
    @Override
    public void accept(final ServerData accepted) {
        this.shadowSupported = false;
    }
    
    @Override
    public void receiveMessage(final String channelName, final PacketBuffer packetBuffer) {
        if (channelName.equals("SHADOW")) {
            final int packetId = packetBuffer.readInt();
            if (packetId == 0) {
                this.packetCounter.set(0);
            }
            if (packetId == 1) {
                try {
                    final Channel channel = LabyMod.getInstance().getNettyChannel();
                    final ChannelPipeline pipeline = channel.pipeline();
                    if (pipeline.context("labytransformerin") == null) {
                        pipeline.addAfter("decoder", "labytransformerin", new ShadowTransformerIn(this));
                        pipeline.addAfter("encoder", "labytransformerout", new ShadowTransformerOut(this));
                    }
                    this.shadowSupported = true;
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            if (packetId == 2) {
                this.shadowSupported = false;
            }
        }
    }
    
    public void increaseCounter() {
        this.packetCounter.incrementAndGet();
    }
    
    public boolean isShadowSupported() {
        return this.shadowSupported;
    }
    
    public AtomicInteger getPacketCounter() {
        return this.packetCounter;
    }
}
