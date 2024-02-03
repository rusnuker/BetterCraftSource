// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client;

import me.nzxtercode.bettercraft.client.Config;

public class ClientBrandRetriever
{
    public static String getClientModName() {
        return Config.getInstance().getConfig("Brand").getAsJsonObject().get("name").getAsString();
    }
}
