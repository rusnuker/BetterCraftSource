// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

public enum ChannelCodec
{
    SPEEX_NARROWBAND("SPEEX_NARROWBAND", 0, 0), 
    SPEEX_WIDEBAND("SPEEX_WIDEBAND", 1, 1), 
    SPEEX_ULTRA_WIDEBAND("SPEEX_ULTRA_WIDEBAND", 2, 2), 
    CELT_MONO("CELT_MONO", 3, 3), 
    OPUS_VOICE("OPUS_VOICE", 4, 4), 
    OPUS_MUSIC("OPUS_MUSIC", 5, 5);
    
    private int id;
    
    private ChannelCodec(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static ChannelCodec byId(final int id) {
        ChannelCodec[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final ChannelCodec channelCodec = values[i];
            if (channelCodec.getId() == id) {
                return channelCodec;
            }
        }
        return ChannelCodec.SPEEX_NARROWBAND;
    }
}
