// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import java.util.Set;
import com.google.common.collect.Maps;
import java.util.Map;

public enum SoundCategory
{
    MASTER("MASTER", 0, "master"), 
    MUSIC("MUSIC", 1, "music"), 
    RECORDS("RECORDS", 2, "record"), 
    WEATHER("WEATHER", 3, "weather"), 
    BLOCKS("BLOCKS", 4, "block"), 
    HOSTILE("HOSTILE", 5, "hostile"), 
    NEUTRAL("NEUTRAL", 6, "neutral"), 
    PLAYERS("PLAYERS", 7, "player"), 
    AMBIENT("AMBIENT", 8, "ambient"), 
    VOICE("VOICE", 9, "voice");
    
    private static final Map<String, SoundCategory> SOUND_CATEGORIES;
    private final String name;
    
    static {
        SOUND_CATEGORIES = Maps.newHashMap();
        SoundCategory[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final SoundCategory soundcategory = values[i];
            if (SoundCategory.SOUND_CATEGORIES.containsKey(soundcategory.getName())) {
                throw new Error("Clash in Sound Category name pools! Cannot insert " + soundcategory);
            }
            SoundCategory.SOUND_CATEGORIES.put(soundcategory.getName(), soundcategory);
        }
    }
    
    private SoundCategory(final String s, final int n, final String nameIn) {
        this.name = nameIn;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static SoundCategory getByName(final String categoryName) {
        return SoundCategory.SOUND_CATEGORIES.get(categoryName);
    }
    
    public static Set<String> getSoundCategoryNames() {
        return SoundCategory.SOUND_CATEGORIES.keySet();
    }
}
