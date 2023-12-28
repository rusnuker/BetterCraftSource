// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.impl;

import java.util.Collection;
import com.google.common.collect.ImmutableList;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Message;
import java.util.ArrayList;
import java.util.List;
import me.amkgre.bettercraft.client.mods.teamspeak.api.MessageTargetMode;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;
import me.amkgre.bettercraft.client.mods.teamspeak.api.Chat;

public abstract class ChatImpl implements Chat
{
    public static final int MAX_MESSAGES = 100;
    protected final TeamSpeakNetworkManager networkManager;
    private final MessageTargetMode type;
    protected final List<MessageImpl> messages;
    
    public ChatImpl(final TeamSpeakNetworkManager networkManager, final MessageTargetMode type) {
        this.messages = new ArrayList<MessageImpl>();
        this.networkManager = networkManager;
        this.type = type;
    }
    
    @Override
    public MessageTargetMode getType() {
        return this.type;
    }
    
    @Override
    public List<? extends Message> getMessages() {
        final List<MessageImpl> list = this.messages;
        synchronized (list) {
            final ImmutableList<Object> copy = ImmutableList.copyOf((Collection<?>)this.messages);
            monitorexit(list);
            return (List<? extends Message>)copy;
        }
    }
    
    public void addMessage(final MessageImpl message) {
        final List<MessageImpl> list = this.messages;
        synchronized (list) {
            this.messages.add(message);
            while (this.messages.size() > 100) {
                this.messages.remove(0);
            }
            monitorexit(list);
        }
    }
}
