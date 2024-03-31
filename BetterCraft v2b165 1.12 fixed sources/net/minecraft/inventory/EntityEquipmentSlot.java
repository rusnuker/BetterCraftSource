// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.inventory;

public enum EntityEquipmentSlot
{
    MAINHAND("MAINHAND", 0, Type.HAND, 0, 0, "mainhand"), 
    OFFHAND("OFFHAND", 1, Type.HAND, 1, 5, "offhand"), 
    FEET("FEET", 2, Type.ARMOR, 0, 1, "feet"), 
    LEGS("LEGS", 3, Type.ARMOR, 1, 2, "legs"), 
    CHEST("CHEST", 4, Type.ARMOR, 2, 3, "chest"), 
    HEAD("HEAD", 5, Type.ARMOR, 3, 4, "head");
    
    private final Type slotType;
    private final int index;
    private final int slotIndex;
    private final String name;
    
    private EntityEquipmentSlot(final String s, final int n, final Type slotTypeIn, final int indexIn, final int slotIndexIn, final String nameIn) {
        this.slotType = slotTypeIn;
        this.index = indexIn;
        this.slotIndex = slotIndexIn;
        this.name = nameIn;
    }
    
    public Type getSlotType() {
        return this.slotType;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public int getSlotIndex() {
        return this.slotIndex;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static EntityEquipmentSlot fromString(final String targetName) {
        EntityEquipmentSlot[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EntityEquipmentSlot entityequipmentslot = values[i];
            if (entityequipmentslot.getName().equals(targetName)) {
                return entityequipmentslot;
            }
        }
        throw new IllegalArgumentException("Invalid slot '" + targetName + "'");
    }
    
    public enum Type
    {
        HAND("HAND", 0), 
        ARMOR("ARMOR", 1);
        
        private Type(final String s, final int n) {
        }
    }
}
