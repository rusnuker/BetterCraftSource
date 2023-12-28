// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.response;

import me.amkgre.bettercraft.client.mods.teamspeak.util.EscapeUtil;
import java.util.HashMap;

public class TeamSpeakResponse
{
    private final String message;
    
    protected TeamSpeakResponse(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message.replace("\n", "").replace("\r", "");
    }
    
    public HashMap<String, String> getParsedResponse() {
        return parse(this.getMessage());
    }
    
    public String getRawMessage() {
        return this.message;
    }
    
    public static HashMap<String, String> parse(final String message) {
        final HashMap<String, String> parsedResponse = new HashMap<String, String>();
        String[] split;
        for (int length = (split = message.split(" ")).length, i = 0; i < length; ++i) {
            final String arg = split[i];
            final int index = arg.indexOf("=");
            if (index == -1) {
                parsedResponse.put(arg, "");
            }
            else {
                final String key = arg.substring(0, index);
                final String value = EscapeUtil.unescape(arg.substring(index + 1, arg.length()));
                parsedResponse.put(key, value);
            }
        }
        return parsedResponse;
    }
    
    @Override
    public String toString() {
        return "TeamSpeakResponse{message='" + this.getMessage() + '\'' + '}';
    }
}
