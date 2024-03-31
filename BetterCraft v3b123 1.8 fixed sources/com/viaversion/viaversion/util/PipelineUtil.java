// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

import java.util.Iterator;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.lang.reflect.Method;

public final class PipelineUtil
{
    private static final Method DECODE_METHOD;
    private static final Method ENCODE_METHOD;
    private static final Method MTM_DECODE;
    
    public static List<Object> callDecode(final ByteToMessageDecoder decoder, final ChannelHandlerContext ctx, final Object input) throws InvocationTargetException {
        final List<Object> output = new ArrayList<Object>();
        try {
            PipelineUtil.DECODE_METHOD.invoke(decoder, ctx, input, output);
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static void callEncode(final MessageToByteEncoder encoder, final ChannelHandlerContext ctx, final Object msg, final ByteBuf output) throws InvocationTargetException {
        try {
            PipelineUtil.ENCODE_METHOD.invoke(encoder, ctx, msg, output);
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Object> callDecode(final MessageToMessageDecoder decoder, final ChannelHandlerContext ctx, final Object msg) throws InvocationTargetException {
        final List<Object> output = new ArrayList<Object>();
        try {
            PipelineUtil.MTM_DECODE.invoke(decoder, ctx, msg, output);
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static boolean containsCause(Throwable t, final Class<?> c) {
        while (t != null) {
            if (c.isAssignableFrom(t.getClass())) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }
    
    public static <T> T getCause(Throwable t, final Class<T> c) {
        while (t != null) {
            if (c.isAssignableFrom(t.getClass())) {
                return (T)t;
            }
            t = t.getCause();
        }
        return null;
    }
    
    public static ChannelHandlerContext getContextBefore(final String name, final ChannelPipeline pipeline) {
        boolean mark = false;
        for (final String s : pipeline.names()) {
            if (mark) {
                return pipeline.context(pipeline.get(s));
            }
            if (!s.equalsIgnoreCase(name)) {
                continue;
            }
            mark = true;
        }
        return null;
    }
    
    public static ChannelHandlerContext getPreviousContext(final String name, final ChannelPipeline pipeline) {
        String previous = null;
        for (final String entry : pipeline.toMap().keySet()) {
            if (entry.equals(name)) {
                return pipeline.context(previous);
            }
            previous = entry;
        }
        return null;
    }
    
    static {
        try {
            (DECODE_METHOD = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class)).setAccessible(true);
            (ENCODE_METHOD = MessageToByteEncoder.class.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class)).setAccessible(true);
            (MTM_DECODE = MessageToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, Object.class, List.class)).setAccessible(true);
        }
        catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
