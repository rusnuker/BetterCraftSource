// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import com.google.common.primitives.Ints;
import me.amkgre.bettercraft.client.mods.teamspeak.util.ImageManager;
import java.awt.image.BufferedImage;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Group;

public class GroupImpl implements Group
{
    private ServerTabImpl serverTab;
    private final int id;
    private String name;
    private boolean showPrefix;
    private int type;
    private int iconId;
    private boolean saveDb;
    private int sortId;
    private int modifyPower;
    private int memberAddPower;
    private int memberRemovePower;
    
    public GroupImpl(final ServerTabImpl serverTab, final int id) {
        this.serverTab = serverTab;
        this.id = id;
    }
    
    @Override
    public int getId() {
        return this.id;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean isShowPrefix() {
        return this.showPrefix;
    }
    
    public void setShowPrefix(final boolean showPrefix) {
        this.showPrefix = showPrefix;
    }
    
    @Override
    public int getType() {
        return this.type;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public BufferedImage getIcon() {
        final ServerInfoImpl serverInfo = this.serverTab.getServerInfo();
        return (serverInfo == null || serverInfo.getUniqueId() == null) ? null : ImageManager.resolveIcon(serverInfo.getUniqueId(), this.iconId);
    }
    
    @Override
    public int getIconId() {
        return this.iconId;
    }
    
    public void setIconId(final int iconId) {
        this.iconId = iconId;
    }
    
    @Override
    public boolean isPersistent() {
        return this.saveDb;
    }
    
    public void setSaveDb(final boolean saveDb) {
        this.saveDb = saveDb;
    }
    
    @Override
    public int getSortId() {
        return this.sortId;
    }
    
    public void setSortId(final int sortId) {
        this.sortId = sortId;
    }
    
    @Override
    public int getModifyPower() {
        return this.modifyPower;
    }
    
    public void setModifyPower(final int modifyPower) {
        this.modifyPower = modifyPower;
    }
    
    @Override
    public int getMemberAddPower() {
        return this.memberAddPower;
    }
    
    public void setMemberAddPower(final int memberAddPower) {
        this.memberAddPower = memberAddPower;
    }
    
    @Override
    public int getMemberRemovePower() {
        return this.memberRemovePower;
    }
    
    public void setMemberRemovePower(final int memberRemovePower) {
        this.memberRemovePower = memberRemovePower;
    }
    
    @Override
    public int compareTo(final Group o) {
        return Ints.compare(this.getSortId(), o.getSortId());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final GroupImpl group = (GroupImpl)o;
        return this.id == group.id;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
}
