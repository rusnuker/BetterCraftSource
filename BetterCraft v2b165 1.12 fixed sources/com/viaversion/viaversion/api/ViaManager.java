// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api;

import java.util.Set;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.api.command.ViaVersionCommand;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.connection.ConnectionManager;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.api.protocol.ProtocolManager;

public interface ViaManager
{
    ProtocolManager getProtocolManager();
    
    ViaPlatform<?> getPlatform();
    
    ConnectionManager getConnectionManager();
    
    ViaProviders getProviders();
    
    ViaInjector getInjector();
    
    ViaVersionCommand getCommandHandler();
    
    ViaPlatformLoader getLoader();
    
    boolean isDebug();
    
    void setDebug(final boolean p0);
    
    Set<String> getSubPlatforms();
    
    void addEnableListener(final Runnable p0);
}
