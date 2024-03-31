// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage;

import net.minecraft.util.math.MathHelper;

public class MapDecoration
{
    private final Type field_191181_a;
    private byte x;
    private byte y;
    private byte rotation;
    
    public MapDecoration(final Type p_i47236_1_, final byte p_i47236_2_, final byte p_i47236_3_, final byte p_i47236_4_) {
        this.field_191181_a = p_i47236_1_;
        this.x = p_i47236_2_;
        this.y = p_i47236_3_;
        this.rotation = p_i47236_4_;
    }
    
    public byte getType() {
        return this.field_191181_a.func_191163_a();
    }
    
    public Type func_191179_b() {
        return this.field_191181_a;
    }
    
    public byte getX() {
        return this.x;
    }
    
    public byte getY() {
        return this.y;
    }
    
    public byte getRotation() {
        return this.rotation;
    }
    
    public boolean func_191180_f() {
        return this.field_191181_a.func_191160_b();
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof MapDecoration)) {
            return false;
        }
        final MapDecoration mapdecoration = (MapDecoration)p_equals_1_;
        return this.field_191181_a == mapdecoration.field_191181_a && this.rotation == mapdecoration.rotation && this.x == mapdecoration.x && this.y == mapdecoration.y;
    }
    
    @Override
    public int hashCode() {
        int i = this.field_191181_a.func_191163_a();
        i = 31 * i + this.x;
        i = 31 * i + this.y;
        i = 31 * i + this.rotation;
        return i;
    }
    
    public enum Type
    {
        PLAYER("PLAYER", 0, false), 
        FRAME("FRAME", 1, true), 
        RED_MARKER("RED_MARKER", 2, false), 
        BLUE_MARKER("BLUE_MARKER", 3, false), 
        TARGET_X("TARGET_X", 4, true), 
        TARGET_POINT("TARGET_POINT", 5, true), 
        PLAYER_OFF_MAP("PLAYER_OFF_MAP", 6, false), 
        PLAYER_OFF_LIMITS("PLAYER_OFF_LIMITS", 7, false), 
        MANSION("MANSION", 8, true, 5393476), 
        MONUMENT("MONUMENT", 9, true, 3830373);
        
        private final byte field_191175_k;
        private final boolean field_191176_l;
        private final int field_191177_m;
        
        private Type(final String s, final int n, final boolean p_i47343_3_) {
            this(s, n, p_i47343_3_, -1);
        }
        
        private Type(final String s, final int n, final boolean p_i47344_3_, final int p_i47344_4_) {
            this.field_191175_k = (byte)this.ordinal();
            this.field_191176_l = p_i47344_3_;
            this.field_191177_m = p_i47344_4_;
        }
        
        public byte func_191163_a() {
            return this.field_191175_k;
        }
        
        public boolean func_191160_b() {
            return this.field_191176_l;
        }
        
        public boolean func_191162_c() {
            return this.field_191177_m >= 0;
        }
        
        public int func_191161_d() {
            return this.field_191177_m;
        }
        
        public static Type func_191159_a(final byte p_191159_0_) {
            return values()[MathHelper.clamp(p_191159_0_, 0, values().length - 1)];
        }
    }
}
