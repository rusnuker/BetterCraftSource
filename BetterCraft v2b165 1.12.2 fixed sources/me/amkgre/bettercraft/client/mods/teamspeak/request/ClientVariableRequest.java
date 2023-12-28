// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

public class ClientVariableRequest extends Request
{
    private static final Parameter[] PARAMS;
    
    static {
        PARAMS = new Parameter[] { Request.array("client_unique_identifier", "client_nickname", "client_input_muted", "client_output_muted", "client_outputonly_muted", "client_input_hardware", "client_output_hardware", "client_meta_data", "client_is_recording", "client_database_id", "client_channel_group_id", "client_servergroups", "client_away", "client_away_message", "client_type", "client_flag_avatar", "client_talk_power", "client_talk_request", "client_talk_request_msg", "client_description", "client_is_talker", "client_is_priority_speaker", "client_unread_messages", "client_nickname_phonetic", "client_needed_serverquery_view_power", "client_icon_id", "client_is_channel_commander", "client_country", "client_channel_group_inherited_channel_id", "client_flag_talking", "client_is_muted", "client_volume_modificator", "client_version", "client_platform", "client_login_name", "client_created", "client_lastconnected", "client_totalconnections", "client_month_bytes_uploaded", "client_month_bytes_downloaded", "client_total_bytes_uploaded", "client_total_bytes_downloaded", "client_input_deactivated") };
    }
    
    public ClientVariableRequest(final int... clientIds) {
        super("clientvariable", new Parameter[] { build(clientIds) });
    }
    
    private static MultiParameter build(final int... clientIds) {
        if (clientIds.length == 0) {
            throw new IllegalArgumentException("Must provide at least one client id!");
        }
        final Parameter[][] result = new Parameter[clientIds.length][ClientVariableRequest.PARAMS.length + 1];
        for (int i = 0; i < clientIds.length; ++i) {
            final Parameter[] parameters = new Parameter[ClientVariableRequest.PARAMS.length + 1];
            parameters[0] = Request.value("clid", clientIds[i]);
            System.arraycopy(ClientVariableRequest.PARAMS, 0, parameters, 1, ClientVariableRequest.PARAMS.length);
            result[i] = parameters;
        }
        return new MultiParameter(result);
    }
}
