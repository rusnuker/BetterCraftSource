// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Collection;
import net.minecraft.util.text.TextFormatting;
import javax.annotation.Nullable;

public abstract class Team
{
    public boolean isSameTeam(@Nullable final Team other) {
        return other != null && this == other;
    }
    
    public abstract String getRegisteredName();
    
    public abstract String formatString(final String p0);
    
    public abstract boolean getSeeFriendlyInvisiblesEnabled();
    
    public abstract boolean getAllowFriendlyFire();
    
    public abstract EnumVisible getNameTagVisibility();
    
    public abstract TextFormatting getChatFormat();
    
    public abstract Collection<String> getMembershipCollection();
    
    public abstract EnumVisible getDeathMessageVisibility();
    
    public abstract CollisionRule getCollisionRule();
    
    public enum CollisionRule
    {
        ALWAYS("ALWAYS", 0, "always", 0), 
        NEVER("NEVER", 1, "never", 1), 
        HIDE_FOR_OTHER_TEAMS("HIDE_FOR_OTHER_TEAMS", 2, "pushOtherTeams", 2), 
        HIDE_FOR_OWN_TEAM("HIDE_FOR_OWN_TEAM", 3, "pushOwnTeam", 3);
        
        private static final Map<String, CollisionRule> nameMap;
        public final String name;
        public final int id;
        
        static {
            nameMap = Maps.newHashMap();
            CollisionRule[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final CollisionRule team$collisionrule = values[i];
                CollisionRule.nameMap.put(team$collisionrule.name, team$collisionrule);
            }
        }
        
        public static String[] getNames() {
            return CollisionRule.nameMap.keySet().toArray(new String[CollisionRule.nameMap.size()]);
        }
        
        @Nullable
        public static CollisionRule getByName(final String nameIn) {
            return CollisionRule.nameMap.get(nameIn);
        }
        
        private CollisionRule(final String s, final int n, final String nameIn, final int idIn) {
            this.name = nameIn;
            this.id = idIn;
        }
    }
    
    public enum EnumVisible
    {
        ALWAYS("ALWAYS", 0, "always", 0), 
        NEVER("NEVER", 1, "never", 1), 
        HIDE_FOR_OTHER_TEAMS("HIDE_FOR_OTHER_TEAMS", 2, "hideForOtherTeams", 2), 
        HIDE_FOR_OWN_TEAM("HIDE_FOR_OWN_TEAM", 3, "hideForOwnTeam", 3);
        
        private static final Map<String, EnumVisible> nameMap;
        public final String internalName;
        public final int id;
        
        static {
            nameMap = Maps.newHashMap();
            EnumVisible[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumVisible team$enumvisible = values[i];
                EnumVisible.nameMap.put(team$enumvisible.internalName, team$enumvisible);
            }
        }
        
        public static String[] getNames() {
            return EnumVisible.nameMap.keySet().toArray(new String[EnumVisible.nameMap.size()]);
        }
        
        @Nullable
        public static EnumVisible getByName(final String nameIn) {
            return EnumVisible.nameMap.get(nameIn);
        }
        
        private EnumVisible(final String s, final int n, final String nameIn, final int idIn) {
            this.internalName = nameIn;
            this.id = idIn;
        }
    }
}
