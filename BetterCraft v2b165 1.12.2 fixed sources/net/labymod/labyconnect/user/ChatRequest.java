// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.user;

import com.mojang.authlib.GameProfile;

public class ChatRequest extends ChatUser
{
    public ChatRequest(final GameProfile gameProfile) {
        super(gameProfile, UserStatus.OFFLINE, "", null, 0, System.currentTimeMillis(), "", 0L, 0L, 0, false);
    }
}
