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
import net.labymod.labyplay.party.model.PartyInvite;
import java.util.UUID;
import net.labymod.labyplay.party.model.PartyMember;
import java.util.Comparator;
import net.labymod.main.LabyMod;
import net.labymod.labyplay.party.model.PartyListener;

public class PartyResponseHandler implements PartyListener
{
    private LabyMod labyMod;
    private final Comparator<PartyMember> memberComparator;
    private PartySystem partySystem;
    
    public PartyResponseHandler(final LabyMod labyMod) {
        this.memberComparator = new Comparator<PartyMember>() {
            @Override
            public int compare(final PartyMember a, final PartyMember b) {
                return Boolean.compare(b.isOwner(), a.isOwner()) * 2 + Boolean.compare(b.isMember(), a.isMember());
            }
        };
        this.labyMod = labyMod;
    }
    
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
    }
    
    @Override
    public void onInviteSuccess(final String username, final UUID uuid) {
    }
    
    @Override
    public void onChatMessage(final String sender, final String message) {
        System.out.println(String.valueOf(String.valueOf(sender)) + ": " + message);
        if (!sender.equals(this.labyMod.getPlayerName())) {
            final SingleChat singlechat = this.labyMod.getLabyConnect().getChatlogManager().getChat(this.partySystem.chatUserDummy);
            singlechat.addMessage(new MessageChatComponent(sender, System.currentTimeMillis(), message));
        }
    }
    
    @Override
    public void onSystemMessage(final PartyActionTypes.Message type, final String[] args) {
        System.out.println(String.valueOf(String.valueOf(type.getKey())) + " (" + Arrays.toString(args) + ")");
    }
    
    @Override
    public void onPartyLeft(final UUID party) {
        System.out.println("party left");
        if (this.partySystem.partyId != null && this.partySystem.partyId.equals(party)) {
            this.partySystem.partyId = null;
            this.partySystem.clientMember = null;
            this.partySystem.members = new PartyMember[0];
            final Iterator<ChatUser> iterator = this.labyMod.getLabyConnect().getFriends().iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().isParty()) {
                    continue;
                }
                iterator.remove();
            }
        }
    }
    
    @Override
    public void onMemberList(final UUID partyId, final PartyMember[] partyMembers) {
        System.out.println("Members:");
        final UUID uuid = this.labyMod.getPlayerUUID();
        for (final PartyMember partymember : partyMembers) {
            System.out.println("- " + partymember.getName());
            if (partymember.getUuid().equals(uuid)) {
                this.partySystem.clientMember = partymember;
            }
        }
        Arrays.sort(partyMembers, this.memberComparator);
        final Iterator<ChatUser> iterator = this.labyMod.getLabyConnect().getFriends().iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isParty()) {
                continue;
            }
            iterator.remove();
        }
        if (partyMembers.length > 1) {
            this.partySystem.chatUserDummy = new ChatUser(null, UserStatus.ONLINE, "", null, 0, System.currentTimeMillis(), "", System.currentTimeMillis(), System.currentTimeMillis(), partyMembers.length, true);
            this.labyMod.getLabyConnect().getFriends().add(this.partySystem.chatUserDummy);
        }
        this.partySystem.partyId = partyId;
        this.partySystem.members = partyMembers;
    }
}
