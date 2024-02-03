// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.teamspeak3;

import java.util.Iterator;
import net.labymod.utils.ModColor;

public class TeamSpeakListener implements ControlListener
{
    @Override
    public void onPokeRecieved(final TeamSpeakUser user, final String pokeMessage) {
        if (user != null) {
            String s = String.valueOf(ModColor.cl("9")) + user.getNickName() + ModColor.cl("a") + " pokes you.";
            if (!pokeMessage.isEmpty()) {
                s = String.valueOf(ModColor.cl("9")) + user.getNickName() + ModColor.cl("a") + " pokes you: " + pokeMessage;
            }
            TeamSpeak.infoAll(s);
            TeamSpeak.overlayWindows.openInfo(user.getClientId(), "You Have Been Poked", s);
        }
    }
    
    @Override
    public void onClientDisconnected(final TeamSpeakUser user, final String reason) {
        if (user != null) {
            TeamSpeak.addChat(null, null, String.valueOf(ModColor.cl("9")) + user.getNickName() + ModColor.cl("7") + " disconnected from the Server (" + reason + ")", EnumTargetMode.SERVER);
            for (final Chat chat : TeamSpeak.chats) {
                if (chat.getChatOwner() != null && chat.getTargetMode() == EnumTargetMode.USER && chat.getChatOwner().getClientId() == user.getClientId()) {
                    chat.addMessage(null, String.valueOf(ModColor.cl("7")) + "Your chat partner has disconnected.");
                }
            }
        }
    }
    
    @Override
    public void onClientTimout(final TeamSpeakUser user) {
        if (user != null) {
            TeamSpeak.addChat(null, null, String.valueOf(ModColor.cl("9")) + user.getNickName() + ModColor.cl("7") + " timed out.", EnumTargetMode.SERVER);
        }
    }
    
    @Override
    public void onClientConnect(final TeamSpeakUser user) {
        TeamSpeak.addChat(null, null, String.valueOf(ModColor.cl("9")) + user.getNickName() + ModColor.cl("7") + " connected to the Server.", EnumTargetMode.SERVER);
    }
    
    @Override
    public void onMessageRecieved(final TeamSpeakUser target, final TeamSpeakUser user, final String message) {
        if (user != null && target != null) {
            TeamSpeak.addChat(target, user, message, EnumTargetMode.USER);
            TeamSpeakController.getInstance().me.getClientId();
            user.getClientId();
        }
    }
    
    @Override
    public void onChannelMessageRecieved(final TeamSpeakUser user, final String message) {
        TeamSpeak.addChat(null, user, message, EnumTargetMode.CHANNEL);
        if (TeamSpeakController.getInstance().me != null) {
            TeamSpeakController.getInstance().me.getClientId();
            user.getClientId();
        }
    }
    
    @Override
    public void onServerMessageRecieved(final TeamSpeakUser user, final String message) {
        TeamSpeak.addChat(null, user, message, EnumTargetMode.SERVER);
    }
    
    @Override
    public void onClientStartTyping(final TeamSpeakUser user) {
    }
    
    @Override
    public void onDisconnect() {
    }
    
    @Override
    public void onConnect() {
        TeamSpeak.scrollChannel = 0;
    }
    
    @Override
    public void onError(final int errorId, final String errorMessage) {
        final String s = String.valueOf(ModColor.cl("c")) + TeamSpeak.fix(errorMessage) + " (Error " + errorId + ")";
        TeamSpeak.error(s);
    }
}
