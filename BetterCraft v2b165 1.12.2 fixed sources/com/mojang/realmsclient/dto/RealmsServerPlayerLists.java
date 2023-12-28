// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class RealmsServerPlayerLists extends ValueObject
{
    private static final Logger LOGGER;
    public List<RealmsServerPlayerList> servers;
    
    public static RealmsServerPlayerLists parse(final String json) {
        final RealmsServerPlayerLists list = new RealmsServerPlayerLists();
        list.servers = new ArrayList<RealmsServerPlayerList>();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject object = parser.parse(json).getAsJsonObject();
            if (object.get("lists").isJsonArray()) {
                final JsonArray jsonArray = object.get("lists").getAsJsonArray();
                final Iterator<JsonElement> it = jsonArray.iterator();
                while (it.hasNext()) {
                    list.servers.add(RealmsServerPlayerList.parse(it.next().getAsJsonObject()));
                }
            }
        }
        catch (final Exception e) {
            RealmsServerPlayerLists.LOGGER.error("Could not parse RealmsServerPlayerLists: " + e.getMessage());
        }
        return list;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
