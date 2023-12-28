// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import javax.annotation.Nullable;
import java.util.Set;
import com.google.common.collect.Maps;
import java.util.Map;

public enum EnumParticleTypes
{
    EXPLOSION_NORMAL("EXPLOSION_NORMAL", 0, "explode", 0, true), 
    EXPLOSION_LARGE("EXPLOSION_LARGE", 1, "largeexplode", 1, true), 
    EXPLOSION_HUGE("EXPLOSION_HUGE", 2, "hugeexplosion", 2, true), 
    FIREWORKS_SPARK("FIREWORKS_SPARK", 3, "fireworksSpark", 3, false), 
    WATER_BUBBLE("WATER_BUBBLE", 4, "bubble", 4, false), 
    WATER_SPLASH("WATER_SPLASH", 5, "splash", 5, false), 
    WATER_WAKE("WATER_WAKE", 6, "wake", 6, false), 
    SUSPENDED("SUSPENDED", 7, "suspended", 7, false), 
    SUSPENDED_DEPTH("SUSPENDED_DEPTH", 8, "depthsuspend", 8, false), 
    CRIT("CRIT", 9, "crit", 9, false), 
    CRIT_MAGIC("CRIT_MAGIC", 10, "magicCrit", 10, false), 
    SMOKE_NORMAL("SMOKE_NORMAL", 11, "smoke", 11, false), 
    SMOKE_LARGE("SMOKE_LARGE", 12, "largesmoke", 12, false), 
    SPELL("SPELL", 13, "spell", 13, false), 
    SPELL_INSTANT("SPELL_INSTANT", 14, "instantSpell", 14, false), 
    SPELL_MOB("SPELL_MOB", 15, "mobSpell", 15, false), 
    SPELL_MOB_AMBIENT("SPELL_MOB_AMBIENT", 16, "mobSpellAmbient", 16, false), 
    SPELL_WITCH("SPELL_WITCH", 17, "witchMagic", 17, false), 
    DRIP_WATER("DRIP_WATER", 18, "dripWater", 18, false), 
    DRIP_LAVA("DRIP_LAVA", 19, "dripLava", 19, false), 
    VILLAGER_ANGRY("VILLAGER_ANGRY", 20, "angryVillager", 20, false), 
    VILLAGER_HAPPY("VILLAGER_HAPPY", 21, "happyVillager", 21, false), 
    TOWN_AURA("TOWN_AURA", 22, "townaura", 22, false), 
    NOTE("NOTE", 23, "note", 23, false), 
    PORTAL("PORTAL", 24, "portal", 24, false), 
    ENCHANTMENT_TABLE("ENCHANTMENT_TABLE", 25, "enchantmenttable", 25, false), 
    FLAME("FLAME", 26, "flame", 26, false), 
    LAVA("LAVA", 27, "lava", 27, false), 
    FOOTSTEP("FOOTSTEP", 28, "footstep", 28, false), 
    CLOUD("CLOUD", 29, "cloud", 29, false), 
    REDSTONE("REDSTONE", 30, "reddust", 30, false), 
    SNOWBALL("SNOWBALL", 31, "snowballpoof", 31, false), 
    SNOW_SHOVEL("SNOW_SHOVEL", 32, "snowshovel", 32, false), 
    SLIME("SLIME", 33, "slime", 33, false), 
    HEART("HEART", 34, "heart", 34, false), 
    BARRIER("BARRIER", 35, "barrier", 35, false), 
    ITEM_CRACK("ITEM_CRACK", 36, "iconcrack", 36, false, 2), 
    BLOCK_CRACK("BLOCK_CRACK", 37, "blockcrack", 37, false, 1), 
    BLOCK_DUST("BLOCK_DUST", 38, "blockdust", 38, false, 1), 
    WATER_DROP("WATER_DROP", 39, "droplet", 39, false), 
    ITEM_TAKE("ITEM_TAKE", 40, "take", 40, false), 
    MOB_APPEARANCE("MOB_APPEARANCE", 41, "mobappearance", 41, true), 
    DRAGON_BREATH("DRAGON_BREATH", 42, "dragonbreath", 42, false), 
    END_ROD("END_ROD", 43, "endRod", 43, false), 
    DAMAGE_INDICATOR("DAMAGE_INDICATOR", 44, "damageIndicator", 44, true), 
    SWEEP_ATTACK("SWEEP_ATTACK", 45, "sweepAttack", 45, true), 
    FALLING_DUST("FALLING_DUST", 46, "fallingdust", 46, false, 1), 
    TOTEM("TOTEM", 47, "totem", 47, false), 
    SPIT("SPIT", 48, "spit", 48, true);
    
    private final String particleName;
    private final int particleID;
    private final boolean shouldIgnoreRange;
    private final int argumentCount;
    private static final Map<Integer, EnumParticleTypes> PARTICLES;
    private static final Map<String, EnumParticleTypes> BY_NAME;
    
    static {
        PARTICLES = Maps.newHashMap();
        BY_NAME = Maps.newHashMap();
        EnumParticleTypes[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumParticleTypes enumparticletypes = values[i];
            EnumParticleTypes.PARTICLES.put(enumparticletypes.getParticleID(), enumparticletypes);
            EnumParticleTypes.BY_NAME.put(enumparticletypes.getParticleName(), enumparticletypes);
        }
    }
    
    private EnumParticleTypes(final String s, final int n, final String particleNameIn, final int particleIDIn, final boolean shouldIgnoreRangeIn, final int argumentCountIn) {
        this.particleName = particleNameIn;
        this.particleID = particleIDIn;
        this.shouldIgnoreRange = shouldIgnoreRangeIn;
        this.argumentCount = argumentCountIn;
    }
    
    private EnumParticleTypes(final String s, final int n, final String particleNameIn, final int particleIDIn, final boolean shouldIgnoreRangeIn) {
        this(s, n, particleNameIn, particleIDIn, shouldIgnoreRangeIn, 0);
    }
    
    public static Set<String> getParticleNames() {
        return EnumParticleTypes.BY_NAME.keySet();
    }
    
    public String getParticleName() {
        return this.particleName;
    }
    
    public int getParticleID() {
        return this.particleID;
    }
    
    public int getArgumentCount() {
        return this.argumentCount;
    }
    
    public boolean getShouldIgnoreRange() {
        return this.shouldIgnoreRange;
    }
    
    @Nullable
    public static EnumParticleTypes getParticleFromId(final int particleId) {
        return EnumParticleTypes.PARTICLES.get(particleId);
    }
    
    @Nullable
    public static EnumParticleTypes getByName(final String nameIn) {
        return EnumParticleTypes.BY_NAME.get(nameIn);
    }
}
