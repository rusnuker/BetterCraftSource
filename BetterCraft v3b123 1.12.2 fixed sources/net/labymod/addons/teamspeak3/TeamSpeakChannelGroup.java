// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.util.ArrayList;
import java.util.List;

public class TeamSpeakChannelGroup
{
    private static final List<TeamSpeakChannelGroup> groups;
    int sgid;
    String groupName;
    int type;
    int iconId;
    int savebd;
    int namemode;
    int nameModifyPower;
    int nameMemberAddPower;
    int nameMemberRemovePower;
    
    static {
        groups = new ArrayList<TeamSpeakChannelGroup>();
    }
    
    public TeamSpeakChannelGroup(final int sgid) {
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
    
    public void setNamemode(final int namemode) {
        this.namemode = namemode;
    }
    
    public void setNameModifyPower(final int nameModifyPower) {
        this.nameModifyPower = nameModifyPower;
    }
    
    public void setNameMemberAddPower(final int nameMemberAddPower) {
        this.nameMemberAddPower = nameMemberAddPower;
    }
    
    public void setNameMemberRemovePower(final int nameMemberRemovePower) {
        this.nameMemberRemovePower = nameMemberRemovePower;
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
    
    public int getNamemode() {
        return this.namemode;
    }
    
    public int getNameModifyPower() {
        return this.nameModifyPower;
    }
    
    public int getNameMemberAddPower() {
        return this.nameMemberAddPower;
    }
    
    public int getNameMemberRemovePower() {
        return this.nameMemberRemovePower;
    }
    
    public static void addGroup(final TeamSpeakChannelGroup group) {
        TeamSpeakChannelGroup.groups.add(group);
    }
    
    public static List<TeamSpeakChannelGroup> getGroups() {
        return TeamSpeakChannelGroup.groups;
    }
}
