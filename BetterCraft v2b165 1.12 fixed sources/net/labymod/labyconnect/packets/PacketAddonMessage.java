// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.packets;

import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import net.labymod.labyconnect.handling.PacketHandler;

public class PacketAddonMessage extends Packet
{
    private String key;
    private byte[] data;
    
    public PacketAddonMessage(final String key, final byte[] data) {
        this.key = key;
        this.data = data;
    }
    
    public PacketAddonMessage(final String key, final String json) {
        this.key = key;
        this.data = this.toBytes(json);
    }
    
    public PacketAddonMessage() {
    }
    
    @Override
    public void read(final PacketBuf buf) {
        this.key = buf.readString();
        final byte[] abyte = new byte[buf.readInt()];
        buf.readBytes(abyte);
        this.data = abyte;
    }
    
    @Override
    public void write(final PacketBuf buf) {
        buf.writeString(this.key);
        buf.writeInt(this.data.length);
        buf.writeBytes(this.data);
    }
    
    @Override
    public void handle(final PacketHandler packetHandler) {
        packetHandler.handle(this);
    }
    
    public String getJson() {
        try {
            final StringBuilder stringbuilder = new StringBuilder();
            if (this.data != null && this.data.length != 0) {
                if (this.isCompressed(this.data)) {
                    final GZIPInputStream gzipinputstream = new GZIPInputStream(new ByteArrayInputStream(this.data));
                    final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(gzipinputstream, StandardCharsets.UTF_8));
                    String s;
                    while ((s = bufferedreader.readLine()) != null) {
                        stringbuilder.append(s);
                    }
                }
                else {
                    stringbuilder.append(Arrays.toString(this.data));
                }
                return stringbuilder.toString();
            }
            return "";
        }
        catch (final IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
    }
    
    private byte[] toBytes(final String in) {
        final byte[] abyte = in.getBytes(StandardCharsets.UTF_8);
        try {
            if (abyte != null && abyte.length != 0) {
                final ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                final GZIPOutputStream gzipoutputstream = new GZIPOutputStream(bytearrayoutputstream);
                gzipoutputstream.write(abyte);
                gzipoutputstream.flush();
                gzipoutputstream.close();
                return bytearrayoutputstream.toByteArray();
            }
            return new byte[0];
        }
        catch (final IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
    }
    
    private boolean isCompressed(final byte[] compressed) {
        return compressed[0] == 31 && compressed[1] == -117;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public byte[] getData() {
        return this.data;
    }
}
