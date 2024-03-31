// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.botattack;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class PacketUtils
{
    public static byte[] readByteArray(final DataInputStream in) {
        final int length = readVarInt(in);
        final byte[] data = new byte[length];
        try {
            in.readFully(data);
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
        return data;
    }
    
    public static int readVarInt(final DataInputStream in) {
        int numRead = 0;
        int result = 0;
        try {
            byte read;
            do {
                read = in.readByte();
                final int value = read & 0x7F;
                result |= value << 7 * numRead;
                if (++numRead > 5) {
                    throw new RuntimeException("VarInt is too big");
                }
            } while ((read & 0x80) != 0x0);
        }
        catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }
    
    public static void writeVarInt(final DataOutputStream out, int paramInt) {
        try {
            while ((paramInt & 0xFFFFFF80) != 0x0) {
                out.writeByte((paramInt & 0x7F) | 0x80);
                paramInt >>>= 7;
            }
            out.writeByte(paramInt);
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void writeVarIntException(final DataOutputStream out, int paramInt) throws IOException {
        while ((paramInt & 0xFFFFFF80) != 0x0) {
            out.writeByte((paramInt & 0x7F) | 0x80);
            paramInt >>>= 7;
        }
        out.writeByte(paramInt);
    }
    
    public static void writeVarInt(final ByteArrayOutputStream out, int paramInt) {
        while ((paramInt & 0xFFFFFF80) != 0x0) {
            out.write((paramInt & 0x7F) | 0x80);
            paramInt >>>= 7;
        }
        out.write(paramInt);
    }
    
    public static void writeByteArray(final DataOutputStream out, final byte[] data) {
        try {
            writeVarInt(out, data.length);
            out.write(data, 0, data.length);
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void writePacket(final DataOutputStream out, final byte[] packet) {
        try {
            writeVarInt(out, packet.length);
            out.write(packet);
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void writePacketException(final DataOutputStream out, final byte[] packet) throws IOException {
        writeVarIntException(out, packet.length);
        out.write(packet);
    }
    
    public static byte[] serialize(final Object obj) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static Object deserialize(final byte[] data) {
        try {
            final ByteArrayInputStream in = new ByteArrayInputStream(data);
            final ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }
        catch (final IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static byte[] createEncryptionResponsePacket(final byte[] encryptedKey, final byte[] encryptedVerifyToken) {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bytes);
        writeVarInt(out, 1);
        writeByteArray(out, encryptedKey);
        writeByteArray(out, encryptedVerifyToken);
        final byte[] data = bytes.toByteArray();
        try {
            bytes.close();
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
        return data;
    }
    
    public static byte[] createHandshakeMessage(final String host, final int port, final int state) {
        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final DataOutputStream handshake = new DataOutputStream(buffer);
            handshake.writeByte(0);
            writeVarInt(handshake, 578);
            writeString(handshake, host, StandardCharsets.UTF_8);
            handshake.writeShort(port);
            writeVarInt(handshake, state);
            return buffer.toByteArray();
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static byte[] createLogin(final String username) {
        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final DataOutputStream login = new DataOutputStream(buffer);
            login.writeByte(0);
            writeString(login, username, StandardCharsets.UTF_8);
            return buffer.toByteArray();
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void writeString(final DataOutputStream out, final String string, final Charset charset) {
        try {
            final byte[] bytes = string.getBytes(charset);
            writeVarInt(out, bytes.length);
            out.write(bytes);
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void writeString(final ByteArrayOutputStream out, final String string, final Charset charset) {
        try {
            final byte[] bytes = string.getBytes(charset);
            writeVarInt(out, bytes.length);
            out.write(bytes);
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void sendPacket(final byte[] packet, final DataOutputStream out) {
        writePacket(out, packet);
    }
    
    public static void sendPacketException(final byte[] packet, final DataOutputStream out) throws IOException {
        writePacketException(out, packet);
    }
}
