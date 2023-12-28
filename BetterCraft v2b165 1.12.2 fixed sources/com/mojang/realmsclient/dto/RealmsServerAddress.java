// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsServerAddress extends ValueObject
{
    private static final Logger LOGGER;
    public String address;
    public String resourcePackUrl;
    public String resourcePackHash;
    
    public static RealmsServerAddress parse(final String json) {
        final JsonParser parser = new JsonParser();
        final RealmsServerAddress serverAddress = new RealmsServerAddress();
        try {
            final JsonObject object = parser.parse(json).getAsJsonObject();
            serverAddress.address = JsonUtils.getStringOr("address", object, null);
            serverAddress.resourcePackUrl = JsonUtils.getStringOr("resourcePackUrl", object, null);
            serverAddress.resourcePackHash = JsonUtils.getStringOr("resourcePackHash", object, null);
        }
        catch (final Exception e) {
            RealmsServerAddress.LOGGER.error("Could not parse RealmsServerAddress: " + e.getMessage());
        }
        return serverAddress;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
