// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import com.google.common.collect.ForwardingMultimap;
import javax.annotation.Nullable;
import net.minecraft.network.INetHandler;
import com.google.common.base.MoreObjects;
import java.io.IOException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.PacketBuffer;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketPlayerListItem implements Packet<INetHandlerPlayClient>
{
    private Action action;
    private final List<AddPlayerData> players;
    
    public SPacketPlayerListItem() {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
    }
    
    public SPacketPlayerListItem(final Action actionIn, final EntityPlayerMP... playersIn) {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
        this.action = actionIn;
        for (final EntityPlayerMP entityplayermp : playersIn) {
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.interactionManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }
    
    public SPacketPlayerListItem(final Action actionIn, final Iterable<EntityPlayerMP> playersIn) {
        this.players = (List<AddPlayerData>)Lists.newArrayList();
        this.action = actionIn;
        for (final EntityPlayerMP entityplayermp : playersIn) {
            this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.interactionManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
        for (int i = buf.readVarIntFromBuffer(), j = 0; j < i; ++j) {
            GameProfile gameprofile = null;
            int k = 0;
            GameType gametype = null;
            ITextComponent itextcomponent = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
                    for (int l = buf.readVarIntFromBuffer(), i2 = 0; i2 < l; ++i2) {
                        final String s = buf.readStringFromBuffer(32767);
                        final String s2 = buf.readStringFromBuffer(32767);
                        if (buf.readBoolean()) {
                            gameprofile.getProperties().put(s, new Property(s, s2, buf.readStringFromBuffer(32767)));
                        }
                        else {
                            gameprofile.getProperties().put(s, new Property(s, s2));
                        }
                    }
                    gametype = GameType.getByID(buf.readVarIntFromBuffer());
                    k = buf.readVarIntFromBuffer();
                    if (buf.readBoolean()) {
                        itextcomponent = buf.readTextComponent();
                        break;
                    }
                    break;
                }
                case UPDATE_GAME_MODE: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    gametype = GameType.getByID(buf.readVarIntFromBuffer());
                    break;
                }
                case UPDATE_LATENCY: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    k = buf.readVarIntFromBuffer();
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    if (buf.readBoolean()) {
                        itextcomponent = buf.readTextComponent();
                        break;
                    }
                    break;
                }
                case REMOVE_PLAYER: {
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    break;
                }
            }
            this.players.add(new AddPlayerData(gameprofile, k, gametype, itextcomponent));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
        buf.writeVarIntToBuffer(this.players.size());
        for (final AddPlayerData spacketplayerlistitem$addplayerdata : this.players) {
            switch (this.action) {
                case ADD_PLAYER: {
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeString(spacketplayerlistitem$addplayerdata.getProfile().getName());
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getProfile().getProperties().size());
                    for (final Property property : ((ForwardingMultimap<K, Property>)spacketplayerlistitem$addplayerdata.getProfile().getProperties()).values()) {
                        buf.writeString(property.getName());
                        buf.writeString(property.getValue());
                        if (property.hasSignature()) {
                            buf.writeBoolean(true);
                            buf.writeString(property.getSignature());
                        }
                        else {
                            buf.writeBoolean(false);
                        }
                    }
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getGameMode().getID());
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getPing());
                    if (spacketplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        continue;
                    }
                    buf.writeBoolean(true);
                    buf.writeTextComponent(spacketplayerlistitem$addplayerdata.getDisplayName());
                    continue;
                }
                case UPDATE_DISPLAY_NAME: {
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    if (spacketplayerlistitem$addplayerdata.getDisplayName() == null) {
                        buf.writeBoolean(false);
                        continue;
                    }
                    buf.writeBoolean(true);
                    buf.writeTextComponent(spacketplayerlistitem$addplayerdata.getDisplayName());
                    continue;
                }
                default: {
                    continue;
                }
                case UPDATE_GAME_MODE: {
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getGameMode().getID());
                    continue;
                }
                case UPDATE_LATENCY: {
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getPing());
                    continue;
                }
                case REMOVE_PLAYER: {
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    continue;
                }
            }
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handlePlayerListItem(this);
    }
    
    public List<AddPlayerData> getEntries() {
        return this.players;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("action", this.action).add("entries", this.players).toString();
    }
    
    public enum Action
    {
        ADD_PLAYER("ADD_PLAYER", 0), 
        UPDATE_GAME_MODE("UPDATE_GAME_MODE", 1), 
        UPDATE_LATENCY("UPDATE_LATENCY", 2), 
        UPDATE_DISPLAY_NAME("UPDATE_DISPLAY_NAME", 3), 
        REMOVE_PLAYER("REMOVE_PLAYER", 4);
        
        private Action(final String s, final int n) {
        }
    }
    
    public class AddPlayerData
    {
        private final int ping;
        private final GameType gamemode;
        private final GameProfile profile;
        private final ITextComponent displayName;
        
        public AddPlayerData(final GameProfile profileIn, final int latencyIn, @Nullable final GameType gameModeIn, final ITextComponent displayNameIn) {
            this.profile = profileIn;
            this.ping = latencyIn;
            this.gamemode = gameModeIn;
            this.displayName = displayNameIn;
        }
        
        public GameProfile getProfile() {
            return this.profile;
        }
        
        public int getPing() {
            return this.ping;
        }
        
        public GameType getGameMode() {
            return this.gamemode;
        }
        
        @Nullable
        public ITextComponent getDisplayName() {
            return this.displayName;
        }
        
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("latency", this.ping).add("gameMode", this.gamemode).add("profile", this.profile).add("displayName", (this.displayName == null) ? null : ITextComponent.Serializer.componentToJson(this.displayName)).toString();
        }
    }
}
