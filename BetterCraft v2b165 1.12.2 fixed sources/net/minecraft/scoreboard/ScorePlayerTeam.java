// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.scoreboard;

import javax.annotation.Nullable;
import java.util.Collection;
import com.google.common.collect.Sets;
import net.minecraft.util.text.TextFormatting;
import java.util.Set;

public class ScorePlayerTeam extends Team
{
    private final Scoreboard theScoreboard;
    private final String registeredName;
    private final Set<String> membershipSet;
    private String teamNameSPT;
    private String namePrefixSPT;
    private String colorSuffix;
    private boolean allowFriendlyFire;
    private boolean canSeeFriendlyInvisibles;
    private EnumVisible nameTagVisibility;
    private EnumVisible deathMessageVisibility;
    private TextFormatting chatFormat;
    private CollisionRule collisionRule;
    
    public ScorePlayerTeam(final Scoreboard theScoreboardIn, final String name) {
        this.membershipSet = (Set<String>)Sets.newHashSet();
        this.namePrefixSPT = "";
        this.colorSuffix = "";
        this.allowFriendlyFire = true;
        this.canSeeFriendlyInvisibles = true;
        this.nameTagVisibility = EnumVisible.ALWAYS;
        this.deathMessageVisibility = EnumVisible.ALWAYS;
        this.chatFormat = TextFormatting.RESET;
        this.collisionRule = CollisionRule.ALWAYS;
        this.theScoreboard = theScoreboardIn;
        this.registeredName = name;
        this.teamNameSPT = name;
    }
    
    @Override
    public String getRegisteredName() {
        return this.registeredName;
    }
    
    public String getTeamName() {
        return this.teamNameSPT;
    }
    
    public void setTeamName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.teamNameSPT = name;
        this.theScoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public Collection<String> getMembershipCollection() {
        return this.membershipSet;
    }
    
    public String getColorPrefix() {
        return this.namePrefixSPT;
    }
    
    public void setNamePrefix(final String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        this.namePrefixSPT = prefix;
        this.theScoreboard.broadcastTeamInfoUpdate(this);
    }
    
    public String getColorSuffix() {
        return this.colorSuffix;
    }
    
    public void setNameSuffix(final String suffix) {
        this.colorSuffix = suffix;
        this.theScoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public String formatString(final String input) {
        return String.valueOf(this.getColorPrefix()) + input + this.getColorSuffix();
    }
    
    public static String formatPlayerName(@Nullable final Team teamIn, final String string) {
        return (teamIn == null) ? string : teamIn.formatString(string);
    }
    
    @Override
    public boolean getAllowFriendlyFire() {
        return this.allowFriendlyFire;
    }
    
    public void setAllowFriendlyFire(final boolean friendlyFire) {
        this.allowFriendlyFire = friendlyFire;
        this.theScoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public boolean getSeeFriendlyInvisiblesEnabled() {
        return this.canSeeFriendlyInvisibles;
    }
    
    public void setSeeFriendlyInvisiblesEnabled(final boolean friendlyInvisibles) {
        this.canSeeFriendlyInvisibles = friendlyInvisibles;
        this.theScoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public EnumVisible getNameTagVisibility() {
        return this.nameTagVisibility;
    }
    
    @Override
    public EnumVisible getDeathMessageVisibility() {
        return this.deathMessageVisibility;
    }
    
    public void setNameTagVisibility(final EnumVisible visibility) {
        this.nameTagVisibility = visibility;
        this.theScoreboard.broadcastTeamInfoUpdate(this);
    }
    
    public void setDeathMessageVisibility(final EnumVisible visibility) {
        this.deathMessageVisibility = visibility;
        this.theScoreboard.broadcastTeamInfoUpdate(this);
    }
    
    @Override
    public CollisionRule getCollisionRule() {
        return this.collisionRule;
    }
    
    public void setCollisionRule(final CollisionRule rule) {
        this.collisionRule = rule;
        this.theScoreboard.broadcastTeamInfoUpdate(this);
    }
    
    public int getFriendlyFlags() {
        int i = 0;
        if (this.getAllowFriendlyFire()) {
            i |= 0x1;
        }
        if (this.getSeeFriendlyInvisiblesEnabled()) {
            i |= 0x2;
        }
        return i;
    }
    
    public void setFriendlyFlags(final int flags) {
        this.setAllowFriendlyFire((flags & 0x1) > 0);
        this.setSeeFriendlyInvisiblesEnabled((flags & 0x2) > 0);
    }
    
    public void setChatFormat(final TextFormatting format) {
        this.chatFormat = format;
    }
    
    @Override
    public TextFormatting getChatFormat() {
        return this.chatFormat;
    }
}
