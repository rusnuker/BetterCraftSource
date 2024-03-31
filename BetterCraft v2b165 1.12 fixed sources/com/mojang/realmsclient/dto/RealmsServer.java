// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.google.gson.JsonParser;
import java.util.HashMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import java.util.Collections;
import java.util.Locale;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import java.util.ArrayList;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.Iterator;
import com.mojang.realmsclient.util.RealmsUtil;
import net.minecraft.realms.Realms;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class RealmsServer extends ValueObject
{
    private static final Logger LOGGER;
    public long id;
    public String remoteSubscriptionId;
    public String name;
    public String motd;
    public State state;
    public String owner;
    public String ownerUUID;
    public List<PlayerInfo> players;
    public Map<Integer, RealmsWorldOptions> slots;
    public boolean expired;
    public boolean expiredTrial;
    public int daysLeft;
    public WorldType worldType;
    public int activeSlot;
    public String minigameName;
    public int minigameId;
    public String minigameImage;
    public RealmsServerPing serverPing;
    
    public RealmsServer() {
        this.serverPing = new RealmsServerPing();
    }
    
    public String getDescription() {
        return this.motd;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getMinigameName() {
        return this.minigameName;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setDescription(final String motd) {
        this.motd = motd;
    }
    
    public void updateServerPing(final RealmsServerPlayerList serverPlayerList) {
        final StringBuilder builder = new StringBuilder();
        int players = 0;
        for (final String uuid : serverPlayerList.players) {
            if (uuid.equals(Realms.getUUID())) {
                continue;
            }
            String name = "";
            try {
                name = RealmsUtil.uuidToName(uuid);
            }
            catch (final Exception exception) {
                RealmsServer.LOGGER.error("Could not get name for " + uuid, exception);
                continue;
            }
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(name);
            ++players;
        }
        this.serverPing.nrOfPlayers = String.valueOf(players);
        this.serverPing.playerList = builder.toString();
    }
    
    public static RealmsServer parse(final JsonObject node) {
        final RealmsServer server = new RealmsServer();
        try {
            server.id = JsonUtils.getLongOr("id", node, -1L);
            server.remoteSubscriptionId = JsonUtils.getStringOr("remoteSubscriptionId", node, null);
            server.name = JsonUtils.getStringOr("name", node, null);
            server.motd = JsonUtils.getStringOr("motd", node, null);
            server.state = getState(JsonUtils.getStringOr("state", node, State.CLOSED.name()));
            server.owner = JsonUtils.getStringOr("owner", node, null);
            if (node.get("players") != null && node.get("players").isJsonArray()) {
                server.players = parseInvited(node.get("players").getAsJsonArray());
                sortInvited(server);
            }
            else {
                server.players = new ArrayList<PlayerInfo>();
            }
            server.daysLeft = JsonUtils.getIntOr("daysLeft", node, 0);
            server.expired = JsonUtils.getBooleanOr("expired", node, false);
            server.expiredTrial = JsonUtils.getBooleanOr("expiredTrial", node, false);
            server.worldType = getWorldType(JsonUtils.getStringOr("worldType", node, WorldType.NORMAL.name()));
            server.ownerUUID = JsonUtils.getStringOr("ownerUUID", node, "");
            if (node.get("slots") != null && node.get("slots").isJsonArray()) {
                server.slots = parseSlots(node.get("slots").getAsJsonArray());
            }
            else {
                server.slots = getEmptySlots();
            }
            server.minigameName = JsonUtils.getStringOr("minigameName", node, null);
            server.activeSlot = JsonUtils.getIntOr("activeSlot", node, -1);
            server.minigameId = JsonUtils.getIntOr("minigameId", node, -1);
            server.minigameImage = JsonUtils.getStringOr("minigameImage", node, null);
        }
        catch (final Exception e) {
            RealmsServer.LOGGER.error("Could not parse McoServer: " + e.getMessage());
        }
        return server;
    }
    
    private static void sortInvited(final RealmsServer server) {
        Collections.sort(server.players, new Comparator<PlayerInfo>() {
            @Override
            public int compare(final PlayerInfo o1, final PlayerInfo o2) {
                return ComparisonChain.start().compare(o2.getAccepted(), o1.getAccepted()).compare(o1.getName().toLowerCase(Locale.ROOT), o2.getName().toLowerCase(Locale.ROOT)).result();
            }
        });
    }
    
    private static List<PlayerInfo> parseInvited(final JsonArray jsonArray) {
        final ArrayList<PlayerInfo> invited = new ArrayList<PlayerInfo>();
        for (final JsonElement aJsonArray : jsonArray) {
            try {
                final JsonObject node = aJsonArray.getAsJsonObject();
                final PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setName(JsonUtils.getStringOr("name", node, null));
                playerInfo.setUuid(JsonUtils.getStringOr("uuid", node, null));
                playerInfo.setOperator(JsonUtils.getBooleanOr("operator", node, false));
                playerInfo.setAccepted(JsonUtils.getBooleanOr("accepted", node, false));
                playerInfo.setOnline(JsonUtils.getBooleanOr("online", node, false));
                invited.add(playerInfo);
            }
            catch (final Exception ex) {}
        }
        return invited;
    }
    
    private static Map<Integer, RealmsWorldOptions> parseSlots(final JsonArray jsonArray) {
        final Map<Integer, RealmsWorldOptions> slots = new HashMap<Integer, RealmsWorldOptions>();
        for (final JsonElement aJsonArray : jsonArray) {
            try {
                final JsonObject node = aJsonArray.getAsJsonObject();
                final JsonParser parser = new JsonParser();
                final JsonElement element = parser.parse(node.get("options").getAsString());
                RealmsWorldOptions options;
                if (element == null) {
                    options = RealmsWorldOptions.getDefaults();
                }
                else {
                    options = RealmsWorldOptions.parse(element.getAsJsonObject());
                }
                final int slot = JsonUtils.getIntOr("slotId", node, -1);
                slots.put(slot, options);
            }
            catch (final Exception ex) {}
        }
        for (int i = 1; i <= 3; ++i) {
            if (!slots.containsKey(i)) {
                slots.put(i, RealmsWorldOptions.getEmptyDefaults());
            }
        }
        return slots;
    }
    
    private static Map<Integer, RealmsWorldOptions> getEmptySlots() {
        final HashMap slots = new HashMap();
        slots.put(1, RealmsWorldOptions.getEmptyDefaults());
        slots.put(2, RealmsWorldOptions.getEmptyDefaults());
        slots.put(3, RealmsWorldOptions.getEmptyDefaults());
        return slots;
    }
    
    public static RealmsServer parse(final String json) {
        RealmsServer server = new RealmsServer();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject object = parser.parse(json).getAsJsonObject();
            server = parse(object);
        }
        catch (final Exception e) {
            RealmsServer.LOGGER.error("Could not parse McoServer: " + e.getMessage());
        }
        return server;
    }
    
    private static State getState(final String state) {
        try {
            return State.valueOf(state);
        }
        catch (final Exception ignored) {
            return State.CLOSED;
        }
    }
    
    private static WorldType getWorldType(final String state) {
        try {
            return WorldType.valueOf(state);
        }
        catch (final Exception ignored) {
            return WorldType.NORMAL;
        }
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.motd).append(this.state).append(this.owner).append(this.expired).toHashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final RealmsServer rhs = (RealmsServer)obj;
        return new EqualsBuilder().append(this.id, rhs.id).append(this.name, rhs.name).append(this.motd, rhs.motd).append(this.state, rhs.state).append(this.owner, rhs.owner).append(this.expired, rhs.expired).append(this.worldType, this.worldType).isEquals();
    }
    
    public RealmsServer clone() {
        final RealmsServer server = new RealmsServer();
        server.id = this.id;
        server.remoteSubscriptionId = this.remoteSubscriptionId;
        server.name = this.name;
        server.motd = this.motd;
        server.state = this.state;
        server.owner = this.owner;
        server.players = this.players;
        server.slots = this.cloneSlots(this.slots);
        server.expired = this.expired;
        server.expiredTrial = this.expiredTrial;
        server.daysLeft = this.daysLeft;
        server.serverPing = new RealmsServerPing();
        server.serverPing.nrOfPlayers = this.serverPing.nrOfPlayers;
        server.serverPing.playerList = this.serverPing.playerList;
        server.worldType = this.worldType;
        server.ownerUUID = this.ownerUUID;
        server.minigameName = this.minigameName;
        server.activeSlot = this.activeSlot;
        server.minigameId = this.minigameId;
        server.minigameImage = this.minigameImage;
        return server;
    }
    
    public Map<Integer, RealmsWorldOptions> cloneSlots(final Map<Integer, RealmsWorldOptions> slots) {
        final Map<Integer, RealmsWorldOptions> newSlots = new HashMap<Integer, RealmsWorldOptions>();
        for (final Map.Entry<Integer, RealmsWorldOptions> entry : slots.entrySet()) {
            newSlots.put(entry.getKey(), entry.getValue().clone());
        }
        return newSlots;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class McoServerComparator implements Comparator<RealmsServer>
    {
        private final String refOwner;
        
        public McoServerComparator(final String owner) {
            this.refOwner = owner;
        }
        
        @Override
        public int compare(final RealmsServer server1, final RealmsServer server2) {
            return ComparisonChain.start().compareTrueFirst(server1.state.equals(State.UNINITIALIZED), server2.state.equals(State.UNINITIALIZED)).compareTrueFirst(server1.expiredTrial, server2.expiredTrial).compareTrueFirst(server1.owner.equals(this.refOwner), server2.owner.equals(this.refOwner)).compareFalseFirst(server1.expired, server2.expired).compareTrueFirst(server1.state.equals(State.OPEN), server2.state.equals(State.OPEN)).compare(server1.id, server2.id).result();
        }
    }
    
    public enum State
    {
        CLOSED, 
        OPEN, 
        UNINITIALIZED;
    }
    
    public enum WorldType
    {
        NORMAL, 
        MINIGAME, 
        ADVENTUREMAP, 
        EXPERIENCE, 
        INSPIRATION;
    }
}
