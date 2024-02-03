// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import net.labymod.utils.ModColor;
import java.util.List;

public class TeamSpeakBridge
{
    public static TeamSpeakChannel getChannel(final String clientName) {
        final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(clientName);
        return getChannel(teamspeakuser);
    }
    
    protected static TeamSpeakChannel getChannel(final TeamSpeakUser user) {
        return TeamSpeakController.getInstance().getChannel(user.getChannelId());
    }
    
    public static List<TeamSpeakUser> getUsers() {
        return TeamSpeakUser.getUsers();
    }
    
    public static List<TeamSpeakChannel> getChannels() {
        return TeamSpeakChannel.getChannels();
    }
    
    public static TeamSpeakUser getUser(final String clientName) {
        return TeamSpeakController.getInstance().getUser(clientName);
    }
    
    public static void pokeClient(final TeamSpeakUser user, final String message) {
        TeamSpeakController.getInstance().sendMessage("clientpoke msg=" + TeamSpeak.unFix(message) + " clid=" + user.getClientId());
        String s = String.valueOf(ModColor.cl("7")) + "You poked " + ModColor.cl("9") + user.getNickName() + ModColor.cl("7") + ".";
        if (!message.isEmpty()) {
            s = String.valueOf(ModColor.cl("7")) + "You poked " + ModColor.cl("9") + user.getNickName() + ModColor.cl("7") + " with message: " + message;
        }
        TeamSpeak.addChat(null, null, s, EnumTargetMode.SERVER);
        TeamSpeak.addChat(null, null, s, EnumTargetMode.CHANNEL);
        if (TeamSpeak.selectedChat >= 0) {
            final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(TeamSpeak.selectedChat);
            if (teamspeakuser != null && teamspeakuser.getClientId() == user.getClientId()) {
                TeamSpeak.addChat(teamspeakuser, null, s, EnumTargetMode.USER);
            }
        }
    }
    
    public static void messagePlayer(final TeamSpeakUser user, final String message) {
        TeamSpeakController.getInstance().sendMessage("sendtextmessage targetmode=1 target=" + user.getClientId() + " msg=" + TeamSpeak.unFix(TeamSpeak.toUrl(message)));
    }
    
    public static void messageChannel(final String message) {
        TeamSpeakController.getInstance().sendMessage("sendtextmessage targetmode=2 msg=" + TeamSpeak.unFix(TeamSpeak.toUrl(message)));
    }
    
    public static void messageServer(final String message) {
        TeamSpeakController.getInstance().sendMessage("sendtextmessage targetmode=3 msg=" + TeamSpeak.unFix(TeamSpeak.toUrl(message)));
    }
    
    public static void moveClient(final int id, final int to) {
        TeamSpeakController.getInstance().sendMessage("clientmove cid=" + to + " clid=" + id);
    }
    
    public static List<TeamSpeakUser> getChannelUsers(final int channelId) {
        final List<TeamSpeakUser> list = new ArrayList<TeamSpeakUser>();
        list.addAll(TeamSpeakUser.getUsers());
        Collections.sort(list, new Comparator<TeamSpeakUser>() {
            @Override
            public int compare(final TeamSpeakUser o1, final TeamSpeakUser o2) {
                return (o1 != null && o2 != null) ? ((o1.getTalkPower() < o2.getTalkPower()) ? 1 : ((o1.getTalkPower() > o2.getTalkPower()) ? -1 : 0)) : 0;
            }
        });
        final List<TeamSpeakUser> list2 = new ArrayList<TeamSpeakUser>();
        for (final TeamSpeakUser teamspeakuser : list) {
            if (teamspeakuser != null && channelId == teamspeakuser.getChannelId()) {
                list2.add(teamspeakuser);
            }
        }
        return list2;
    }
    
    public static void setNickname(final String nickname) {
        TeamSpeakController.getInstance().sendMessage("clientupdate client_nickname=" + TeamSpeak.unFix(nickname));
    }
    
    public static void setAway(final boolean away, final String message) {
        TeamSpeakController.getInstance().sendMessage("clientupdate client_away=" + TeamSpeak.booleanToInteger(away));
        if (message.isEmpty()) {
            TeamSpeakController.getInstance().sendMessage("clientupdate client_away_message");
        }
        else {
            TeamSpeakController.getInstance().sendMessage("clientupdate client_away_message=" + TeamSpeak.unFix(message));
        }
    }
    
    public static void setInputMuted(final boolean muted) {
        TeamSpeakController.getInstance().sendMessage("clientupdate client_input_muted=" + TeamSpeak.booleanToInteger(muted));
    }
    
    public static void setOutputMuted(final boolean muted) {
        TeamSpeakController.getInstance().sendMessage("clientupdate client_output_muted=" + TeamSpeak.booleanToInteger(muted));
    }
    
    public static void setInputDeactivated(final boolean muted) {
        TeamSpeakController.getInstance().sendMessage("clientupdate client_input_deactivated=" + TeamSpeak.booleanToInteger(muted));
    }
    
    public static void setChannelCommander(final boolean commander) {
        TeamSpeakController.getInstance().sendMessage("clientupdate client_is_channel_commander=" + TeamSpeak.booleanToInteger(commander));
    }
    
    public static void setMetaData(final String message) {
        TeamSpeakController.getInstance().sendMessage("clientupdate client_meta_data=" + TeamSpeak.unFix(message));
    }
    
    public static void sendTextMessage(final int id, final String msg) {
        if (id == -2) {
            messageServer(msg);
        }
        else if (id == -1) {
            messageChannel(msg);
        }
        else {
            final TeamSpeakUser teamspeakuser = TeamSpeakController.getInstance().getUser(id);
            if (teamspeakuser != null) {
                messagePlayer(teamspeakuser, msg);
            }
            else {
                TeamSpeak.error("User is offline");
            }
        }
    }
}
