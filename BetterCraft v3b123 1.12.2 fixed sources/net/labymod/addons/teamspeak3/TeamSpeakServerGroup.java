// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.util.ArrayList;
import java.util.List;

public class TeamSpeakServerGroup
{
    private static final List<TeamSpeakServerGroup> groups;
    int sgid;
    String groupName;
    int type;
    int iconId;
    int savebd;
    
    static {
        groups = new ArrayList<TeamSpeakServerGroup>();
    }
    
    public TeamSpeakServerGroup(final int sgid) {
        this.sgid = sgid;
    }
    
    public void setSgid(final int sgid) {
        this.sgid = sgid;
    }
    
    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setIconId(final int iconId) {
        this.iconId = iconId;
    }
    
    public void setSavebd(final int savebd) {
        this.savebd = savebd;
    }
    
    public int getSgid() {
        return this.sgid;
    }
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getIconId() {
        return this.iconId;
    }
    
    public int getSavebd() {
        return this.savebd;
    }
    
    public static void addGroup(final TeamSpeakServerGroup group) {
        TeamSpeakServerGroup.groups.add(group);
    }
    
    public static List<TeamSpeakServerGroup> getGroups() {
        return TeamSpeakServerGroup.groups;
    }
}
