// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak;

import java.util.Collection;
import com.google.common.collect.ImmutableList;
import me.amkgre.bettercraft.client.mods.teamspeak.api.ServerTab;
import me.amkgre.bettercraft.client.mods.teamspeak.util.EmptyRunnable;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import me.amkgre.bettercraft.client.mods.teamspeak.util.BlockingRunnable;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import java.util.ArrayList;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.ServerTabImpl;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;
import me.amkgre.bettercraft.client.mods.teamspeak.listener.DisconnectListener;
import me.amkgre.bettercraft.client.mods.teamspeak.listener.ConnectListener;
import java.util.List;
import me.amkgre.bettercraft.client.mods.teamspeak.api.TeamSpeakClient;

public class TeamSpeakClientImpl implements TeamSpeakClient
{
    private static final String HOST = "localhost";
    private static final int PORT = 25639;
    private final List<ConnectListener> CONNECT_LISTENERS;
    private final List<DisconnectListener> DISCONNECT_LISTENERS;
    private boolean connected;
    private boolean autoReconnect;
    private TeamSpeakNetworkManager networkManager;
    private final List<ServerTabImpl> tabs;
    
    TeamSpeakClientImpl() {
        this.CONNECT_LISTENERS = new ArrayList<ConnectListener>();
        this.DISCONNECT_LISTENERS = new ArrayList<DisconnectListener>();
        this.connected = false;
        this.autoReconnect = true;
        this.tabs = new ArrayList<ServerTabImpl>();
    }
    
    @Override
    public void connect() {
        this.connect(null);
    }
    
    @Override
    public void connect(final String authKey) {
        if (this.connected) {
            throw new IllegalStateException("TeamSpeak API is already connected to Client Query!");
        }
        this.connected = false;
        this.networkManager = new TeamSpeakNetworkManager(this, authKey);
        new Runnable() {
            private final Object LOCK = new Object();
            private List<Integer> result;
            
            @Override
            public void run() {
                try {
                    TeamSpeakClientImpl.this.networkManager.connect("localhost", 25639);
                    final int currentTab = TeamSpeakClientImpl.this.networkManager.getSelectedTab();
                    TeamSpeakClientImpl.this.networkManager.getTabs(new Callback<List<Integer>>() {
                        @Override
                        public void onDone(final List<Integer> tabIds) {
                            TeamSpeakClientImpl$1.access$0(Runnable.this, tabIds);
                            final Object object = Runnable.this.LOCK;
                            synchronized (object) {
                                Runnable.this.LOCK.notifyAll();
                                monitorexit(object);
                            }
                        }
                    });
                    Object object = this.LOCK;
                    synchronized (object) {
                        this.LOCK.wait();
                        monitorexit(object);
                    }
                    for (Integer tabId : this.result) {
                        final ServerTabImpl serverTab = new ServerTabImpl(TeamSpeakClientImpl.this.networkManager, tabId);
                        Object object2 = TeamSpeakClientImpl.this.tabs;
                        synchronized (object2) {
                            TeamSpeakClientImpl.this.tabs.add(serverTab);
                            monitorexit(object2);
                        }
                        TeamSpeakClientImpl.this.networkManager.getTabInfo(serverTab, new Runnable() {
                            @Override
                            public void run() {
                                final Object object = Runnable.this.LOCK;
                                synchronized (object) {
                                    Runnable.this.LOCK.notifyAll();
                                    monitorexit(object);
                                }
                            }
                        });
                        object2 = this.LOCK;
                        synchronized (object2) {
                            this.LOCK.wait();
                            monitorexit(object2);
                        }
                    }
                    TeamSpeakClientImpl.access$4(TeamSpeakClientImpl.this, true);
                    TeamSpeakClientImpl.this.getServerTab(currentTab).setSelected(true);
                    TeamSpeakClientImpl.this.networkManager.trySelectTab(currentTab, new BlockingRunnable());
                    object = TeamSpeakClientImpl.this.CONNECT_LISTENERS;
                    synchronized (object) {
                        for (final ConnectListener listener : TeamSpeakClientImpl.this.CONNECT_LISTENERS) {
                            listener.onConnected();
                        }
                        monitorexit(object);
                    }
                }
                catch (final Throwable throwable) {
                    if (TeamSpeakClientImpl.this.networkManager == null) {
                        TeamSpeakClientImpl.this.disconnect(throwable);
                    }
                    TeamSpeakClientImpl.this.networkManager.disconnect(throwable);
                }
            }
            
            static /* synthetic */ void access$0(final TeamSpeakClientImpl$1 runnable, final List result) {
                runnable.result = result;
            }
        }.run();
    }
    
