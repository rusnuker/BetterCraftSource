// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.party;

import java.util.Iterator;
import com.google.gson.JsonArray;
import java.net.ProtocolException;
import net.labymod.labyplay.party.model.PartyActionTypes;
import net.labymod.labyplay.party.model.PartyMessage;
import java.util.ArrayList;
import net.labymod.main.LabyMod;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.labyplay.party.model.PartyInvite;
import java.util.List;
import net.labymod.labyplay.party.model.PartyMember;
import java.util.UUID;
import net.labymod.labyplay.party.model.PartyListener;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.labymod.labyconnect.packets.PacketAddonMessage;
import net.labymod.utils.Consumer;

public class PartySystem implements Consumer<PacketAddonMessage>
{
    public static final long TOTAL_INVITE_DURATION = 60000L;
    private final JsonParser jsonParser;
    private final Gson gson;
    private final PartyListener partyResponse;
    protected UUID partyId;
    protected PartyMember[] members;
    private List<PartyInvite> partyInvites;
    protected PartyMember clientMember;
    protected ChatUser chatUserDummy;
    private LabyMod labyMod;
    private static int[] $SWITCH_TABLE$net$labymod$labyplay$party$model$PartyActionTypes$Server;
    
    public PartySystem(final LabyMod labyMod) {
        this.jsonParser = new JsonParser();
        this.gson = new Gson();
        this.partyResponse = new PartyResponseHandler(this);
        this.members = new PartyMember[0];
        this.partyInvites = new ArrayList<PartyInvite>();
        this.clientMember = null;
        this.labyMod = labyMod;
    }
    
    public boolean hasParty() {
        return this.partyId != null && this.members.length > 1;
    }
    
    public void onTick() {
        if (!this.labyMod.getLabyConnect().isOnline()) {
            this.partyId = null;
            this.clientMember = null;
            if (this.members == null || this.members.length != 0) {
                this.members = new PartyMember[0];
            }
            if (this.partyInvites == null || this.partyInvites.size() != 0) {
                this.partyInvites.clear();
            }
        }
    }
    
    @Override
    public void accept(final PacketAddonMessage packet) {
        if (packet.getKey().equals("party")) {
            final String s = packet.getJson();
            System.out.println("[IN] " + s);
            final PartyMessage partymessage = this.gson.fromJson(s, PartyMessage.class);
            final PartyActionTypes.Server partyactiontypes$server = PartyActionTypes.Server.getByKey(partymessage.getAction());
            if (partyactiontypes$server != null) {
                try {
                    switch ($SWITCH_TABLE$net$labymod$labyplay$party$model$PartyActionTypes$Server()[partyactiontypes$server.ordinal()]) {
                        case 1: {
                            final String s2 = partymessage.getString("name");
                            final UUID uuid = partymessage.getUUID("party");
                            final String s3 = partymessage.getString("partyName");
                            this.partyResponse.onInvitedPlayer(s2, uuid, s3);
                            break;
                        }
                        case 2: {
                            final String s4 = partymessage.getString("name");
                            final UUID uuid2 = partymessage.getUUID("uuid");
                            this.partyResponse.onInviteSuccess(s4, uuid2);
                            break;
                        }
                        case 3: {
                            final String s5 = partymessage.getString("sender");
                            final String s6 = partymessage.getString("message");
                            this.partyResponse.onChatMessage(s5, s6);
                            break;
                        }
                        case 4: {
                            final PartyActionTypes.Message partyactiontypes$message = PartyActionTypes.Message.getByKey(partymessage.getString("key"));
                            final JsonArray jsonarray = partymessage.getElement("args").getAsJsonArray();
                            final String[] astring = new String[jsonarray.size()];
                            for (int i = 0; i < jsonarray.size(); ++i) {
                                astring[i] = jsonarray.get(i).getAsString();
                            }
                            this.partyResponse.onSystemMessage(partyactiontypes$message, astring);
                            break;
                        }
                        case 5: {
                            final UUID uuid3 = partymessage.getUUID("party");
                            this.partyResponse.onPartyLeft(uuid3);
                            break;
                        }
                        case 6: {
                            final PartyMember[] apartymember = this.gson.fromJson(partymessage.getElement("members"), PartyMember[].class);
                            final UUID uuid4 = partymessage.getUUID("uuid");
                            this.partyResponse.onMemberList(uuid4, apartymember);
                            break;
                        }
                    }
                }
                catch (final ProtocolException protocolexception) {
                    protocolexception.printStackTrace();
                }
            }
        }
    }
    
