// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.request;

import com.google.common.base.Strings;
import com.google.common.base.Objects;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ChannelCodec;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ChannelLifespan;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Channel;

public class ChannelEditRequest extends Request
{
    public ChannelEditRequest(final Channel channel, final String name, final String encodedPassword, final String topic, final String description, final ChannelLifespan lifespan, final boolean defaultChannel, final Channel parentChannel, final Channel orderChannel, final boolean bottomPosition, final int neededTalkPower, final ChannelCodec codec, final int codecQuality, final int maxClients) {
        super("channeledit", new Parameter[0]);
        this.addParam(Request.value("cid", channel.getId()));
        if (!Objects.equal(channel.getName(), name)) {
            this.addParam(Request.value("channel_name", name));
        }
        if (!Strings.isNullOrEmpty(encodedPassword)) {
            this.addParam(Request.value("channel_password", encodedPassword));
        }
        else if (channel.requiresPassword()) {
            this.addParam(Request.value("channel_password", ""));
        }
        if (!Objects.equal(Strings.isNullOrEmpty(channel.getTopic()) ? null : channel.getTopic(), Strings.isNullOrEmpty(topic) ? null : topic)) {
            this.addParam(Request.value("channel_topic", (topic == null) ? "" : topic));
        }
        if (!Objects.equal(Strings.isNullOrEmpty(channel.getDescription()) ? null : channel.getDescription(), Strings.isNullOrEmpty(description) ? null : description)) {
            this.addParam(Request.value("channel_description", (description == null) ? "" : description));
        }
        if (lifespan != null) {
            switch (lifespan) {
                case SEMI_PERMANENT: {
                    if (!channel.isSemiPermanent()) {
                        this.addParam(Request.value("channel_flag_semi_permanent", true));
                    }
                    if (!channel.isPermanent()) {
                        break;
                    }
                    this.addParam(Request.value("channel_flag_permanent", false));
                    break;
                }
                case PERMANENT: {
                    if (!channel.isPermanent()) {
                        this.addParam(Request.value("channel_flag_permanent", true));
                    }
                    if (!channel.isSemiPermanent()) {
                        break;
                    }
                    this.addParam(Request.value("channel_flag_semi_permanent", false));
                    break;
                }
                default: {
                    if (channel.isSemiPermanent()) {
                        this.addParam(Request.value("channel_flag_semi_permanent", false));
                    }
                    if (!channel.isPermanent()) {
                        break;
                    }
                    this.addParam(Request.value("channel_flag_permanent", false));
                    break;
                }
            }
        }
        if (channel.isDefault() != defaultChannel) {
            this.addParam(Request.value("channel_flag_default", defaultChannel));
        }
        if (!Objects.equal(channel.getParent(), parentChannel)) {
            this.addParam(Request.value("cpid", (parentChannel == null) ? 0 : parentChannel.getId()));
        }
        if (!bottomPosition && !Objects.equal(channel.getAbove(), orderChannel)) {
            this.addParam(Request.value("channel_order", (orderChannel == null) ? 0 : orderChannel.getId()));
        }
        if (channel.getNeededTalkPower() != neededTalkPower) {
            this.addParam(Request.value("channel_needed_talk_power", neededTalkPower));
        }
        if (channel.getCodec() != codec) {
            this.addParam(Request.value("channel_codec", (codec == null) ? ChannelCodec.OPUS_VOICE.getId() : codec.getId()));
        }
        if (channel.getCodecQuality() != codecQuality) {
            this.addParam(Request.value("channel_codec_quality", codecQuality));
        }
        if (channel.getMaxClients() != maxClients) {
            this.addParam(Request.value("channel_maxclients", maxClients));
            if (maxClients == -1) {
                this.addParam(Request.value("channel_flag_maxclients_unlimited", true));
            }
            else {
                this.addParam(Request.value("channel_flag_maxclients_unlimited", false));
            }
        }
    }
    
    public ChannelEditRequest(final int channelId, final int orderId) {
        super("channeledit", new Parameter[0]);
        this.addParam(Request.value("cid", channelId));
        this.addParam(Request.value("channel_order", orderId));
    }
}
