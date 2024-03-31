// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui.serverlists.partner;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.multiplayer.ServerData;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class PartnerServerList
{
    private static final Logger logger;
    private final Minecraft mc;
    private final List<ServerData> servers;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public PartnerServerList(final Minecraft mcIn) {
        this.servers = (List<ServerData>)Lists.newArrayList();
        this.mc = mcIn;
        this.loadServerList();
    }
    
    public void loadServerList() {
        try {
            this.servers.clear();
            this.addServerData(new ServerData("MarvinMC", "marvinmc.net", false));
        }
        catch (final Exception e) {
            PartnerServerList.logger.error("Couldn't load partner server list", e);
        }
    }
    
    public ServerData getServerData(final int p_78850_1_) {
        return this.servers.get(p_78850_1_);
    }
    
    public void addServerData(final ServerData server) {
        this.servers.add(server);
    }
    
    public int countServers() {
        return this.servers.size();
    }
}
