// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.authlib.minecraft;

import java.util.Map;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.net.InetAddress;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.GameProfile;

public interface MinecraftSessionService
{
    void joinServer(final GameProfile p0, final String p1, final String p2) throws AuthenticationException;
    
    GameProfile hasJoinedServer(final GameProfile p0, final String p1, final InetAddress p2) throws AuthenticationUnavailableException;
    
    Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(final GameProfile p0, final boolean p1);
    
    GameProfile fillProfileProperties(final GameProfile p0, final boolean p1);
}
