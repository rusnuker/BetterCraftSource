// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.pathfinding;

public enum PathNodeType
{
    BLOCKED("BLOCKED", 0, -1.0f), 
    OPEN("OPEN", 1, 0.0f), 
    WALKABLE("WALKABLE", 2, 0.0f), 
    TRAPDOOR("TRAPDOOR", 3, 0.0f), 
    FENCE("FENCE", 4, -1.0f), 
    LAVA("LAVA", 5, -1.0f), 
    WATER("WATER", 6, 8.0f), 
    RAIL("RAIL", 7, 0.0f), 
    DANGER_FIRE("DANGER_FIRE", 8, 8.0f), 
    DAMAGE_FIRE("DAMAGE_FIRE", 9, 16.0f), 
    DANGER_CACTUS("DANGER_CACTUS", 10, 8.0f), 
    DAMAGE_CACTUS("DAMAGE_CACTUS", 11, -1.0f), 
    DANGER_OTHER("DANGER_OTHER", 12, 8.0f), 
    DAMAGE_OTHER("DAMAGE_OTHER", 13, -1.0f), 
    DOOR_OPEN("DOOR_OPEN", 14, 0.0f), 
    DOOR_WOOD_CLOSED("DOOR_WOOD_CLOSED", 15, -1.0f), 
    DOOR_IRON_CLOSED("DOOR_IRON_CLOSED", 16, -1.0f);
    
    private final float priority;
    
    private PathNodeType(final String s, final int n, final float priorityIn) {
        this.priority = priorityIn;
    }
    
    public float getPriority() {
        return this.priority;
    }
}
