// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_12to1_11_1;

import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;

public enum ClientboundPackets1_12 implements ClientboundPacketType
{
    SPAWN_ENTITY, 
    SPAWN_EXPERIENCE_ORB, 
    SPAWN_GLOBAL_ENTITY, 
    SPAWN_MOB, 
    SPAWN_PAINTING, 
    SPAWN_PLAYER, 
    ENTITY_ANIMATION, 
    STATISTICS, 
    BLOCK_BREAK_ANIMATION, 
    BLOCK_ENTITY_DATA, 
    BLOCK_ACTION, 
    BLOCK_CHANGE, 
    BOSSBAR, 
    SERVER_DIFFICULTY, 
    TAB_COMPLETE, 
    CHAT_MESSAGE, 
    MULTI_BLOCK_CHANGE, 
    WINDOW_CONFIRMATION, 
    CLOSE_WINDOW, 
    OPEN_WINDOW, 
    WINDOW_ITEMS, 
    WINDOW_PROPERTY, 
    SET_SLOT, 
    COOLDOWN, 
    PLUGIN_MESSAGE, 
    NAMED_SOUND, 
    DISCONNECT, 
    ENTITY_STATUS, 
    EXPLOSION, 
    UNLOAD_CHUNK, 
    GAME_EVENT, 
    KEEP_ALIVE, 
    CHUNK_DATA, 
    EFFECT, 
    SPAWN_PARTICLE, 
    JOIN_GAME, 
    MAP_DATA, 
    ENTITY_MOVEMENT, 
    ENTITY_POSITION, 
    ENTITY_POSITION_AND_ROTATION, 
    ENTITY_ROTATION, 
    VEHICLE_MOVE, 
    OPEN_SIGN_EDITOR, 
    PLAYER_ABILITIES, 
    COMBAT_EVENT, 
    PLAYER_INFO, 
    PLAYER_POSITION, 
    USE_BED, 
    UNLOCK_RECIPES, 
    DESTROY_ENTITIES, 
    REMOVE_ENTITY_EFFECT, 
    RESOURCE_PACK, 
    RESPAWN, 
    ENTITY_HEAD_LOOK, 
    SELECT_ADVANCEMENTS_TAB, 
    WORLD_BORDER, 
    CAMERA, 
    HELD_ITEM_CHANGE, 
    DISPLAY_SCOREBOARD, 
    ENTITY_METADATA, 
    ATTACH_ENTITY, 
    ENTITY_VELOCITY, 
    ENTITY_EQUIPMENT, 
    SET_EXPERIENCE, 
    UPDATE_HEALTH, 
    SCOREBOARD_OBJECTIVE, 
    SET_PASSENGERS, 
    TEAMS, 
    UPDATE_SCORE, 
    SPAWN_POSITION, 
    TIME_UPDATE, 
    TITLE, 
    SOUND, 
    TAB_LIST, 
    COLLECT_ITEM, 
    ENTITY_TELEPORT, 
    ADVANCEMENTS, 
    ENTITY_PROPERTIES, 
    ENTITY_EFFECT;
    
    @Override
    public int getId() {
        return this.ordinal();
    }
    
    @Override
    public String getName() {
        return this.name();
    }
}