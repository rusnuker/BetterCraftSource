// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.platform;

import java.util.Collection;
import com.viaversion.viaversion.util.ConcurrentList;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.lang.reflect.Method;
import com.viaversion.viaversion.bukkit.util.NMSUtil;
import org.bukkit.Bukkit;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.bukkit.plugin.PluginDescriptionFile;
import com.viaversion.viaversion.bukkit.handlers.BukkitChannelInitializer;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.ChannelInitializer;
import java.util.Iterator;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.util.ListWrapper;
import java.util.ArrayList;
import java.lang.reflect.Field;
import com.viaversion.viaversion.util.Pair;
import io.netty.channel.ChannelFuture;
import java.util.List;
import com.viaversion.viaversion.api.platform.ViaInjector;

public class BukkitViaInjector implements ViaInjector
{
    private final List<ChannelFuture> injectedFutures;
    private final List<Pair<Field, Object>> injectedLists;
    private boolean protocolLib;
    
    public BukkitViaInjector() {
        this.injectedFutures = new ArrayList<ChannelFuture>();
        this.injectedLists = new ArrayList<Pair<Field, Object>>();
    }
    
    @Override
    public void inject() throws Exception {
        if (PaperViaInjector.PAPER_INJECTION_METHOD) {
            PaperViaInjector.setPaperChannelInitializeListener();
            return;
        }
        try {
            final Object connection = getServerConnection();
            if (connection == null) {
                throw new Exception("We failed to find the core component 'ServerConnection', please file an issue on our GitHub.");
            }
            for (final Field field : connection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                final Object value = field.get(connection);
                if (value instanceof List) {
                    final List wrapper = new ListWrapper((List)value) {
                        @Override
                        public void handleAdd(final Object o) {
                            if (o instanceof ChannelFuture) {
                                try {
                                    BukkitViaInjector.this.injectChannelFuture((ChannelFuture)o);
                                }
                                catch (final Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    this.injectedLists.add(new Pair<Field, Object>(field, connection));
                    field.set(connection, wrapper);
                    synchronized (wrapper) {
                        for (final Object o : (List)value) {
                            if (!(o instanceof ChannelFuture)) {
                                break;
                            }
                            this.injectChannelFuture((ChannelFuture)o);
                        }
                    }
                }
            }
        }
        catch (final Exception e) {
            Via.getPlatform().getLogger().severe("Unable to inject ViaVersion, please post these details on our GitHub and ensure you're using a compatible server version.");
            throw e;
        }
    }
    
    private void injectChannelFuture(final ChannelFuture future) throws Exception {
        try {
            final List<String> names = future.channel().pipeline().names();
            ChannelHandler bootstrapAcceptor = null;
            for (final String name : names) {
                final ChannelHandler handler = future.channel().pipeline().get(name);
                try {
                    ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class);
                    bootstrapAcceptor = handler;
                }
                catch (final Exception ex) {}
            }
            if (bootstrapAcceptor == null) {
                bootstrapAcceptor = future.channel().pipeline().first();
            }
            try {
                final ChannelInitializer<Channel> oldInit = ReflectionUtil.get(bootstrapAcceptor, "childHandler", ChannelInitializer.class);
                final ChannelInitializer newInit = new BukkitChannelInitializer(oldInit);
                ReflectionUtil.set(bootstrapAcceptor, "childHandler", newInit);
                this.injectedFutures.add(future);
            }
            catch (final NoSuchFieldException e) {
                final ClassLoader cl = bootstrapAcceptor.getClass().getClassLoader();
                if (cl.getClass().getName().equals("org.bukkit.plugin.java.PluginClassLoader")) {
                    final PluginDescriptionFile yaml = ReflectionUtil.get(cl, "description", PluginDescriptionFile.class);
                    throw new Exception("Unable to inject, due to " + bootstrapAcceptor.getClass().getName() + ", try without the plugin " + yaml.getName() + "?");
                }
                throw new Exception("Unable to find core component 'childHandler', please check your plugins. issue: " + bootstrapAcceptor.getClass().getName());
            }
        }
        catch (final Exception e2) {
            Via.getPlatform().getLogger().severe("We failed to inject ViaVersion, have you got late-bind enabled with something else?");
            throw e2;
        }
    }
    
    @Override
    public void uninject() throws Exception {
        if (PaperViaInjector.PAPER_INJECTION_METHOD) {
            PaperViaInjector.removePaperChannelInitializeListener();
            return;
        }
        for (final ChannelFuture future : this.injectedFutures) {
            final List<String> names = future.channel().pipeline().names();
            ChannelHandler bootstrapAcceptor = null;
            for (final String name : names) {
                final ChannelHandler handler = future.channel().pipeline().get(name);
                try {
                    final ChannelInitializer<Channel> oldInit = ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class);
                    if (!(oldInit instanceof BukkitChannelInitializer)) {
                        continue;
                    }
                    bootstrapAcceptor = handler;
                }
                catch (final Exception ex) {}
            }
            if (bootstrapAcceptor == null) {
                bootstrapAcceptor = future.channel().pipeline().first();
            }
            try {
                final ChannelInitializer<Channel> oldInit2 = ReflectionUtil.get(bootstrapAcceptor, "childHandler", ChannelInitializer.class);
                if (!(oldInit2 instanceof BukkitChannelInitializer)) {
                    continue;
                }
                ReflectionUtil.set(bootstrapAcceptor, "childHandler", ((BukkitChannelInitializer)oldInit2).getOriginal());
            }
            catch (final Exception e) {
                Via.getPlatform().getLogger().severe("Failed to remove injection handler, reload won't work with connections, please reboot!");
            }
        }
        this.injectedFutures.clear();
        for (final Pair<Field, Object> pair : this.injectedLists) {
            try {
                final Object o = pair.getKey().get(pair.getValue());
                if (!(o instanceof ListWrapper)) {
                    continue;
                }
                pair.getKey().set(pair.getValue(), ((ListWrapper)o).getOriginalList());
            }
            catch (final IllegalAccessException e2) {
                Via.getPlatform().getLogger().severe("Failed to remove injection, reload won't work with connections, please reboot!");
            }
        }
        this.injectedLists.clear();
    }
    
    @Override
    public boolean lateProtocolVersionSetting() {
        return true;
    }
    
    @Override
    public int getServerProtocolVersion() throws Exception {
        if (PaperViaInjector.PAPER_PROTOCOL_METHOD) {
            return Bukkit.getUnsafe().getProtocolVersion();
        }
        try {
            final Class<?> serverClazz = NMSUtil.nms("MinecraftServer", "net.minecraft.server.MinecraftServer");
            final Object server = ReflectionUtil.invokeStatic(serverClazz, "getServer");
            final Class<?> pingClazz = NMSUtil.nms("ServerPing", "net.minecraft.network.protocol.status.ServerPing");
            Object ping = null;
            for (final Field f : serverClazz.getDeclaredFields()) {
                if (f.getType() != null && f.getType().getSimpleName().equals("ServerPing")) {
                    f.setAccessible(true);
                    ping = f.get(server);
                }
            }
            if (ping != null) {
                Object serverData = null;
                for (final Field f2 : pingClazz.getDeclaredFields()) {
                    if (f2.getType() != null && f2.getType().getSimpleName().endsWith("ServerData")) {
                        f2.setAccessible(true);
                        serverData = f2.get(ping);
                    }
                }
                if (serverData != null) {
                    int protocolVersion = -1;
                    for (final Field f3 : serverData.getClass().getDeclaredFields()) {
                        if (f3.getType() != null && f3.getType() == Integer.TYPE) {
                            f3.setAccessible(true);
                            protocolVersion = (int)f3.get(serverData);
                        }
                    }
                    if (protocolVersion != -1) {
                        return protocolVersion;
                    }
                }
            }
        }
        catch (final Exception e) {
            throw new Exception("Failed to get server", e);
        }
        throw new Exception("Failed to get server");
    }
    
    @Override
    public String getEncoderName() {
        return "encoder";
    }
    
    @Override
    public String getDecoderName() {
        return this.protocolLib ? "protocol_lib_decoder" : "decoder";
    }
    
    public static Object getServerConnection() throws Exception {
        final Class<?> serverClazz = NMSUtil.nms("MinecraftServer", "net.minecraft.server.MinecraftServer");
        final Object server = ReflectionUtil.invokeStatic(serverClazz, "getServer");
        Object connection = null;
        for (final Method m : serverClazz.getDeclaredMethods()) {
            if (m.getReturnType() != null && m.getReturnType().getSimpleName().equals("ServerConnection") && m.getParameterTypes().length == 0) {
                connection = m.invoke(server, new Object[0]);
            }
        }
        return connection;
    }
    
    public static boolean isBinded() {
        if (PaperViaInjector.PAPER_INJECTION_METHOD) {
            return true;
        }
        try {
            final Object connection = getServerConnection();
            if (connection == null) {
                return false;
            }
            for (final Field field : connection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                final Object value = field.get(connection);
                if (value instanceof List) {
                    synchronized (value) {
                        final Iterator iterator = ((List)value).iterator();
                        if (iterator.hasNext()) {
                            final Object o = iterator.next();
                            if (o instanceof ChannelFuture) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        catch (final Exception ex) {}
        return false;
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject data = new JsonObject();
        final JsonArray injectedChannelInitializers = new JsonArray();
        for (final ChannelFuture cf : this.injectedFutures) {
            final JsonObject info = new JsonObject();
            info.addProperty("futureClass", cf.getClass().getName());
            info.addProperty("channelClass", cf.channel().getClass().getName());
            final JsonArray pipeline = new JsonArray();
            for (final String pipeName : cf.channel().pipeline().names()) {
                final JsonObject pipe = new JsonObject();
                pipe.addProperty("name", pipeName);
                if (cf.channel().pipeline().get(pipeName) != null) {
                    pipe.addProperty("class", cf.channel().pipeline().get(pipeName).getClass().getName());
                    try {
                        final Object child = ReflectionUtil.get(cf.channel().pipeline().get(pipeName), "childHandler", ChannelInitializer.class);
                        pipe.addProperty("childClass", child.getClass().getName());
                        if (child instanceof BukkitChannelInitializer) {
                            pipe.addProperty("oldInit", ((BukkitChannelInitializer)child).getOriginal().getClass().getName());
                        }
                    }
                    catch (final Exception ex) {}
                }
                pipeline.add(pipe);
            }
            info.add("pipeline", pipeline);
            injectedChannelInitializers.add(info);
        }
        data.add("injectedChannelInitializers", injectedChannelInitializers);
        final JsonObject wrappedLists = new JsonObject();
        final JsonObject currentLists = new JsonObject();
        try {
            for (final Pair<Field, Object> pair : this.injectedLists) {
                final Object list = pair.getKey().get(pair.getValue());
                currentLists.addProperty(pair.getKey().getName(), list.getClass().getName());
                if (list instanceof ListWrapper) {
                    wrappedLists.addProperty(pair.getKey().getName(), ((ListWrapper)list).getOriginalList().getClass().getName());
                }
            }
            data.add("wrappedLists", wrappedLists);
            data.add("currentLists", currentLists);
        }
        catch (final Exception ex2) {}
        data.addProperty("binded", isBinded());
        return data;
    }
    
    public static void patchLists() throws Exception {
        if (PaperViaInjector.PAPER_INJECTION_METHOD) {
            return;
        }
        final Object connection = getServerConnection();
        if (connection == null) {
            Via.getPlatform().getLogger().warning("We failed to find the core component 'ServerConnection', please file an issue on our GitHub.");
            return;
        }
        for (final Field field : connection.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            final Object value = field.get(connection);
            if (value instanceof List) {
                if (!(value instanceof ConcurrentList)) {
                    final ConcurrentList list = new ConcurrentList();
                    list.addAll((Collection)value);
                    field.set(connection, list);
                }
            }
        }
    }
    
    public void setProtocolLib(final boolean protocolLib) {
        this.protocolLib = protocolLib;
    }
}
