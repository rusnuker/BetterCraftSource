// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.partnerlist;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import java.util.Iterator;
import me.amkgre.bettercraft.client.utils.ServerDataFeaturedUtils;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.multiplayer.ServerData;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class ServerPartnerList
{
    private static final Logger LOGGER;
    private final Minecraft mc;
    private final List<ServerData> servers;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public ServerPartnerList(final Minecraft mcIn) {
        this.servers = (List<ServerData>)Lists.newCopyOnWriteArrayList();
        this.mc = mcIn;
        this.loadServerList();
    }
    
    private void loadFeaturedServers() {
        this.addServerData(new ServerDataFeaturedUtils("Client Coder's Server", "nzxter.tk"));
        this.addServerData(new ServerDataFeaturedUtils("WorldCrafter", "worldcrafter.de"));
        this.addServerData(new ServerDataFeaturedUtils("TeamHoly", "teamholy.de"));
        this.addServerData(new ServerDataFeaturedUtils("Griefhub", "griefhub.de"));
        this.addServerData(new ServerDataFeaturedUtils("MelonCity", "meloncity.de"));
        this.addServerData(new ServerDataFeaturedUtils("Inkaru", "inkaru.net"));
        this.addServerData(new ServerDataFeaturedUtils("Surnex", "surnex.net"));
        this.addServerData(new ServerDataFeaturedUtils("TomatenCraft", "tomatencraft.de"));
        this.addServerData(new ServerDataFeaturedUtils("FreeMC", "freemc.eu"));
        this.addServerData(new ServerDataFeaturedUtils("SlownEU", "slown.eu"));
    }
    
    public int getFeaturedServerCount() {
        int count = 0;
        for (final ServerData sd : this.servers) {
            if (sd instanceof ServerDataFeaturedUtils) {
                ++count;
            }
        }
        return count;
    }
    
    public void loadServerList() {
        try {
            this.servers.clear();
            this.loadFeaturedServers();
        }
        catch (final Exception exception) {
            ServerPartnerList.LOGGER.error("Couldn't load server list", exception);
        }
    }
    
    public void saveServerList() {
        try {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final ServerData serverdata : this.servers) {
                if (!(serverdata instanceof ServerDataFeaturedUtils)) {
                    nbttaglist.appendTag(serverdata.getNBTCompound());
                }
            }
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("servers", nbttaglist);
        }
        catch (final Exception exception) {
            ServerPartnerList.LOGGER.error("Couldn't save server list", exception);
        }
    }
    
    public ServerData getServerData(final int index) {
        return this.servers.get(index);
    }
    
    public void removeServerData(final int index) {
        this.servers.remove(index);
    }
    
    public void addServerData(final ServerData server) {
        this.servers.add(server);
    }
    
    public int countServers() {
        return this.servers.size();
    }
    
    public void swapServers(final int pos1, final int pos2) {
        final ServerData serverdata = this.getServerData(pos1);
        this.servers.set(pos1, this.getServerData(pos2));
        this.servers.set(pos2, serverdata);
        this.saveServerList();
    }
    
    public void set(final int index, final ServerData server) {
        this.servers.set(index, server);
    }
    
    public static void saveSingleServer(final ServerData server) {
        final ServerPartnerList serverlist = new ServerPartnerList(Minecraft.getMinecraft());
        serverlist.loadServerList();
        for (int i = 0; i < serverlist.countServers(); ++i) {
            final ServerData serverdata = serverlist.getServerData(i);
            if (serverdata.serverName.equals(server.serverName) && serverdata.serverIP.equals(server.serverIP)) {
                serverlist.set(i, server);
                break;
            }
        }
        serverlist.saveServerList();
    }
}
