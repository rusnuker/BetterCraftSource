// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import com.google.common.base.Strings;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ChannelCodec;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Channel;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ChannelLifespan;

public class ChannelCreateRequest extends Request
{
    public ChannelCreateRequest(final String name, final String encodedPassword, final String topic, final String description, final ChannelLifespan lifespan, final boolean defaultChannel, final Channel parentChannel, final Channel orderChannel, final boolean bottomPosition, final int neededTalkPower, final ChannelCodec codec, final int codecQuality, final int maxClients) {
        super("channelcreate", new Parameter[0]);
        this.addParam(Request.value("channel_name", name));
        if (!Strings.isNullOrEmpty(encodedPassword)) {
            this.addParam(Request.value("channel_password", encodedPassword));
        }
        if (!Strings.isNullOrEmpty(topic)) {
            this.addParam(Request.value("channel_topic", topic));
        }
        if (!Strings.isNullOrEmpty(description)) {
            this.addParam(Request.value("channel_description", description));
        }
        if (lifespan != null) {
            switch (lifespan) {
                case SEMI_PERMANENT: {
                    this.addParam(Request.value("channel_flag_semi_permanent", true));
                    break;
                }
                case PERMANENT: {
                    this.addParam(Request.value("channel_flag_permanent", true));
                    break;
                }
            }
        }
        if (defaultChannel) {
            this.addParam(Request.value("channel_flag_default", true));
        }
        this.addParam(Request.value("cpid", (parentChannel == null) ? 0 : parentChannel.getId()));
        if (!bottomPosition) {
            this.addParam(Request.value("channel_order", (orderChannel == null) ? 0 : orderChannel.getId()));
        }
        if (neededTalkPower != 0) {
            this.addParam(Request.value("channel_needed_talk_power", neededTalkPower));
        }
        if (codec != null) {
            this.addParam(Request.value("channel_codec", codec.getId()));
        }
        this.addParam(Request.value("channel_codec_quality", codecQuality));
        this.addParam(Request.value("channel_maxclients", maxClients));
        if (maxClients == -1) {
            this.addParam(Request.value("channel_flag_maxclients_unlimited", true));
        }
        else {
            this.addParam(Request.value("channel_flag_maxclients_unlimited", false));
        }
    }
}
