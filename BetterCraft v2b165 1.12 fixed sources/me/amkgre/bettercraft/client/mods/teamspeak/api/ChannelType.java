// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

import java.util.regex.Matcher;
import org.apache.logging.log4j.LogManager;
import java.util.regex.Pattern;

public enum ChannelType
{
    NORMAL("NORMAL", 0, (Pattern)null), 
    CSPACER("CSPACER", 1, Pattern.compile("\\[cspacer.*\\](.+)")), 
    SPACER("SPACER", 2, Pattern.compile("\\[\\*[c]?spacer.*\\](.+)")), 
    SPACER_TEXT("SPACER_TEXT", 3, Pattern.compile("\\[spacer.*\\](.+)"));
    
    private Pattern pattern;
    
    private ChannelType(final String s, final int n, final Pattern pattern) {
        this.pattern = pattern;
    }
    
    public String formatName(final String name) {
        if (this.pattern == null) {
            return name;
        }
        final Matcher matcher = this.pattern.matcher(name);
        if (!matcher.matches()) {
            LogManager.getLogger().error("Could not match pattern of channel type " + this.toString() + " for name " + name + "!");
            return name;
        }
        return matcher.group(1);
    }
    
    public static ChannelType byName(final String name) {
        ChannelType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final ChannelType channelType = values[i];
            final Matcher matcher;
            if (channelType.pattern != null && (matcher = channelType.pattern.matcher(name)).matches()) {
                return channelType;
            }
        }
        return ChannelType.NORMAL;
    }
}