    @Override
    public void disconnect() {
        if (this.networkManager != null) {
            this.networkManager.disconnect(null);
        }
    }
    
    @Override
    public boolean isConnected() {
        return this.networkManager != null && this.networkManager.isConnected();
    }
    
    public void disconnect(final Throwable cause) {
        if (TeamSpeak.isDebugMode()) {
            LogManager.getLogger().info("Disconnected from TeamSpeak ClientQuery: " + cause);
        }
        this.connected = false;
        for (final ServerTabImpl tab : this.tabs) {
            this.networkManager.clearTabInfo(tab);
        }
        this.tabs.clear();
        this.networkManager = null;
        final List<DisconnectListener> list = this.DISCONNECT_LISTENERS;
        synchronized (list) {
            for (final DisconnectListener listener : this.DISCONNECT_LISTENERS) {
                listener.onDisconnect(cause);
            }
            monitorexit(list);
        }
        if (this.autoReconnect && cause != null) {
            new Thread("TeamSpeak Reconnect Thread") {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000L);
                    }
                    catch (final InterruptedException ex) {}
                    if (TeamSpeak.isDebugMode()) {
                        LogManager.getLogger().info("Reconnecting to TeamSpeak ClientQuery...");
                    }
                    TeamSpeakClientImpl.this.connect();
                }
            }.start();
        }
    }
    
    public void addServerTab(final ServerTabImpl tab) {
        final List<ServerTabImpl> list = this.tabs;
        synchronized (list) {
            this.tabs.add(tab);
            monitorexit(list);
        }
    }
    
    public void removeServerTab(final ServerTabImpl tab) {
        final List<ServerTabImpl> list = this.tabs;
        synchronized (list) {
            this.networkManager.clearTabInfo(tab);
            this.tabs.remove(tab);
            if (!this.tabs.isEmpty() && this.networkManager.getSelectedTab() == tab.getId()) {
                this.networkManager.trySelectTab(this.tabs.get(this.tabs.size() - 1).getId(), new EmptyRunnable());
            }
            monitorexit(list);
        }
    }
    
    @Override
    public void addConnectListener(final ConnectListener listener) {
        final List<ConnectListener> list = this.CONNECT_LISTENERS;
        synchronized (list) {
            this.CONNECT_LISTENERS.add(listener);
            monitorexit(list);
        }
    }
    
    @Override
    public void removeConnectListener(final ConnectListener listener) {
        final List<ConnectListener> list = this.CONNECT_LISTENERS;
        synchronized (list) {
            this.CONNECT_LISTENERS.remove(listener);
            monitorexit(list);
        }
    }
    
    @Override
    public void addDisconnectListener(final DisconnectListener listener) {
        final List<DisconnectListener> list = this.DISCONNECT_LISTENERS;
        synchronized (list) {
            this.DISCONNECT_LISTENERS.add(listener);
            monitorexit(list);
        }
    }
    
    @Override
    public void removeDisconnectListener(final DisconnectListener listener) {
        final List<DisconnectListener> list = this.DISCONNECT_LISTENERS;
        synchronized (list) {
            this.DISCONNECT_LISTENERS.remove(listener);
            monitorexit(list);
        }
    }
    
    @Override
    public void setAutoReconnect(final boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }
    
    @Override
    public boolean isAutoReconnect() {
        return this.autoReconnect;
    }
    
    @Override
    public List<? extends ServerTab> getServerTabs() {
        final List<ServerTabImpl> list = this.tabs;
        synchronized (list) {
            final ImmutableList<Object> copy = ImmutableList.copyOf((Collection<?>)this.tabs);
            monitorexit(list);
            return (List<? extends ServerTab>)copy;
        }
    }
    
    @Override
    public ServerTabImpl getServerTab(final int id) {
        final List<ServerTabImpl> list = this.tabs;
        synchronized (list) {
            for (ServerTabImpl tab : this.tabs) {
                if (tab.getId() != id) {
                    continue;
                }
                final ServerTabImpl serverTabImpl = tab;
                monitorexit(list);
                return serverTabImpl;
            }
            monitorexit(list);
        }
        return null;
    }
    
    @Override
    public ServerTabImpl getSelectedTab() {
        return (this.networkManager == null) ? null : this.getServerTab(this.networkManager.getSelectedTab());
    }
    
    public TeamSpeakNetworkManager getNetworkManager() {
        return this.networkManager;
    }
    
    static /* synthetic */ void access$4(final TeamSpeakClientImpl teamSpeakClientImpl, final boolean connected) {
        teamSpeakClientImpl.connected = connected;
    }
}
