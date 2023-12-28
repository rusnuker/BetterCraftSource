// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.List;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsServerPlayerList
{
    private static final Logger LOGGER;
    private static final JsonParser jsonParser;
    public long serverId;
    public List<String> players;
    
    public static RealmsServerPlayerList parse(final JsonObject node) {
        final RealmsServerPlayerList playerList = new RealmsServerPlayerList();
        try {
            playerList.serverId = JsonUtils.getLongOr("serverId", node, -1L);
            final String playerListString = JsonUtils.getStringOr("playerList", node, null);
            if (playerListString != null) {
                final JsonElement element = RealmsServerPlayerList.jsonParser.parse(playerListString);
                if (element.isJsonArray()) {
                    playerList.players = parsePlayers(element.getAsJsonArray());
                }
                else {
                    playerList.players = new ArrayList<String>();
                }
            }
            else {
                playerList.players = new ArrayList<String>();
            }
        }
        catch (final Exception e) {
            RealmsServerPlayerList.LOGGER.error("Could not parse RealmsServerPlayerList: " + e.getMessage());
        }
        return playerList;
    }
    
    private static List<String> parsePlayers(final JsonArray jsonArray) {
        final ArrayList<String> players = new ArrayList<String>();
        for (final JsonElement aJsonArray : jsonArray) {
            try {
                players.add(aJsonArray.getAsString());
            }
            catch (final Exception ex) {}
        }
        return players;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        jsonParser = new JsonParser();
    }
}
