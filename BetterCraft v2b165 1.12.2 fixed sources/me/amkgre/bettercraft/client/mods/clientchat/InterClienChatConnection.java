// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.clientchat;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import java.util.Iterator;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
import java.net.Socket;

public class InterClienChatConnection
{
    static InterClienChatConnection self;
    private Socket connection;
    public static List<String> msgs;
    public static int seenMsgs;
    private SynchronKeyHandler packetInterface;
    private ReceiverThread thread;
    private String lastMesuredIp;
    public static ArrayList<String> onlinePlayers;
    public static boolean hasLostConnection;
    private static String user;
    
    static {
        InterClienChatConnection.msgs = new ArrayList<String>();
        InterClienChatConnection.onlinePlayers = new ArrayList<String>();
        InterClienChatConnection.hasLostConnection = true;
    }
    
    public InterClienChatConnection() {
        this.lastMesuredIp = null;
        InterClienChatConnection.self = this;
        try {
            this.connection = new Socket(new Object() {
                int t;
                
                @Override
                public String toString() {
                    final byte[] buf = new byte[16];
                    this.t = 1938262053;
                    buf[0] = (byte)(this.t >>> 24);
                    this.t = 872574996;
                    buf[1] = (byte)(this.t >>> 23);
                    this.t = -2092054180;
                    buf[2] = (byte)(this.t >>> 19);
                    this.t = -1710019511;
                    buf[3] = (byte)(this.t >>> 11);
                    this.t = -1973523282;
                    buf[4] = (byte)(this.t >>> 8);
                    this.t = -659017150;
                    buf[5] = (byte)(this.t >>> 7);
                    this.t = 835656872;
                    buf[6] = (byte)(this.t >>> 23);
                    this.t = 1527612142;
                    buf[7] = (byte)(this.t >>> 22);
                    this.t = -1429077109;
                    buf[8] = (byte)(this.t >>> 17);
                    this.t = -1398557160;
                    buf[9] = (byte)(this.t >>> 21);
                    this.t = 1142654307;
                    buf[10] = (byte)(this.t >>> 14);
                    this.t = 1977433488;
                    buf[11] = (byte)(this.t >>> 14);
                    this.t = -1440117865;
                    buf[12] = (byte)(this.t >>> 6);
                    this.t = -1226407894;
                    buf[13] = (byte)(this.t >>> 20);
                    this.t = -1026908093;
                    buf[14] = (byte)(this.t >>> 17);
                    this.t = 955767528;
                    buf[15] = (byte)(this.t >>> 1);
                    return new String(buf);
                }
            }.toString(), 51680);
            this.packetInterface = new SynchronKeyHandler(this.connection.getInputStream(), this.connection.getOutputStream());
            this.thread = new ReceiverThread(this.packetInterface);
            final SynchronKeyHandler packetInterface = this.packetInterface;
            final int id = 1;
            final String[] content = { null };
            final int n = 0;
            Minecraft.getMinecraft();
            content[n] = (InterClienChatConnection.user = Minecraft.session.getUsername());
            packetInterface.writePacket(makePacket(id, content));
            Minecraft.getMinecraft();
            updateAccountName(Minecraft.session.getUsername());
            if (InterClienChatConnection.hasLostConnection) {
                InterClienChatConnection.hasLostConnection = false;
            }
        }
        catch (final Exception e) {
            InterClienChatConnection.hasLostConnection = true;
        }
    }
    
    public static void sendJoinServer(final String server) {
        try {
            InterClienChatConnection.self.packetInterface.writePacket(makePacket(5, server));
        }
        catch (final Exception ex) {}
    }
    
    public static void sendLeaveServer() {
        try {
            InterClienChatConnection.self.packetInterface.writePacket(new byte[] { 6 });
        }
        catch (final Exception ex) {}
    }
    
    public static void updateIp(final String ip) {
        if (ip != null) {
            if (InterClienChatConnection.self.lastMesuredIp == null) {
                sendJoinServer(ip);
            }
            else if (!InterClienChatConnection.self.lastMesuredIp.equals(ip)) {
                sendJoinServer(ip);
            }
        }
        InterClienChatConnection.self.lastMesuredIp = ip;
    }
    
    public static void sendMsgMessage(final String player, final String msg) {
        try {
            InterClienChatConnection.self.packetInterface.writePacket(makePacket(4, player, msg));
        }
        catch (final Exception ex) {}
    }
    