    public void invitePlayer(final String target) {
        if (!target.equalsIgnoreCase(this.labyMod.getPlayerName())) {
            final Iterator<PartyInvite> iterator = this.partyInvites.iterator();
            while (iterator.hasNext()) {
                final PartyInvite partyinvite = iterator.next();
                if (!partyinvite.getUsername().equalsIgnoreCase(target)) {
                    continue;
                }
                iterator.remove();
            }
            new PartyMessage.Builder(this.labyMod, PartyActionTypes.Client.INVITE_PLAYER).putString("target", target).send();
        }
    }
    
    public void sendInvitePlayerResponse(final UUID uuid, final boolean acceptInvite) {
        final Iterator<PartyInvite> iterator = this.partyInvites.iterator();
        while (iterator.hasNext()) {
            final PartyInvite partyinvite = iterator.next();
            if (!partyinvite.getPartyUUID().equals(uuid)) {
                continue;
            }
            iterator.remove();
        }
        new PartyMessage.Builder(this.labyMod, PartyActionTypes.Client.INVITE_PLAYER_RESPONSE).putUUID("party", uuid).putBoolean("accepted", acceptInvite).send();
    }
    
    public void sendChatMessage(final String message) {
        new PartyMessage.Builder(this.labyMod, PartyActionTypes.Client.CHAT).putString("message", message).send();
    }
    
    public void leaveParty() {
        new PartyMessage.Builder(this.labyMod, PartyActionTypes.Client.LEAVE_PARTY).send();
    }
    
    public void kickPlayer(final UUID uuid) {
        new PartyMessage.Builder(this.labyMod, PartyActionTypes.Client.KICK_PLAYER).putUUID("target", uuid).send();
    }
    
    public void changeOwner(final UUID uuid) {
        new PartyMessage.Builder(this.labyMod, PartyActionTypes.Client.CHANGE_OWNER).putUUID("new_owner", uuid).send();
    }
    
    public JsonParser getJsonParser() {
        return this.jsonParser;
    }
    
    public Gson getGson() {
        return this.gson;
    }
    
    public PartyListener getPartyResponse() {
        return this.partyResponse;
    }
    
    public UUID getPartyId() {
        return this.partyId;
    }
    
    public PartyMember[] getMembers() {
        return this.members;
    }
    
    public List<PartyInvite> getPartyInvites() {
        return this.partyInvites;
    }
    
    public PartyMember getClientMember() {
        return this.clientMember;
    }
    
    public ChatUser getChatUserDummy() {
        return this.chatUserDummy;
    }
    
    static int[] $SWITCH_TABLE$net$labymod$labyplay$party$model$PartyActionTypes$Server() {
        if (PartySystem.$SWITCH_TABLE$net$labymod$labyplay$party$model$PartyActionTypes$Server != null) {
            final int[] arrn = new int[0];
            return arrn;
        }
        final int[] arrn = new int[PartyActionTypes.Server.values().length];
        try {
            arrn[PartyActionTypes.Server.CHAT.ordinal()] = 3;
        }
        catch (final NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[PartyActionTypes.Server.INVITED_PLAYER.ordinal()] = 1;
        }
        catch (final NoSuchFieldError noSuchFieldError2) {}
        try {
            arrn[PartyActionTypes.Server.INVITE_SUCCESS.ordinal()] = 2;
        }
        catch (final NoSuchFieldError noSuchFieldError3) {}
        try {
            arrn[PartyActionTypes.Server.MEMBER_LIST.ordinal()] = 6;
        }
        catch (final NoSuchFieldError noSuchFieldError4) {}
        try {
            arrn[PartyActionTypes.Server.SYSTEM_MESSAGE.ordinal()] = 4;
        }
        catch (final NoSuchFieldError noSuchFieldError5) {}
        try {
            arrn[PartyActionTypes.Server.YOU_LEFT.ordinal()] = 5;
        }
        catch (final NoSuchFieldError noSuchFieldError6) {}
        return PartySystem.$SWITCH_TABLE$net$labymod$labyplay$party$model$PartyActionTypes$Server = arrn;
    }
}
