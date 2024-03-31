// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.party;

import net.labymod.labyconnect.user.ServerInfo;
import com.mojang.authlib.GameProfile;
import net.labymod.labyconnect.user.UserStatus;
import java.util.Iterator;
import net.labymod.labyconnect.user.ChatUser;
import java.util.Arrays;
import net.labymod.labyplay.party.model.PartyActionTypes;
import net.labymod.labyconnect.log.SingleChat;
import net.labymod.labyconnect.log.MessageChatComponent;
import net.labymod.main.LabyMod;
import net.labymod.labyplay.party.model.PartyInvite;
import java.util.UUID;
import net.labymod.labyplay.party.model.PartyMember;
import java.util.Comparator;
import net.labymod.labyplay.party.model.PartyListener;

public class PartyResponseHandler implements PartyListener
{
    private final Comparator<PartyMember> memberComparator;
    private PartySystem partySystem;
    
    public PartyResponseHandler(final PartySystem partySystem) {
        this.memberComparator = new Comparator<PartyMember>() {
            @Override
            public int compare(final PartyMember a, final PartyMember b) {
                return Boolean.compare(b.isOwner(), a.isOwner()) * 2 + Boolean.compare(b.isMember(), a.isMember());
            }
        };
        this.partySystem = partySystem;
    }
    
    @Override
    public void onInvitedPlayer(final String username, final UUID party, final String partyName) {
        System.out.println("invited by " + username + " to party of " + partyName);
        this.partySystem.getPartyInvites().add(new PartyInvite(username, party, partyName));
        this.partySystem.updatePartyGui();
    }
    
    @Override
    public void onInviteSuccess(final String username, final UUID uuid) {
    }
    
    @Override
    public void onChatMessage(final String sender, final String message) {
        System.out.println(String.valueOf(sender) + ": " + message);
        if (!sender.equals(LabyMod.getInstance().getPlayerName())) {
            final SingleChat singleChat = LabyMod.getInstance().getLabyConnect().getChatlogManager().getChat(this.partySystem.chatUserDummy);
            singleChat.addMessage(new MessageChatComponent(sender, System.currentTimeMillis(), message));
        }
    }
    
    @Override
    public void onSystemMessage(final PartyActionTypes.Message type, final String[] args) {
        System.out.println(String.valueOf(type.getKey()) + " (" + Arrays.toString(args) + ")");
        LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(type.getKey(), Arrays.toString(args));
    }
    
    @Override
    public void onPartyLeft(final UUID party) {
        System.out.println("party left");
        if (this.partySystem.partyId != null && this.partySystem.partyId.equals(party)) {
            this.partySystem.partyId = null;
            this.partySystem.clientMember = null;
            this.partySystem.members = new PartyMember[0];
            final Iterator<ChatUser> iterator = LabyMod.getInstance().getLabyConnect().getFriends().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isParty()) {
                    iterator.remove();
                }
            }
        }
        this.partySystem.updatePartyGui();
    }
    
    @Override
    public void onMemberList(final UUID partyId, final PartyMember[] partyMembers) {
        System.out.println("Members:");
        final UUID clientUUID = LabyMod.getInstance().getPlayerUUID();
        for (final PartyMember member : partyMembers) {
            System.out.println("- " + member.getName());
            if (member.getUuid().equals(clientUUID)) {
                this.partySystem.clientMember = member;
            }
        }
        Arrays.sort(partyMembers, this.memberComparator);
        final Iterator<ChatUser> iterator = LabyMod.getInstance().getLabyConnect().getFriends().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isParty()) {
                iterator.remove();
            }
        }
        if (partyMembers.length > 1) {
            this.partySystem.chatUserDummy = new ChatUser(null, UserStatus.ONLINE, "", null, 0, System.currentTimeMillis(), 0L, "", System.currentTimeMillis(), System.currentTimeMillis(), partyMembers.length, true);
            LabyMod.getInstance().getLabyConnect().getFriends().add(this.partySystem.chatUserDummy);
            LabyMod.getInstance().getLabyConnect().sortFriendList(LabyMod.getSettings().friendSortType);
        }
        this.partySystem.partyId = partyId;
        this.partySystem.members = partyMembers;
        this.partySystem.updatePartyGui();
    }
}
