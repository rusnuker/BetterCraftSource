// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc;

public class DiscordRPUser
{
    private String avatar;
    private String discriminator;
    private String userId;
    private String username;
    
    public DiscordRPUser(final String avatar, final String discriminator, final String userId, final String username) {
        this.avatar = avatar;
        this.discriminator = discriminator;
        this.userId = userId;
        this.username = username;
    }
    
    public String getAvatar() {
        return this.avatar;
    }
    
    public String getDiscriminator() {
        return this.discriminator;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public String getUsername() {
        return this.username;
    }
}
