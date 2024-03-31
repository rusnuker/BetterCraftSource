// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ClientUpdateRequest extends Request
{
    public ClientUpdateRequest(final Ident ident, final Object value) {
        super("clientupdate", new Parameter[] { Request.value(ident.getName(), value) });
    }
    
    public enum Ident
    {
        NICKNAME("NICKNAME", 0, "client_nickname"), 
        AWAY("AWAY", 1, "client_away"), 
        AWAY_MESSAGE("AWAY_MESSAGE", 2, "away_message"), 
        INPUT_MUTED("INPUT_MUTED", 3, "client_input_muted"), 
        OUTPUT_MUTED("OUTPUT_MUTED", 4, "client_output_muted"), 
        INPUT_DEACTIVATED("INPUT_DEACTIVATED", 5, "client_input_deactivated"), 
        CHANNEL_COMMANDER("CHANNEL_COMMANDER", 6, "client_is_channel_commander"), 
        NICKNAME_PHONETIC("NICKNAME_PHONETIC", 7, "client_nickname_phonetic"), 
        AVATAR("AVATAR", 8, "client_flag_avatar"), 
        META_DATA("META_DATA", 9, "client_meta_data"), 
        DEFAULT_TOKEN("DEFAULT_TOKEN", 10, "client_default_token");
        
        private String name;
        
        private Ident(final String s, final int n, final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
