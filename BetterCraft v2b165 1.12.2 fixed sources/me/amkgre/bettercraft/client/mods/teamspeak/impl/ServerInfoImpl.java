// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import java.awt.image.BufferedImage;
import me.amkgre.bettercraft.client.mods.teamspeak.util.ImageManager;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerImage;
import me.amkgre.bettercraft.client.mods.teamspeak.tslogs.LogFileParseManager;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerInfo;

public class ServerInfoImpl implements ServerInfo
{
    private final ServerTabImpl serverTab;
    private final String ip;
    private final int port;
    private String name;
    private String uniqueId;
    private String platform;
    private String version;
    private long created;
    private String bannerURL;
    private String bannerImageURL;
    private int bannerImageInterval;
    private String hostButtonTooltip;
    private String hostButtonURL;
    private String hostButtonImageURL;
    private float prioritySpeakerDimmModificator;
    private String phoneticName;
    private int iconId;
    private LogFileParseManager parseManager;
    
    public ServerInfoImpl(final ServerTabImpl serverTab, final String ip, final int port) {
        this.serverTab = serverTab;
        this.ip = ip;
        this.port = port;
    }
    
    public ServerTabImpl getServerTab() {
        return this.serverTab;
    }
    
    @Override
    public String getIp() {
        return this.ip;
    }
    
    @Override
    public int getPort() {
        return this.port;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }
    
    public void setUniqueId(final String uniqueId) {
        this.uniqueId = uniqueId;
        if (this.parseManager != null) {
            this.parseManager.stop();
            this.parseManager = null;
        }
        this.parseManager = new LogFileParseManager(this);
    }
    
    @Override
    public String getPlatform() {
        return this.platform;
    }
    
    public void setPlatform(final String platform) {
        this.platform = platform;
    }
    
    @Override
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
    }
    
    @Override
    public long getTimeCreated() {
        return this.created;
    }
    
    public void setTimeCreated(final long created) {
        this.created = created;
    }
    
    @Override
    public ServerImage getServerBanner() {
        return ImageManager.resolveServerImage(this.bannerURL, this.bannerImageURL);
    }
    
    @Override
    public ServerImage getServerHostButton() {
        return ImageManager.resolveServerImage(this.hostButtonURL, this.hostButtonImageURL);
    }
    
    public void setBannerURL(final String bannerURL) {
        this.bannerURL = bannerURL;
    }
    
    public void setBannerImageURL(final String bannerImageURL) {
        this.bannerImageURL = bannerImageURL;
    }
    
    public void setBannerImageInterval(final int bannerImageInterval) {
        this.bannerImageInterval = bannerImageInterval;
    }
    
    public float getPrioritySpeakerDimmModificator() {
        return this.prioritySpeakerDimmModificator;
    }
    
    public void setPrioritySpeakerDimmModificator(final float prioritySpeakerDimmModificator) {
        this.prioritySpeakerDimmModificator = prioritySpeakerDimmModificator;
    }
    
    public void setHostButtonTooltip(final String hostButtonTooltip) {
        this.hostButtonTooltip = hostButtonTooltip;
    }
    
    public void setHostButtonURL(final String hostButtonURL) {
        this.hostButtonURL = hostButtonURL;
    }
    
    public void setHostButtonImageURL(final String hostButtonImageURL) {
        this.hostButtonImageURL = hostButtonImageURL;
    }
    
    public void setPhoneticName(final String phoneticName) {
        this.phoneticName = phoneticName;
    }
    
    public void setIconId(final int iconId) {
        this.iconId = iconId;
    }
    
    @Override
    public BufferedImage getIcon() {
        return ImageManager.resolveIcon(this.uniqueId, this.iconId);
    }
    
    public LogFileParseManager getParseManager() {
        return this.parseManager;
    }
    
    @Override
    public String toString() {
        return "ServerInfo{ip='" + this.ip + '\'' + ", port=" + this.port + '}';
    }
}
