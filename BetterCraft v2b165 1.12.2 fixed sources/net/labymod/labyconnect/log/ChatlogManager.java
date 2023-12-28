// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.log;

import java.nio.charset.Charset;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import net.labymod.labyconnect.user.ServerInfo;
import net.labymod.labyconnect.user.UserStatus;
import com.mojang.authlib.GameProfile;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.UUID;
import java.util.Iterator;
import net.labymod.labyconnect.user.ChatUser;
import java.util.ArrayList;
import net.labymod.main.LabyMod;
import java.util.List;

public class ChatlogManager
{
    private final int MAX_LOG_MESSAGE_COUNT = 1000;
    private List<SingleChat> chats;
    private LabyMod labyMod;
    
    public ChatlogManager(final LabyMod labyMod) {
        this.chats = new ArrayList<SingleChat>();
        this.labyMod = labyMod;
    }
    
    public SingleChat getChat(final ChatUser user) {
        for (final SingleChat singlechat : this.chats) {
            if (!singlechat.getChatPartner().equals(user)) {
                continue;
            }
            return singlechat.apply(user);
        }
        final SingleChat singlechat2 = new SingleChat(this.labyMod, this.chats.size(), user, new ArrayList<MessageChatComponent>());
        this.chats.add(singlechat2);
        return singlechat2;
    }
    
    public void loadChatlogs(final UUID accountUUID) {
        this.chats.clear();
        final File file1 = new File(String.format("LabyMod/chatlog/%s.log", accountUUID.toString()));
        if (file1.exists()) {
            try {
                final DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
                for (int i = datainputstream.readInt(), j = 0; j < i; ++j) {
                    datainputstream.readInt();
                    final String s = this.readString(datainputstream);
                    final UUID uuid = new UUID(datainputstream.readLong(), datainputstream.readLong());
                    final ArrayList<MessageChatComponent> arraylist = new ArrayList<MessageChatComponent>();
                    final int k = datainputstream.readInt();
                    if (k < 1000) {
                        for (int l = 0; l < k; ++l) {
                            final String s2 = this.readString(datainputstream);
                            final long i2 = datainputstream.readLong();
                            final String s3 = this.readString(datainputstream);
                            arraylist.add(new MessageChatComponent(s2, i2, s3));
                        }
                    }
                    final GameProfile gameprofile = new GameProfile(uuid, s);
                    final SingleChat singlechat = this.getChat(new ChatUser(gameprofile, UserStatus.OFFLINE, "", null, 0, System.currentTimeMillis(), "", 0L, 0L, 0, false));
                    singlechat.getMessages().addAll(arraylist);
                }
                datainputstream.close();
            }
            catch (final Exception exception) {
                file1.delete();
                exception.printStackTrace();
            }
        }
    }
    
    public void saveChatlogs(final UUID accountUUID) {
        final File file1 = new File(String.format("LabyMod/chatlog/%s.log", accountUUID.toString()));
        if (!file1.getParentFile().exists()) {
            file1.getParentFile().mkdirs();
        }
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            }
            catch (final IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
        try {
            final ArrayList<SingleChat> list = new ArrayList<SingleChat>(this.chats);
            final Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().getChatPartner().isParty()) {
                    continue;
                }
                iterator.remove();
            }
            final DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
            dataoutputstream.writeInt(list.size());
            for (final SingleChat singlechat : list) {
                dataoutputstream.writeInt(singlechat.getId());
                final GameProfile gameprofile = singlechat.getChatPartner().getGameProfile();
                this.writeString(dataoutputstream, gameprofile.getName());
                dataoutputstream.writeLong(gameprofile.getId().getMostSignificantBits());
                dataoutputstream.writeLong(gameprofile.getId().getLeastSignificantBits());
                final List<MessageChatComponent> list2 = singlechat.getMessages();
                final int i;
                int j = i = list2.size();
                final boolean flag = i > 300;
                dataoutputstream.writeInt(flag ? 300 : i);
                for (int k = 0; k < i; ++k) {
                    if (flag && j > 300) {
                        --j;
                    }
                    else {
                        final MessageChatComponent messagechatcomponent = list2.get(k);
                        this.writeString(dataoutputstream, messagechatcomponent.getSender());
                        dataoutputstream.writeLong(messagechatcomponent.getSentTime());
                        this.writeString(dataoutputstream, messagechatcomponent.getMessage());
                    }
                }
            }
            dataoutputstream.flush();
            dataoutputstream.close();
        }
        catch (final Exception exception) {
            exception.printStackTrace();
        }
    }
    
    private void writeString(final DataOutputStream dos, final String string) {
        try {
            final byte[] abyte = string.getBytes(Charset.forName("UTF8"));
            dos.writeInt(abyte.length);
            byte[] array;
            for (int length = (array = abyte).length, i = 0; i < length; ++i) {
                final byte b0 = array[i];
                dos.writeByte(b0);
            }
        }
        catch (final IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private String readString(final DataInputStream dis) {
        try {
            final int i = dis.readInt();
            final byte[] abyte = new byte[i];
            for (int j = 0; j < i; ++j) {
                abyte[j] = dis.readByte();
            }
            return new String(abyte, Charset.forName("UTF8"));
        }
        catch (final Exception var5) {
            return "";
        }
    }
    
    public int getMAX_LOG_MESSAGE_COUNT() {
        this.getClass();
        return 1000;
    }
    
    public List<SingleChat> getChats() {
        return this.chats;
    }
}
