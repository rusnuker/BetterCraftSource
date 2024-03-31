// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.party;

import net.minecraft.client.gui.GuiScreen;
import net.labymod.labyplay.gui.GuiPlayLayout;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import com.google.gson.JsonArray;
import java.net.ProtocolException;
import net.labymod.support.util.Debug;
import net.labymod.labyplay.party.model.PartyActionTypes;
import net.labymod.labyplay.party.model.PartyMessage;
import net.lenni0451.eventapi.events.EventTarget;
import me.nzxtercode.bettercraft.client.events.ClientTickEvent;
import net.labymod.main.LabyMod;
import java.util.ArrayList;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.labyplay.party.model.PartyMember;
import java.util.UUID;
import net.labymod.labyplay.party.model.PartyInvite;
import java.util.List;
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
    private final List<PartyInvite> partyInvites;
    protected UUID partyId;
    protected PartyMember[] members;
    protected PartyMember clientMember;
    protected ChatUser chatUserDummy;
    
    public PartySystem() {
        this.jsonParser = new JsonParser();
        this.gson = new Gson();
        this.partyResponse = new PartyResponseHandler(this);
        this.members = new PartyMember[0];
        this.partyInvites = new ArrayList<PartyInvite>();
        this.clientMember = null;
        LabyMod.getInstance().getEventManager().registerAddonMessage(this);
        LabyMod.getInstance().getLabyModAPI().registerForgeListener(this);
    }
    
    public boolean hasParty() {
        return this.partyId != null && this.members.length > 1;
    }
    
    @EventTarget
    public void handleEvent(final ClientTickEvent event) {
        if (!LabyMod.getInstance().getLabyConnect().isOnline()) {
            this.partyId = null;
            this.clientMember = null;
            if (this.members == null || this.members.length != 0) {
                this.members = new PartyMember[0];
            }
            if (this.partyInvites == null || this.partyInvites.size() != 0) {
                assert this.partyInvites != null;
                this.partyInvites.clear();
            }
        }
    }
    
    @Override
    public void accept(final PacketAddonMessage packet) {
        if (!packet.getKey().equals("party")) {
            return;
        }
        final String json = packet.getJson();
        System.out.println("[IN] " + json);
        final PartyMessage partyMessage = this.gson.fromJson(json, PartyMessage.class);
        final PartyActionTypes.Server actionType = PartyActionTypes.Server.getByKey(partyMessage.getAction());
        if (actionType == null) {
            Debug.log(Debug.EnumDebugMode.LABY_PLAY, "Unknown party action type by server: " + partyMessage.getAction());
            return;
        }
        try {
            switch (actionType) {
                case INVITED_PLAYER: {
                    final String name = partyMessage.getString("name");
                    final UUID party = partyMessage.getUUID("party");
                    final String partyName = partyMessage.getString("partyName");
                    this.partyResponse.onInvitedPlayer(name, party, partyName);
                    break;
                }
                case INVITE_SUCCESS: {
                    final String name = partyMessage.getString("name");
                    final UUID uuid = partyMessage.getUUID("uuid");
                    this.partyResponse.onInviteSuccess(name, uuid);
                    break;
                }
                case CHAT: {
                    final String sender = partyMessage.getString("sender");
                    final String message = partyMessage.getString("message");
                    this.partyResponse.onChatMessage(sender, message);
                    break;
                }
                case SYSTEM_MESSAGE: {
                    final PartyActionTypes.Message type = PartyActionTypes.Message.getByKey(partyMessage.getString("key"));
                    final JsonArray jsonArray = partyMessage.getElement("args").getAsJsonArray();
                    final String[] messageArgs = new String[jsonArray.size()];
                    for (int i = 0; i < jsonArray.size(); ++i) {
                        messageArgs[i] = jsonArray.get(i).getAsString();
                    }
                    this.partyResponse.onSystemMessage(type, messageArgs);
                    break;
                }
                case YOU_LEFT: {
                    final UUID partyUUID = partyMessage.getUUID("party");
                    this.partyResponse.onPartyLeft(partyUUID);
                    break;
                }
                case MEMBER_LIST: {
                    final PartyMember[] currentPartyMembers = this.gson.fromJson(partyMessage.getElement("members"), PartyMember[].class);
                    final UUID partyId = partyMessage.getUUID("uuid");
                    this.partyResponse.onMemberList(partyId, currentPartyMembers);
                    break;
                }
            }
        }
        catch (final ProtocolException exception) {
            exception.printStackTrace();
        }
    }
    
    public void invitePlayer(final String target) {
        if (target.equalsIgnoreCase(LabyMod.getInstance().getPlayerName())) {
            return;
        }
        final Iterator<PartyInvite> iterator = this.partyInvites.iterator();
        while (iterator.hasNext()) {
            final PartyInvite party = iterator.next();
            if (party.getUsername().equalsIgnoreCase(target)) {
                iterator.remove();
            }
        }
        this.updatePartyGui();
        new PartyMessage.Builder(PartyActionTypes.Client.INVITE_PLAYER).putString("target", target).send();
    }
    
    public void sendInvitePlayerResponse(final UUID uuid, final boolean acceptInvite) {
        final Iterator<PartyInvite> iterator = this.partyInvites.iterator();
        while (iterator.hasNext()) {
            final PartyInvite party = iterator.next();
            if (party.getPartyUUID().equals(uuid)) {
                iterator.remove();
            }
        }
        this.updatePartyGui();
        new PartyMessage.Builder(PartyActionTypes.Client.INVITE_PLAYER_RESPONSE).putUUID("party", uuid).putBoolean("accepted", acceptInvite).send();
    }
    
    public void sendChatMessage(final String message) {
        new PartyMessage.Builder(PartyActionTypes.Client.CHAT).putString("message", message).send();
    }
    
    public void leaveParty() {
        new PartyMessage.Builder(PartyActionTypes.Client.LEAVE_PARTY).send();
    }
    
    public void kickPlayer(final UUID uuid) {
        new PartyMessage.Builder(PartyActionTypes.Client.KICK_PLAYER).putUUID("target", uuid).send();
    }
    
    public void changeOwner(final UUID uuid) {
        new PartyMessage.Builder(PartyActionTypes.Client.CHANGE_OWNER).putUUID("new_owner", uuid).send();
    }
    
    protected void updatePartyGui() {
        final GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen != null && currentScreen instanceof GuiPlayLayout) {
            final GuiPlayLayout partyLayout = (GuiPlayLayout)currentScreen;
            partyLayout.initLayout();
        }
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
}
