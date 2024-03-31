// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.multiplayer.ServerData;

public class ServerDataFeaturedUtils extends ServerData
{
    public static final ResourceLocation STAR_ICON;
    
    static {
        STAR_ICON = new ResourceLocation("textures/misc/star.png");
    }
    
    public ServerDataFeaturedUtils(final String serverName, final String serverIP) {
        super(serverName, serverIP, false);
    }
}
