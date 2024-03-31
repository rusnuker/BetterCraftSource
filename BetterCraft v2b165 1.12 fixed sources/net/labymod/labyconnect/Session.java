// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import com.mojang.util.UUIDTypeAdapter;
import com.mojang.authlib.GameProfile;

public class Session
{
    public final String username;
    public final String playerID;
    public final String token;
    public final Type sessionType;
    
    public Session(final String username, final String uuid, final String token, final String sessionType) {
        this.username = username;
        this.playerID = uuid;
        this.token = token;
        this.sessionType = Type.setSessionType(sessionType);
    }
    
    public String getSessionID() {
        return "token:" + this.token + ":" + this.playerID;
    }
    
    public String getPlayerID() {
        return this.playerID;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public GameProfile getProfile() {
        try {
            final UUID var1 = UUIDTypeAdapter.fromString(this.getPlayerID());
            return new GameProfile(var1, this.getUsername());
        }
        catch (final IllegalArgumentException var2) {
            return new GameProfile(null, this.getUsername());
        }
    }
    
    public Type getSessionType() {
        return this.sessionType;
    }
    
    public enum Type
    {
        LEGACY("LEGACY", 0, "LEGACY", 0, "LEGACY", 0, "legacy"), 
        MOJANG("MOJANG", 1, "MOJANG", 1, "MOJANG", 1, "mojang");
        
        private static final Map types;
        private final String sessionType;
        private static final Type[] $VALUES;
        
        static {
            types = Maps.newHashMap();
            $VALUES = new Type[] { Type.LEGACY, Type.MOJANG };
            Type[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Type var4 = values[i];
                Type.types.put(var4.sessionType, var4);
            }
        }
        
        private Type(final String s2, final int n3, final String s, final int n2, final String upperSessionType, final int id, final String sessionType) {
            this.sessionType = sessionType;
        }
        
        public static Type setSessionType(final String sessionType) {
            return Type.types.get(sessionType.toLowerCase());
        }
    }
}