    public static void sendIRCMessage(final String msg) {
        try {
            InterClienChatConnection.self.packetInterface.writePacket(makePacket(3, msg));
        }
        catch (final Exception ex) {}
    }
    
    public static void updateAccountName(final String name) {
        try {
            InterClienChatConnection.self.packetInterface.writePacket(makePacket(2, name));
        }
        catch (final Exception ex) {}
    }
    
    public static void stop() {
        try {
            InterClienChatConnection.onlinePlayers.clear();
            InterClienChatConnection.self.packetInterface.writePacket(makePacket(7, InterClienChatConnection.user));
            InterClienChatConnection.self.connection.getInputStream().close();
            InterClienChatConnection.self.connection.getOutputStream().close();
            InterClienChatConnection.self.connection.close();
            InterClienChatConnection.hasLostConnection = true;
        }
        catch (final Exception ex) {}
    }
    
    public static void start() {
        try {
            InterClienChatConnection.self = new InterClienChatConnection();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static byte[] makePacket(final int ID, final String... content) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(ID);
        new DataOutputStream(baos).writeInt(content.length);
        for (final String s : content) {
            final byte[] data = s.getBytes("UTF-8");
            new DataOutputStream(baos).writeInt(data.length);
            baos.write(data);
        }
        return baos.toByteArray();
    }
    
    public static void make7z(final File inDir, final File outFile) {
        try {
            final SevenZOutputFile zo = new SevenZOutputFile(outFile);
            final ArrayList<File> list = new ArrayList<File>();
            listFiles(inDir, list);
            final int substrLen = inDir.getAbsolutePath().length() + 1;
            for (final File f : list) {
                final SevenZArchiveEntry entry = zo.createArchiveEntry(new File(""), f.getAbsolutePath().substring(substrLen));
                zo.putArchiveEntry(entry);
                final InputStream in = new BufferedInputStream(new FileInputStream(f));
                final byte[] b = new byte[1024];
                int count = 0;
                while ((count = in.read(b)) > 0) {
                    zo.write(b, 0, count);
                }
                in.close();
                zo.closeArchiveEntry();
            }
            zo.close();
        }
        catch (final Exception ex) {}
    }
    
    public void unpack7z(final File inFile, final File outDir) {
    }
    
    private static void listFiles(final File dir, final ArrayList<File> list) {
        File[] listFiles;
        for (int length = (listFiles = dir.listFiles()).length, i = 0; i < length; ++i) {
            final File f = listFiles[i];
            if (f.isFile()) {
                list.add(f);
            }
            else {
                listFiles(f, list);
            }
        }
    }
    
    public class ReceiverThread implements Runnable
    {
        SynchronKeyHandler packetInterface;
        
        public ReceiverThread(final SynchronKeyHandler a) {
            this.packetInterface = a;
            new Thread(this).start();
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    final byte[] packet = this.packetInterface.readPacket();
                    switch (packet[0] & 0xFF) {
                        case 1: {
                            final String text = new String(packet, 1, packet.length - 1, "UTF-8");
                            InterClienChatConnection.msgs.add(text);
                            continue;
                        }
                        case 2: {
                            final String players = new String(packet, 1, packet.length - 1, "UTF-8");
                            final String[] argPlayers = players.split(",");
                            for (int i = 0; i < argPlayers.length; ++i) {
                                if (!InterClienChatConnection.onlinePlayers.contains(argPlayers[i])) {
                                    InterClienChatConnection.onlinePlayers.add(argPlayers[i]);
                                }
                            }
                            continue;
                        }
                        case 3: {
                            final String s = new String(packet, 1, packet.length - 1, "UTF-8");
                            if (!s.equals(InterClienChatConnection.user) && !InterClienChatConnection.onlinePlayers.contains(s)) {
                                InterClienChatConnection.onlinePlayers.add(s);
                                continue;
                            }
                            continue;
                        }
                        case 4: {
                            final String ss = new String(packet, 1, packet.length - 1, "UTF-8");
                            if (!ss.equals(InterClienChatConnection.user)) {
                                InterClienChatConnection.onlinePlayers.remove(ss);
                                continue;
                            }
                            continue;
                        }
                    }
                }
            }
            catch (final Exception e) {
                InterClienChatConnection.hasLostConnection = true;
            }
        }
    }
}
