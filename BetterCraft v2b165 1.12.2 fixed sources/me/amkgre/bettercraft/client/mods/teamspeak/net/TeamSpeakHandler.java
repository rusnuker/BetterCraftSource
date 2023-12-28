// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.net;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import io.netty.channel.Channel;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakResponse;
import io.netty.channel.SimpleChannelInboundHandler;

public class TeamSpeakHandler extends SimpleChannelInboundHandler<TeamSpeakResponse>
{
    private final TeamSpeakNetworkManager networkManager;
    private Channel channel;
    private final Queue<OutboundPacket> QUEUE;
    
    TeamSpeakHandler(final TeamSpeakNetworkManager networkManager) {
        this.QUEUE = new ConcurrentLinkedQueue<OutboundPacket>();
        this.networkManager = networkManager;
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
        this.flushOutboundQueue();
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.networkManager.disconnect(null);
    }
    
    @Override
    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final TeamSpeakResponse response) throws Exception {
        this.networkManager.handleResponse(response);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        this.networkManager.disconnect(cause);
    }
    
    public void closeChannel() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
        }
    }
    
    public void send(final Request request, final GenericFutureListener... futureListeners) {
        if (this.channel != null && this.channel.isOpen()) {
            final ChannelFuture channelFuture = this.channel.writeAndFlush(request);
            channelFuture.addListeners((GenericFutureListener<? extends Future<? super Void>>[])futureListeners);
        }
        else {
            this.QUEUE.add(new OutboundPacket(request, futureListeners));
        }
    }
    
    private void flushOutboundQueue() {
        while (!this.QUEUE.isEmpty()) {
            final OutboundPacket outboundPacket = this.QUEUE.poll();
            this.send(outboundPacket.request, outboundPacket.futureListeners);
        }
    }
    
    private static class OutboundPacket
    {
        private Request request;
        private GenericFutureListener[] futureListeners;
        
        public OutboundPacket(final Request request, final GenericFutureListener... futureListeners) {
            this.request = request;
            this.futureListeners = futureListeners;
        }
    }
}
