// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class WorldDownload
{
    private static final Logger LOGGER;
    public String downloadLink;
    public String resourcePackUrl;
    public String resourcePackHash;
    
    public static WorldDownload parse(final String json) {
        final JsonParser jsonParser = new JsonParser();
        final JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        final WorldDownload worldDownload = new WorldDownload();
        try {
            worldDownload.downloadLink = JsonUtils.getStringOr("downloadLink", jsonObject, "");
            worldDownload.resourcePackUrl = JsonUtils.getStringOr("resourcePackUrl", jsonObject, "");
            worldDownload.resourcePackHash = JsonUtils.getStringOr("resourcePackHash", jsonObject, "");
        }
        catch (final Exception e) {
            WorldDownload.LOGGER.error("Could not parse WorldDownload: " + e.getMessage());
        }
        return worldDownload;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
