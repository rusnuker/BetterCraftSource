// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.authlib;

import com.mojang.authlib.minecraft.MinecraftSessionService;

public interface AuthenticationService
{
    UserAuthentication createUserAuthentication(final Agent p0);
    
    MinecraftSessionService createMinecraftSessionService();
    
    GameProfileRepository createProfileRepository();
}
