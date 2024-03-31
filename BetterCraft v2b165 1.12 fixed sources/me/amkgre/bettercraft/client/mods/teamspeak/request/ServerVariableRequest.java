// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ServerVariableRequest extends Request
{
    private static final Parameter[] PARAMS;
    
    static {
        PARAMS = new Parameter[] { Request.array("virtualserver_name", "virtualserver_unique_identifier", "virtualserver_platform", "virtualserver_version", "virtualserver_created", "virtualserver_codec_encryption_mode", "virtualserver_default_server_group", "virtualserver_default_channel_group", "virtualserver_hostbanner_url", "virtualserver_hostbanner_gfx_url", "virtualserver_hostbanner_gfx_interval", "virtualserver_priority_speaker_dimm_modificator", "virtualserver_id", "virtualserver_hostbutton_tooltip", "virtualserver_hostbutton_url", "virtualserver_hostbutton_gfx_url", "virtualserver_name_phonetic", "virtualserver_icon_id", "virtualserver_ip", "virtualserver_ask_for_privilegekey", "virtualserver_hostbanner_mode") };
    }
    
    public ServerVariableRequest() {
        super("servervariable", ServerVariableRequest.PARAMS);
    }
}
